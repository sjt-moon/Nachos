package nachos.unittest;

import nachos.machine.*;
import nachos.threads.ThreadedKernel;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Unit tests for Alarm
 * @author sjt-moon
 */
public class AlarmTest {
    // private static final Logger LOGGER = Logger.getLogger( AlarmTest.class.getName() );

    private static final String TIME_ERROR_TEMPLATE = "time used: %d, time used prediction: %d, error: %.2f%%";

    private static final double MAX_ERROR_PERCENTAGE = 0.5;

    public static void test1() {
        int durations[] = {1000, 10*1000, 100*1000};
        long t0, t1;

        for (int d : durations) {
            t0 = Machine.timer().getTime();
            ThreadedKernel.alarm.waitUntil (d);
            t1 = Machine.timer().getTime();
            double errorPercentage = 1.0 * Math.abs(t1 - t0 - d) / d;
            System.out.println(String.format(TIME_ERROR_TEMPLATE, t1-t0, d, 100.0*errorPercentage));
            //LOGGER.log(Level.FINE, String.format(TIME_ERROR_TEMPLATE, t1-t0, d, 100.0*errorPercentage));
            assert errorPercentage < MAX_ERROR_PERCENTAGE;
        }
    }
}