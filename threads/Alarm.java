package nachos.threads;

import nachos.machine.*;
import nachos.threads.KThreadWithTimestamp;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Uses the hardware timer to provide preemption, and to allow threads to sleep
 * until a certain time.
 *
 * @author sjt-moon
 */
public class Alarm {
	final PriorityQueue<KThreadWithTimestamp> waitQueue = new PriorityQueue<>(new Comparator<KThreadWithTimestamp>() {
		@Override
		public int compare(KThreadWithTimestamp o1, KThreadWithTimestamp o2) {
			return o1.getTimestamp() > o2.getTimestamp() ? 1 : -1;
		}
	});

	/**
	 * Allocate a new Alarm. Set the machine's timer interrupt handler to this
	 * alarm's callback.
	 * 
	 * <p>
	 * <b>Note</b>: Nachos will not function correctly with more than one alarm.
	 */
	public Alarm() {
		Machine.timer().setInterruptHandler(new Runnable() {
			public void run() {
				timerInterrupt();
			}
		});
	}

	/**
	 * The timer interrupt handler. This is called by the machine's timer
	 * periodically (approximately every 500 clock ticks). Causes the current
	 * thread to yield, forcing a context switch if there is another thread that
	 * should be run.
	 */
	public void timerInterrupt() {
        Machine.interrupt().disable();
        while (!waitQueue.isEmpty() && waitQueue.peek().getTimestamp() <= Machine.timer().getTime()) {
            waitQueue.poll().getThread().ready();
        }
        KThread.currentThread().yield();
	}

	/**
	 * Put the current thread to sleep for at least <i>x</i> ticks, waking it up
	 * in the timer interrupt handler. The thread must be woken up (placed in
	 * the scheduler ready set) during the first timer interrupt where
	 * 
	 * <p>
	 * <blockquote> (current time) >= (WaitUntil called time)+(x) </blockquote>
	 * 
	 * @param x the minimum number of clock ticks to wait.
	 * 
	 * @see nachos.machine.Timer#getTime()
	 */
	public void waitUntil(long x) {
        Machine.interrupt().disable();
        KThreadWithTimestamp kThreadWithTimestamp = new KThreadWithTimestamp.Builder()
                .setKThread(KThread.currentThread())
                .setTimestamp(Machine.timer().getTime() + x)
                .build();
        waitQueue.offer(kThreadWithTimestamp);
        KThread.currentThread().sleep();
	}
}
