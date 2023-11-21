package com.example.datajpa.repository;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class UsernameDto {
    private String username;

    /**
     * Projections 을 위해 사용되는 클래스는 반드시 생성자의 이 파라미터 이름이 매핑하려는 데이터와 일치해야한다.
     * */
    public UsernameDto(String username) {
        this.username = username;
    }
}
