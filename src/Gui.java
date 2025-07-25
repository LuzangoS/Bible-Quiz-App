import javax.swing.*;
import java.awt.Container;
import java.awt.GridLayout;


class Window extends JFrame{
    JButton button =new JButton("sample");
    JButton button1 =new JButton("I Am new Button");
    JButton button2 =new JButton("What is new");
    Container container;
    JPanel mainPanel=new JPanel(new GridLayout(1,3));
    JTextField text=new JTextField("I am learning Java");
    public Window(){
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
        container=getContentPane();

        mainPanel.add(button);
        mainPanel.add(button1);
        mainPanel.add(button2);
        mainPanel.add(text);
        container.add(mainPanel);
        setVisible(true);
    }
}

public class Gui{
    public static void main(String[]args){
        Window window=new Window();
    }
}