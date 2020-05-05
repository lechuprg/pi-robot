package com.robot.motor;

import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.GpioUtil;
import com.robot.servo.MicroServoSG90;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

/**
 * Created by Lechu on 06.08.2016.
 */
public class MotorDc implements Motor {
    static final Logger logger = LoggerFactory.getLogger(MicroServoSG90.class);

    private GpioPinDigitalOutput motorPwm; // IA
    private GpioPinDigitalOutput motorDir; // IB
    private GpioController gpioFactory;
    private Pin pinPwm;
    private Pin pinDir;


    public MotorDc() {
        logger.warn("Enabled non privilaged access");
        GpioUtil.enableNonPrivilegedAccess();
        gpioFactory = GpioFactory.getInstance();
//        allPins = RaspiPin.allPins(SystemInfo.BoardType.RaspberryPi_3B);
    }

    @PostConstruct
    public void initialize() {
        logger.info("Initializing {}", this.getClass());
        motorPwm = gpioFactory.provisionDigitalOutputPin(pinPwm, PinState.LOW);
        motorDir = gpioFactory.provisionDigitalOutputPin(pinDir, PinState.LOW);
        logger.info("Set security stop listener...");
    }

    @Override
    public boolean moveForward() {
        motorPwm.high();
        motorDir.low();
        return true;
    }

    @Override
    public boolean moveBackward() {
        motorPwm.low();
        motorDir.high();
        return true;
    }

    @Override
    public boolean stop() {
        motorPwm.high();
        motorDir.high();
        return true;
    }

    public void setMotorPwm(String pin) {
        pinPwm = RaspiPin.getPinByName(pin);
    }

    public void setMotorDir(String pin) {
        pinDir = RaspiPin.getPinByName(pin);
    }
}
