package com.robot.conf;


import com.robot.detector.Detector;
import com.robot.detector.DistanceDetector;
import com.robot.detector.MoveDetector;
import com.robot.detector.MoveDetectorImpl;
import com.robot.led.LedLight;
import com.robot.motor.MotorDc;
import com.robot.motor.MotorDcStopListener;
import com.robot.sensor.DHT22SensorReader;
import com.robot.servo.MicroServoSG90;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Observer;

@Configuration
public class ConfigurationBean {

    @Bean
    public MicroServoSG90 servoSg90horizontal() {
        MicroServoSG90 servo = new MicroServoSG90();
        servo.setCalibrationDegree(0);
        servo.setServoPin("GPIO 7");
        servo.setInitialPosition(90);
        return servo;
    }

    @Bean
    public MicroServoSG90 servoSg90vertical() {
        MicroServoSG90 servo = new MicroServoSG90();
        servo.setCalibrationDegree(0);
        servo.setServoPin("GPIO 15");
        servo.setInitialPosition(90);
        return servo;
    }

    @Bean
    public MotorDc motorDcLeftWheel() {
        MotorDc motorDc = new MotorDc();
        motorDc.setMotorDir("GPIO 28");
        motorDc.setMotorPwm("GPIO 27");
        return motorDc;
    }


    @Bean
    public MotorDc motorDcRightWheel() {
        MotorDc motorDc = new MotorDc();
        motorDc.setMotorDir("GPIO 25");
        motorDc.setMotorPwm("GPIO 24");
        return motorDc;
    }

    @Bean
    public MotorDcStopListener motorStopListener() {
        return new MotorDcStopListener();
    }

    @Bean
    public ArrayList<Observer> motorObservers() {

        ArrayList<Observer> observers = new ArrayList<>();
        observers.add(frontMoveDetector());
        return observers;
    }

    @Bean
    public MoveDetectorImpl frontMoveDetector() {
        MoveDetectorImpl moveDetector = new MoveDetectorImpl();
        moveDetector.setMovePin("GPIO 3");
        return moveDetector;
    }


    @Bean
    public MoveDetectorImpl moveDetectorBack() {
        MoveDetectorImpl moveDetector = new MoveDetectorImpl();
        moveDetector.setMovePin("GPIO 0");
        return moveDetector;
    }

    @Bean
    public DHT22SensorReader temperatureSensor(){
        DHT22SensorReader dht22SensorReader = new DHT22SensorReader(2);
        return dht22SensorReader;
    }

    @Bean
    public DistanceDetector distanceDetector() {
        DistanceDetector distanceDetector = new DistanceDetector();
        distanceDetector.setEchoPin("GPIO 4");
        distanceDetector.setTriggerPin("GPIO 5");
        return distanceDetector;
    }

}
