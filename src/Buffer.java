import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Buffer implements Runnable {
    
    private final static Queue<String> queue = new LinkedList<>();
    private final static Object lock = new Object();
    private static boolean lockProduce;
    private static boolean lockConsume;
    private final static int repeat = 5;

    public Buffer(){
        lockProduce = false;
        lockConsume = true;
    }

    public void produce(){
        synchronized (lock) {
            try {
                while(lockProduce){
                    lock.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            System.out.println("\nProducer working:");

            Random rdm = new Random();

            int num = rdm.nextInt(0, 21);

            queue.add("Data #" + num);
            num = rdm.nextInt(0, 21);

            printBuffer();
    
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            if(queue.size() == 5){
                lockProduce = true;
                lockConsume = false;
                lock.notify();
            }
        }
        
    }

    public void consume(){
        synchronized (lock) {

            try {
                while(lockConsume){
                    lock.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("\nConsumer working:");

            if(!queue.isEmpty()){
                queue.poll();
                printBuffer();
        
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if(queue.isEmpty()){
                lockConsume = true;
                lockProduce = false;
                lock.notify();
            }
        
        }

    }

    public void printBuffer(){

        if(queue.size() > 0){
            Object[] str = queue.toArray();

            System.out.print("Buffer Data: {");

            for(int i=0; i<queue.size(); i++){
                if(i == queue.size()-1){
                    System.out.println(str[i] + "}");
                }
                else{
                    System.out.print(str[i] + ", ");
                }
            }
        }
        else{
            System.out.println("Buffer Data: {}");
        }
        
    }

    @Override
    public void run() {
        for(int h=0; h<repeat; h++){
                if(Thread.currentThread().getName().equalsIgnoreCase("Producer")){
                for (int i = 0; i < 5; i++) {
                    produce();
                }
                
            }
            else if(Thread.currentThread().getName().equalsIgnoreCase("Consumer")){
                for (int i = 0; i < 5; i++) {
                    consume();
                }
            }
        }
    }

}
