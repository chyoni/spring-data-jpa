package com.example.datajpa.controller;

import com.example.datajpa.entity.Member;
import com.example.datajpa.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    /**
     * 도메인 클래스 컨버터를 기본으로 스프링 부트는 해주는데, 즉, Long id는 pk를 의미하고 그 pk로부터 유저를 가져오는데
     * 그것을 스프링 부트가 대신해줘서 애시당초에 파라미터로 받을때 해당 엔티티로 받을 수 있다. 근데 그렇게 막 자주 쓰이진 않는다고 한다.
     * */
    @GetMapping("/members2/{id}")
    public String findMember(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    @PostConstruct
    public void init() {
        memberRepository.save(new Member("member1", 10));
    }
}
