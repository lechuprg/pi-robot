package com.robot.msg.request;


import com.robot.msg.response.ServiceDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lechu on 21.09.2016.
 */
public class RobotRequest {
    List<ServiceDetail> serviceDetails;

    public void addService(ServiceDetail serviceDetail) {
        if(serviceDetails==null) {
            serviceDetails = new ArrayList<>(5);
        }
        serviceDetails.add(serviceDetail);
    }

    public List<ServiceDetail> getServiceDetails() {
        return serviceDetails;
    }

}
