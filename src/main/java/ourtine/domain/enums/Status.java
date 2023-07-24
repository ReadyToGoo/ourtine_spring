package ourtine.domain.enums;

import lombok.Getter;

@Getter
public enum Status {

    ACTIVE("활성"),
    INACTIVE("비활성");
    private final String description;

    Status(String description) {
        this.description=description;
    }

}
