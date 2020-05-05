package com.robot.detector;

import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.GpioUtil;
import com.robot.msg.response.OperationTypes;
import com.robot.msg.response.ServiceDetail;
import com.robot.utils.ThredUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by Lechu on 06.08.2016.
 */
public class DistanceDetector implements Detector {
    private final int SPEEDOFSOUND = 34029; // Speed of sound = 34029 cm/s
    private final static long BILLION = (long) 1E9;
    private final static double SOUND_SPEED = 34_300d;       // in cm, 343.00 m/s
    private final static double DIST_FACT = SOUND_SPEED / 2;
    private Pin echoPin;
    private Pin triggerPin;
    private GpioPinDigitalInput echo;
    private GpioPinDigitalOutput trigger;
    Logger logger = LoggerFactory.getLogger(DistanceDetector.class);

    @PostConstruct
    public void initialize() {
        logger.warn("Enabled non privilaged access");
        GpioUtil.enableNonPrivilegedAccess();
        GpioController gpioFactory = GpioFactory.getInstance();
        echo = gpioFactory.provisionDigitalInputPin(echoPin);
        trigger = gpioFactory.provisionDigitalOutputPin(triggerPin, PinState.LOW);
    }

    @Override
    public int getTimeInterval() {
        return 30*60;
    }

    @Override
    public void run() {
        checkDistance();
    }

    public ServiceDetail getServiceDetails() {
        ServiceDetail serviceDetail = new ServiceDetail(OperationTypes.DISTANCE_CHECK);
        double distance = checkDistance();
        serviceDetail.addKeyValue("Distance", String.valueOf(distance));
        return serviceDetail;
    }
    public double checkDistance() {
        long startTime = System.nanoTime();
        trigger.high();
        ThredUtil.threadWait(10_000);
        trigger.low();
        long start = startTime;
        long stop = startTime;
        long maxWaitingTime = startTime + 1_000_000_000L * 2;

        while (echo.isLow() && (start < maxWaitingTime)) {
        }
        start = System.nanoTime();

        while ((echo.isHigh()) && (stop < maxWaitingTime)) {
        }
        stop = System.nanoTime();
        double travelTime = (stop - start);
//        long distance = (stop - start) * SPEEDOFSOUND / (2 * 10000);
        double pulseDuration = travelTime / (double) BILLION; // in seconds
        double distance = pulseDuration * DIST_FACT;
        logger.info("Simple distance in cm {} ", distance);
        return distance;
    }
    public void setEchoPin(String pin) {
        echoPin = RaspiPin.getPinByName(pin);
    }

    public void setTriggerPin(String pin) {
        triggerPin = RaspiPin.getPinByName(pin);
    }
}
