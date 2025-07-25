import javax.swing.*;
import java.awt.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class TextAreaExercise extends JFrame implements documentListener{
 JPanel panel= new JPanel(new GridLayout(2,1));
 JTextArea textArea= new JTextArea(10,30);
 JLabel label = new JLabel("numbers of letters");

 Container container;
   public TextAreaExercise(){
         setSize(400,300);
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

         container=getContentPane();
         panel.add(textArea);
         panel.add(label);
         container.add(panel);
         
        setVisible(true);
    }
public static void main(String[]args){
  new TextAreaExercise();
}
}

