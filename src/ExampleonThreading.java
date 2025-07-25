import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ExampleonThreading extends JFrame implements Runnable{
    JLabel label = new JLabel("Time will show here");
    public ExampleonThreading(){ 
        setSize(300,200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel= new JPanel();
        panel.add(label);
        setContentPane(panel);
        setVisible(true);//makes the Jframe visible
    }

   


@Override
public void run(){
    for(;;){

        Date date = new Date();//create a new date object
        label.setText(date.toString());
        try{
            Thread.sleep(1000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }    
    }
}

public static void main(String[] args) {
    ExampleonThreading od =new ExampleonThreading();
    new Thread(od).start();//create and start the thread in one line
}
}