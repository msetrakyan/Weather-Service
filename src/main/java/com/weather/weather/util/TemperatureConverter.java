package com.weather.weather.util;



public class TemperatureConverter {

    public static String converter(Measurement measurement, String temperature) {
        if(measurement.getName().equals(Measurement.FAHRENHEIT.getName())) {
            return toFahrenheit(temperature);
        }
        return toCelsius(temperature);

    }

    private static String toCelsius(String Kelvin) {
        return String.valueOf(Double.parseDouble(Kelvin) - 273.15);
    }

    private static String toFahrenheit(String Kelvin) {
        return String.valueOf((Double.parseDouble(Kelvin) - 273.15) * 1.8 + 32);
    }


}
