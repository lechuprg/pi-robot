package com.robot.detector;

/**
 * Created by Lechu on 06.08.2016.
 */
public interface Detector extends Runnable {

    /**
     * Time in seconds
     * @return
     */
    int getTimeInterval();
}
