package com.robot.ws;

import com.robot.detector.DistanceDetector;
import com.robot.msg.request.RobotRequest;
import com.robot.msg.response.OperationTypes;
import com.robot.msg.response.RobotResponse;
import com.robot.msg.response.ServiceDetail;
import com.robot.msg.response.Status;
import com.robot.service.PropertyService;
import com.robot.service.SystemInfoService;
import com.robot.service.TemperatureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Lechu on 30.09.2016.
 */
@RestController
public class PingService {
    Logger logger = LoggerFactory.getLogger(PingService.class);
    @Autowired
    TemperatureService temperatureService;
    @Autowired
    DistanceDetector distanceDetector;
    @Autowired
    SystemInfoService systemInfoService;
    @Autowired
    PropertyService propertyService;

    @RequestMapping("/pingRobot")
    @ResponseBody
    public RobotResponse pingRobot(@RequestParam(value = "move", defaultValue = "STOP") String move) throws InterruptedException {
        RobotResponse robotResponse = new RobotResponse(Status.OK, OperationTypes.PING);
        robotResponse.addService(getTemperature());
        robotResponse.addService(getDistance());
        return robotResponse;
    }

    @RequestMapping("/readProperties")
    @ResponseBody
    public RobotResponse readProperties() throws InterruptedException {
        RobotResponse robotResponse = new RobotResponse(Status.OK, OperationTypes.PROPERTY);
        robotResponse.addService(propertyService.getWritableProperties());
        return robotResponse;
    }

    @RequestMapping(value="/updateProperties", method = RequestMethod.POST, consumes = { "application/json" })
    @ResponseBody
    public RobotResponse updateProperties(@RequestBody RobotRequest  requestBody) throws InterruptedException {
        logger.info("Update properties...");
        RobotResponse robotResponse = new RobotResponse(Status.OK, OperationTypes.PROPERTY);
        requestBody.getServiceDetails().stream().forEach(
            service->service.getKeyValue().entrySet().stream().forEach(
                entrySet-> propertyService.updateProperties(entrySet.getKey(), entrySet.getValue())
            )
        );
        logger.info("Update completed.");
        robotResponse.addService(propertyService.getWritableProperties());
        return robotResponse;
    }

    @RequestMapping("/systemInfo")
    @ResponseBody
    public RobotResponse systemInfo() {
        RobotResponse robotResponse = new RobotResponse(Status.OK, OperationTypes.SYSTEM_INFO);
        robotResponse.addService(systemInfoService.getSystemInfo());
        return robotResponse;
    }

    private ServiceDetail getDistance() {
        return distanceDetector.getServiceDetails();
    }

    private ServiceDetail getTemperature() {
        return temperatureService.checkTemperature(ServiceDetail.class);
    }

}
