package com.robot.service.impl;

import com.robot.service.VideoStreamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Lechu on 21.10.2016.
 */
@Service
public class VideoStreamServiceImpl implements VideoStreamService {
    private static final Logger logger = LoggerFactory.getLogger(VideoStreamServiceImpl.class);
    public static final String SHELL_SCRIPT_START_VIDEO = "start_stream.sh";
    public static final String SHELL_SCRIPT_STOP_VIDEO = "stop_stream.sh";

    public boolean startVideo() {
        return executeScript(SHELL_SCRIPT_START_VIDEO);
    }

    @PostConstruct
    public void init() {
        startVideo();
    }

    @Override
    public boolean stopVideo() {
        return executeScript(SHELL_SCRIPT_STOP_VIDEO);
    }

    @Override
    public String takePhoto() throws IOException {
        logger.info("Take photo...");
        if (startVideo()) {
            logger.info("Camera on...");
            String formatDateTime = getDateTimeNow();
            URL website = new URL("http://127.0.0.1:9000/?action=snapshot");
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            String filePath = "/home/pi/robot/photo/" + formatDateTime + "_robot.jpg";
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            logger.info("Image save {}", filePath);
            stopVideo();
            return filePath;
        }
        return null;
    }

    private String getDateTimeNow() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss_SSS");
        return dateTime.format(formatter);
    }

    private boolean executeScript(String scriptName) {
        try {
            ProcessBuilder pb = new ProcessBuilder("/home/pi/" + scriptName);
            pb.start();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
