package com.robot.job;

import com.robot.detector.Detector;
import com.robot.detector.MoveDetectorImpl;
import com.robot.service.impl.TemperatureServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by Lechu on 06.08.2016.
 */

@Service
public class RobotJobScheduler  {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);
    Logger logger = LoggerFactory.getLogger(RobotJobScheduler.class);

    @Resource(name = "temperatureServiceImpl")
    private Detector temperatureServiceImpl;
//    @Resource(name = "frontMoveDetector")
//    private MoveDetectorImpl frontMoveDetector;

    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        List<Detector> detectors = asList(temperatureServiceImpl);
        logger.info("start scheduled job {}", detectors);

        detectors.stream().forEach(this::scheduleJob);
    }

    private void scheduleJob(Detector detector) {
        scheduler.scheduleAtFixedRate(detector, 60, detector.getTimeInterval(), SECONDS);
    }

}
