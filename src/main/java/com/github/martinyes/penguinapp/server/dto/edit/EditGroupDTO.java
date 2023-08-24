package com.github.martinyes.penguinapp.server.dto.edit;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EditGroupDTO {

    private String groupName, groupDesc;
}