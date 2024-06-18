package io.jmix.windturbines.test_data;

import net.datafaker.Faker;
import net.datafaker.providers.base.DateAndTime;
import net.datafaker.providers.base.Number;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class RandomValues {

    public static String randomPhoneDigits() {
        return String.format("+49451%09d", new Random().nextInt(100_000_000));
    }
    public static int randomNumber() {
        return new Random().nextInt(100_000_000);
    }

    public static String randomEmail() {
        return randomId() + "@example.com";
    }

    public static String randomId() {
        return randomUuid().toString();
    }
    public static String withRandomSuffix(String value) {
        return "%s-%s".formatted(value, randomId());
    }

    public static UUID randomUuid() {
        return UUID.randomUUID();
    }


    public static <T> T randomOfList(List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(random().nextInt(list.size()));
    }

    public static <T> T randomOfList(T... list) {
        List<T> collection = Arrays.asList(list);
        if (CollectionUtils.isEmpty(collection)) {
            return null;
        }
        return collection.get(random().nextInt(collection.size()));
    }

    public static Boolean randomBoolean() {
        return random().nextBoolean();
    }

    private static Random random() {
        return new Random();
    }


    public static int randomPositiveNumber(int upUntil) {
        return new Faker().number().numberBetween(1, upUntil);
    }

    public static int randomPositiveNumber(int from, int until) {
        return new Faker().number().numberBetween(from, until);
    }

    public static LocalDateTime randomFutureLocalDateTime(int upUntilInDays) {
        DateAndTime date = new Faker().date();
        Number number = new Faker().number();
        int hour = number.numberBetween(8, 18);
        int minute = number.numberBetween(0, 60);

        return date.future(upUntilInDays, TimeUnit.DAYS)
                .toLocalDateTime()
                .withHour(hour)
                .withMinute(minute);
    }
    public static LocalDate randomPastLocalDate(int atMostInDays) {
        return new Faker().date().past(atMostInDays, TimeUnit.DAYS).toLocalDateTime().toLocalDate();
    }

    public static <T> Optional<T> withLikelihoodOf(double probability, Supplier<T> valueIfTrue) {
        if (probability < 0.0 || probability > 1.0) {
            throw new IllegalArgumentException("Probability must be between 0.0 and 1.0");
        }
        if (new Faker().random().nextDouble() < probability) {
            return Optional.ofNullable(valueIfTrue.get());
        } else {
            return Optional.empty();
        }
    }
}
