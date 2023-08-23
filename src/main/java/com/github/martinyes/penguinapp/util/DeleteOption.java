package com.github.martinyes.penguinapp.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeleteOption {
    YES("Yes, delete them"),
    NO("No, keep them");

    private final String message;
}