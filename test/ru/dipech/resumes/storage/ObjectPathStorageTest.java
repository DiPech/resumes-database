package ru.dipech.resumes.storage;

import ru.dipech.resumes.storage.serialization.ObjectSerializationStrategy;

public class ObjectPathStorageTest extends AbstractStorageTest {

    public ObjectPathStorageTest() {
        super(new PathStorage(STORAGE_PATH, new ObjectSerializationStrategy()));
    }

}
