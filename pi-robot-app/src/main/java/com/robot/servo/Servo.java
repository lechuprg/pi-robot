package com.robot.servo;

import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.GpioUtil;
import com.robot.utils.ThredUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

/**
 * Created by Lechu on 22.07.2016.
 */
public abstract class Servo {
    private static final Logger logger = LoggerFactory.getLogger(Servo.class);
    private static final int SEC_2_NANOSEC = 1000000;
    private static final int THREASHOLD = 15;

    private GpioPinDigitalOutput myServo;
    private Pin servoPin;

    public abstract void move(int degree) throws InterruptedException;
    public abstract void setInitialPosition() throws InterruptedException;

    protected Servo() {
    }

    @PostConstruct
    public void initialize() throws InterruptedException {
        logger.info("Enabled non privilaged access");
        GpioUtil.enableNonPrivilegedAccess();
        GpioController gpioFactory = GpioFactory.getInstance();
        myServo = gpioFactory.provisionDigitalOutputPin(
            servoPin, PinState.LOW);
        logger.info("Servo started on pin {}", servoPin.getName());
        setInitialPosition();
    }

    //RaspiPin.GPIO_07
    public void setServoPin(String pin) {
        servoPin = RaspiPin.getPinByName(pin);
    }

    protected void move(long period, long highTime, int repeatCount) throws InterruptedException {
        logger.warn("Start moving");

        long lowTime = period - highTime;

//        long upMs = getMsFromSignal(highTime, "Up");
//        long upNanos = getNanosFromSignal(highTime, "Up");
//
//        long lowMs = getMsFromSignal(lowTime, "Low");
//        long lowNanos = getNanosFromSignal(lowTime, "Low");

        int i = 0;

        while (i++<THREASHOLD + repeatCount) {
            myServo.high();
//            Thread.sleep(upMs);
            ThredUtil.threadWait(highTime);
            myServo.low();
//            Thread.sleep(lowMs);
            ThredUtil.threadWait(lowTime);
        }
    }

//    private long getNanosFromSignal(long highTime, String msg) {
//        long nanos = highTime % SEC_2_NANOSEC;
//        logger.info("{} signal nanos : {}", msg,  nanos);
//        return nanos;
//    }
//
//    private long getMsFromSignal(long highTime, String msg) {
//        long ms = Math.floorDiv(highTime, SEC_2_NANOSEC);
//        logger.info("{} signal ms: {}", msg,  ms);
//        return ms;
//    }

}
