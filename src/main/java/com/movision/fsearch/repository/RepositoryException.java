package com.movision.fsearch.repository;

public class RepositoryException extends Exception {

    public RepositoryException() {
    }

    public RepositoryException(String msg) {
        super(msg);
    }

    public RepositoryException(Exception ex) {
        super(ex);
    }

    public RepositoryException(String msg, Exception ex) {
        super(msg, ex);
    }
}
