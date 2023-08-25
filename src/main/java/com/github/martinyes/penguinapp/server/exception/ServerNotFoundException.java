package com.github.martinyes.penguinapp.server.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ServerNotFoundException extends RuntimeException {

    public ServerNotFoundException(String message) {
        super(message);
    }
}