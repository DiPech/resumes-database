package ru.dipech.resumes.storage.serialization;

import ru.dipech.resumes.exception.StorageException;
import ru.dipech.resumes.model.Resume;

import java.io.*;

public class ObjectSerializationStrategy implements SerializationStrategy {

    @Override
    public void serialize(OutputStream os, Resume resume) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(resume);
        }
    }

    @Override
    public Resume deserialize(InputStream is) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(is)) {
            return (Resume) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new StorageException("Error read resume", e);
        }
    }

}
