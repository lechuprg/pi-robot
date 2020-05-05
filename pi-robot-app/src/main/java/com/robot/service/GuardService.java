package com.robot.service;

import com.robot.service.impl.monitor.ProtectedZone;

/**
 * Created by Lechu on 28.02.2017.
 */
public interface GuardService {


    void takePhotoAndSend(ProtectedZone zone);
}
