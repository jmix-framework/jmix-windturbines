package io.jmix.windturbines.test_data.entity;

import io.jmix.windturbines.entity.Manufacturer;
import io.jmix.windturbines.entity.Operator;
import io.jmix.windturbines.entity.Turbine;
import io.jmix.windturbines.entity.TurbineStatus;
import io.jmix.windturbines.test_data.TestDataProvider;
import net.datafaker.Faker;

import static io.jmix.windturbines.test_data.RandomValues.*;

public class TurbineData implements TestDataProvider<Turbine> {

    public static String DEFAULT_MODEL = "Model";
    private final Manufacturer manufacturer;
    private final Operator operator;

    public TurbineData(Manufacturer manufacturer, Operator operator) {
        this.manufacturer = manufacturer;
        this.operator = operator;

    }

    @Override
    public Class<Turbine> getEntityClass() {
        return Turbine.class;
    }

    @Override
    public void accept(Turbine turbine) {
        turbine.setManufacturer(manufacturer);
        turbine.setOperator(operator);
        turbine.setTurbineId("#" + randomNumber());
        turbine.setModel(withRandomSuffix(DEFAULT_MODEL));
        turbine.setStatus(new Faker().options().option(TurbineStatus.values()));
        turbine.setLocation(new Faker().address().fullAddress());
        turbine.setRotorDiameter(randomPositiveNumber(70, 120));
        turbine.setHeight(randomPositiveNumber(70, 130));
        turbine.setInstallationDate(randomPastLocalDate(5 * 365));
        turbine.setLastMaintenanceDate(
                withLikelihoodOf(0.7, () -> randomPastLocalDate(5 * 365)).orElse(null)
        );
    }
}
