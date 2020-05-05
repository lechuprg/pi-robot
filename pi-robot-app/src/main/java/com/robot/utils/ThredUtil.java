package com.robot.utils;

/**
 * Created by Lechu on 06.08.2016.
 */
public class ThredUtil {

    private ThredUtil(){};

    public static void threadWait(long time) {
        long start = System.nanoTime();
        long end=0;
        do{
            end = System.nanoTime();
        }while(start + time >= end);
    }

    /**
     * Less precise method but less cpu usage
     * @param timeMilis
     */
    public static void threadSleep(long timeMilis) {
        try {
            Thread.sleep(timeMilis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
