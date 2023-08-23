package com.github.martinyes.penguinapp.server.data.edit;

import com.github.martinyes.penguinapp.server.ServerGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class EditServerData {

    private String serverName, serverAddr, serverDesc;
    private ServerGroup serverGroup;
}