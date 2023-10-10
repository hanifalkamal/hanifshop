package com.hanif.test.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Street {
    private String name;
    private Integer number;

    public String fullStreet(){
        return String.format("%s %s", number, name);
    }
}
