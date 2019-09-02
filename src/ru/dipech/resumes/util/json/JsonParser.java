package ru.dipech.resumes.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.dipech.resumes.model.Section;

import java.io.Reader;
import java.io.Writer;

public class JsonParser {

    private final Gson gson;

    public JsonParser() {
        this(builder -> {});
    }

    public JsonParser(Initializer initializer) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Section.class, new SectionJsonAdapter());
        initializer.init(builder);
        gson = builder.create();
    }

    public <T> T read(Reader reader, Class<T> clazz) {
        return gson.fromJson(reader, clazz);
    }

    public <T> T read(String string, Class<T> clazz) {
        return gson.fromJson(string, clazz);
    }

    public <T> void write(Writer writer, T t) {
        gson.toJson(t, writer);
    }

    public <T> String write(T t) {
        return gson.toJson(t);
    }

    public <T> String write(T t, Class<T> clazz) {
        return gson.toJson(t, clazz);
    }

    public interface Initializer {
        void init(GsonBuilder builder);
    }

}
