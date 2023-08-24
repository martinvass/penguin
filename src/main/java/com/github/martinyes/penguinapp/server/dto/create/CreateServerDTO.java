package com.github.martinyes.penguinapp.server.dto.create;

import lombok.*;

/**
 * Data Transfer Object (DTO) class for creating a server.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateServerDTO {

    private String address;
    private String name;
    private String description;
}