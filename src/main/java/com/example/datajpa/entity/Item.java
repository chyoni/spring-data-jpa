package com.example.datajpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseEntity implements Persistable<String> {

    @Id
    private String id;

    public Item(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    /**
     * 이거는 이제 이 엔티티의 기본키를 @GeneratedValue 사용하지 않고 직접 할당하는 방식으로 구현하는 경우,
     * JpaRepository save() 메소드를 사용할 때 이 엔티티를 생성하는 시점에 직접 할당한 Id가 있기 때문에 save() 메소드를 사용할 때
     * 이미 DB에 있는 엔티티로 판단한다. 그래서 persist()가 아닌 merge()를 호출하는 문제가 생기는데 이를 방지하기 위해
     * Persistable interface를 구현해서 isNew를 Override 해야한다.
     * */
    @Override
    public boolean isNew() {
        return this.getCreatedDate() == null;
    }
}
