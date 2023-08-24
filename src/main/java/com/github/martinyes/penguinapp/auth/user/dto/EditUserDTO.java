package com.github.martinyes.penguinapp.auth.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) class for editing a user.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditUserDTO {

    private String username, email;
}