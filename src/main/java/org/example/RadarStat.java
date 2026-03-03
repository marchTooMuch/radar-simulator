package org.example;

public class RadarStat {
    private final String name;
    public  String value;

    public RadarStat(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() { return name; }
    public String getValue() { return value; }
}
