package com.test.myapplication.model;

import java.io.Serializable;


public class ModelMain implements Serializable {

    public int id;
    public String name;
    public String description;
    public String img;

    public ModelMain(int id, String name, String img, String description) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.description = description;
    }
}
