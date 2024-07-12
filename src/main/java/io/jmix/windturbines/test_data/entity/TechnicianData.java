package io.jmix.windturbines.test_data.entity;

import io.jmix.windturbines.entity.User;
import io.jmix.windturbines.test_data.TestDataProvider;

import static io.jmix.windturbines.test_data.RandomValues.withRandomSuffix;

public class TechnicianData implements TestDataProvider<User> {

    private final String firstName;
    private final String lastName;
    private final String DEFAULT_FIRST_NAME = "Tech";
    private final String DEFAULT_LAST_NAME = "Nician";

    public TechnicianData(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public TechnicianData() {
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
