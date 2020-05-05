package com.robot.service.impl.monitor;

import com.robot.detector.Detector;
import com.robot.service.GuardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by Lechu on 08.05.2017.
 */
@Service
public class MoveDetectorGuardImpl implements Detector {
    private Logger logger = LoggerFactory.getLogger(MoveDetectorGuardImpl.class);

    public static final int TIME_INTERVAL_SEC = 2;
    @Autowired
    private GuardService guardService;

    @Autowired
    private Shield shield;

    @Override
    public int getTimeInterval() {
        return TIME_INTERVAL_SEC;
    }

    @Override
    public void run() {
        check360();
    }

    private void check360() {
        logger.info("Start checking area");
        Optional<List<ProtectedZone>> moveDetected = shield.protect();

        if (moveDetected.isPresent() && !moveDetected.get().isEmpty()) {
            logger.info("Move detected, take photo and send");
            moveDetected.get().stream().forEach(guardService::takePhotoAndSend);
        }
    }
}
