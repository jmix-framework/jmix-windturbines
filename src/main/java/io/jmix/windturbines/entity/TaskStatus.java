package io.jmix.windturbines.entity;

import io.jmix.core.metamodel.datatype.EnumClass;
import org.springframework.lang.Nullable;


public enum TaskStatus implements EnumClass<String> {

    SCHEDULED("SCHEDULED", "scheduled"),
    DEFERRED("DEFERRED", "deferred"),
    COMPLETED("COMPLETED", "completed");

    private final String id;
    private final String badgeThemeName;

    TaskStatus(String id, String badgeThemeName) {
        this.id = id;
        this.badgeThemeName = badgeThemeName;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static TaskStatus fromId(String id) {
        for (TaskStatus at : TaskStatus.values()) {
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
