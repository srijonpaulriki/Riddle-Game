package Srijon;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

class InputReader implements Runnable {
    static final class Entry {
        final String text;
        final long receivedAt;
        final boolean eof;

        Entry(String text, boolean eof) {
            this.text = text;
            this.eof = eof;
            this.receivedAt = System.nanoTime();
        }
    }

    private final BlockingQueue<Entry> queue =
            new LinkedBlockingQueue<>(256);
    private volatile boolean closed;

    @Override
    public void run() {

        Scanner scanner = new Scanner(System.in);
        try {
            while (scanner.hasNextLine()) {
                queue.put(new Entry(scanner.nextLine(), false));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            closed = true;
            queue.offer(new Entry("", true));
        }
    }

    Entry take() throws InterruptedException {
        if (closed && queue.isEmpty()) {
            return new Entry("", true);
        }
        return queue.take();
    }

    Entry poll(long nanos) throws InterruptedException {
        if (closed && queue.isEmpty()) {
            return new Entry("", true);
        }
        return queue.poll(Math.max(0, nanos), TimeUnit.NANOSECONDS);
    }
}
