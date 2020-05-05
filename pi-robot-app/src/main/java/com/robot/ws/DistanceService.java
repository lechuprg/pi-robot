package com.robot.ws;

import com.robot.detector.DistanceDetector;
import com.robot.msg.response.OperationTypes;
import com.robot.msg.response.RobotResponse;
import com.robot.msg.response.ServiceDetail;
import com.robot.msg.response.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DistanceService {

    Logger logger = LoggerFactory.getLogger(DistanceService.class);
    @Autowired
    private DistanceDetector distanceDetector;

    @RequestMapping("/checkDistance")
    @ResponseBody
    public RobotResponse checkDistance() {
        RobotResponse robotResponse = new RobotResponse(Status.OK, OperationTypes.DISTANCE_CHECK);
        logger.warn("Check distance");

        ServiceDetail serviceDetail = distanceDetector.getServiceDetails();
        robotResponse.addService(serviceDetail);
        logger.info("Distance is {}", serviceDetail);
        return robotResponse;
    }
}
