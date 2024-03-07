package com.solaramps.loginservice.utils;

import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class Utility {

    public static String getFormattedMillis(long millis) {
        return String.format("%02d min, %02d sec",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }
}
