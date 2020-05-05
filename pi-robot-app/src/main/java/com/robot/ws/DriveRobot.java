package com.robot.ws;

import com.robot.db.WeatherDb;
import com.robot.detector.DistanceDetector;
import com.robot.motor.MoveDirection;
import com.robot.msg.response.OperationTypes;
import com.robot.msg.response.RobotResponse;
import com.robot.msg.response.ServiceDetail;
import com.robot.msg.response.Status;
import com.robot.service.MotorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Created by Lechu on 07.08.2016.
 * This WS is to control movment(drive) robot.
 */
@RestController
public class DriveRobot {
    Logger logger = LoggerFactory.getLogger(DriveRobot.class);

    @Autowired
    private MotorService motorService;

    @Autowired
    private WeatherDb weatherDb;

    @Autowired
    private DistanceDetector distanceDetector;

    @RequestMapping("/moveDc")
    @ResponseBody
    public RobotResponse moveMotor(@RequestParam(value = "move", defaultValue = "STOP") String move, @RequestParam(value = "duration", required = false) Integer durationInMillis) throws InterruptedException {
        RobotResponse robotResponse = new RobotResponse(Status.OK, OperationTypes.MOVE_MOTOR);
        Optional<Integer> duration = Optional.ofNullable(durationInMillis);
        logger.warn("Start motor.............");
        switch (MoveDirection.valueOf(move)) {
            case FORWARD:
                logger.warn("Move forward done");
                motorService.moveForward(duration);
                break;
            case BACKWARD:
                logger.warn("Move backward");
                motorService.moveBackward(duration);
                break;
            case LEFT:
                logger.warn("Move left"); //temporary cables are missmatched
                motorService.turnRight(duration);
                break;
            case RIGHT:
                logger.warn("Move right");
                motorService.turnLeft(duration);
                break;
            case STOP:
                logger.warn("Move stop");
                motorService.stop();
                ServiceDetail serviceDetail1 = checkDistance();
                robotResponse.addService(serviceDetail1);
                break;
            default:
                logger.warn("Incorrect order, movment stop");
                motorService.stop();
                ServiceDetail serviceDetail = checkDistance();
                robotResponse.addService(serviceDetail);
                break;
        }

        robotResponse.addService(weatherDb.getLastWeatherInfo());
        return robotResponse;
    }

    private ServiceDetail checkDistance() {
        return distanceDetector.getServiceDetails();
    }
}
