package com.atopion.UGC_repository.security;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class APIAuthenticationProvider implements AuthenticationProvider {

    public static class APIAuthenticationToken extends AbstractAuthenticationToken {

        public APIAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
            super(authorities);
        }

        @Override
        public Object getCredentials() {
            return "PASS";
        }

        @Override
        public Object getPrincipal() {
            return "REST-API";
        }
    }

    /**
     * Extracts the authentication information from custom headers
     */
    public static class HeaderExtractFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

            Map<String, String> details = new HashMap<>();
            details.put("X-Auth-Access-Id",    request.getHeader("X-Auth-Access-Id"));
            details.put("X-Auth-Access-Key",   request.getHeader("X-Auth-Access-Key"));
            details.put("X-Auth-Access-Token", request.getHeader("X-Auth-Access-Token"));

            APIAuthenticationToken token = new APIAuthenticationToken(new LinkedList<>());
            token.setDetails(details);

            SecurityContextHolder.getContext().setAuthentication(token);

            filterChain.doFilter(request, response);
        }
    }


    @Value("${app.debug-disable-auth:false}")
    private boolean DISABLE_AUTH;

    private String masterKey;

    private DataSource dataSource;

    private final Logger logger = LoggerFactory.getLogger(APIAuthenticationProvider.class);

    public APIAuthenticationProvider(@Qualifier("userDataSource") DataSource dataSource, @Autowired String masterKey) {
        this.dataSource = dataSource;
        this.masterKey = masterKey;            System.out.println("AUTHENTICATION: " + DISABLE_AUTH);

    }

    @Override
    @SuppressWarnings("unchecked")
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            //System.out.println(authentication);
            //System.out.println(authentication.getDetails());

            if(DISABLE_AUTH) {
                System.err.println("WARNING: AUTHENTICATION DISABLED");
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                auth.setAuthenticated(true);
                SecurityContextHolder.getContext().setAuthentication(auth);
                List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
                grantedAuthorityList.add(new SimpleGrantedAuthority("USER"));
                return new UsernamePasswordAuthenticationToken("anonymous", "", grantedAuthorityList);
            }

            Map<String, String> details;

            try {
                details = (Map<String, String>) authentication.getDetails();

            } catch (ClassCastException ex) {
                throw new Exception("Wrong header structure");
            }

            String id    = details.get("X-Auth-Access-Id");
            String key   = details.get("X-Auth-Access-Key");
            String token = details.get("X-Auth-Access-Token");

            if(id == null || !id.matches("[0-9]{1,4}"))
                throw new Exception("Wrong id");

            if(key == null || !key.matches("[0-9A-F]{128}"))
                throw new Exception("Wrong key");

            if(token == null || !token.matches("[0-9A-F]{128}"))
                throw new Exception("Wrong token");

            Map<String, String> securityDetails = searchUserById(Integer.parseInt(id));

            if(securityDetails == null)
                throw new Exception("User not found");

            if(!securityDetails.containsKey("user_keyhash"))
                throw new Exception("Keyhash not found");

            if(!securityDetails.containsKey("user_secret"))
                throw new Exception("Secret not found");

            if(!BCrypt.checkpw(Hex.decodeHex(key), securityDetails.get("user_keyhash")))
                throw new Exception("Key invalid");

            String secret = decryptSecret(securityDetails.get("user_secret"), key, masterKey);

            if(secret == null)
                throw new Exception("Secret invalid");

            boolean found = false;
            for(String step : TokenGenerator.getTimeSteps()) {
                if (TokenGenerator.generateTOTP(secret, step).equals(token))
                    found = true;
            }

            if(!found)
                throw new Exception("Token invalid");


            List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
            grantedAuthorityList.add(new SimpleGrantedAuthority("USER"));
            return new UsernamePasswordAuthenticationToken(id, "", grantedAuthorityList);

        } catch (Exception e) {
            throw new BadCredentialsException("Authorization invalid");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }

    private Map<String, String> searchUserById(int id) {

        String query = "SELECT user_keyhash, user_secret FROM users.users WHERE user_id = ?";
        ResultSet set = null;
        Map<String, String> result = new HashMap<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement sqlStatement = connection.prepareStatement(query)) {

            sqlStatement.setInt(1, id);
            set = sqlStatement.executeQuery();
            if(set.next())
                for(int i = 1; i <= set.getMetaData().getColumnCount(); i++)
                    result.put(set.getMetaData().getColumnName(i), set.getString(i));
            else
                result = null;

        } catch (SQLException ex) {
            logger.error("Error getting user.", ex);
            result = null;
        } finally {
            try {
                if(set != null)
                    set.close();
            } catch (SQLException e) {
                logger.error("Error during closing of set.", e);
                result = null;
            }
        }
        return result;
    }

    private String decryptSecret(String secret, String key, String masterkey) {

        try {
            byte[] _secret = Hex.decodeHex(secret);
            byte[] _key    = Hex.decodeHex(key);
            byte[] _master = Hex.decodeHex(masterkey);

            byte[] _result = new byte[64];

            if(_secret.length != 64 || _key.length != 64 || _master.length != 64)
                throw new Exception();

            for(int i = 0; i < _secret.length; i++) {
                _result[i] = (byte) (((byte) (_secret[i] ^ _key[i]))  ^ _master[i]);
            }

            return Hex.encodeHexString(_result);

        } catch (Exception e) {
            return null;
        }
    }
}
