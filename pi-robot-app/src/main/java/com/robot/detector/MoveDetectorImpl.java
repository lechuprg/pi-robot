package com.robot.detector;

import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.GpioUtil;
import com.robot.motor.MoveDirection;
import com.robot.property.PiProperty;
import com.robot.service.GuardService;
import com.robot.service.MotorService;
import com.robot.service.PropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Lechu on 06.08.2016.
 * Class observs motors but is observable by guard
 */
@Service
@Scope("prototype")
public class MoveDetectorImpl implements MoveDetector, Observer, BeanNameAware {

    @Autowired
    PropertyService propertyService;

    @Autowired
    GuardService guardService;

    private static final Logger logger = LoggerFactory.getLogger(MoveDetectorImpl.class);
    public static final int CHECK_DELAY_IN_MILLIS = 300;
    private static final int SINGLE_CHECK_PERIOD = 4; // by default this detector will be running for 1 sec
    private boolean isDetectorOn = true;
    private boolean isDetectorPropertyOn = false;
    private GpioPinDigitalInput myMoveDetector;
    private Pin movePin;
    private int warningThreshold = 1;
    private String beanName;

    @PostConstruct
    public void initialize() {
        logger.warn("Enabled non privileged access");
        GpioUtil.enableNonPrivilegedAccess();
        GpioController gpioFactory = GpioFactory.getInstance();
        myMoveDetector = gpioFactory.provisionDigitalInputPin(movePin);
        readDetectorOn();
    }

    @Override
    public boolean isMoveDetected() {
        int i = 0;
        int moveDetected = 0;
        readDetectorOn();
        readThreshold();
        if (isDetectorOn && isDetectorPropertyOn) {
            logger.info("Check movement: " + beanName);
            while (i++ < SINGLE_CHECK_PERIOD) {
                if (myMoveDetector.isHigh()) {
                    if (++moveDetected >= warningThreshold) {
                        logger.warn("WARNING something is moving..." + beanName);
                        return true;
                    }
                }
                sleep();
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return beanName;
    }

    private void readDetectorOn() {
        try {
            String property = propertyService.getProperty(PiProperty.MOVE_DETECTOR_ON);
            if (property != null) {
                isDetectorPropertyOn = Boolean.valueOf(property);
            }
        } catch (Exception e) {
            logger.warn("Couldnt read property", e);
        }
    }

    private void readThreshold() {
        String property = propertyService.getProperty(PiProperty.MOVE_DETECTOR_THRESHOLD);
        if (property != null) {
            int number = Integer.valueOf(property);
            if (number > 0) {
                warningThreshold = number;
            }
        }
    }

    private void sleep() {
        try {
            Thread.sleep(CHECK_DELAY_IN_MILLIS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof MotorService) {
            if (arg != null && arg instanceof MoveDirection) {
                if (MoveDirection.STOP.equals(arg)) {
                    isDetectorOn = true;
                }
            } else {
                isDetectorOn = false;
            }
        }
    }

    public void setMovePin(String pin) {
        movePin = RaspiPin.getPinByName(pin);
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }


}
