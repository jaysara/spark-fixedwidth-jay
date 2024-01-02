package com.fixedwidth.jay.datasource;


import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Person implements Serializable {
    public String idString;
    public String fname;
    public String laname;
    public String email;
    public String gender;
    public String ip;

    public static Person createPersonFromString(String personString)
    {
        // System.out.println("Person string is "+personString);
        return new Person(personString.substring(0,2),
        personString.substring(2,10),
        personString.substring(10,18),
        personString.substring(18,41),
        personString.substring(41,48),
        personString.substring(48, 63));    
    }
}

