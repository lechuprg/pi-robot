package com.robot.msg.response;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Lechu on 13.08.2016.
 */
public class ServiceDetail {
    public OperationTypes serviceName;
    public Status status;
    public Map<String, String> keyValue;

    public ServiceDetail() {};

    public ServiceDetail(OperationTypes serviceName) {
        this.serviceName = serviceName;
    }

    public void addKeyValue(String key, String value) {
        if(keyValue== null) {
            keyValue = new LinkedHashMap<>(5);
        }
        keyValue.put(key, value);
    }

    public OperationTypes getServiceName() {
        return serviceName;
    }

    public void setServiceName(OperationTypes serviceName) {
        this.serviceName = serviceName;
    }

    public Map<String, String> getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(Map<String, String> keyValue) {
        this.keyValue = keyValue;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
