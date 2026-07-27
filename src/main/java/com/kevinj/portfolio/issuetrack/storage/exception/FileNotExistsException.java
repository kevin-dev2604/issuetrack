package com.kevinj.portfolio.issuetrack.storage.exception;

public class FileNotExistsException extends StorageException{
    public FileNotExistsException() {
        super(StorageErrorCode.FILE_NOT_EXISTS);
    }
}
