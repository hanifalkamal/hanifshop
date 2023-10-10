package com.hanif.test.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Hanif al kamal 10/10/23
 * @contact hanif.alkamal@gmail.com
 */

@Getter
@Setter
public class Person {
    public List<Result> results;
    public Info info;

    public Result firstResult(){
        return results.get(0);
    }
}


