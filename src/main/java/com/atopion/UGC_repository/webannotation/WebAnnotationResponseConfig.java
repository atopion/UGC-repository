package com.atopion.UGC_repository.webannotation;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class WebAnnotationResponseConfig {

    @Bean
    public FilterRegistrationBean<Filter> linkHeaderFilter() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.addUrlPatterns("/rest/webannotation/*");
        registrationBean.setName("addLinkHeaderFilter");
        registrationBean.setOrder(1);
        registrationBean.setFilter((request, response, chain) -> {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setHeader("Link", "<http://www.w3.org/ns/ldp#Resource>; rel=type, <http://www.w3.org/ns/oa#Annotation>; rel=type");
            chain.doFilter(request, response);
        });
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<ShallowEtagHeaderFilter> shallowEtagHeaderFilter() {
        FilterRegistrationBean<ShallowEtagHeaderFilter> registrationBean = new FilterRegistrationBean<>(new ShallowEtagHeaderFilter());
        registrationBean.addUrlPatterns("/rest/webannotation/*");
        registrationBean.setName("etagFilter");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> corsHeaderFilter() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.addUrlPatterns("/rest/webannotation/*");
        registrationBean.setName("corsHeaderFilter");
        registrationBean.setOrder(1);
        registrationBean.setFilter((request, response, chain) -> {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
            httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, PATCH, OPTIONS, DELETE");
            httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
            httpServletResponse.setHeader("Access-Control-Allow-Headers", "Authorization, x-xsrf-token, Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, " +
                    "Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");

            chain.doFilter(request, response);
        });
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> allowAndVaryHeaderFilter() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.addUrlPatterns("/rest/webannotation/*");
        registrationBean.setName("allowAndVaryHeaderFilter");
        registrationBean.setOrder(1);
        registrationBean.setFilter((request, response, chain) -> {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setHeader("Allow", "GET, HEAD, POST, PUT, PATCH, OPTIONS, DELETE");
            httpServletResponse.setHeader("Vary", "Accept");
            chain.doFilter(request, response);
        });
        return registrationBean;
    }

}
