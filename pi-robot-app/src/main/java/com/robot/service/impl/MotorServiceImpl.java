package com.robot.service.impl;


import com.robot.motor.Motor;
import com.robot.motor.MoveDirection;
import com.robot.service.MotorService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

import static com.robot.utils.ThredUtil.threadSleep;

/**
 * Created by Lechu on 07.08.2016.
 */
@Service
public class MotorServiceImpl extends Observable implements MotorService {

    @Resource(name = "motorDcLeftWheel")
    Motor motorLeft;
    @Resource(name = "motorDcRightWheel")
    Motor motorRight;

    public void moveForward() {
        motorLeft.moveForward();
        motorRight.moveForward();
        setChanged();
        notifyObservers();
    }

    public void moveBackward() {
        motorLeft.moveBackward();
        motorRight.moveBackward();
        setChanged();
        notifyObservers();
    }

    public void turnLeft() {
        motorLeft.moveBackward();
        motorRight.moveForward();
        setChanged();
        notifyObservers();
    }

    public void turnRight() {
        motorRight.moveBackward();
        motorLeft.moveForward();
        setChanged();
        notifyObservers();
    }

    @Override
    public void moveForward(Optional<Integer> milisec) {
        moveForward();
        stopIfNeeded(milisec);
    }

    @Override
    public void moveBackward(Optional<Integer> milisec) {
        moveBackward();
        stopIfNeeded(milisec);
    }

    @Override
    public void turnLeft(Optional<Integer> milisec) {
        turnLeft();
        stopIfNeeded(milisec);
    }

    @Override
    public void turnRight(Optional<Integer> milisec) {
        turnRight();
        stopIfNeeded(milisec);
    }

    private void stopIfNeeded(Optional<Integer> milisec) {
        milisec.ifPresent(d -> {
            threadSleep(d);
            stop();
        });
    }

    public void stop() {
        motorRight.stop();
        motorLeft.stop();
        setChanged();
        notifyObservers(MoveDirection.STOP);
    }


    public void setMotorLeft(Motor motorLeft) {
        this.motorLeft = motorLeft;
    }

    public void setMotorRight(Motor motorRight) {
        this.motorRight = motorRight;
    }

    @Resource(name = "motorObservers")
    public void setObserver(List<Observer> observers) {
        for (Observer ob : observers) {
            addObserver(ob);
        }
    }

}
