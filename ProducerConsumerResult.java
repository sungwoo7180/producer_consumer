class CircularQueue {
    private int[] buffer;
    private int head;
    private int tail;
    private int count;
    
    public CircularQueue(int size) {
        buffer = new int[size];
        head = 0;
        tail = 0;
        count = 0;
    }
    
    public synchronized void put(int value, String name) {
        while (count == buffer.length) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        head = (head + 1) % buffer.length;
        buffer[head] = value;
        count++;
        
        System.out.println(name + " put: " + value);
        
        notifyAll();
    }
    
    public synchronized int get(String name) {
        while (count == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        tail = (tail + 1) % buffer.length;
        int value = buffer[tail];
        count--;
        
        System.out.println(name + " got: " + value);
        
        notifyAll();
        
        return value;
    }
}

class Producer extends Thread {
    private CircularQueue boundedBuffer;
    private String number;
    
    public Producer(CircularQueue c, String number) {
        super("Producer #" + number);
        boundedBuffer = c;
        this.number = number;
    }
    
    public void run() {
        for (int i = 0; i < 10; i++) {
            boundedBuffer.put(i, getName());
            try {
                sleep((int) (Math.random() * 100));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

class Consumer extends Thread {
    private CircularQueue boundedBuffer;
    private String number;
    
    public Consumer(CircularQueue c, String number) {
        super("Consumer #" + number);
        boundedBuffer = c;
        this.number = number;
    }
    
    public void run() {
        int value = 0;
        for (int i = 0; i < 10; i++) {
            value = boundedBuffer.get(getName());
        }
    }
}

public class ProducerConsumerResult {
    public static void main(String[] args) {
        CircularQueue c = new CircularQueue(1);
        
        Producer p1 = new Producer(c, "1");
        Consumer c1 = new Consumer(c, "1");
        
        p1.start();
        c1.start();
    }
}