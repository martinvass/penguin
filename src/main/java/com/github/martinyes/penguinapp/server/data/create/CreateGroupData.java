package com.github.martinyes.penguinapp.server.data.create;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateGroupData {

    private AppUser user;
    private String groupName;
    private String groupDesc;
}