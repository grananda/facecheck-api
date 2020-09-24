package com.grananda.facecheckapi.utils;

import com.github.javafaker.Faker;
import com.grananda.facecheckapi.domain.User;

public class UserFactory{

    public static User create() {
        return User.builder()
                .email(Faker.instance().internet().emailAddress())
                .firstName(Faker.instance().name().firstName())
                .lastName(Faker.instance().name().lastName())
                .username(Faker.instance().lorem().word())
                .password(Faker.instance().internet().password())
                .build();
    }
}
