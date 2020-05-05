package com.robot.service.impl;

import com.robot.db.PropertiesDb;
import com.robot.db.WeatherDb;
import com.robot.detector.Detector;
import com.robot.mail.MailClient;
import com.robot.msg.response.OperationTypes;
import com.robot.msg.response.ServiceDetail;
import com.robot.property.PiProperty;
import com.robot.sensor.DHT22SensorReader;
import com.robot.sensor.WeatherInfo;
import com.robot.service.GuardService;
import com.robot.service.TemperatureService;
import com.robot.utils.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Optional;

import static com.robot.db.WeatherDb.HUMIDITY;
import static com.robot.db.WeatherDb.TEMPERATURE;

/**
 * Created by Lechu on 11.09.2016.
 */
@Service
public class TemperatureServiceImpl implements Detector, TemperatureService {
    public static final double DEF_MAX_TEMP_VALUE = 35.0;
    public static final double DEF_MIN_TEMP_VALUE = 17.0;
    Logger logger = LoggerFactory.getLogger(TemperatureServiceImpl.class);

    @Resource(name = "temperatureSensor")
    private DHT22SensorReader temperatureSensor;

    @Autowired
    private WeatherDb weatherDb;

    @Autowired
    private PropertiesDb propertiesDb;

    @Autowired
    private MailClient mailClient;

    @Override
    public void run() {
        logger.info("Check temperature scheduled.");
        WeatherInfo weatherInfo = checkAndSave(WeatherInfo.class);

        double tempMax = getDoubleFromProp(PiProperty.TEMPERATURE_MAX_THRESHOLD, DEF_MAX_TEMP_VALUE);
        double tempMin = getDoubleFromProp(PiProperty.TEMPERATURE_MIN_THRESHOLD, DEF_MIN_TEMP_VALUE);
        logger.info("Temperature check");
        sendAlert(weatherInfo, tempMax, tempMin);
    }

    private void sendAlert(WeatherInfo weatherInfo, double tempMax, double tempMin) {
        if (tempMax < weatherInfo.getTemperatureCelsius() || weatherInfo.getTemperatureCelsius() < tempMin) {
            logger.warn("Sending TEMPERATURE allert");
            String threshHold = propertiesDb.readProperty(PiProperty.NUMBER_OF_TEMP_WARNINGS);
            int countAlerts = 0;
            if (threshHold != null && threshHold != "") {
                countAlerts = Integer.valueOf(threshHold);
            }
            propertiesDb.updateProperty(PiProperty.NUMBER_OF_TEMP_WARNINGS, String.valueOf(++countAlerts));
            String emailTo = propertiesDb.readProperty(PiProperty.NOTIFICATION_EMAIL);
            logger.info("About to send email.");
            mailClient.sendEmail("TEMPERATURE ALERT!", String.format("Current temperature %s humidity %s", weatherInfo.getTemperatureCelsius(), weatherInfo.getHumidity()), null, emailTo);
        }
    }

    private double getDoubleFromProp(PiProperty prop, double defValue) {
        String tempMaxS = propertiesDb.readProperty(prop);
        if (!StringUtils.isEmpty(tempMaxS)) {
            return Double.valueOf(tempMaxS);
        }
        return defValue;
    }

    public <T> T checkTemperature(Class<T> tClass) {
        Optional<WeatherInfo> sensorReadings = checkTemperature();
        if (sensorReadings.isPresent()) {
            logger.info("Readings from sensor: {}", sensorReadings);
            weatherDb.saveCurrentWeather(sensorReadings.get());
            logger.info("Save to DB done.");
            if (ServiceDetail.class.equals(tClass)) {
                return (T) buildServiceDetails(sensorReadings.get());
            }
            return (T) sensorReadings;
        }
        return null;
    }

    public Optional<WeatherInfo> checkTemperature() {
        Optional<Pair<Double, Double>> weatherPair = temperatureSensor.readData();
        if (weatherPair.isPresent()) {
            return Optional.of(new WeatherInfo(weatherPair.get().getValue(), weatherPair.get().getKey()));
        }
        return Optional.empty();
    }

    public <T> T checkAndSave(Class<T> tClass) {
        logger.info("Update temperature info.");
        Optional<WeatherInfo> weatherInfo = checkTemperature();
        if (weatherInfo.isPresent()) {
            logger.info("Readings from sensor: {}", weatherInfo.get());
            weatherDb.saveCurrentWeather(weatherInfo.get());
            logger.info("Save to DB done.");
            if (ServiceDetail.class.equals(tClass)) {
                return (T) buildServiceDetails(weatherInfo.get());
            }
        }
        return (T) weatherInfo.get();
    }

    public ServiceDetail getTemperatureStatistics(int days) {
        logger.info("Get temperature&humidity statistics from last {}", days);
        return weatherDb.getWeatherStatistics(days);
    }

    public ServiceDetail buildServiceDetails(WeatherInfo sensorReadings) {
        ServiceDetail sd = new ServiceDetail(OperationTypes.TEMPERATURE_CHECK);
        sd.addKeyValue(TEMPERATURE, String.valueOf(sensorReadings.getTemperatureCelsius()));
        sd.addKeyValue(HUMIDITY, String.valueOf(sensorReadings.getHumidity()));
        return sd;
    }

    public void setTemperatureSensor(DHT22SensorReader temperatureSensor) {
        this.temperatureSensor = temperatureSensor;
    }

    public WeatherDb getWeatherDb() {
        return weatherDb;
    }

    public void setWeatherDb(WeatherDb weatherDb) {
        this.weatherDb = weatherDb;
    }


    /**
     * Check each hour temperature
     *
     * @return
     */
    @Override
    public int getTimeInterval() {
        return 60 * 60;
    }
}
