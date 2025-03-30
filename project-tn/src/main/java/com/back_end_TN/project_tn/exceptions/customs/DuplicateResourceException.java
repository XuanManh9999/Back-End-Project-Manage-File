package com.back_end_TN.project_tn.exceptions.customs;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
