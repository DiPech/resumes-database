package ru.dipech.resumes.exception;

public abstract class AbstractItemStorageException extends StorageException {
    private final String uuid;

    public AbstractItemStorageException(String message, String uuid) {
        super(message);
        this.uuid = uuid;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " [UUID: " + uuid + "]";
    }

}
