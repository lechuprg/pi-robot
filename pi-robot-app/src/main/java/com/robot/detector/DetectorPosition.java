package com.robot.detector;

/**
 * Created by Lechu on 08.05.2017.
 */
public final class DetectorPosition implements Comparable {

    private static final int CIRCLE_MAX_ANGLE = 360;
    private final String name;
    private final int startAngle;
    private final int angleSize;
    private final MoveDetector detector;

    public DetectorPosition(String name, MoveDetector detector, int startAngle, int angleSize) {
        this.name = name;
        this.startAngle = startAngle;
        this.angleSize = angleSize;
        this.detector = detector;
    }


    public int getStartAngle() {
        return startAngle;
    }

    public int getEndAngle() {
        int endAngleTotal = startAngle + angleSize;
        if (endAngleTotal <= CIRCLE_MAX_ANGLE) {
            return endAngleTotal;
        }
        return endAngleTotal - CIRCLE_MAX_ANGLE;
    }

    public int getAngleSize() {
        return angleSize;
    }

    public MoveDetector getDetector() {
        return detector;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
