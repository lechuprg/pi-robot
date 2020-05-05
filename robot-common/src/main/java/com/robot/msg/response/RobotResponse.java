package com.robot.msg.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lechu on 07.08.2016.
 */
public class RobotResponse {
    public Status status;
    public OperationTypes operationType;
    List<ServiceDetail> serviceDetails;

    public RobotResponse() {}
    public RobotResponse(Status status, OperationTypes operationType) {
        this.status = status;
        this.operationType = operationType;
    }

    public void addService(ServiceDetail serviceDetail) {
        if(serviceDetails==null) {
            serviceDetails = new ArrayList<>(5);
        }
        serviceDetails.add(serviceDetail);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public OperationTypes getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationTypes operationType) {
        this.operationType = operationType;
    }

    public List<ServiceDetail> getServiceDetails() {
        return serviceDetails;
    }

    public void setServiceDetails(List<ServiceDetail> serviceDetails) {
        this.serviceDetails = serviceDetails;
    }
}
