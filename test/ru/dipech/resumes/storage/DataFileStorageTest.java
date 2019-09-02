package ru.dipech.resumes.storage;

import ru.dipech.resumes.storage.serialization.DataSerializationStrategy;

public class DataFileStorageTest extends AbstractStorageTest {

    public DataFileStorageTest() {
        super(new FileStorage(STORAGE_PATH, new DataSerializationStrategy()));
    }

}
