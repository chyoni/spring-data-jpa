package com.example.datajpa.repository;

import com.example.datajpa.entity.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * JPA의 인터페이스만으로 해결이 되는 경우가 아니라 QueryDSL같은것을 사용해야 하는 경우 구현을 따로 해야한다.
 * 그럴 때 하는 구현 규칙 (Custom 하는 구현체의 이름은 XXX + Impl).
 * 근데 항상 생각할점은 이렇게 반드시 커스텀 리포지토리를 만들고 JPA 리포지토리로 사용하는 인터페이스에서 이것을 구현해야 하는건 아니다.
 * 그냥 자기만의 리포지토리를 만들고 거기서 이 아래처럼 엔티티매니저 인젝션해서 QueryDSL을 사용하던 뭘 사용하던 해서 만든 후에
 * 가져다가 사용하는 클래스에서는 그 리포지토리를 인젝션하면 된다. 막 규칙처럼 반드시 정해져있는 그런것은 아니니까 유도리있게 하면 된다.
 * */
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {
    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("SELECT m FROM Member m", Member.class).getResultList();
    }
}
