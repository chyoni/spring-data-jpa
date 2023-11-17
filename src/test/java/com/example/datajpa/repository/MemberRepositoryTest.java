package com.example.datajpa.repository;

import com.example.datajpa.dto.MemberQueryDto;
import com.example.datajpa.entity.Member;
import com.example.datajpa.entity.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA", 10);

        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).orElseThrow();

        assertEquals(findMember.getId(), member.getId());
        assertEquals(findMember.getUsername(), member.getUsername());
        assertEquals(findMember, member);
    }

    @Test
    public void basicCRUD() {
        Member memberA = new Member("memberA", 10);
        Member memberB = new Member("memberB", 10);

        memberRepository.save(memberA);
        memberRepository.save(memberB);

        Member findMemberA = memberRepository.findById(memberA.getId()).orElseThrow();
        Member findMemberB = memberRepository.findById(memberB.getId()).orElseThrow();

        assertEquals(memberA, findMemberA);
        assertEquals(memberB, findMemberB);

        List<Member> all =  memberRepository.findAll();
        assertEquals(all.size(), 2);

        long count = memberRepository.count();
        assertEquals(count, 2);

        memberRepository.delete(memberA);
        memberRepository.delete(memberB);
        assertEquals(memberRepository.count(), 0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member memberA = new Member("AAA", 10);
        Member memberB = new Member("AAA", 20);

        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertEquals(result.get(0).getUsername(), "AAA");
        assertEquals(result.get(0).getAge(), 20);
        assertEquals(result.size(), 1);

    }

    @Test
    public void findUserByQuery() {
        Member memberA = new Member("AAA", 10);
        Member memberB = new Member("AAA", 20);

        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<Member> result = memberRepository.findUser("AAA", 10);

        assertEquals(result.get(0).getUsername(), "AAA");
        assertEquals(result.get(0).getAge(), 10);
        assertEquals(result.size(), 1);

    }

    @Test
    public void findUsernameList() {
        Member memberA = new Member("AAA", 10);
        Member memberB = new Member("BBB", 20);

        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<String> usernameList = memberRepository.findUsernameList();

        assertEquals(usernameList.size(), 2);

        List<String> usernames = new ArrayList<>();
        usernames.add("AAA");
        usernames.add("BBB");

        assertEquals(usernameList, usernames);
    }

    @Test
    public void findUsers() {
        Team team = new Team("Team");
        teamRepository.save(team);

        Member memberA = new Member("AAA", 10, team);
        Member memberB = new Member("BBB", 20, team);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<MemberQueryDto> users = memberRepository.findUsers();

        List<MemberQueryDto> mUsers = new ArrayList<>();
        mUsers.add(new MemberQueryDto("AAA", 10, team));
        mUsers.add(new MemberQueryDto("BBB", 20, team));

        assertEquals(users, mUsers);
    }

    @Test
    public void findUsersByUsernames() {
        Member memberA = new Member("AAA", 10);
        Member memberB = new Member("BBB", 20);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<Member> usersByUsernames = memberRepository.findUsersByUsernames(Arrays.asList("AAA", "BBB"));

        List<Member> members = new ArrayList<>();
        members.add(memberA);
        members.add(memberB);

        assertEquals(members, usersByUsernames);
    }
}