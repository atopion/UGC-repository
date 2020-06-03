package com.atopion.UGC_repository.util;

import com.atopion.UGC_repository.webannotation.entities.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.boot.WebApplicationType;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

public class JSONDeserializer<T> extends StdDeserializer<T> {

    private final Class<?> vc;
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public JSONDeserializer() {
        this(null);
    }

    protected JSONDeserializer(Class<?> vc) {
        super(vc);
        this.vc = vc;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        System.out.println("Currently in: " + p.getCurrentName() + " -> " + vc);
        /*if(p.getParsingContext() != null)
            System.out.println(
                    (p.getParsingContext().getCurrentValue() != null ? p.getParsingContext().getCurrentValue().getClass() : " - ") +
                    " -> " + (p.getParsingContext().getParent().getCurrentValue() != null ?
                                p.getParsingContext().getParent().getCurrentValue().getClass() : " - "));
        else
            System.out.println(" - ");*/

        Object val = null;
        if(p.getParsingContext() != null && p.getParsingContext().getCurrentValue() != null)
            val = p.getParsingContext().getCurrentValue();
        else if(p.getParsingContext() != null && p.getParsingContext().getParent() != null && p.getParsingContext().getParent().getCurrentValue() != null)
            val = p.getParsingContext().getParent().getCurrentValue();

        if(vc == WebAnnotationAgentNameEntity.class) {
            return (T) new WebAnnotationAgentNameEntity(p.getValueAsString(), (WebAnnotationAgentEntity) p.getCurrentValue());
        } else if(vc == WebAnnotationAccessibilityEntity.class) {
            if(p.getCurrentValue().getClass().equals(WebAnnotationTargetEntity.class))
                return (T) new WebAnnotationAccessibilityEntity(p.getValueAsString(), (WebAnnotationTargetEntity) p.getCurrentValue(), null);
            else
                return (T) new WebAnnotationAccessibilityEntity(p.getValueAsString(), null, (WebAnnotationBodyEntity) p.getCurrentValue());
        } else if(vc == WebAnnotationAgentEmailEntity.class) {
            return (T) new WebAnnotationAgentEmailEntity(p.getValueAsString(), (WebAnnotationAgentEntity) p.getCurrentValue());
        } else if(vc == WebAnnotationAgentHomepageEntity.class) {
            return (T) new WebAnnotationAgentHomepageEntity(p.getValueAsString(), (WebAnnotationAgentEntity) p.getCurrentValue());
        } else if(vc == WebAnnotationEmailSha1Entity.class) {
            return (T) new WebAnnotationEmailSha1Entity(p.getValueAsString(), (WebAnnotationAgentEntity) p.getCurrentValue());
        } else if(vc == WebAnnotationMotivationEntity.class) {
            return (T) new WebAnnotationMotivationEntity(p.getValueAsString(), (WebAnnotationEntity) p.getCurrentValue());
        } else if(vc == WebAnnotationViaEntity.class) {
            if(val != null && val.getClass().equals(WebAnnotationEntity.class))
                return (T) new WebAnnotationViaEntity(p.getValueAsString(), (WebAnnotationEntity) p.getCurrentValue(), null, null);
            else if(val != null && val.getClass().equals(WebAnnotationTargetEntity.class))
                return (T) new WebAnnotationViaEntity(p.getValueAsString(), null, (WebAnnotationTargetEntity) p.getCurrentValue(), null);
            else if(val != null && val.getClass().equals(WebAnnotationBodyEntity.class))
                return (T) new WebAnnotationViaEntity(p.getValueAsString(), null, null, (WebAnnotationBodyEntity) p.getCurrentValue());
        } else if(vc == WebAnnotationTypeEntity.class) {
            if(p.getCurrentValue().getClass().equals(WebAnnotationEntity.class))
                return (T) new WebAnnotationTypeEntity(p.getValueAsString(), (WebAnnotationEntity) p.getCurrentValue(), null, null, null, null, null, null);
            if(p.getCurrentValue().getClass().equals(WebAnnotationBodyEntity.class))
                return (T) new WebAnnotationTypeEntity(p.getValueAsString(), null, (WebAnnotationBodyEntity) p.getCurrentValue(), null, null, null, null, null);
            if(p.getCurrentValue().getClass().equals(WebAnnotationTargetEntity.class))
                return (T) new WebAnnotationTypeEntity(p.getValueAsString(), null, null, (WebAnnotationTargetEntity) p.getCurrentValue(),null, null, null, null);
            if(p.getCurrentValue().getClass().equals(WebAnnotationAgentEntity.class))
                return (T) new WebAnnotationTypeEntity(p.getValueAsString(), null, null, null, (WebAnnotationAgentEntity) p.getCurrentValue(), null, null, null);
            if(p.getCurrentValue().getClass().equals(WebAnnotationAudienceEntity.class))
                return (T) new WebAnnotationTypeEntity(p.getValueAsString(), null, null, null, null, (WebAnnotationAudienceEntity) p.getCurrentValue(), null, null);
            if(p.getCurrentValue().getClass().equals(WebAnnotationStylesheetEntity.class))
                return (T) new WebAnnotationTypeEntity(p.getValueAsString(), null, null, null, null, null, (WebAnnotationStylesheetEntity) p.getCurrentValue(), null);
            else
                return (T) new WebAnnotationTypeEntity(p.getValueAsString(), null, null, null, null, null, null, (WebAnnotationSelectorEntity) p.getCurrentValue());
        } else if(vc == WebAnnotationFormatEntity.class) {
            if(p.getCurrentValue().getClass().equals(WebAnnotationBodyEntity.class))
                return (T) new WebAnnotationFormatEntity(p.getValueAsString(), (WebAnnotationBodyEntity) p.getCurrentValue(), null);
            else
                return (T) new WebAnnotationFormatEntity(p.getValueAsString(), null, (WebAnnotationTargetEntity) p.getCurrentValue());
        } else if(vc == WebAnnotationPurposeEntity.class) {
            if (p.getCurrentValue().getClass().equals(WebAnnotationBodyEntity.class))
                return (T) new WebAnnotationPurposeEntity(p.getValueAsString(), (WebAnnotationBodyEntity) p.getCurrentValue(), null);
            else
                return (T) new WebAnnotationPurposeEntity(p.getValueAsString(), null, (WebAnnotationTargetEntity) p.getCurrentValue());
        } else if(vc == WebAnnotationScopeEntity.class) {
            if (p.getCurrentValue().getClass().equals(WebAnnotationBodyEntity.class))
                return (T) new WebAnnotationScopeEntity(p.getValueAsString(), (WebAnnotationBodyEntity) p.getCurrentValue(), null);
            else
                return (T) new WebAnnotationScopeEntity(p.getValueAsString(), null, (WebAnnotationTargetEntity) p.getCurrentValue());
        } else if(vc == WebAnnotationStyleClassEntity.class) {
            if (p.getCurrentValue().getClass().equals(WebAnnotationBodyEntity.class))
                return (T) new WebAnnotationStyleClassEntity(p.getValueAsString(), (WebAnnotationBodyEntity) p.getCurrentValue(), null);
            else
                return (T) new WebAnnotationStyleClassEntity(p.getValueAsString(), null, (WebAnnotationTargetEntity) p.getCurrentValue());
        } else if(vc == WebAnnotationSourceDateEntity.class) {
            try {
                return (T) new WebAnnotationSourceDateEntity(formatter.parse(p.getValueAsString()), (WebAnnotationStateEntity) p.getCurrentValue());
            } catch (ParseException e) {
                return (T) new WebAnnotationSourceDateEntity(new Date(), (WebAnnotationStateEntity) p.getCurrentValue());
            }
        } else if(vc == WebAnnotationCachedEntity.class) {
            return (T) new WebAnnotationCachedEntity(p.getValueAsString(), (WebAnnotationStateEntity) p.getCurrentValue());
        } else if(vc == WebAnnotationLanguageEntity.class) {
            if (p.getCurrentValue().getClass().equals(WebAnnotationBodyEntity.class))
                return (T) new WebAnnotationLanguageEntity(p.getValueAsString(), (WebAnnotationBodyEntity) p.getCurrentValue(), null);
            else
                return (T) new WebAnnotationLanguageEntity(p.getValueAsString(), null, (WebAnnotationTargetEntity) p.getCurrentValue());
        } else if(vc == WebAnnotationRightsEntity.class) {
            if (val != null && val.getClass().equals(WebAnnotationRightsEntity.class))
                return (T) new WebAnnotationRightsEntity(p.getValueAsString(), (WebAnnotationEntity) p.getCurrentValue(), null, null);
            else if (val != null && val.getClass().equals(WebAnnotationBodyEntity.class))
                return (T) new WebAnnotationRightsEntity(p.getValueAsString(), null, (WebAnnotationBodyEntity) p.getCurrentValue(), null);
            else if (val != null && val.getClass().equals(WebAnnotationTargetEntity.class))
                return (T) new WebAnnotationRightsEntity(p.getValueAsString(), null, null, (WebAnnotationTargetEntity) p.getCurrentValue());
            else
                return (T) p.readValueAs(vc);
        } else if(vc == WebAnnotationContextEntity.class) {
            return (T) new WebAnnotationContextEntity(p.getValueAsString(), (WebAnnotationEntity) p.getCurrentValue());
        } else if(vc == WebAnnotationAudienceEntity.class) {
            if(val != null && val.getClass().equals(WebAnnotationEntity.class)) {
                WebAnnotationAudienceEntity entity = p.readValuesAs(WebAnnotationAudienceEntity.class).next();
                entity.setAnnotationEntity((WebAnnotationEntity) val);
                return (T) entity;
            }
            return (T) p.readValuesAs(vc);
        } else if(vc == WebAnnotationAgentEntity.class) {
            if(val != null && val.getClass().equals(WebAnnotationEntity.class)) {
                WebAnnotationAgentEntity entity = p.readValuesAs(WebAnnotationAgentEntity.class).next();
                entity.setAnnotationEntity((WebAnnotationEntity) val);
                return (T) entity;
            } else if(val != null && val.getClass().equals(WebAnnotationBodyEntity.class)) {
                WebAnnotationAgentEntity entity = p.readValuesAs(WebAnnotationAgentEntity.class).next();
                entity.setBodyEntity((WebAnnotationBodyEntity) val);
                return (T) entity;
            }
            return (T) p.readValueAs(vc);
        } else if(vc == WebAnnotationBodyEntity.class) {
            if(val != null && val.getClass().equals(WebAnnotationEntity.class)) {
                WebAnnotationBodyEntity entity = p.readValuesAs(WebAnnotationBodyEntity.class).next();
                entity.setAnnotationEntity((WebAnnotationEntity) val);
                return (T) entity;
            } else if(val != null && val.getClass().equals(WebAnnotationBodyEntity.class)) {
                WebAnnotationBodyEntity entity = p.readValuesAs(WebAnnotationBodyEntity.class).next();
                entity.setChoiceEntity((WebAnnotationBodyEntity) val);
                return (T) entity;
            }
            return (T) p.readValuesAs(vc);
        } else if(vc == WebAnnotationTargetEntity.class) {
            if(val != null && val.getClass().equals(WebAnnotationEntity.class)) {
                WebAnnotationTargetEntity entity = p.readValuesAs(WebAnnotationTargetEntity.class).next();
                entity.setAnnotationEntity((WebAnnotationEntity) val);
                return (T) entity;
            }
            return (T) p.readValuesAs(vc);
        } else if(vc == WebAnnotationStateEntity.class) {
            if(val != null && val.getClass().equals(WebAnnotationTargetEntity.class)) {
                WebAnnotationStateEntity entity = p.readValuesAs(WebAnnotationStateEntity.class).next();
                entity.setTargetEntity((WebAnnotationTargetEntity) val);
                return (T) entity;
            } else if(val != null && val.getClass().equals(WebAnnotationBodyEntity.class)) {
                WebAnnotationStateEntity entity = p.readValuesAs(WebAnnotationStateEntity.class).next();
                entity.setBodyEntity((WebAnnotationBodyEntity) val);
                return (T) entity;
            } else if(val != null && val.getClass().equals(WebAnnotationStateEntity.class)) {
                WebAnnotationStateEntity entity = p.readValuesAs(WebAnnotationStateEntity.class).next();
                entity.setRefiningState((WebAnnotationStateEntity) val);
                return (T) entity;
            } else if(val != null && val.getClass().equals(WebAnnotationSelectorEntity.class)) {
                WebAnnotationStateEntity entity = p.readValuesAs(WebAnnotationStateEntity.class).next();
                entity.setRefiningSelector((WebAnnotationSelectorEntity) val);
                return (T) entity;
            }
            return (T) p.readValuesAs(vc);
        } else if(vc == WebAnnotationRenderedViaEntity.class) {
            if(val != null && val.getClass().equals(WebAnnotationTargetEntity.class)) {
                WebAnnotationRenderedViaEntity entity = p.readValuesAs(WebAnnotationRenderedViaEntity.class).next();
                entity.setTargetEntity((WebAnnotationTargetEntity) val);
                return (T) entity;
            }
            return (T) p.readValuesAs(vc);
        } else if(vc == WebAnnotationSelectorEntity.class) {
            if(val != null && val.getClass().equals(WebAnnotationTargetEntity.class)) {
                WebAnnotationSelectorEntity entity = p.readValuesAs(WebAnnotationSelectorEntity.class).next();
                entity.setTargetEntity((WebAnnotationTargetEntity) val);
                return (T) entity;
            }
            return (T) p.readValuesAs(vc);
        } else if(vc == WebAnnotationStylesheetEntity.class) {
            if(val != null && val.getClass().equals(WebAnnotationEntity.class)) {
                WebAnnotationStylesheetEntity entity = p.readValuesAs(WebAnnotationStylesheetEntity.class).next();
                entity.setAnnotationEntity((WebAnnotationEntity) val);
                return (T) entity;
            }
            return (T) p.readValuesAs(vc);
        } /*else if(vc == WebAnnotationEntity.class) {
            return (T) p.getCodec().treeToValue(p.getCodec().readTree(p), WebAnnotationEntity.class);
            /*try {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                JsonNode node = p.getCodec().readTree(p);
                WebAnnotationEntity entity = new WebAnnotationEntity(
                        p.getCodec().treeToValue(node.get("id"), String.class),
                        df.parse(p.getCodec().treeToValue(node.get("created"), String.class)),
                        df.parse(p.getCodec().treeToValue(node.get("modified"), String.class)),
                        df.parse(p.getCodec().treeToValue(node.get("generated"), String.class)),
                        p.getCodec().treeToValue(node.get("bodyValue"), String.class),
                        p.getCodec().treeToValue(node.get("canonical"), String.class),
                        p.getCodec().readValue(p.getCodec().treeAsTokens(node.get("generator")),
                                new TypeReference<LinkedHashSet<WebAnnotationAgentEntity>>() {}),
                        p.getCodec().readValue(p.getCodec().treeAsTokens(node.get("motivation")),
                                new TypeReference<LinkedHashSet<WebAnnotationMotivationEntity>>() {}),
                        p.getCodec().readValue(p.getCodec().treeAsTokens(node.get("@context")),
                                new TypeReference<LinkedHashSet<WebAnnotationContextEntity>>() {}),
                        p.getCodec().readValue(p.getCodec().treeAsTokens(node.get("creator")),
                                new TypeReference<LinkedHashSet<WebAnnotationAgentEntity>>() {}),
                        p.getCodec().readValue(p.getCodec().treeAsTokens(node.get("via")),
                                new TypeReference<LinkedHashSet<WebAnnotationViaEntity>>() {}),
                        p.getCodec().readValue(p.getCodec().treeAsTokens(node.get("body")),
                                new TypeReference<LinkedHashSet<WebAnnotationBodyEntity>>() {}),
                        p.getCodec().readValue(p.getCodec().treeAsTokens(node.get("target")),
                                new TypeReference<LinkedHashSet<WebAnnotationTargetEntity>>() {}),
                        p.getCodec().readValue(p.getCodec().treeAsTokens(node.get("rights")),
                                new TypeReference<LinkedHashSet<WebAnnotationRightsEntity>>() {}),
                        p.getCodec().readValue(p.getCodec().treeAsTokens(node.get("audience")),
                                new TypeReference<LinkedHashSet<WebAnnotationAudienceEntity>>() {}),
                        p.getCodec().readValue(p.getCodec().treeAsTokens(node.get("type")),
                                new TypeReference<LinkedHashSet<WebAnnotationTypeEntity>>() {}),
                        p.getCodec().readValue(p.getCodec().treeAsTokens(node.get("stylesheet")),
                                new TypeReference<LinkedHashSet<WebAnnotationStylesheetEntity>>() {})
                );
                System.out.println("NODE: " + entity);
                return (T) entity;
            } catch (ParseException e) {
                throw new IOException("Cannot parse Date: " + e.getLocalizedMessage());
            }*/
        //}

        return (T) p.readValueAs(vc);
    }

    public static class AgentEntity extends JSONDeserializer<WebAnnotationAgentEntity> {
        public AgentEntity() { super(WebAnnotationAgentEntity.class); }
    }
    public static class AccessibilityEntity extends JSONDeserializer<WebAnnotationAccessibilityEntity> {
        public AccessibilityEntity() { super(WebAnnotationAccessibilityEntity.class); }
    }
    public static class AgentEmailEntity extends JSONDeserializer<WebAnnotationAgentEmailEntity> {
        public AgentEmailEntity() { super(WebAnnotationAgentEmailEntity.class); }
    }
    public static class AgentHomepageEntity extends JSONDeserializer<WebAnnotationAgentHomepageEntity> {
        public AgentHomepageEntity() { super(WebAnnotationAgentHomepageEntity.class); }
    }
    public static class AgentNameEntity extends JSONDeserializer<WebAnnotationAgentNameEntity> {
        public AgentNameEntity() { super(WebAnnotationAgentNameEntity.class); }
    }
    public static class AudienceEntity extends JSONDeserializer<WebAnnotationAudienceEntity> {
        public AudienceEntity() { super(WebAnnotationAudienceEntity.class); }
    }
    public static class BodyEntity extends JSONDeserializer<WebAnnotationBodyEntity> {
        public BodyEntity() { super(WebAnnotationBodyEntity.class); }
    }
    public static class CachedEntity extends JSONDeserializer<WebAnnotationCachedEntity> {
        public CachedEntity() { super(WebAnnotationCachedEntity.class); }
    }
    public static class ContextEntity extends JSONDeserializer<WebAnnotationContextEntity> {
        public ContextEntity() { super(WebAnnotationContextEntity.class); }
    }
    public static class EmailSha1Entity extends JSONDeserializer<WebAnnotationEmailSha1Entity> {
        public EmailSha1Entity() { super(WebAnnotationEmailSha1Entity.class); }
    }
    public static class FormatEntity extends JSONDeserializer<WebAnnotationFormatEntity> {
        public FormatEntity() { super(WebAnnotationFormatEntity.class); }
    }
    public static class LanguageEntity extends JSONDeserializer<WebAnnotationLanguageEntity> {
        public LanguageEntity() { super(WebAnnotationLanguageEntity.class); }
    }
    public static class MotivationEntity extends JSONDeserializer<WebAnnotationMotivationEntity> {
        public MotivationEntity() { super(WebAnnotationMotivationEntity.class); }
    }
    public static class PurposeEntity extends JSONDeserializer<WebAnnotationPurposeEntity> {
        public PurposeEntity() { super(WebAnnotationPurposeEntity.class); }
    }
    public static class RenderedViaEntity extends JSONDeserializer<WebAnnotationRenderedViaEntity> {
        public RenderedViaEntity() { super(WebAnnotationRenderedViaEntity.class); }
    }
    public static class RightsEntity extends JSONDeserializer<WebAnnotationRightsEntity> {
        public RightsEntity() { super(WebAnnotationRightsEntity.class); }
    }
    public static class ScopeEntity extends JSONDeserializer<WebAnnotationScopeEntity> {
        public ScopeEntity() { super(WebAnnotationScopeEntity.class); }
    }
    public static class SelectorEntity extends JSONDeserializer<WebAnnotationSelectorEntity> {
        public SelectorEntity() { super(WebAnnotationSelectorEntity.class); }
    }
    public static class SourceDateEntity extends JSONDeserializer<WebAnnotationSourceDateEntity> {
        public SourceDateEntity() { super(WebAnnotationSourceDateEntity.class); }
    }
    public static class StateEntity extends JSONDeserializer<WebAnnotationStateEntity> {
        public StateEntity() { super(WebAnnotationStateEntity.class); }
    }
    public static class StyleClassEntity extends JSONDeserializer<WebAnnotationStyleClassEntity> {
        public StyleClassEntity() { super(WebAnnotationStyleClassEntity.class); }
    }
    public static class StylesheetEntity extends JSONDeserializer<WebAnnotationStylesheetEntity> {
        public StylesheetEntity() { super(WebAnnotationStylesheetEntity.class); }
    }
    public static class TargetEntity extends JSONDeserializer<WebAnnotationTargetEntity> {
        public TargetEntity() { super(WebAnnotationTargetEntity.class); }
    }
    public static class TypeEntity extends JSONDeserializer<WebAnnotationTypeEntity> {
        public TypeEntity() { super(WebAnnotationTypeEntity.class); }
    }
    public static class ViaEntity extends JSONDeserializer<WebAnnotationViaEntity> {
        public ViaEntity() { super(WebAnnotationViaEntity.class); }
    }
    public static class AnnotationEntity extends JSONDeserializer<WebAnnotationEntity> {
        public AnnotationEntity() { super(WebAnnotationEntity.class); }
    }

    /*public static class ContextEntity extends StdDeserializer<Set<WebAnnotationContextEntity>> {

        public ContextEntity() {
            super(Set.class);
        }

        @Override
        public Set<WebAnnotationContextEntity> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return Set.of(new WebAnnotationContextEntity(p.getValueAsString(), (WebAnnotationEntity) p.getCurrentValue()));
        }
    }*/
}
