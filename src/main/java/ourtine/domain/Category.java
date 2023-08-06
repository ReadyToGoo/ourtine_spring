package ourtine.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ourtine.domain.enums.CategoryList;
import ourtine.domain.mapping.UserCategory;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CategoryList name;

    @OneToMany(mappedBy = "category")
    private List<UserCategory> userCategoryList;


    @Builder
    public Category(CategoryList name){
        this.name = name;
    }

}
