package ru.dipech.resumes.storage.serialization;

import ru.dipech.resumes.model.Resume;
import ru.dipech.resumes.util.json.JsonParser;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class JsonSerializationStrategy implements SerializationStrategy {

    private final JsonParser jsonParser = new JsonParser();

    @Override
    public void serialize(OutputStream os, Resume resume) throws IOException {
        try (Writer w = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
            jsonParser.write(w, resume);
        }
    }

    @Override
    public Resume deserialize(InputStream is) throws IOException {
        try (Reader r = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            return jsonParser.read(r, Resume.class);
        }
    }

}
