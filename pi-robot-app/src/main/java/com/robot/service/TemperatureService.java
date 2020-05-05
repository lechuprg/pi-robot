package com.robot.service;

import com.robot.msg.response.ServiceDetail;
import com.robot.sensor.WeatherInfo;

import java.util.Optional;

/**
 * Created by Lechu on 24.10.2016.
 */
public interface TemperatureService {
    <T>T  checkTemperature(Class<T> tClass);

    <T>T checkAndSave(Class<T> tClass);

    ServiceDetail getTemperatureStatistics(int days);

    Optional<WeatherInfo> checkTemperature();

    ServiceDetail buildServiceDetails(WeatherInfo aVoid);
}
