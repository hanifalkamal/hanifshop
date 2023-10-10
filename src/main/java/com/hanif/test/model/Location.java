package com.hanif.test.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Location {
    private Street street;
    private String city;
    private String state;
    private String country;
    private String postcode;
    private Coordinates coordinates;
    private Timezone timezone;

    public String address(){
        return String.format("%s %s" , street.fullStreet(), city);
    }
}
