package com.project.internship.studentzone;

/**
 * Created by Rahul on 15-Sep-17.
 */

public class Model {
    private int key;
    private String name;

    public Model() {
    }

    public Model(String key, String name) {
        this.key = Integer.parseInt(key);
        this.name = name;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
