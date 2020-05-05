package com.robot.service.impl;

import com.robot.db.PropertiesDb;
import com.robot.msg.response.OperationTypes;
import com.robot.msg.response.ServiceDetail;
import com.robot.msg.response.Status;
import com.robot.property.PiProperty;
import com.robot.service.PropertyService;
import com.robot.utils.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by Lechu on 10.10.2016.
 */
@Service
public class PropertyServiceImpl implements PropertyService {
    Logger logger = LoggerFactory.getLogger(ServoServiceImpl.class);
    @Autowired
    private PropertiesDb propertiesDb;

    @PostConstruct
    public void initialize() {
        checkProperties();
        propertiesDb.updateProperty(PiProperty.SYSTEM_START_TIME, DateTime.now().toString());
    }

    public ServiceDetail getProperties() {
        logger.info("Read properties....");
        final ServiceDetail serviceDetail = new ServiceDetail(OperationTypes.PROPERTY);
        Map<String, String> allProperty = propertiesDb.getAllProperty();
        allProperty.entrySet().stream().forEach(entry -> {
            serviceDetail.addKeyValue(entry.getKey().toLowerCase(), entry.getValue());
        });
        return serviceDetail;
    }

    public ServiceDetail getWritableProperties() {
        logger.info("Read properties....");
        final ServiceDetail serviceDetail = new ServiceDetail(OperationTypes.PROPERTY);
        Map<String, String> allProperty = propertiesDb.getWritableProperty();
        allProperty.entrySet().stream().forEach(entry -> {
            serviceDetail.addKeyValue(entry.getKey().toLowerCase(), entry.getValue());
        });
        return serviceDetail;
    }

    @Override
    public String getProperty(PiProperty property) {
        return propertiesDb.readProperty(property);
    }


    public boolean updateProperties(String key, String value) {
        propertiesDb.updateProperty(PiProperty.fromString(key), value);
        return true;
    }

    public ServiceDetail updateProperties(Map<String, String> values) {

        ServiceDetail serviceDetail = new ServiceDetail(OperationTypes.PROPERTY);
        try {
            values.entrySet().stream().forEach(entry -> updateProperties(entry.getKey(), entry.getValue()));
            serviceDetail.setStatus(Status.OK);
        } catch (Exception e) {
            logger.info("Unable to updaate properiteis", values);
            serviceDetail.setStatus(Status.ERROR);
        }
        return serviceDetail;
    }

    public void checkProperties() {
        Arrays.stream(PiProperty.values()).forEach(
                property -> {
                    try {
                        propertiesDb.readProperty(property);
                    } catch (Exception e) {
                        propertiesDb.addProperty(property, "");
                    }
                }
        );
    }
}
