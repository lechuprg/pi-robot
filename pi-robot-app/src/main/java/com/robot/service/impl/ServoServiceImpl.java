package com.robot.service.impl;

import com.robot.service.ServoService;
import com.robot.servo.CameraDirection;
import com.robot.servo.Servo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Lechu on 14.09.2016.
 */
@Service
public class ServoServiceImpl implements ServoService {

    static final Logger logger = LoggerFactory.getLogger(ServoServiceImpl.class);

    @Resource(name = "servoSg90horizontal")
    Servo horizontal;
    @Resource(name = "servoSg90vertical")
    Servo vertical;
    public void moveServo(String direction, int degree) {
        try {
            logger.info("Move camera {} degree {}", direction, degree);
            switch (CameraDirection.valueOf(direction)) {
                case HORIZONTAL:
                    horizontal.move(degree);
                    break;
                case VERTICAL:
                    vertical.move(degree);
                    break;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
