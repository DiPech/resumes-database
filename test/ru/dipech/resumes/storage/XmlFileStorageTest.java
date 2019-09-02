package ru.dipech.resumes.storage;

import ru.dipech.resumes.storage.serialization.XmlSerializationStrategy;

public class XmlFileStorageTest extends AbstractStorageTest {

    public XmlFileStorageTest() {
        super(new FileStorage(STORAGE_PATH, new XmlSerializationStrategy()));
    }

}
