package com.gambasoftware.gson.models;

public class Person<T> {
    T person;

    public T getPerson() {
        return person;
    }

    public void setPerson(T person) {
        this.person = person;
    }
}
