import java.util.Scanner;

//making a thread using a thread class
/*public class Multithreading extends Thread{
*/
//making a thread using a thread interface this allows implementation of many threads
public class Multithreading implements Runnable{
    @Override
        public void run(){
        for(int i=0; i<10; i++){
            System.out.println("counting " + i);
            try{
                Thread.sleep(3000);
            }catch(InterruptedException ex){
        }
    }
}
    
      
public static void main(String[] args){
    //this is what we do when we use thread class
    /*  Multithreading cd= new Multithreading();*/

    //down here is how to go when we use interface


    Multithreading cd= new Multithreading();
    Thread thread = new Thread(cd);
    System.out.println("I am starting the thread");
    //call cd.start not run
    thread.start();
    System.out.println("This is after the thread");
   
    /*how to do this in one line
    (new Thread(new Multithreading())).start();
     * 
     */
     
       
      
      
        
} 
}
