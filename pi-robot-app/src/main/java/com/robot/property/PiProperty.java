package com.robot.property;

/**
 * Created by Lechu on 10.10.2016.
 */
public enum PiProperty {
    NOTIFICATION_EMAIL,
    WEATHER_CHECK_INTERVAL,
    WEATHER_DEFAULT_RETURN_DAYS,
    TEMPERATURE_MIN_THRESHOLD,
    TEMPERATURE_MAX_THRESHOLD,
    MOVE_DETECTOR_ON,
    MOVE_DETECTOR_THRESHOLD,
    /**
     * System information
     */
    SYSTEM_START_TIME(false),
    NUMBER_OF_TEMP_WARNINGS(false),
    NUMBER_OF_MOVE_WARNINGS(false)
    ;

    private boolean writable;
    PiProperty() {

        this.writable = true;
    }

    PiProperty(boolean writable) {

        this.writable = writable;
    }

    @Override
    public String toString() {
        return this.name().replace("_", ".").toLowerCase();
    }

    public static PiProperty fromString(String key) {
        return PiProperty.valueOf(key.replace(".", "_").toUpperCase());
    }

    public boolean isWritable() {
        return writable;
    }
}
