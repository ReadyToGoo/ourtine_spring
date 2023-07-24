package ourtine.domain.mapping;

import ourtine.domain.Category;
import ourtine.domain.User;
import ourtine.domain.enums.Status;

import javax.persistence.*;

public class UserCategory {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
    @Enumerated(value = EnumType.STRING)
    private Status status;

}
