package com.robot.service;

import java.io.IOException;

/**
 * Created by Lechu on 24.10.2016.
 */
public interface VideoStreamService {
    boolean startVideo();

    boolean stopVideo();

    String takePhoto() throws IOException;
}
