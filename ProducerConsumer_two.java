class CircularQueue{
	private int contents[], size;
	private int head, tail;
	private int count;
	
	public CircularQueue(int sz) {
		size = sz;
		head = tail = 0; 
		count = 0;
		contents = new int[size];
	}
	public synchronized int get(String name) {
		int res;
		while (count == 0)
			try { wait();
			} catch (InterruptedException e) {}
		res = contents[tail];
		System.out.println(name + " got: " + contents[tail]);
		tail = (tail + 1) % size;
		notify();
		count--;
		return res;
	}
	public synchronized void put(int value, String name) {
		while(count == size)
			try { wait();
			} catch (InterruptedException e) {}
		contents[head] = value;
		System.out.println(name + " put: " + contents[head]);
		head = (head + 1) % size;
		notify();
		count++;
	}
}

class Producer extends Thread{
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
				sleep((int)(Math.random() * 100));
			} catch (InterruptedException e) {}
		}
	}
}

class Consumer extends Thread{
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
public class ProducerConsumer_two {
	public static void main(String[] args) {
		CircularQueue c = new CircularQueue(2);
		
		Producer p1 = new Producer(c, "1");
		Producer p2 = new Producer(c, "2");
		Consumer c1 = new Consumer(c, "1");
		Consumer c2 = new Consumer(c, "2");
		
		p1.start();
		p2.start();
		c1.start();
		c2.start();
	}
}