package com.robot.service.impl;

import com.robot.mail.MailClient;
import com.robot.property.PiProperty;
import com.robot.service.*;
import com.robot.service.impl.monitor.ProtectedZone;
import com.robot.servo.CameraDirection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static java.time.Duration.between;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.util.Optional.of;

/**
 * Created by Lechu on 28.02.2017.
 */
@Service
public class GuardServiceImpl implements GuardService {


    public static final Duration EMAIL_TIME_TRESHOLD = Duration.of(5, MINUTES);
    Logger logger = LoggerFactory.getLogger(GuardServiceImpl.class);

    @Autowired
    private VideoStreamService videoStreamService;

    @Autowired
    private ServoService servoService;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private MotorService motorService;
    @Autowired
    private MailClient mailClient;

    private LocalDateTime lastEmailSent = LocalDateTime.MIN;

    public void takePhotoAndSend(ProtectedZone zone) {
        if (zone.getMiddlePoint() > 180) {
            logger.info("Move robot to position {}", zone.getMiddlePoint());
            moveRobot(zone);
        }

        logger.info("Move detected");
        servoService.moveServo(CameraDirection.HORIZONTAL.name(), (int) zone.getMiddlePoint());
        servoService.moveServo(CameraDirection.VERTICAL.name(), 110);
        try {
            String filePath = videoStreamService.takePhoto();
            logger.info("Photo done");
            sendWarning(filePath);
            logger.info("email sent");
        } catch (IOException e) {
            logger.warn("GUARD error", e);
            sendWarning();
        }
    }

    private double moveRobot(ProtectedZone zone) {
        if(zone.getMiddlePoint()<240) {
            motorService.moveBackward(of(400));
            motorService.turnRight(of(800));
            return 90;
        }

        if(zone.getMiddlePoint()<300) {
            motorService.moveBackward(of(400));
            motorService.turnRight(of(1600));
            return 180;
        }

        if(zone.getMiddlePoint()<360) {
            motorService.moveBackward(of(400));
            motorService.turnLeft(of(800));
            return 270;
        }
        return 0;
    }

    private void sendWarning() {
        sendWarning(null);
    }

    /**
     * this method send warning via sms/mail
     */
    private void sendWarning(String filePath) {
        Duration duration = between(lastEmailSent, LocalDateTime.now());
        if (duration.getSeconds() < EMAIL_TIME_TRESHOLD.getSeconds()) {
            LocalDateTime dateTime = LocalDateTime.now();
            String msg = String.format("At %s robot detect move...", dateTime.toString());
            String emailTo = propertyService.getProperty(PiProperty.NOTIFICATION_EMAIL);
            mailClient.sendEmail("MOVE DETECTED", msg, filePath, emailTo);
        }
    }

}

