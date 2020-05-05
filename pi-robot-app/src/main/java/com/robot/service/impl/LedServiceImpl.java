package com.robot.service.impl;


import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.GpioUtil;
import com.robot.motor.Motor;
import com.robot.service.LedService;
import com.robot.servo.MicroServoSG90;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by Lechu on 06.12.2016.
 */
@Service
@Data
public class LedServiceImpl implements LedService {

    static final Logger logger = LoggerFactory.getLogger(MicroServoSG90.class);

    private GpioPinDigitalOutput motorPwm; // IA
    private GpioPinDigitalOutput motorDir; // IB
    private GpioController gpioFactory;
    private Pin pinPwm;
    private Pin pinDir;


    public LedServiceImpl() {
        logger.warn("Enabled non privilaged access");
        GpioUtil.enableNonPrivilegedAccess();
        gpioFactory = GpioFactory.getInstance();
        pinPwm = RaspiPin.getPinByName("GPIO 22");
        pinDir = RaspiPin.getPinByName("GPIO 23");
    }

    @PostConstruct
    public void initialize() {
        logger.info("Initializing {}", this.getClass());
        motorPwm = gpioFactory.provisionDigitalOutputPin(pinPwm, PinState.LOW);
        motorDir = gpioFactory.provisionDigitalOutputPin(pinDir, PinState.LOW);
        logger.info("Set security stop listener...");
    }


    @Override
    public void turnOnLed() {
        motorPwm.high();
        motorDir.low();
    }

    @Override
    public void turnOff() {
        motorPwm.high();
        motorDir.high();
    }
}
