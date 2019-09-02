package ru.dipech.resumes.storage;

import ru.dipech.resumes.exception.FilePathStorageException;
import ru.dipech.resumes.exception.StorageException;
import ru.dipech.resumes.model.Resume;
import ru.dipech.resumes.storage.serialization.SerializationStrategy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {

    private Path storage;
    private SerializationStrategy serializationStrategy;

    public PathStorage(String storagePath, SerializationStrategy serializationStrategy) {
        Objects.requireNonNull(storagePath, "storagePath can't be null");
        this.serializationStrategy = serializationStrategy;
        storage = Paths.get(storagePath);
        if (!Files.isDirectory(storage)) {
            throw new FilePathStorageException("Isn't a directory", storagePath);
        }
        if (!Files.isReadable(storage) || !Files.isWritable(storage)) {
            throw new FilePathStorageException("Storage isn't accessible", storagePath);
        }
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return storage.resolve(uuid);
    }

    @Override
    protected boolean checkBySearchKey(Path path) {
        return Files.exists(path);
    }

    @Override
    protected Resume getBySearchKey(Path path) {
        Resume resume;
        try {
            resume = serializationStrategy.deserialize(new BufferedInputStream(Files.newInputStream(path)));
        } catch (IOException e) {
            throw new FilePathStorageException("Can't read object", path.toString(), e);
        }
        return resume;
    }

    @Override
    protected void saveBySearchKey(Path path, Resume resume) {
        try {
            Files.createFile(path);
            writeToFile(path, resume);
        } catch (IOException e) {
            throw new FilePathStorageException("Can't create object", path.toString(), e);
        }
    }

    @Override
    protected void updateBySearchKey(Path path, Resume resume) {
        try {
            writeToFile(path, resume);
        } catch (IOException e) {
            throw new FilePathStorageException("Can't update object", path.toString(), e);
        }
    }

    @Override
    protected void deleteBySearchKey(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new FilePathStorageException("Can't delete object", path.toString(), e);
        }
    }

    @Override
    protected List<Resume> getAllAsList() {
        return getStorageChildren().map(this::getBySearchKey).collect(Collectors.toList());
    }

    @Override
    public void clear() {
        getStorageChildren().forEach(this::deleteBySearchKey);
    }

    @Override
    public int size() {
        return (int) getStorageChildren().count();
    }

    private Stream<Path> getStorageChildren() {
        Stream<Path> stream;
        try {
            stream = Files.list(storage);
        } catch (IOException e) {
            throw new StorageException("Can't read storage children files", e);
        }
        return stream;
    }

    private void writeToFile(Path path, Resume resume) throws IOException {
        serializationStrategy.serialize(new BufferedOutputStream(Files.newOutputStream(path)), resume);
    }

}
