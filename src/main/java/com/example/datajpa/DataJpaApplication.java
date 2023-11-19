package com.example.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing
@SpringBootApplication
public class DataJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataJpaApplication.class, args);
    }

    /**
     * 이건 이제 엔티티에서 @createdBy, @lastModifiedBy를 쓸 때 엔티티가 새로 생성 또는 업데이트 될 때 이 아래 빈을 호출해서 값을 받은것을
     * 저 어노테이션이 달린 필드들이 그때마다 꺼내가서 채워준다.
     * */
    @Bean
    public AuditorAware<String> auditorProvider() {
        // ! 실제로는 여기서 어떤 유저인지 찾아서 (유저를 관리하는 방법에 따라 달라지겠지) 그 유저를 저 Optional.of()에 넣으면 된다.
        return () -> Optional.of(UUID.randomUUID().toString());
    }
}
