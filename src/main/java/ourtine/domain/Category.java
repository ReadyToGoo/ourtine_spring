package ourtine.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // 운동, 생활습관, 독서, 스터디, 외국어, 취미생활, 돈관리, 커리어

    @Builder
    public Category(String name){
        this.name = name;
    }

}
