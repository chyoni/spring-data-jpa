package com.example.datajpa.repository;

import com.example.datajpa.dto.MemberQueryDto;
import com.example.datajpa.entity.Member;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface MemberRepository
        extends JpaRepository<Member, Long>, MemberRepositoryCustom, JpaSpecificationExecutor<Member> {
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

    @Modifying(clearAutomatically = true) // executeUpdate() 후 영속성 컨텍스트 초기화;
    @Query(value = "UPDATE Member m SET m.age = m.age + 1 WHERE m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Override
    @EntityGraph(attributePaths = {"team"}) // 얘는 그냥 페치 조인을 하는거
    List<Member> findAll();

    /**
     * 변경감지를 통해 업데이트를 치는 경우 영속성 컨텍스트는 변경감지를 체크하기 위해 기존 원본데이터(스냅샷)을 저장하는데 그런것들이 결국은 비용이다.
     * 그런 비용조차도 최소한으로 하고싶을 때 이렇게 QueryHint를 사용해서 난 이거로 가져온 데이터는 변경을 절대 하지않을것이니 스냅샷도 만들지마!
     * 라고 QueryHint로 알려줄 수 있다. 그게 아래와 같은 내용.
     * */
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    <T> List<T> findToDtoByUsername(@Param("username") String username, Class<T> clazz);

}