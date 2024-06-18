package io.jmix.windturbines.entity.inspection;

import io.jmix.core.metamodel.datatype.EnumClass;
import org.springframework.lang.Nullable;


public enum RotorBladesAnswer implements EnumClass<String> {

    YES("YES"),
    NO("NO"),
    MAJOR("MAJOR"),
    MINOR("MINOR");

    private final String id;

    RotorBladesAnswer(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static RotorBladesAnswer fromId(String id) {
        for (RotorBladesAnswer at : RotorBladesAnswer.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
