package com.github.martinyes.penguinapp.server.dto.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) class for creating a server group.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateGroupDTO {

    private String groupName;
    private String groupDesc;
}