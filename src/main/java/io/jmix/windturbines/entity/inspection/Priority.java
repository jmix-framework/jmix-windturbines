package io.jmix.windturbines.entity.inspection;

import io.jmix.core.metamodel.datatype.EnumClass;
import org.springframework.lang.Nullable;


public enum Priority implements EnumClass<Integer> {

    LOW(10, "success"),
    MEDIUM(20, "contrast"),
    HIGH(30, "error"),
    CRITICAL(40, "error");

    private final Integer id;
    private final String badgeThemeName;

    Priority(Integer id, String badgeThemeName) {
        this.id = id;
        this.badgeThemeName = badgeThemeName;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static Priority fromId(Integer id) {
        for (Priority at : Priority.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }

    public String getBadgeThemeName() {
        return badgeThemeName;
    }
}
