package com.robot.service.impl.monitor;

import com.robot.detector.MoveDetector;

import java.util.Set;

/**
 * Created by Lechu on 10.05.2017.
 * Zone descirbe from which to which angel is protection done
 */
public final class ProtectedZone {
    private final int zone;
    private final double fromAngle;
    private final double toAngle;
    private final Set<MoveDetector> detectorReuiered;
    private final double middlePoint;

    public ProtectedZone(int zone, int fromAngle, int toAngle, Set<MoveDetector> detectorReuiered) {
        this.zone = zone;
        this.fromAngle = fromAngle;
        this.toAngle = toAngle;
        this.detectorReuiered = detectorReuiered;
        this.middlePoint = toAngle - (toAngle - fromAngle) / 2;
    }

    public int getZone() {
        return zone;
    }

    public double getFromAngle() {
        return fromAngle;
    }

    public double getToAngle() {
        return toAngle;
    }

    public double getMiddlePoint() {
        return middlePoint;
    }

    public Set<MoveDetector> getDetectorReuiered() {
        return detectorReuiered;
    }
}
