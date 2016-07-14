package de.hhu.imtgg.objects;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TrackingHelper {
    static LocalDateTime start;
    static LocalDateTime stop;

    public static void startTrackingCounter(){
        start = LocalDateTime.now();
    }

    public static void stopTrackingCounter(){
        stop = LocalDateTime.now();
    }

    public static long difference(){
         return ChronoUnit.SECONDS.between(start,stop);
    }
}
