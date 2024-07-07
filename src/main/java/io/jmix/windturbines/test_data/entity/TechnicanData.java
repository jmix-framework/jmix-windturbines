package io.jmix.windturbines.test_data.entity;

import io.jmix.windturbines.entity.User;
import io.jmix.windturbines.test_data.TestDataProvider;
import net.datafaker.Faker;

import static io.jmix.windturbines.test_data.RandomValues.withRandomSuffix;

public class TechnicanData implements TestDataProvider<User> {

    private final String firstName;
    private final String lastName;
    private final String DEFAULT_FIRST_NAME = "Tech";
    private final String DEFAULT_LAST_NAME = "Nician";

    public TechnicanData(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public TechnicanData() {
        this.firstName = DEFAULT_FIRST_NAME;
        this.lastName = DEFAULT_LAST_NAME;
    }

    @Override
    public Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    public void accept(User user) {
        user.setActive(true);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        String email = "%s@windturbines.com".formatted(
                withRandomSuffix("%s.%s".formatted(firstName, lastName)
                )
        );
        user.setEmail(email);
        user.setUsername(email);
    }
}
