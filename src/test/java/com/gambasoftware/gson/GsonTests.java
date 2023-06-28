package com.gambasoftware.gson;

import com.gambasoftware.gson.config.LocalDateTimeDeserializer;
import com.gambasoftware.gson.config.LocalDateTimeSerializer;
import com.gambasoftware.gson.models.NoDefaultConstructor;
import com.gambasoftware.gson.models.Person;
import com.gambasoftware.gson.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GsonTests {

    @Test
    public void primitive_examples() {
        Gson gson = new Gson();

        assertEquals("1", gson.toJson(1));
        assertEquals("2", gson.toJson(2L));
        assertEquals("\"abcd\"", gson.toJson("abcd"));
        assertEquals("[1,2,3]", gson.toJson(new int[]{1, 2, 3}));
    }

    @Test
    public void object_serialize_example() {
        var dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        Gson gson = gsonBuilder.create();
        User user = new User();
        user.setId(100L);
        user.setName("Marlon");
        user.setEmail("marlon@gambasoftware.com");
        user.setCreatedAt(LocalDateTime.now());

        assertEquals("{\"id\":100,\"name\":\"Marlon\",\"email\":\"marlon@gambasoftware.com\",\"createdAt\":\""+
                user.getCreatedAt().format(dateFormatter) +"\"}", gson.toJson(user));
    }

    @Test
    public void object_deserialize_example() {
        var dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        LocalDateTime localDateTime = LocalDateTime.of(2023,6,19,11,11,11);
        String localDateTimeAsString = localDateTime.format(dateFormatter);
        String json = "{\"id\":100,\"name\":\"Marlon\",\"email\":\"marlon@gambasoftware.com\",\"createdAt\":\""+
                localDateTimeAsString +"\"}";

        User user = gson.fromJson(json, User.class);

        assertEquals(Long.valueOf(100L), user.getId());
        assertEquals("Marlon", user.getName());
        assertEquals("marlon@gambasoftware.com", user.getEmail());
        assertEquals(LocalDateTime.parse(localDateTimeAsString, dateFormatter), user.getCreatedAt());
    }

    @Test
    public void map_deserialize_example(){
        Gson gson = new Gson();

        TypeToken<Map<String, String>> mapTypeToken = new TypeToken<>() {};
        String json = "{\"key\":\"value\"}";

        Map<String, String> map = gson.fromJson(json, mapTypeToken);
        assertEquals("value", map.get("key"));
    }

    @Test
    public void generic_type_serialization(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.create();

        Person<User> person = new Person<>();
        User user = new User();
        user.setEmail("marlon@email.com");
        person.setPerson(new User());
        String json = gson.toJson(person);
        Person<User> result = gson.fromJson(json, new TypeToken<Person<User>>() {}.getType());

        assertNotNull(json);
        assertEquals(person.getPerson().getEmail(), result.getPerson().getEmail());
    }

}
