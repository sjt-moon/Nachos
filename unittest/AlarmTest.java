package nachos.unittest;

import nachos.machine.*;
import nachos.threads.ThreadedKernel;

/**
 * Unit tests for Alarm
 * @author sjt-moon
 */
public class AlarmTest {
    public static void test1() {
        int durations[] = {1000, 10*1000, 100*1000};
        long t0, t1;

        for (int d : durations) {
            t0 = Machine.timer().getTime();
            ThreadedKernel.alarm.waitUntil (d);
            t1 = Machine.timer().getTime();
            System.out.println ("alarmTest1: waited for " + (t1 - t0) + " ticks");
        }
    }
}