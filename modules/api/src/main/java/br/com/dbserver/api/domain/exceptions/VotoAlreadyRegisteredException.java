package br.com.dbserver.api.domain.exceptions;

public class VotoAlreadyRegisteredException extends RuntimeException {
    public VotoAlreadyRegisteredException(String message) {
        super(message);
    }
}