package com.example.datajpa.repository;

import com.example.datajpa.dto.MemberQueryDto;
import com.example.datajpa.entity.Member;
import com.example.datajpa.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
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
    @PersistenceContext
    EntityManager entityManager;

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

    @Test
    public void paging() {
        Team team = new Team("TEAMA");
        teamRepository.save(team);

        memberRepository.save(new Member("member1", 10, team));
        memberRepository.save(new Member("member2", 10, team));
        memberRepository.save(new Member("member3", 10, team));
        memberRepository.save(new Member("member4", 10, team));
        memberRepository.save(new Member("member5", 10, team));

        int age = 10;
        PageRequest page =
                PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Page<Member> byAge = memberRepository.findByAge(age, page);

        List<MemberQueryDto> memberQueryDtos =
                byAge.map(m -> new MemberQueryDto(m.getUsername(), m.getAge(), m.getTeam()))
                        .toList();

        List<Member> content = byAge.getContent(); // 데이터
        long totalElements = byAge.getTotalElements(); // 전체 개수

        assertEquals(content.size(), 3);
        assertEquals(totalElements, 5);
        assertEquals(byAge.getNumber(), 0); // 현재 페이지
        assertEquals(byAge.getTotalPages(), 2); // 전체 페이지
        assertTrue(byAge.isFirst()); // 첫번째 페이지입니까?
        assertTrue(byAge.hasNext()); // 다음 페이지가 있습니까?

        for (MemberQueryDto member : memberQueryDtos) {
            System.out.println("member = " + member);
        }

        /*Pageable pageable = byAge.nextPageable();
        Page<Member> byAge1 = memberRepository.findByAge(age, pageable);

        assertEquals(byAge1.getContent().size(), 2);
        assertEquals(byAge1.getTotalElements(), 5);
        assertEquals(byAge1.getNumber(), 1);
        assertEquals(byAge1.getTotalPages(), 2);
        assertFalse(byAge1.isFirst());
        assertFalse(byAge1.hasNext());

        for (Member member : byAge1) {
            System.out.println("member = " + member);
        }*/
    }

    @Test
    public void getSlice() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest page =
                PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Slice<Member> sliceByAge = memberRepository.getSliceByAge(age, page);

        assertEquals(sliceByAge.getSize(), 3);
        assertEquals(sliceByAge.getNumber(), 0);
        assertEquals(sliceByAge.getNumberOfElements(), 3);
        assertTrue(sliceByAge.isFirst());
        assertTrue(sliceByAge.hasNext());

        for (Member member : sliceByAge.getContent()) {
            System.out.println("member = " + member);
        }

        Pageable pageable = sliceByAge.nextPageable();
        Slice<Member> sliceByAge1 = memberRepository.getSliceByAge(age, pageable);

        for (Member member : sliceByAge1.getContent()) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void bulk() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        int result = memberRepository.bulkAgePlus(20);
        List<Member> notAcceptedBulk = memberRepository.findAll();
        for (Member member : notAcceptedBulk) {
            System.out.println("not accepted member = " + member);
        }

        /*! 이걸 하는 이유는 벌크 연산을 하는것은 DB에 다이렉트로 반영을 하는것이기 때문에 영속성 컨텍스트와는 무관하다.
          ! 근데 영속성 컨텍스트의 속성중 하나인 같은 트랜잭션 내 영속 컨텍스트에 들어있는 객체는 동일성을 보장하기 때문에
          ! 위에서 만든 멤버들은 벌크 연산을 수행한 후 이 트랜잭션안에서는 그 값이 반영되어 있는 상태가 아니다. 그래서 벌크 연산후에 같은 트랜잭션에서
          ! 또 추가적인 로직이 있는 경우 반드시 영속성 컨텍스트를 초기화한 후 진행해야 한다.
          ! 근데, 스프링 데이터 JPA가 @Modifying(clearAutomatically = true)로 해놓으면 아래 코드를 자동으로 실행해준다.
        entityManager.flush();
        entityManager.clear();
         */

        List<Member> acceptedBulk = memberRepository.findAll();
        for (Member member : acceptedBulk) {
            System.out.println("member = " + member);
        }

        assertEquals(result, 3);

    }

    @Test
    public void findMemberLazy() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);

        memberRepository.save(member1);
        memberRepository.save(member2);

        entityManager.flush();
        entityManager.clear();

        List<Member> all = memberRepository.findAll();
        for (Member member : all) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member team = " + member.getTeam().getName());
        }
    }

    @Test
    public void queryHint() {
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        entityManager.flush();
        entityManager.clear();

    }

    @Test
    public void callCustom() {
        List<Member> memberCustom = memberRepository.findMemberCustom();
    }

    @Test
    public void JpaEventBaseEntity() {
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1); // @PrePersist

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        member1.setUsername("member2"); // @PreUpdate

        entityManager.flush();
        entityManager.clear();

        Member findMember = memberRepository.findById(member1.getId()).get();

        System.out.println("findMember createDate = " + findMember.getCreatedDate());
        System.out.println("findMember updateDate = " + findMember.getLastModifiedDate());
        System.out.println("findMember createdBy = " + findMember.getCreatedBy());
        System.out.println("findMember lastModifiedBy = " + findMember.getLastModifiedBy());
    }

    @Test
    public void specBasic() {
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member memberA = new Member("memberA", 10, team);
        Member memberB = new Member("memberB", 20, team);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        entityManager.flush();
        entityManager.clear();

        // Specification
        Specification<Member> spec = MemberSpec.username("memberA").and(MemberSpec.teamName("teamA"));
        List<Member> all = memberRepository.findAll(spec);

        assertEquals(all.size(), 1);
    }

    @Test
    public void queryByExample() {
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member memberA = new Member("memberA", 10, team);
        Member memberB = new Member("memberB", 20, team);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        entityManager.flush();
        entityManager.clear();

        Member member = new Member("memberA", 10);
        Example<Member> example = Example.of(member);
        List<Member> all = memberRepository.findAll(example);

        assertEquals(all.size(), 1);
    }

    @Test
    public void projections() {
        Member memberA = new Member("memberA", 10);
        Member memberB = new Member("memberB", 20);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        entityManager.flush();
        entityManager.clear();

        List<UsernameDto> members = memberRepository.findToDtoByUsername("memberA", UsernameDto.class);
        for (UsernameDto member : members) {
            System.out.println("member = " + member);
        }
    }
}