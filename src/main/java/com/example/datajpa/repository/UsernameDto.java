package com.example.datajpa.repository;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class UsernameDto {
    private String username;

    public UsernameDto(String username) {
        this.username = username;
    }
}
