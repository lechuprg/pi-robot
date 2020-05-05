package com.robot.service;

import com.robot.msg.response.ServiceDetail;
import com.robot.property.PiProperty;

/**
 * Created by Lechu on 24.10.2016.
 */
public interface PropertyService {
    ServiceDetail getProperties();
    ServiceDetail getWritableProperties();
    String getProperty(PiProperty property);

    boolean updateProperties(String key, String value);
}
