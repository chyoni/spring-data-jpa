package com.example.datajpa.dto;

import com.example.datajpa.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class MemberQueryDto {
    private String username;
    private int age;
    private Team team;

    public MemberQueryDto(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        this.team = team;
    }
}
