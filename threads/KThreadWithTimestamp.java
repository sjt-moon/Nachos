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

    public KThreadWithTimestamp(Builder builder) {
        this.thread = builder.thread;
        this.timestamp = builder.timestamp;
    }

    public long getTimestamp() {return this.timestamp;}

    public KThread getThread() {return this.thread;}

    public static class Builder {
        private KThread thread;
        private long timestamp;

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