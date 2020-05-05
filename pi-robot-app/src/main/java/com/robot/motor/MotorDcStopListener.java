package com.robot.motor;

import com.robot.job.RobotJobScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Lechu on 07.08.2016.
 */

public class MotorDcStopListener implements Observer {

    private final int STOP_MOTOR_AFTER_SECONDS = 4000;
    private static final Logger logger = LoggerFactory.getLogger(RobotJobScheduler.class);

    final ExecutorService executor = Executors.newSingleThreadExecutor();
    final  ExecutionTime executionTime;
    Future<?> submit;

    public MotorDcStopListener() {
        super();
        executionTime = new ExecutionTime();
    }

    @Override
    public void update(Observable observable, Object arg) {
        logger.debug("Event catch for motors");
        executionTime.lastEvent = System.currentTimeMillis();

        if(!killProcess(arg) && (submit == null || submit.isDone())) {
            submit = executor.submit(() -> {
                try {
                    while (executionTime.lastEvent>System.currentTimeMillis()-STOP_MOTOR_AFTER_SECONDS){
                        Thread.sleep(STOP_MOTOR_AFTER_SECONDS/4);
                    }
                    logger.info("Stop motors...");
                    Motor observableMotor = (Motor) observable;
                    observableMotor.stop();
                } catch (InterruptedException e) {
                    logger.info("Interrupted, so exiting.");
                }
            });
        }
    }

    private boolean killProcess(Object arg) {
        if(arg!=null && arg instanceof MoveDirection) {
            if(arg.equals(MoveDirection.STOP)) {
                logger.info("Motor stop, kill thread");
                executor.shutdownNow();
                return true;
            }
        }
        return false;
    }

    class ExecutionTime {
        public long lastEvent = 0;
    }

}
