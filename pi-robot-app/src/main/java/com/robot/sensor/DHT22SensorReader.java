package com.robot.sensor;

import com.pi4j.util.NativeLibraryLoader;
import com.robot.utils.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author lechu
 */
public class DHT22SensorReader {
    private final int pin;
    public static native int[] readData(int pin);
    Logger logger = LoggerFactory.getLogger(DHT22SensorReader.class);
    static {
        try {
            NativeLibraryLoader.loadLibraryFromClasspath("/dht22sensor.so");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DHT22SensorReader(int pinNumber) {
        this.pin = pinNumber;
    }

    /**
     * read data 3 times and choose mediana
     * @return
     */
    public Optional<Pair<Double, Double>> readData() {
        try {
            HashMap<String, Pair<Double, Double>> readData = new HashMap();
            for (int i = 0; i < 3; i++) {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                readData.put(String.valueOf(i), getSingleRead());
            }
            List<Map.Entry<String, Pair<Double, Double>>> collect = readData.entrySet().stream().sorted((s1, s2) -> s1.getValue().getKey().compareTo(s2.getValue().getKey())).collect(Collectors.toList());

            return Optional.of(collect.get(1).getValue());
        } catch (RuntimeException e) {
            logger.warn("TEM CHECK WARNING", e);
        }
        return Optional.empty();
    }

    public Pair<Double, Double> getSingleRead() {
        int[] data = DHT22SensorReader.readData(pin);
        int stopCounter = 0;
        while (!isValid(data)) {
            stopCounter++;
            if (stopCounter > 10) {
                throw new RuntimeException("Sensor return invalid data 10 times:" + data[0] + ", " + data[1]);
            }
            data = DHT22SensorReader.readData(pin);
        }
//        printData(data);
        Pair<Double,Double> dataHumTemp =  new Pair(buildNumber(data,0,16), buildNumber(data,16,32));
        logger.debug("Temp done");
        return dataHumTemp;
    }

    private boolean isValid(int[] data) {
        return data[0] > 0 && data[0] < 100 && data[1] > 0 && data[1] < 100;
    }

    private double buildNumber(int[] dht11_dat1, int start, int end) {
        StringBuilder number = new StringBuilder();
        for(int i =start;i<end;i++) {
            int bit = dht11_dat1[i]<28? 0: 1;
            number.append(bit);
        }
        int intNumber = Integer.parseInt(number.toString(), 2);
        return intNumber/10f;
    }

    private void printData(int[] data) {
        for(int i:data) {
            System.err.print("," + i);
        }
    }
}