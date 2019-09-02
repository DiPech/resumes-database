package ru.dipech.resumes.storage.serialization;

import ru.dipech.resumes.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface SerializationStrategy {

    void serialize(OutputStream os, Resume resume) throws IOException;

    Resume deserialize(InputStream is) throws IOException;

}
