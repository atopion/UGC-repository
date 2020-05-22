package com.atopion.UGC_repository.util;

import com.atopion.UGC_repository.webannotation.entities.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class JSONSetSerializer<T> extends StdSerializer<Set<T>> {

    Class<T> clazz;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @SuppressWarnings("unchecked")
    public JSONSetSerializer(Class<T> t) {
        super((Class<Set<T>>)(Class<?>)Set.class);
        this.clazz = t;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void serialize(Set<T> value, JsonGenerator gen, SerializerProvider provider) throws IOException {

        List<String> list = new LinkedList<>();
        if(clazz.equals(WebAnnotationTypeEntity.class)) {
            for(WebAnnotationTypeEntity type : (Set<WebAnnotationTypeEntity>) value)
                list.add(type.getType());
        } else if(clazz.equals(WebAnnotationFormatEntity.class)) {
            for(WebAnnotationFormatEntity el : (Set<WebAnnotationFormatEntity>) value)
                list.add(el.getFormat());
        } else if(clazz.equals(WebAnnotationRightsEntity.class)) {
            for(WebAnnotationRightsEntity el : (Set<WebAnnotationRightsEntity>) value)
                list.add(el.getRights());
        } else if(clazz.equals(WebAnnotationAccessibilityEntity.class)) {
            for(WebAnnotationAccessibilityEntity el : (Set<WebAnnotationAccessibilityEntity>) value)
                list.add(el.getAccessibility());
        } else if(clazz.equals(WebAnnotationLanguageEntity.class)) {
            for(WebAnnotationLanguageEntity el : (Set<WebAnnotationLanguageEntity>) value)
                list.add(el.getLanguage());
        } else if(clazz.equals(WebAnnotationCachedEntity.class)) {
            for(WebAnnotationCachedEntity el : (Set<WebAnnotationCachedEntity>) value)
                list.add(el.getCached());
        } else if(clazz.equals(WebAnnotationSourceDateEntity.class)) {
            for(WebAnnotationSourceDateEntity el : (Set<WebAnnotationSourceDateEntity>) value)
                list.add(simpleDateFormat.format(el.getSource_date()));
        } else if(clazz.equals(WebAnnotationScopeEntity.class)) {
            for(WebAnnotationScopeEntity el : (Set<WebAnnotationScopeEntity>) value)
                list.add(el.getId());
        } else if(clazz.equals(WebAnnotationPurposeEntity.class)) {
            for(WebAnnotationPurposeEntity el : (Set<WebAnnotationPurposeEntity>) value)
                list.add(el.getPurpose());
        } else if(clazz.equals(WebAnnotationStyleClassEntity.class)) {
            for(WebAnnotationStyleClassEntity el : (Set<WebAnnotationStyleClassEntity>) value)
                list.add(el.getStyle_class());
        } else if(clazz.equals(WebAnnotationViaEntity.class)) {
            for(WebAnnotationViaEntity el : (Set<WebAnnotationViaEntity>) value)
                list.add(el.getVia());
        } else if(clazz.equals(WebAnnotationAgentHomepageEntity.class)) {
            for(WebAnnotationAgentHomepageEntity el : (Set<WebAnnotationAgentHomepageEntity>) value)
                list.add(el.getHomepage());
        } else if(clazz.equals(WebAnnotationEmailSha1Entity.class)) {
            for(WebAnnotationEmailSha1Entity el : (Set<WebAnnotationEmailSha1Entity>) value)
                list.add(el.getEmail_sha1());
        } else if(clazz.equals(WebAnnotationAgentEmailEntity.class)) {
            for(WebAnnotationAgentEmailEntity el : (Set<WebAnnotationAgentEmailEntity>) value)
                list.add(el.getEmail());
        } else if(clazz.equals(WebAnnotationAgentNameEntity.class)) {
            for(WebAnnotationAgentNameEntity el : (Set<WebAnnotationAgentNameEntity>) value)
                list.add(el.getName());
        } else if(clazz.equals(WebAnnotationContextEntity.class)) {
            for(WebAnnotationContextEntity el : (Set<WebAnnotationContextEntity>) value)
                list.add(el.getContext());
        } else if(clazz.equals(WebAnnotationMotivationEntity.class)) {
            for(WebAnnotationMotivationEntity el : (Set<WebAnnotationMotivationEntity>) value)
                list.add(el.getMotivation());
        }
        if(!list.isEmpty())
            gen.writeObject(list);
    }

    @Override
    public boolean isEmpty(SerializerProvider provider, Set<T> value) {
        return value.isEmpty();
    }


    public static class TypeSetSerializer extends JSONSetSerializer<WebAnnotationTypeEntity> {
        public TypeSetSerializer() { super(WebAnnotationTypeEntity.class); }
    }

    public static class FormatSetSerializer extends JSONSetSerializer<WebAnnotationFormatEntity> {
        public FormatSetSerializer() { super(WebAnnotationFormatEntity.class); }
    }

    public static class RightsSetSerializer extends JSONSetSerializer<WebAnnotationRightsEntity> {
        public RightsSetSerializer() { super(WebAnnotationRightsEntity.class); }
    }

    public static class AccessibilitySetSerializer extends JSONSetSerializer<WebAnnotationAccessibilityEntity> {
        public AccessibilitySetSerializer() { super(WebAnnotationAccessibilityEntity.class); }
    }

    public static class LanguageSetSerializer extends  JSONSetSerializer<WebAnnotationLanguageEntity> {
        public LanguageSetSerializer() { super(WebAnnotationLanguageEntity.class); }
    }

    public static class CachedSetSerializer extends  JSONSetSerializer<WebAnnotationCachedEntity> {
        public CachedSetSerializer() { super(WebAnnotationCachedEntity.class); }
    }

    public static class SourceDateSetSerializer extends  JSONSetSerializer<WebAnnotationSourceDateEntity> {
        public SourceDateSetSerializer() { super(WebAnnotationSourceDateEntity.class); }
    }

    public static class ScopeSetSerializer extends  JSONSetSerializer<WebAnnotationScopeEntity> {
        public ScopeSetSerializer() { super(WebAnnotationScopeEntity.class); }
    }

    public static class PurposeSetSerializer extends  JSONSetSerializer<WebAnnotationPurposeEntity> {
        public PurposeSetSerializer() { super(WebAnnotationPurposeEntity.class); }
    }

    public static class StyleClassSetSerializer extends  JSONSetSerializer<WebAnnotationStyleClassEntity> {
        public StyleClassSetSerializer() { super(WebAnnotationStyleClassEntity.class); }
    }

    public static class ViaSetSerializer extends  JSONSetSerializer<WebAnnotationViaEntity> {
        public ViaSetSerializer() { super(WebAnnotationViaEntity.class); }
    }

    public static class AgentHomepageSetSerializer extends  JSONSetSerializer<WebAnnotationAgentHomepageEntity> {
        public AgentHomepageSetSerializer() { super(WebAnnotationAgentHomepageEntity.class); }
    }

    public static class EmailSha1SetSerializer extends  JSONSetSerializer<WebAnnotationEmailSha1Entity> {
        public EmailSha1SetSerializer() { super(WebAnnotationEmailSha1Entity.class); }
    }

    public static class AgentEmailSetSerializer extends  JSONSetSerializer<WebAnnotationAgentEmailEntity> {
        public AgentEmailSetSerializer() { super(WebAnnotationAgentEmailEntity.class); }
    }

    public static class AgentNameSetSerializer extends  JSONSetSerializer<WebAnnotationAgentNameEntity> {
        public AgentNameSetSerializer() { super(WebAnnotationAgentNameEntity.class); }
    }

    public static class ContextSetSerializer extends  JSONSetSerializer<WebAnnotationContextEntity> {
        public ContextSetSerializer() { super(WebAnnotationContextEntity.class); }
    }

    public static class MotivationSetSerializer extends  JSONSetSerializer<WebAnnotationMotivationEntity> {
        public MotivationSetSerializer() { super(WebAnnotationMotivationEntity.class); }
    }
}
