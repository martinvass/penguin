package com.github.martinyes.penguinapp.server.dto.create;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateServerDTO {

    private String address;
    private String name;
    private String description;
}