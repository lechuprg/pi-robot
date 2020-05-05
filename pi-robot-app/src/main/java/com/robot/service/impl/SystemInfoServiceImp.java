package com.robot.service.impl;

import com.robot.db.PropertiesDb;
import com.robot.msg.response.OperationTypes;
import com.robot.msg.response.ServiceDetail;
import com.robot.service.SystemInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.robot.property.PiProperty.*;

/**
 * Created by Lechu on 10.10.2016.
 */
@Service
public class SystemInfoServiceImp implements SystemInfoService {
    Logger logger = LoggerFactory.getLogger(SystemInfoServiceImp.class);
    @Autowired
    PropertiesDb propertiesDb;

    public ServiceDetail getSystemInfo() {
        logger.info("Read properties....");
        ServiceDetail serviceDetail = new ServiceDetail(OperationTypes.SYSTEM_INFO);
        String startTime = propertiesDb.readProperty(SYSTEM_START_TIME);
        String tempWarnings = propertiesDb.readProperty(NUMBER_OF_TEMP_WARNINGS);
        String moveWarnings = propertiesDb.readProperty(NUMBER_OF_MOVE_WARNINGS);
        serviceDetail.addKeyValue("Author","LeChU");
        serviceDetail.addKeyValue("Version","1.0-SNAPSHOT");
        serviceDetail.addKeyValue(SYSTEM_START_TIME.toString(), startTime);
        serviceDetail.addKeyValue(NUMBER_OF_TEMP_WARNINGS.toString(), tempWarnings);
        serviceDetail.addKeyValue(NUMBER_OF_MOVE_WARNINGS.toString(), moveWarnings);

        return serviceDetail;
    }

}
