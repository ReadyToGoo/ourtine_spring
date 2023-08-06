package ourtine.domain.mapping;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import ourtine.domain.Category;
import ourtine.domain.User;
import ourtine.domain.enums.Status;

import javax.persistence.*;
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCategory {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

<<<<<<< HEAD
    @ManyToOne
    //@Column(nullable = false)
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;

    @ManyToOne
    //@Column(nullable = false)
=======
     @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;

     @ManyToOne(fetch = FetchType.LAZY)
>>>>>>> develop
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    @ColumnDefault("'ACTIVE'")
    private Status status;

    public UserCategory(User user, Category category) {
        this.user=user;
        this.category=category;
    }
}
