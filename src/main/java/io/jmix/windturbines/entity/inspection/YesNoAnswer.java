package io.jmix.windturbines.entity.inspection;

import io.jmix.core.metamodel.datatype.EnumClass;
import org.springframework.lang.Nullable;


public enum YesNoAnswer implements EnumClass<String> {

    YES("YES"),
    NO("NO");

    private final String id;

    YesNoAnswer(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static YesNoAnswer fromId(String id) {
        for (YesNoAnswer at : YesNoAnswer.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
