package com.hanif.test.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result {
    String gender;
    private Name name;
    private Location location;
    private String email;
    private Login login;
    private DateOfBirth dob;
    private Registered registered;
    private String phone;
    private String cell;
    private Id id;
    private Picture picture;
    private String nat;

}
