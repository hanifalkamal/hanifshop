package com.hanif.test.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Name {
    private String title;
    private String first;
    private String last;

    public String fullName(){
        return String.format("%s %s %s", title, first, last);
    }
}
