package com.atopion.UGC_repository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Configuration
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
                .mediaType("", MediaType.APPLICATION_FORM_URLENCODED);
    }

    /**
     * IMPORTANT: Does not care about quality values in headers, uses the first value that is known.
     */

    public static class CustomContentNegotiationStrategy implements ContentNegotiationStrategy {

        @Override
        public List<MediaType> resolveMediaTypes(NativeWebRequest webRequest) {

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
        }
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");
    }


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
}
