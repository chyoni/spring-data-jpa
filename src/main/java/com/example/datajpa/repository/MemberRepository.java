package com.example.datajpa.repository;

import com.example.datajpa.dto.MemberQueryDto;
import com.example.datajpa.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

    /* 만약 쿼리문이 복잡한 경우 그 복잡한 쿼리문에 대해 카운트 쿼리도 복잡해지기 때문에 성능 이슈가 있을 수 있다. 그럴 땐 이렇게 카운트 쿼리를 분리할 수 있다.
    @Query(value = "SELECT m FROM Member m JOIN m.team t",
            countQuery = "SELECT COUNT(m.username) FROM Member m")*/
    Page<Member> findByAge(int age, Pageable pageable);
    Slice<Member> getSliceByAge(int age, Pageable pageable);
}
