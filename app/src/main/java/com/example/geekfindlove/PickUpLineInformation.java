package com.example.geekfindlove;

//to save key
public class PickUpLineInformation {
    private String key;
    private String value;

    public PickUpLineInformation(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public PickUpLineInformation() {
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "PickUpLineInformation{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
