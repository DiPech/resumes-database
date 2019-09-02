package ru.dipech.resumes.exception;

public class FilePathStorageException extends StorageException {
    private final String filePath;

    public FilePathStorageException(String message, String filePath) {
        this(message, filePath, null);
    }

    public FilePathStorageException(String message, String filePath, Exception exception) {
        super(message, exception);
        this.filePath = filePath;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " [path: " + filePath + "]";
    }

}
