package com.robot.service.impl.monitor;

import java.util.List;
import java.util.Optional;

/**
 *
 * Created by Lechu on 08.05.2017.
 */

public interface Shield {

    /**
     * @return degree from 0 to 360 with current position of enemy
     */
    Optional<List<ProtectedZone>> protect();
}