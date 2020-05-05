package com.robot.servo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Lechu on 27.07.2016.
 * 1.5 ms middle 90
 * 1 ms -90
 * 2 ms 90
 * Operating speed: 0.12s/60 degree => 180 degree -> 0.36s
 */
public class MicroServoSG90 extends Servo {
    static final Logger logger = LoggerFactory.getLogger(MicroServoSG90.class);
    static final long PERIOD = 20_000_000;  //20ms
    static final int MAX_ANGLE = 180;
    static  int POSITION_0 = 1_000_000; //1 ms
    static  double POSITION_180 = 2_000_000; //2 ms
    public static final int MAX_DEGREE_WITHIN_ONE_SIGNAL = 10;

    private int oneDegree;
    private int currentPosition = 0 ;

    protected int calibrationDegree=0;
    protected int initialPosition=POSITION_0;


    public MicroServoSG90() {
        super();
        setUpOneDegree();
    }

    @Override
    public void setInitialPosition() throws InterruptedException {
        logger.info("Move servo to initial position");
        move(initialPosition);
        setCurrentPosition(initialPosition);
        logger.info("Servo initial complected.");
    }

    @Override
    public void move(int degree) throws InterruptedException {
        int calibratedDegree = calcCalibrationDegree(degree);
        if(Math.abs(calibratedDegree *100 / (1+ currentPosition)) > 0.05) {
            if (calibratedDegree >= 0 && calibratedDegree <= MAX_ANGLE) {
                logger.info("Move time is");
                move(PERIOD, POSITION_0 + (calibratedDegree * oneDegree), calculateNumberOfSignalRepeat(calibratedDegree));
                setCurrentPosition(calibratedDegree);
            }
        }
    }

    private int calcCalibrationDegree(int degree) {
        int newDegre = degree + calibrationDegree;
        return newDegre<0?0:newDegre>MAX_ANGLE?MAX_ANGLE:newDegre;
    }

    private int calculateNumberOfSignalRepeat(int degree) {
        return Math.floorDiv(Math.abs((degree-currentPosition)), MAX_DEGREE_WITHIN_ONE_SIGNAL);
    }


    private void setUpOneDegree() {
        oneDegree = (int) (((POSITION_180 - POSITION_0)) / MAX_ANGLE);
        logger.info("One degree is {}", oneDegree);
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public int getInitialPosition() {
        return initialPosition;
    }

    public void setInitialPosition(int initialPosition) {
        this.initialPosition = initialPosition;
    }

    public int getCalibrationDegree() {
        return calibrationDegree;
    }

    public void setCalibrationDegree(int calibrationDegree) {
        this.calibrationDegree = calibrationDegree;
    }
}
