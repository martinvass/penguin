package com.github.martinyes.penguinapp.server.data;

import com.github.martinyes.penguinapp.auth.user.AppUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CreateServerData {

    private AppUser user;
    private String address;
    private String name;
    private String description;
}