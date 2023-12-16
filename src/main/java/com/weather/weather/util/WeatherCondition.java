package com.weather.weather.util;

import lombok.*;

@Getter
@AllArgsConstructor
public enum WeatherCondition {

    CLEAR_SKY("Clear sky"),
    FEW_CLOUDS("Few clouds"),
    SCATTERED_CLOUDS("Scattered clouds"),
    BROKEN_CLOUDS("Broken clouds"),
    SHOWER_RAIN("Shower rain"),
    RAIN("Rain"),
    THUNDERSTORM("Thunderstorm"),
    SNOW("Snow"),
    MIST("Mist"),
    UNKNOWN("Unknown");

    private final String description;

}
