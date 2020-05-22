package com.atopion.UGC_repository;

import com.atopion.UGC_repository.util.JSONSetSerializer;
import com.atopion.UGC_repository.webannotation.entities.WebAnnotationEntity;
import com.atopion.UGC_repository.webannotation.entities.WebAnnotationTypeEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.fail;


public class Tryout {

    public static class UnwrappedUser {
        public int id;

        @JsonUnwrapped
        public Name name;

        public UnwrappedUser(int id, Name name) {
            this.id = id;
            this.name = name;
        }

        public static class Name {
            public String firstName;
            public String lastName;

            public Name(String firstName, String lastName) {
                this.firstName = firstName;
                this.lastName = lastName;
            }
        }
    }

    @Test
    public void whenSerializingUsingJsonUnwrapped_thenCorrect() throws JsonProcessingException, ParseException {
        UnwrappedUser.Name name = new UnwrappedUser.Name("John", "Doe");
        UnwrappedUser user = new UnwrappedUser(1, name);

        String result = new ObjectMapper()
                .writeValueAsString(user);

        System.out.println(result);
        assertThat(result, containsString("John"));
        assertThat(result, not(containsString("name")));
    }

    @Test
    public void findType() {
        System.out.println("Class: " + new JSONSetSerializer<WebAnnotationTypeEntity>(WebAnnotationTypeEntity.class).getClazz());
    }

    private String json = "{\n" +
            "  \"id\" : \"https://data.forum-wissen.de/rest/annotations/2\",\n" +
            "  \"created\" : \"2020-04-25 02:00:00\",\n" +
            "  \"modified\" : \"2020-04-25 02:00:00\",\n" +
            "  \"generated\" : \"2020-04-25 02:00:00\",\n" +
            "  \"canonical\" : \"https://hdl.handle.net/21.11107/record_DE-MUS-069123_118\",\n" +
            "  \"generator\" : {\n" +
            "    \"id\" : \"https://sammlungsportal.uni-goettingen.de/user/kheck\",\n" +
            "    \"nickname\" : \"kheck\",\n" +
            "    \"name\" : \"Karsten Heck\",\n" +
            "    \"email\" : \"heck@kustodie.uni-goettingen.de\",\n" +
            "    \"emailsSha1\" : \"171bc3343d089b46d57f111b2939ad8a6b565a0f\",\n" +
            "    \"homepage\" : \"https://www.uni-goettingen.de/de/521338.html\"\n" +
            "  },\n" +
            "  \"motivation\" : \"describing\",\n" +
            "  \"creator\" : {\n" +
            "    \"id\" : \"https://sammlungsportal.uni-goettingen.de/user/kheck\",\n" +
            "    \"nickname\" : \"kheck\",\n" +
            "    \"name\" : \"Karsten Heck\",\n" +
            "    \"email\" : \"heck@kustodie.uni-goettingen.de\",\n" +
            "    \"emailsSha1\" : \"171bc3343d089b46d57f111b2939ad8a6b565a0f\",\n" +
            "    \"homepage\" : \"https://www.uni-goettingen.de/de/521338.html\"\n" +
            "  },\n" +
            "  \"via\" : \"https://sammlungen.uni-goettingen.de/objekt/record_DE-MUS-069123_118/1/-/\",\n" +
            "  \"body\" : {\n" +
            "    \"id\" : \"https://data.forum-wissen.de/annotations/body/2\",\n" +
            "    \"processing_language\" : \"de\",\n" +
            "    \"text_direction\" : \"ltr\",\n" +
            "    \"value\" : \"Eine interessante Kugel\",\n" +
            "    \"modified\" : \"2020-04-25 02:00:00\",\n" +
            "    \"created\" : \"2020-04-25 02:00:00\",\n" +
            "    \"canonical\" : \"https://hdl.handle.net/21.11107/record_DE-MUS-069123_118\",\n" +
            "    \"type\" : \"TextualBody\",\n" +
            "    \"format\" : \"text/html\",\n" +
            "    \"purpose\" : \"describing\",\n" +
            "    \"scope\" : \"https://sammlungen.uni-goettingen.de/mirador/\",\n" +
            "    \"styleClass\" : \"blue\"\n" +
            "  },\n" +
            "  \"target\" : {\n" +
            "    \"id\" : \"https://data.forum-wissen.de/annotations/target/2\",\n" +
            "    \"processing_language\" : \"de\",\n" +
            "    \"text_direction\" : \"ltr\",\n" +
            "    \"canonical\" : \"https://hdl.handle.net/21.11107/record_DE-MUS-069123_118\",\n" +
            "    \"source\" : \"https://sammlungen.uni-goettingen.de/rest/image/record_DE-MUS-069123_118/i118_0.jpg\",\n" +
            "    \"state\" : {\n" +
            "      \"type\" : \"TimeState\",\n" +
            "      \"sourceDate\" : \"2020-04-25 02:00:00\",\n" +
            "      \"cached\" : \"https://sammlungen.uni-goettingen.de/objekt/record_DE-MUS-069123_118/1/-/\"\n" +
            "    },\n" +
            "    \"type\" : \"SpecificResource\",\n" +
            "    \"language\" : \"de\",\n" +
            "    \"accessibility\" : \"resizeText/CSSEnabled\",\n" +
            "    \"renderedVia\" : {\n" +
            "      \"id\" : \"https://www.google.com/intl/de_de/chrome/\",\n" +
            "      \"type\" : \"Software\"\n" +
            "    },\n" +
            "    \"selector\" : {\n" +
            "      \"value\" : \"/full/!400,400/0/default.jpg\",\n" +
            "      \"conformsTo\" : \"https://iiif.io/api/image/2.1/#region\",\n" +
            "      \"type\" : \"FragmentSelector\"\n" +
            "    },\n" +
            "    \"rights\" : \"https://www.deutsche-digitale-bibliothek.de/content/lizenzen/rv-fz\"\n" +
            "  },\n" +
            "  \"audience\" : {\n" +
            "    \"id\" : \"https://container.uni-goettingen.de/roles/Studenten\",\n" +
            "    \"type\" : \"Studenten\"\n" +
            "  },\n" +
            "  \"type\" : \"Annotation\",\n" +
            "  \"stylesheet\" : {\n" +
            "    \"value\" : \".blue { color: blue }\",\n" +
            "    \"type\" : \"CssStylesheet3\"\n" +
            "  },\n" +
            "  \"@context\" : \"http://www.w3.org/ns/anno.jsonld\"\n" +
            "}";

    @Test
    public void tryoutJackson() {

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            WebAnnotationEntity entity = mapper.readValue(json, WebAnnotationEntity.class);

            System.out.println(entity.getContext());

            System.out.println(entity);
        } catch (Exception e) {
            //fail(e.getMessage());
            e.printStackTrace();
        }

    }
}
