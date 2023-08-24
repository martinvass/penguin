package com.github.martinyes.penguinapp.server.dto.edit;

import lombok.*;

/**
 * Data Transfer Object (DTO) class for editing a server group.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EditGroupDTO {

    private String groupName, groupDesc;
}