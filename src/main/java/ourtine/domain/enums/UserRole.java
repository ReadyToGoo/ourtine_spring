package ourtine.domain.enums;

public enum UserRole {

    ADMIN("관리자"),
    USER("일반유저");

    private final String description;

    UserRole(String description) {
        this.description=description;
    }

}
