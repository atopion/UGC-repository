package com.atopion.UGC_repository;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
                .favorParameter(false)
                .favorPathExtension(false)
                .ignoreAcceptHeader(true)
                .defaultContentTypeStrategy(new CustomContentNegotiationStrategy())
                .mediaType("csv", MediaType.valueOf("text/csv"))
                .mediaType("xml", MediaType.APPLICATION_XML)
                .mediaType("html", MediaType.TEXT_HTML)
                .mediaType("json", MediaType.APPLICATION_JSON)
                .mediaType("json-ld", MediaType.valueOf("application/ld+json"))
                .mediaType("", MediaType.APPLICATION_FORM_URLENCODED);
    }

    /**
     * IMPORTANT: Does not care about quality values in headers, uses the first value that is known.
     *
     *
     */

    public static class CustomContentNegotiationStrategy implements ContentNegotiationStrategy {

        @Override
        public List<MediaType> resolveMediaTypes(NativeWebRequest webRequest) {

            String path = webRequest.getDescription(false);
            String format = webRequest.getParameter("format");
            String header = webRequest.getHeader("Accept");

            List<Triple<String, String, MediaType>> values = List.of(
                    Triple.of("json", "application/json", MediaType.APPLICATION_JSON),
                    Triple.of("json-ld", "application/ld+json", MediaType.valueOf("application/ld+json")),
                    Triple.of("xml", "application/xml", MediaType.APPLICATION_XML),
                    Triple.of("html", "text/html", MediaType.TEXT_HTML),
                    Triple.of("csv", "text/csv", MediaType.valueOf("text/csv"))
            );

            Optional<Triple<String, String, MediaType>> result = Optional.empty();

            if (format != null) {
                result = values
                        .stream()
                        .filter(v -> v.getLeft().equals(format))
                        .findFirst();
            } else if (header != null) {
                result = Arrays.stream(header.replaceAll(".*;[a-zA-Z0-9.;:-_/\"]*,", ",").split(","))
                        .map(t -> values.stream().filter(v -> v.getMiddle().equals(t)).findFirst())
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .findFirst();
            }

            if (result.isPresent()) {
                return Collections.singletonList(result.get().getRight());
            }

            // Default values if neither format nor header is given (or are unknown)
            if (path.contains("/sql")) {
                return Collections.singletonList(MediaType.TEXT_HTML);
            } else if (path.contains("/rest/webannotation")) {
                return Collections.singletonList(MediaType.valueOf("application/ld+json"));
            } else if (path.contains("/rest")) {
                return Collections.singletonList(MediaType.APPLICATION_JSON);
            } else {
                return Collections.singletonList(MediaType.ALL);
            }
        }
            /*

            if(webRequest.getDescription(false).contains("/sql")) {
                String format = webRequest.getParameter("format");
                if(format == null) {
                    String header = webRequest.getHeader("Accept");
                    if(header == null)
                        return Collections.singletonList(MediaType.TEXT_HTML);
                    else {
                        String[] contents = header.split(",");
                        for(String type : contents) {
                            switch (type) {
                                case "text/html":
                                    return Collections.singletonList(MediaType.TEXT_HTML);
                                case "application/json":
                                    return Collections.singletonList(MediaType.APPLICATION_JSON);
                                case "application/xml":
                                    return Collections.singletonList(MediaType.APPLICATION_XML);
                                case "text/csv":
                                    return Collections.singletonList(MediaType.valueOf("text/csv"));
                            }
                        }
                        return Collections.singletonList(MediaType.TEXT_HTML);
                    }
                }
                else {
                    switch (format) {
                        case "json":
                            return Collections.singletonList(MediaType.APPLICATION_JSON);
                        case "xml":
                            return Collections.singletonList(MediaType.APPLICATION_XML);
                        case "html":
                            return Collections.singletonList(MediaType.TEXT_HTML);
                        case "csv":
                            return Collections.singletonList(MediaType.valueOf("text/csv"));
                        default:
                            return Collections.singletonList(MediaType.TEXT_HTML);
                    }
                }
            }
            else if(webRequest.getDescription(false).contains("/rest/webannotation")) {
                String format = webRequest.getParameter("format");
                if(format == null) {
                    String header = webRequest.getHeader("Accept");
                    if(header == null)
                        return Collections.singletonList(MediaType.valueOf("application/ld+json"));
                    else {
                        String[] contents = header.replaceAll(".*;[a-zA-Z0-9.;:-_/\"]*,", ",").split(",");
                        for(String type : contents) {
                            switch (type) {
                                case "application/ld+json":
                                    return Collections.singletonList(MediaType.valueOf("application/ld+json"));
                                case "application/json":
                                    return Collections.singletonList(MediaType.APPLICATION_JSON);
                                case "application/xml":
                                    return Collections.singletonList(MediaType.APPLICATION_XML);
                            }
                        }
                        return Collections.singletonList(MediaType.valueOf("application/ld+json"));
                    }
                }
                else {
                    switch (format) {
                        case "json-ld":
                            return Collections.singletonList(MediaType.valueOf("application/ld+json"));
                        case "json":
                            return Collections.singletonList(MediaType.APPLICATION_JSON);
                        case "xml":
                            return Collections.singletonList(MediaType.APPLICATION_XML);
                    }
                    return Collections.singletonList(MediaType.valueOf("application/ld+json"));
                }
            }
            else if(webRequest.getDescription(false).contains("/rest")) {
                String format = webRequest.getParameter("format");
                if(format == null) {
                    String header = webRequest.getHeader("Accept");
                    if(header == null)
                        return Collections.singletonList(MediaType.APPLICATION_JSON);
                    else {
                        String[] contents = header.split(",");
                        for(String type : contents) {
                            switch (type) {
                                case "text/html":
                                    return Collections.singletonList(MediaType.TEXT_HTML);
                                case "application/json":
                                    return Collections.singletonList(MediaType.APPLICATION_JSON);
                                case "application/xml":
                                    return Collections.singletonList(MediaType.APPLICATION_XML);
                                case "text/csv":
                                    return Collections.singletonList(MediaType.valueOf("text/csv"));
                            }
                        }
                        return Collections.singletonList(MediaType.APPLICATION_JSON);
                    }
                }
                else {
                    switch (format) {
                        case "json":
                            return Collections.singletonList(MediaType.APPLICATION_JSON);
                        case "xml":
                            return Collections.singletonList(MediaType.APPLICATION_XML);
                        case "html":
                            return Collections.singletonList(MediaType.TEXT_HTML);
                        case "csv":
                            return Collections.singletonList(MediaType.valueOf("text/csv"));
                        default:
                            return Collections.singletonList(MediaType.APPLICATION_JSON);
                    }
                }
            }
            return Collections.singletonList(MediaType.ALL);
        }*/
    }

    /*
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/rest")
                .allowedOrigins("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
        registry.addMapping("/rest/webannotation")
                .allowedOrigins("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
        registry.addMapping("/sql")
                .allowedOrigins("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
    }
    */


    @Bean
    public Formatter<Date> dateFormatter() {
        return new Formatter<>() {
            @Override
            public Date parse(String text, Locale locale) throws ParseException {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(text);
            }

            @Override
            public String print(Date object, Locale locale) {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(object);
            }
        };
    }

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context ctx) {
                SecurityConstraint constraint = new SecurityConstraint();
                constraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                constraint.addCollection(collection);
                ctx.addConstraint(constraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(getHTTPConnector());
        return tomcat;
    }

    private Connector getHTTPConnector() {
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setScheme("http");
        connector.setPort(8080);
        connector.setSecure(false);
        connector.setRedirectPort(443);
        return connector;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

        converters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));

        //FormHttpMessageConverter formConverter = new FormHttpMessageConverter();


        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.serializationInclusion(JsonInclude.Include.NON_NULL);
        builder.serializationInclusion(JsonInclude.Include.NON_EMPTY);
        builder.featuresToEnable(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED);
        builder.featuresToEnable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        builder.indentOutput(true);
        converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
        converters.add(new MappingJackson2XmlHttpMessageConverter(builder.createXmlMapper(true).build()));
    }
}