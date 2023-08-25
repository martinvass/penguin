package com.github.martinyes.penguinapp.server.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor

public class ServerGroupNotFoundException extends RuntimeException {

    public ServerGroupNotFoundException(String message) {
        super(message);
    }
}