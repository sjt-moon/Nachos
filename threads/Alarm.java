package nachos.threads;

import nachos.machine.*;
import java.util.PriorityQueue;

/**
 * Uses the hardware timer to provide preemption, and to allow threads to sleep
 * until a certain time.
 */
public class Alarm {
    final PriorityQueue<KThreadWithTimestamp> waitQueue = new PriorityQueue<>((t1, t2) -> {
        t1.getTimestamp() - t2.getTimestamp();
    })

	final class KThreadWithTimestamp {
	    private KThread thread;
	    private long timestamp;

	    public KThreadWithTimestamp(Builder builder) {
            this.thread = builder.thread;
            this.timestamp = builder.timestamp;
        }

        public long getTimestamp() {return this.timestamp;}

        public KThread getThread() {return this.thread;}

        public static class Builder {
	        private KThread thread;
	        private long timestamp;

	        public static Builder newInstance() {return new Builder();}

	        private Builder() {}

	        public Builder setKThread(KThread thread) {
	            this.thread = thread;
	            return this;
            }

            public Builder setTimestamp(long timestamp) {
	            this.timestamp = timestamp;
	            return this;
            }

            public KThreadWithTimestamp build() {
	            return new KThreadWithTimestamp(this);
            }
        }
    }

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
	    // KThread.currentThread().yield();
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
		// for now, cheat just to get something working (busy waiting is bad)
		// long wakeTime = Machine.timer().getTime() + x;
		// while (wakeTime > Machine.timer().getTime())
		//  	KThread.yield();
        Machine.interrupt().disable();
        KThreadWithTimestamp kThreadWithTimestamp = KThreadWithTimestamp.Builder.newInstance()
                .setKThread(KThread.currentThread())
                .setTimestamp(Machine.timer().getTime() + x)
                .build();
        waitQueue.offer(kThreadWithTimestamp);
        KThread.currentThread().sleep();
	}
}
