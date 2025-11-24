package com.flightapp.model;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "airlines")
public class Airline {

    @Id
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private String code;

    public Airline() {
    }

    public Airline(String id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
