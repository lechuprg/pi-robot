package com.robot.ws;

import com.robot.msg.response.OperationTypes;
import com.robot.msg.response.RobotResponse;
import com.robot.msg.response.ServiceDetail;
import com.robot.msg.response.Status;
import com.robot.property.PiProperty;
import com.robot.sensor.WeatherInfo;
import com.robot.service.TemperatureService;
import com.robot.service.impl.PropertyServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Created by Lechu on 14.09.2016.
 * This WS is to control camera.
 */
@RestController
public class TempCheck {
    private static final int DEFAULT_DAYS_INTERVAL = 1;
    Logger logger = LoggerFactory.getLogger(TempCheck.class);

    @Autowired
    private PropertyServiceImpl propertyService;

    @Autowired
    private TemperatureService temperatureService;

    @RequestMapping("/tempCheck")
    @ResponseBody
    public RobotResponse checkTemp() throws InterruptedException {
        logger.info("Check temperature .............");
        Optional<WeatherInfo> weatherInfo = temperatureService.checkTemperature();
        RobotResponse robotResponse = new RobotResponse(Status.OK, OperationTypes.TEMPERATURE_CHECK);
        if(weatherInfo.isPresent()) {
            ServiceDetail serviceDetail = temperatureService.buildServiceDetails(weatherInfo.get());
            logger.info("Data returned from temp {}", serviceDetail);
            robotResponse.addService(serviceDetail);
            return robotResponse;
        }
        robotResponse.setStatus(Status.ERROR);
        return robotResponse;
    }

    @RequestMapping("/tempCheckAndSave")
    @ResponseBody
    public RobotResponse checkTempAndSave() throws InterruptedException {
        logger.warn("Check temperature .............");
        ServiceDetail serviceDetail = temperatureService.checkAndSave(ServiceDetail.class);
        logger.info("Data returned from temp {}", serviceDetail);
        RobotResponse robotResponse = new RobotResponse(Status.OK, OperationTypes.TEMPERATURE_CHECK);
        robotResponse.addService(serviceDetail);
        return robotResponse;
    }

    @RequestMapping("/tempCheckHistorical")
    @ResponseBody
    public RobotResponse tempCheckHistorical(@RequestParam(value = "days", defaultValue = "999") int days) throws InterruptedException {
        logger.warn("Check temperature .............");
        if (days == 999) {
            days = getPropertyAsInt();
        }
        ServiceDetail serviceDetail = temperatureService.getTemperatureStatistics(days);
        logger.info("Data returned from temp {}", serviceDetail);
        RobotResponse robotResponse = new RobotResponse(Status.OK, OperationTypes.TEMPERATURE_CHECK);
        robotResponse.addService(serviceDetail);
        return robotResponse;
    }

    private int getPropertyAsInt() {
        String property = propertyService.getProperty(PiProperty.WEATHER_DEFAULT_RETURN_DAYS);
        if (!StringUtils.isEmpty(property)) {
            return Integer.valueOf(property);
        }
        return DEFAULT_DAYS_INTERVAL;
    }
}
