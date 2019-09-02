package ru.dipech.resumes.storage;

import ru.dipech.resumes.storage.serialization.JsonSerializationStrategy;

public class JsonFileStorageTest extends AbstractStorageTest {

    public JsonFileStorageTest() {
        super(new FileStorage(STORAGE_PATH, new JsonSerializationStrategy()));
    }

}
