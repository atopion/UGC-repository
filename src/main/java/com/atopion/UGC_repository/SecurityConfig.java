package com.atopion.UGC_repository;

import com.atopion.UGC_repository.security.APIAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private APIAuthenticationProvider authProvider;

    private APIAuthenticationProvider.HeaderExtractFilter filter;


    public SecurityConfig(@Autowired APIAuthenticationProvider authProvider)  {
        this.authProvider = authProvider;
        this.filter = new APIAuthenticationProvider.HeaderExtractFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .requiresChannel().anyRequest().requiresSecure()

                .and()
                .authorizeRequests()
                    // Allow statics
                    .antMatchers("/css/*", "/js/*", "/favicon.ico").permitAll()
                    // Allow universal access to /sql
                    .antMatchers("/sql", "/sql/**").permitAll()

                    .antMatchers("/metrics", "/metrics/**").permitAll()

                .and()
                    .antMatcher("/rest/**").addFilterBefore(filter, BasicAuthenticationFilter.class)
                        .authenticationProvider(authProvider)
                        // Allow authorized access to /rest
                        .authorizeRequests().antMatchers("/rest", "/rest/**").authenticated()
                    // Deny everything else
                    .anyRequest().denyAll()

                /*.and()
                    .httpBasic()*/
                /* .and()
                    .headers().cacheControl().disable()*/

                .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
