package io.jmix.windturbines.test_data.entity;

import io.jmix.windturbines.entity.User;
import io.jmix.windturbines.test_data.TestDataProvider;
import net.datafaker.Faker;

import static io.jmix.windturbines.test_data.RandomValues.withRandomSuffix;

public class TechnicanData implements TestDataProvider<User> {

    private String DEFAULT_FIRST_NAME = "Tech";
    private String DEFAULT_LAST_NAME = "Nician";

    @Override
    public Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    public void accept(User user) {
        user.setActive(true);
        String email = "%s@windturbines.com".formatted(
                withRandomSuffix("%s.%s".formatted(DEFAULT_FIRST_NAME, DEFAULT_LAST_NAME)
                )
        );
        user.setEmail(email);
        user.setFirstName(DEFAULT_FIRST_NAME);
        user.setLastName(DEFAULT_LAST_NAME);
        user.setUsername(email);
    }
}
