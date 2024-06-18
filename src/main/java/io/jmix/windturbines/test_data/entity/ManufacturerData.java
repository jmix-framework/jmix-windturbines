package io.jmix.windturbines.test_data.entity;

import io.jmix.windturbines.entity.Manufacturer;
import io.jmix.windturbines.test_data.TestDataProvider;

public class ManufacturerData implements TestDataProvider<Manufacturer> {

    private final String name;

    public ManufacturerData(String name) {
        this.name = name;
    }

    @Override
    public Class<Manufacturer> getEntityClass() {
        return Manufacturer.class;
    }

    @Override
    public void accept(Manufacturer manufacturer) {
        manufacturer.setName(name);
    }
}
