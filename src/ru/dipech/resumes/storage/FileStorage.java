package ru.dipech.resumes.storage;

import ru.dipech.resumes.exception.FilePathStorageException;
import ru.dipech.resumes.exception.StorageException;
import ru.dipech.resumes.model.Resume;
import ru.dipech.resumes.storage.serialization.SerializationStrategy;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<File> {

    private File storage;
    private SerializationStrategy serializationStrategy;

    public FileStorage(String storagePath, SerializationStrategy serializationStrategy) {
        Objects.requireNonNull(storagePath, "storagePath can't be null");
        this.serializationStrategy = serializationStrategy;
        storage = new File(storagePath);
        if (!storage.isDirectory()) {
            throw new FilePathStorageException("Isn't a directory", storagePath);
        }
        if (!storage.canRead() || !storage.canWrite()) {
            throw new FilePathStorageException("Storage isn't accessible", storagePath);
        }
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(storage, uuid);
    }

    @Override
    protected boolean checkBySearchKey(File file) {
        return file.exists();
    }

    @Override
    protected Resume getBySearchKey(File file) {
        Resume resume;
        try {
            resume = serializationStrategy.deserialize(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new FilePathStorageException("Can't read object", file.getAbsolutePath(), e);
        }
        return resume;
    }

    @Override
    protected void saveBySearchKey(File file, Resume resume) {
        try {
            file.createNewFile();
            writeToFile(file, resume);
        } catch (IOException e) {
            throw new FilePathStorageException("Can't create object", file.getAbsolutePath(), e);
        }
    }

    @Override
    protected void updateBySearchKey(File file, Resume resume) {
        try {
            writeToFile(file, resume);
        } catch (IOException e) {
            throw new FilePathStorageException("Can't update object", file.getAbsolutePath(), e);
        }
    }

    @Override
    protected void deleteBySearchKey(File file) {
        if (!file.delete()) {
            throw new FilePathStorageException("Can't delete object", file.getAbsolutePath());
        }
    }

    @Override
    protected List<Resume> getAllAsList() {
        File[] files = getStorageChildren();
        if (files.length == 0) {
            return new ArrayList<>();
        }
        List<Resume> resumes = new ArrayList<>();
        for (File file : files) {
            resumes.add(getBySearchKey(file));
        }
        return resumes;
    }

    @Override
    public void clear() {
        File[] children = getStorageChildren();
        for (File child : children) {
            if (!child.delete()) {
                throw new FilePathStorageException("Can't delete object", child.getAbsolutePath());
            }
        }
    }

    @Override
    public int size() {
        File[] children = getStorageChildren();
        return children.length;
    }

    private File[] getStorageChildren() {
        File[] children = storage.listFiles();
        if (children == null) {
            throw new StorageException("Can't read storage children files");
        }
        return children;
    }

    private void writeToFile(File file, Resume resume) throws IOException {
        serializationStrategy.serialize(new BufferedOutputStream(new FileOutputStream(file)), resume);
    }

}
