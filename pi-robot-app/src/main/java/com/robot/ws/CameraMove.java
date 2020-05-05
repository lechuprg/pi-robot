package com.robot.ws;

import com.robot.db.WeatherDb;
import com.robot.msg.response.OperationTypes;
import com.robot.msg.response.RobotResponse;
import com.robot.msg.response.Status;
import com.robot.service.ServoService;
import com.robot.servo.CameraDirection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Lechu on 14.09.2016.
 * This WS is to control camera.
 */
@RestController
public class CameraMove {
    Logger logger = LoggerFactory.getLogger(CameraMove.class);

    @Autowired
    private ServoService cameraService;

    @Autowired
    private WeatherDb weatherDb;

    @RequestMapping("/moveCamera")
    @ResponseBody
    public RobotResponse moveCamera(@RequestParam(value = "degree", defaultValue = "0") String degree, @RequestParam(value = "direction", defaultValue = "HORIZONTAL") String direction) throws InterruptedException {
        logger.warn("Move Camera .............");
        cameraService.moveServo(direction, Integer.valueOf(degree));
        RobotResponse robotResponse = new RobotResponse(Status.OK, OperationTypes.MOVE_SERVO);
        robotResponse.addService(weatherDb.getLastWeatherInfo());
        return robotResponse;
    }

    @RequestMapping("/moveCameraAll")
    @ResponseBody
    public RobotResponse moveCameraAll(@RequestParam(value = "horizontalDegree") String horizontalDegree, @RequestParam(value = "verticalDegree") String verticalDegree) throws InterruptedException {
        logger.warn("Move Camera .............");
        cameraService.moveServo(CameraDirection.HORIZONTAL.name(), Integer.valueOf(horizontalDegree));
        cameraService.moveServo(CameraDirection.VERTICAL.name(), Integer.valueOf(verticalDegree));
        RobotResponse robotResponse = new RobotResponse(Status.OK, OperationTypes.MOVE_SERVO);
        robotResponse.addService(weatherDb.getLastWeatherInfo());
        return robotResponse;
    }
}
