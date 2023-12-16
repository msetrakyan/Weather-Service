package com.weather.weather.util;

import lombok.Getter;

@Getter
public enum Measurement {

    CELSIUS("CELSIUS"),
    FAHRENHEIT("FAHRENHEIT"),
    KELVIN("KELVIN");
    private final String name;

    Measurement(String name) {
        this.name = name;
    }
}
