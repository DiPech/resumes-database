package ru.dipech.resumes.storage;

import ru.dipech.resumes.storage.serialization.ObjectSerializationStrategy;

public class ObjectFileStorageTest extends AbstractStorageTest {

    public ObjectFileStorageTest() {
        super(new FileStorage(STORAGE_PATH, new ObjectSerializationStrategy()));
    }

}
