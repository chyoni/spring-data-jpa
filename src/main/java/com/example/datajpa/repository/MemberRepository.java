package com.example.datajpa.repository;

import com.example.datajpa.dto.MemberQueryDto;
import com.example.datajpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    @Query(value = "SELECT m FROM Member m WHERE m.username = :username AND m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    //! 값 조회
    @Query(value = "SELECT m.username FROM Member m")
    List<String> findUsernameList();

    //! DTO 로 조회
    @Query(value = "" +
            "SELECT new com.example.datajpa.dto.MemberQueryDto(m.username, m.age, t) " +
            "FROM Member m " +
            "JOIN m.team t")
    List<MemberQueryDto> findUsers();

    //! IN 절 컬렉션으로 조회
    @Query(value = "SELECT m FROM Member m WHERE m.username in :names")
    List<Member> findUsersByUsernames(@Param("names") Collection<String> names);
}
