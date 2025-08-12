package br.com.dbserver.api.domain.exceptions;

public class AssembleiaClosedException extends RuntimeException {
    public AssembleiaClosedException(String message) {
        super(message);
    }
}