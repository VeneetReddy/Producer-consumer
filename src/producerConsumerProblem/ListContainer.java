package producerConsumerProblem;

import java.util.ArrayList;
import java.util.List;

class Adder implements Runnable {

	private ListContainer container;
	
	Adder(ListContainer container) {
		this.container = container;
	}
	
	@Override
	public void run() {
		
		for(int counter = 0; counter <= 10; counter++)
		{
			container.add(counter);
		}
	}
	
}

class Remover implements Runnable {

	private ListContainer container;
	
	Remover(ListContainer container)
	{
		this.container = container;
	}
	
	@Override
	public void run() {
		while(true)
		{
			/**
			 * Using this to stop the thread.
			 * Call Thread.interrupt() from the main, the isInterrupted will return true
			 * and stop this thread
			 */
			if(Thread.currentThread().isInterrupted())
				break;
			container.remove();
		}
	}
}

public class ListContainer {

	 static List<Integer> myList = new ArrayList<>();
	
	public synchronized void add(int item)
	{
		myList.add(item);
		notify();
		System.out.println("Added: " + item);

	}
	
	public synchronized int remove()
	{
		/**
		 * Returns -1 when there is something wrong.
		 * Precisely when an InterruptedException is thrown.
		 */
		
		int item = -1;
		try {
			if(myList.size() == 0)
				wait();
			item = myList.remove(0);
			System.out.println("Removed: " + item);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return item;
	}
	
	public static void main(String[] args) {
		ListContainer myListContainer = new ListContainer();
		Thread adderThread = new Thread(new Adder(myListContainer));
		Thread removerThread = new Thread(new Remover(myListContainer));
		

		removerThread.start();
		adderThread.start();
		
		try {
			adderThread.join();
			removerThread.join(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//Calling interrupt to stop the thread
		removerThread.interrupt();
		
		System.out.println("Finished adding and removing!");
		
	}

}
