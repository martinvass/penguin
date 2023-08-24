package com.github.martinyes.penguinapp.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * An enum representing options for deletion of servers within a group. It also provides messages for the frontend.
 */
@Getter
@AllArgsConstructor
public enum DeleteOption {
    /**
     * Option indicating to proceed with deletion of servers within a group.
     */
    YES("Yes, delete them"),

    /**
     * Option indicating to keep the items and not proceed with deletion of servers within a group.
     */
    NO("No, keep them");

    private final String message;
}