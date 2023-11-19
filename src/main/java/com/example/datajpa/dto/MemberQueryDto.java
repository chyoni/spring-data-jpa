package com.example.datajpa.dto;

import com.example.datajpa.entity.Member;
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

    /**
    * 이렇게 DTO는 엔티티를 바라봐도 된다. 그러나 반대(엔티티가 DTO를 바라보는 경우)는 안된다.
    * 그래서 좀 더 간단하게 DTO로 엔티티를 이런식으로 변환할 수 있다. (아 물론 당연히 엔티티가 DTO의 필드로 들어가면 안되고)
    * */
    public MemberQueryDto(Member member) {
        this.username = member.getUsername();
        this.age = member.getAge();
        this.team = member.getTeam();
    }
}
