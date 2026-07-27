package com.kevinj.portfolio.issuetrack.storage.exception;

public class EmptyFileException extends StorageException{
    public EmptyFileException() {
        super(StorageErrorCode.EMPTY_FILE);
    }
}
