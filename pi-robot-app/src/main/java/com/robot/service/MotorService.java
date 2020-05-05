package com.robot.service;

import java.util.Optional;

/**
 * Created by Lechu on 24.10.2016.
 */
public interface MotorService {

    void moveForward(Optional<Integer> milisec);
    void moveBackward(Optional<Integer> milisec);
    void turnLeft(Optional<Integer> milisec) ;
    void turnRight(Optional<Integer> milisec);
    void stop() ;
}
