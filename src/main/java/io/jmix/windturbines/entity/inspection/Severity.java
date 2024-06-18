package io.jmix.windturbines.entity.inspection;

import io.jmix.core.metamodel.datatype.EnumClass;
import org.springframework.lang.Nullable;


public enum Severity implements EnumClass<Integer> {
    LOW(10, "success"),
    MEDIUM(20, "contrast"),
    HIGH(30, "error");


    private final Integer id;
    private final String badgeThemeName;


    Severity(Integer id, String badgeThemeName) {
        this.id = id;
        this.badgeThemeName = badgeThemeName;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static Severity fromId(Integer id) {
        for (Severity at : Severity.values()) {
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
