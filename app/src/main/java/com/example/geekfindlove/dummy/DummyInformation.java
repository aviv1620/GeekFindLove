package com.example.geekfindlove.dummy;

import java.util.ArrayList;

public class DummyInformation {
    private String text;
    private ArrayList<String> arrays;

    @Override
    public String toString() {
        return "DummyInformation{" +
                "text='" + text + '\'' +
                ", arrays=" + arrays +
                '}';
    }

    public DummyInformation(){

    }

    public DummyInformation(String text, ArrayList<String> arrays) {
        this.text = text;
        this.arrays = arrays;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setArrays(ArrayList<String> arrays) {
        this.arrays = arrays;
    }

    public String getText() {
        return text;
    }

    public ArrayList<String> getArrays() {
        return arrays;
    }
}
