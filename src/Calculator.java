import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Window extends JFrame {
    JTextField text = new JTextField("0");
    
    JButton button0 = new JButton("0");
    JButton button1 = new JButton("1");
    JButton button2 = new JButton("2");
    JButton button3 = new JButton("3");
    JButton button4 = new JButton("4");
    JButton button5 = new JButton("5");
    JButton button6 = new JButton("6");
    JButton button7 = new JButton("7");
    JButton button8 = new JButton("8");
    JButton button9 = new JButton("9");
    
    JButton addButton = new JButton("+");
    JButton subButton = new JButton("-");
    JButton mulButton = new JButton("*");
    JButton divButton = new JButton("/");
    JButton equButton = new JButton("=");
    
    Container container;
    
    JPanel textPanel = new JPanel(new FlowLayout());
    JPanel buttonPanel = new JPanel(new GridLayout(1,2));
    JPanel numberPanel = new JPanel(new GridLayout(4,3));
    JPanel operatorPanel = new JPanel(new GridLayout(5,1));
    
    public Window() {
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        container = getContentPane();
        container.setLayout(new BorderLayout());

         // Set preferred size for text field to make it smaller
        text.setPreferredSize(new Dimension(300, 100));
        
        textPanel.add(text);
        
        buttonPanel.add(numberPanel);
        buttonPanel.add(operatorPanel);
        
        numberPanel.add(button7);
        numberPanel.add(button8);
        numberPanel.add(button9);
        numberPanel.add(button4);
        numberPanel.add(button5);
        numberPanel.add(button6);
        numberPanel.add(button1);
        numberPanel.add(button2);
        numberPanel.add(button3);
        numberPanel.add(new JLabel());
        numberPanel.add(button0);
        numberPanel.add(new JLabel());
        
        operatorPanel.add(addButton);
        operatorPanel.add(subButton);
        operatorPanel.add(mulButton);
        operatorPanel.add(divButton);
        operatorPanel.add(equButton);
        
        // Add components to BorderLayout
        container.add(textPanel, BorderLayout.NORTH);
        container.add(buttonPanel, BorderLayout.CENTER);
        
        setVisible(true);
    }
    
   
}

 public class Calculator extends JFrame implements ActionListener{
    JPanel panel=new JPanel();
    JButton button= new JButton("Click me");
    JButton button2= new JButton("Click hello");
    Container container;

    public Calculator(){
         setSize(400,300);
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

         container=getContentPane();
         container.add(panel);
         panel.add(button);
         panel.add(button2);
         button.addActionListener(this);
         button2.addActionListener(this);
        setVisible(true);
    }
    public static void main(String[] args) {
        new Calculator();
       // Window window =new Window();
    }
    @Override
    public void actionPerformed(ActionEvent e){
        //this line helps differentiate which button is being clicked
        String bt=((JButton)e.getSource()).getText();
        if (bt.equals("Click me")){
            
        }
        String data= JOptionPane.showInputDialog("enter some value");

        JOptionPane.showMessageDialog(
            null,
            bt,
            "You called me",
            JOptionPane.INFORMATION_MESSAGE
            );
    }
}