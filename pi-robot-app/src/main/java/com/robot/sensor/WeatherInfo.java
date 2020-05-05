package com.robot.sensor;

/**
 * Created by Lechu on 04.09.2016.
 */
public class WeatherInfo {

    /**
     * Temperature in °C
     */
    private  double temperature;
    /**
     * Humidity
     */
    private  double humidity;

    /**
     * @param tempDegCel The temperature in °C
     * @param humidity   The humidity value in percent
     */
    public WeatherInfo(double tempDegCel, double humidity) {
        this.temperature = tempDegCel;
        this.humidity = humidity;
    }

    /**
     * Method to get the temperature in °C
     *
     * @return The temperature value in °C
     */
    public double getTemperatureCelsius() {
        return temperature;
    }

    /**
     * Method to get the temperature in °F
     *
     * @return The temperature value in °F
     */
    public double getTemperatureFahrenheit() {
        return temperature * 1.8f + 32;
    }

    /**
     * Method to get the humidity.
     *
     * @return The humidity value in percent
     */
    public double getHumidity() {
        return humidity;
    }
       public void  setHumidity(double humidity) {
           this.humidity = humidity;
       }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "WeatherInfo{" +
            "temperature=" + temperature +
            ", humidity=" + humidity +
            '}';
    }
}

