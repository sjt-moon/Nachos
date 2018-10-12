package nachos.threads;

import nachos.machine.*;
import nachos.threads.KThread;

/**
 * KThread with timestamp to be called back from waiting state
 * @author sjt-moon
 */
public class KThreadWithTimestamp {
    private KThread thread;
    private long timestamp;

    public KThreadWithTimestamp(KThread thread, long timestamp) {
        this.thread = thread;
        this.timestamp = timestamp;
    }

    public long getTimestamp() {return this.timestamp;}

    public KThread getThread() {return this.thread;}

}
