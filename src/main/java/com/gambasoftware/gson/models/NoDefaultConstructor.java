package com.gambasoftware.gson.models;

public class NoDefaultConstructor {
    private String value;

    private NoDefaultConstructor(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "NoDefaultConstructor{" +
                "value='" + value + '\'' +
                '}';
    }
}
