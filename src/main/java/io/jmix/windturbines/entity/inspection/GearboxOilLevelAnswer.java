package io.jmix.windturbines.entity.inspection;

import io.jmix.core.metamodel.datatype.EnumClass;
import org.springframework.lang.Nullable;


public enum GearboxOilLevelAnswer implements EnumClass<String> {

    LOW("LOW"),
    MEDIUM("MEDIUM"),
    HIGH("HIGH");

    private final String id;

    GearboxOilLevelAnswer(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static GearboxOilLevelAnswer fromId(String id) {
        for (GearboxOilLevelAnswer at : GearboxOilLevelAnswer.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
