package com.atopion.UGC_repository;

import com.atopion.UGC_repository.security.APIAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private APIAuthenticationProvider authProvider;

    private APIAuthenticationProvider.HeaderExtractFilter filter = new APIAuthenticationProvider.HeaderExtractFilter();

    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    // Allow statics
                    .antMatchers("/css/*", "/js/*", "/favicon.ico").permitAll()
                    // Allow universal access to /sql
                    .antMatchers("/sql", "/sql/**").permitAll()

                .and()
                    .antMatcher("/rest/**").addFilterBefore(filter, BasicAuthenticationFilter.class)
                        .authenticationProvider(authProvider)
                        // Allow authorized access to /rest
                        .authorizeRequests().antMatchers("/rest", "/rest/**").authenticated()
                    // Deny everything else
                    .anyRequest().denyAll();

                /*.and()
                    .httpBasic()

                .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);*/
    }


    @Bean
    public String masterKey() {
        try {
            return Files.readAllLines(Path.of("/home/atopi/IdeaProjects/UGC-repository/db/masterkey.txt")).get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
