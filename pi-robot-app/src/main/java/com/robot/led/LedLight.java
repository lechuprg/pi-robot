package com.robot.led;

import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.GpioUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

/**
 * Created by Lechu on 06.08.2016.
 */
public class LedLight implements Led {
    private final int SPEEDOFSOUND = 34029; // Speed of sound = 34029 cm/s
    private Pin pinGreen;
    private Pin pinBlue;
    private GpioPinDigitalOutput blue;
    private GpioPinDigitalOutput green;
    Logger logger = LoggerFactory.getLogger(LedLight.class);

    @PostConstruct
    public void initialize() {
        logger.warn("Enabled non privilaged access");
        GpioUtil.enableNonPrivilegedAccess();
        GpioController gpioFactory = GpioFactory.getInstance();
        blue = gpioFactory.provisionDigitalOutputPin(pinBlue, PinState.LOW);
        green = gpioFactory.provisionDigitalOutputPin(pinGreen, PinState.LOW);
    }

    public void setBlueColor () {
        green.low();
        blue.high();
    }

    public void setGreenColor () {
        blue.low();
        green.high();
    }

    @Override
    public void turnOff() {
        blue.low();
        green.low();
    }

    public void setPinGreen(String pin) {
        pinGreen = RaspiPin.getPinByName(pin);
    }

    public void setPinBlue(String pin) {
        pinBlue = RaspiPin.getPinByName(pin);
    }
}
