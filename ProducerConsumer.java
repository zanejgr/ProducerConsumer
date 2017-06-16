import java.util.concurrent.*;

public class ProducerConsumer{
	static int theBuffer;
	static Semaphore s = new Semaphore(1);
	
	public static void main(String []args){
		Producer [] p = new Producer[4515];
		Consumer [] c = new Consumer[5735];
		
		for(int i = 0; i < p.length; i++){
			p[i]=new Producer(i);
			p[i].start();
		}
		
		for(int i = 0; i < c.length;i++){
			c[i]=new Consumer(i);
			c[i].start();
		}
	}
	
	private static class Producer extends Thread{
		int i;
		public Producer(int i){
			super();
			this.i=i;
		}
		public void run(){
			for(;;){
				try{
					sleep((int)(Math.random()*1000));
				}catch(InterruptedException e){}
				System.out.println("Producer " + i + ": attempting to acquire");
				try{s.acquire();}
					catch(InterruptedException e){}
				System.out.println("Producer "+i+": resource acquired!");
				try{
					sleep((int)(Math.random()*1000));
				}catch(InterruptedException e){}
				theBuffer+=((int)(Math.random()*6));
				System.out.println("Producer "+i+": resource released. Buffer:"+theBuffer);
				
				s.release();
			}
		}
	}
	
	private static class Consumer extends Thread{
		int i = 0;
		public Consumer(int i){
			super();
			this.i=i;
		}
		public void run(){
			for(;;){
				try{
					sleep((int)(Math.random()*1000));
				}catch(InterruptedException e){}
				System.out.println("Consumer " + i + ": attempting to acquire");
				try{s.acquire();}
				catch(InterruptedException e){}
				System.out.println("Consumer "+i+": resource acquired!");
				try{
					sleep((int)(Math.random()*1000));
				}catch(InterruptedException e){}
				int need = (int)(Math.random()*6);
				if(theBuffer>=need){
					theBuffer -= need;
					System.out.println("Consumer "+i+": Got what I needed!");
				}
				else{
					System.out.println("Consumer "+i+": resource unavailable");
				}
				s.release();
				System.out.println("Consumer "+i+": resource released. Buffer:"+theBuffer);
			}
		}
	}
}
