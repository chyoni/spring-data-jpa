package com.example.datajpa.repository;

import com.example.datajpa.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberJpaRepositoryTest {

    @Autowired MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA", 10);

        Member savedMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(savedMember.getId());

        assertEquals(findMember.getId(), member.getId());
        assertEquals(findMember.getUsername(), member.getUsername());
        assertEquals(findMember, member);
    }

    @Test
    public void basicCRUD() {
        Member memberA = new Member("memberA", 10);
        Member memberB = new Member("memberB", 10);

        memberJpaRepository.save(memberA);
        memberJpaRepository.save(memberB);

        Member findMemberA = memberJpaRepository.findById(memberA.getId()).orElseThrow();
        Member findMemberB = memberJpaRepository.findById(memberB.getId()).orElseThrow();

        assertEquals(memberA, findMemberA);
        assertEquals(memberB, findMemberB);

        List<Member> all =  memberJpaRepository.findAll();
        assertEquals(all.size(), 2);

        long count = memberJpaRepository.count();
        assertEquals(count, 2);

        memberJpaRepository.delete(memberA);
        memberJpaRepository.delete(memberB);
        assertEquals(memberJpaRepository.count(), 0);
    }
}