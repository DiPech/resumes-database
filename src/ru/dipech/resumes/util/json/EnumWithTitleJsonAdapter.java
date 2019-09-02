package ru.dipech.resumes.util.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class EnumWithTitleJsonAdapter<T extends Enum<T> & EnumWithTitleInterface> implements JsonSerializer<T> {

    @Override
    public JsonElement serialize(T object, Type type, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", object.name());
        jsonObject.addProperty("title", object.getTitle());
        return jsonObject;
    }

}
