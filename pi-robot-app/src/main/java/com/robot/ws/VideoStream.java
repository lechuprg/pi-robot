package com.robot.ws;

import com.robot.msg.response.OperationTypes;
import com.robot.msg.response.RobotResponse;
import com.robot.msg.response.Status;
import com.robot.service.GuardService;
import com.robot.service.VideoStreamService;
import com.robot.service.impl.monitor.ProtectedZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Lechu on 17.10.2016.
 */
@RestController
public class VideoStream {

    Logger logger = LoggerFactory.getLogger(TempCheck.class);

    @Autowired
    private VideoStreamService videoStreamService;

    @Autowired
    private GuardService guardService;


    @RequestMapping("/startVideo")
    @ResponseBody
    public RobotResponse startVideo() throws InterruptedException {
        logger.info("Start video");
        boolean b = videoStreamService.startVideo();
        RobotResponse robotResponse = new RobotResponse(b? Status.OK: Status.ERROR, OperationTypes.CAMERA_ON);
        return robotResponse;
    }

    @RequestMapping("/stopVideo")
    @ResponseBody
    public RobotResponse stopVideo() throws InterruptedException {
        logger.info("Stop video");
        boolean b = videoStreamService.stopVideo();
        RobotResponse robotResponse = new RobotResponse(b? Status.OK: Status.ERROR, OperationTypes.CAMERA_OFF);
        return robotResponse;
    }

    @RequestMapping("/takePhoto")
    @ResponseBody
    public RobotResponse takePhoto() throws InterruptedException {
        logger.info("Take photo and send");
        guardService.takePhotoAndSend(new ProtectedZone(1, 0,180, null));
        RobotResponse robotResponse = new RobotResponse(Status.OK, OperationTypes.CAMERA_ON);
        return robotResponse;
    }
}
