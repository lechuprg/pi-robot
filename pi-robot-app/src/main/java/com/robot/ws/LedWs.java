package com.robot.ws;

import com.robot.msg.response.OperationTypes;
import com.robot.msg.response.RobotResponse;
import com.robot.msg.response.Status;
import com.robot.service.LedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Lechu on 14.09.2016.
 * This WS is to control camera.
 */
@RestController
public class LedWs {
    Logger logger = LoggerFactory.getLogger(LedWs.class);

    @Autowired
    private LedService ledService;

    @RequestMapping("/turnOnLed")
    @ResponseBody
    public RobotResponse setBlueColor() {
        logger.warn("Led Blue .............");
        ledService.turnOnLed();
        RobotResponse robotResponse = new RobotResponse(Status.OK, OperationTypes.LED);
        return robotResponse;
    }


    @RequestMapping("/turnOffLed")
    @ResponseBody
    public RobotResponse turnOffLed() {
        logger.warn("Turn off led.............");
        ledService.turnOff();
        RobotResponse robotResponse = new RobotResponse(Status.OK, OperationTypes.LED);
        return robotResponse;
    }
}
