package io.jmix.windturbines.entity;

import io.jmix.core.metamodel.datatype.EnumClass;
import org.springframework.lang.Nullable;


public enum TurbineStatus implements EnumClass<String> {

    OPERATING("OPERATING", "success"),
    MAINTENANCE("MAINTENANCE", "contrast"),
    IDLE("IDLE", "contrast"),
    OUT_OF_SERVICE("OUT_OF_SERVICE", "error");

    private final String id;
    private final String badgeThemeName;

    TurbineStatus(String id, String badgeThemeName) {
        this.id = id;
        this.badgeThemeName = badgeThemeName;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static TurbineStatus fromId(String id) {
        for (TurbineStatus at : TurbineStatus.values()) {
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
