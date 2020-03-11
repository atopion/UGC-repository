package com.atopion.UGC_repository;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
                .favorParameter(false)
                .ignoreAcceptHeader(true)
                .favorPathExtension(false)
                .defaultContentTypeStrategy(new CustomContentNegotiationStrategy())
                .mediaType("html", MediaType.TEXT_HTML)
                .mediaType("json", MediaType.APPLICATION_JSON);
    }

    public static class CustomContentNegotiationStrategy implements ContentNegotiationStrategy {

        @Override
        public List<MediaType> resolveMediaTypes(NativeWebRequest webRequest) {

            String format = webRequest.getParameter("format");
            if(format == null) {
                if(webRequest.getDescription(false).contains("/sql"))
                    return Collections.singletonList(MediaType.TEXT_HTML);
                else if(webRequest.getDescription(false).contains("/rest"))
                    return Collections.singletonList(MediaType.APPLICATION_JSON);
                else
                    return Collections.singletonList(MediaType.TEXT_HTML);
            } else {
                switch (format) {
                    case "json":
                        return Collections.singletonList(MediaType.APPLICATION_JSON);
                    case "xml":
                        return Collections.singletonList(MediaType.APPLICATION_XML);
                    case "html":
                        return Collections.singletonList(MediaType.TEXT_HTML);
                    case "csv":
                        return Collections.singletonList(MediaType.TEXT_PLAIN);
                    default:
                        return Collections.singletonList(MediaType.ALL);
                }
            }
        }
    }
}
