package io.jmix.windturbines.test_data.entity;

import io.jmix.windturbines.entity.Operator;
import io.jmix.windturbines.test_data.TestDataProvider;

import static io.jmix.windturbines.test_data.RandomValues.*;

public class OperatorData implements TestDataProvider<Operator> {

    @Override
    public Class<Operator> getEntityClass() {
        return Operator.class;
    }

    @Override
    public void accept(Operator operator) {
        operator.setName("Operator " + randomNumber());
        operator.setContactPerson("Contact Person " + randomNumber());
        operator.setEmail(randomEmail());
        operator.setPhone(randomPhoneDigits());
        operator.setAddress("Address " + randomNumber());
    }
}
