package com.github.martinyes.penguinapp.auth.user.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserAlreadyExistsException extends IllegalStateException {

    public UserAlreadyExistsException(String msg) {
        super(msg);
    }
}