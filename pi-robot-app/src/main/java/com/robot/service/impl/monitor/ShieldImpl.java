package com.robot.service.impl.monitor;

import com.robot.detector.DetectorPosition;
import com.robot.detector.MoveDetector;
import com.robot.utils.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.*;

/**
 * Created by Lechu on 08.05.2017.
 */
@Service
public class ShieldImpl implements Shield {
    Logger logger = LoggerFactory.getLogger(ShieldImpl.class);
    List<DetectorPosition> detectorPositions;

    List<ProtectedZone> protectedZones;
//    ExecutorService thradExecutor;

    @PostConstruct
    public void buildShield() {
        logger.info("Build shield");
        AtomicInteger position = new AtomicInteger(0);
        if(detectorPositions == null) {
            return;
        }
        List<DetectorPosition> ordered = detectorPositions.stream()
            .sorted(Comparator.comparingInt(DetectorPosition::getStartAngle))
            .collect(Collectors.toList());

        List<Pair<Integer, DetectorPosition>> points = detectorPositions.stream()
            .flatMap(dp -> {
                    List<Pair<Integer, DetectorPosition>> pairs = asList(new Pair(dp.getStartAngle(), dp), new Pair(dp.getEndAngle(), dp));
                    return pairs.stream();
                }
            )
            .sorted(Comparator.comparingInt(dp -> dp.getKey()))
            .collect(toList());

        logger.info("Available {} detectors", ordered.size());
        protectedZones = points.stream().map(
            dp -> {
                int positionZone = position.getAndIncrement();
                Pair<Integer, DetectorPosition> nextAngel;
                Integer fromPosition = dp.getKey();
                if (points.size() > positionZone + 1) {
                    nextAngel = points.get(positionZone + 1);
                } else {
                    Set<MoveDetector> detectors = new HashSet<>();
                    detectors.add(dp.getValue().getDetector());
                    return new ProtectedZone(positionZone, fromPosition, dp.getValue().getEndAngle(), detectors);
                }
                int toPosition = nextAngel.getKey();
                Set<MoveDetector> detectors = ordered.stream()
                    .filter(detPos -> detPos.getStartAngle() <= fromPosition
                        && toPosition <= detPos.getEndAngle())
                    .map(ndp -> ndp.getDetector())
                    .collect(toSet());
                return new ProtectedZone(positionZone, fromPosition, toPosition, detectors);
            }
        ).collect(Collectors.toList());
        logger.info("Shield ready....");
        protectedZones.stream()
            .forEach(z ->
                logger.info("Zone {} prtect from {} to {} including detectors[{}]",
                        z.getZone(), z.getFromAngle(), z.getToAngle(), z.getDetectorReuiered().stream()
                        .map(d->d.getName()).collect(joining(",")))
            );
    }

    @Override
    public Optional<List<ProtectedZone>> protect() {
        return Optional.ofNullable(checkAllZones());
    }

    private List<ProtectedZone> checkAllZones() {
        return protectedZones.stream().parallel().filter(zone -> {
            long numberOrDetectorsWithMoveDetected = zone.getDetectorReuiered().stream()
                .parallel()
                .filter(md -> md.isMoveDetected()).count();
            if (numberOrDetectorsWithMoveDetected == zone.getDetectorReuiered().size()) {
                logger.info("Move detected in zone {}  [{}, {}]",
                    zone.getZone(), zone.getFromAngle(), zone.getToAngle());
                return true;
            }
            return false;
        }).collect(toList());
    }

    public List<ProtectedZone> getProtectedZones() {
        return protectedZones;
    }

    public List<DetectorPosition> getDetectorPositions() {
        return detectorPositions;
    }

    public void setDetectorPositions(List<DetectorPosition> detectorPositions) {
        this.detectorPositions = detectorPositions;
    }
}
