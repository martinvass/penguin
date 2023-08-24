package com.github.martinyes.penguinapp.server.dto.edit;

import com.github.martinyes.penguinapp.server.ServerGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EditServerDTO {

    private String serverName, serverAddr, serverDesc, serverGroupName;
}