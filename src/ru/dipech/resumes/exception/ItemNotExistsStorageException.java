package ru.dipech.resumes.exception;

public class ItemNotExistsStorageException extends AbstractItemStorageException {
    public ItemNotExistsStorageException(String uuid) {
        super("Item not exists in storage", uuid);
    }
}
