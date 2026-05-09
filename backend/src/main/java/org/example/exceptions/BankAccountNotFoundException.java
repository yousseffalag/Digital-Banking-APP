package org.example.exceptions;

@org.springframework.web.bind.annotation.ResponseStatus(org.springframework.http.HttpStatus.NOT_FOUND)
public class BankAccountNotFoundException extends Exception {
    public BankAccountNotFoundException(String message) {
        super(message);
    }
}