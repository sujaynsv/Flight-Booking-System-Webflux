package com.flightapp.model;

import com.flightapp.model.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class Passenger {

    @NotBlank
    private String name;

    @NotNull
    private Gender gender;

    @NotNull
    @Positive
    private Integer age;

    public Passenger() {

    }

    public Passenger(String name, Gender gender, Integer age) {
        this.name = name;
        this.gender = gender;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
