package device;
import java.awt.FlowLayout;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
class ButtonFrame extends JFrame
{
    JButton button ;
    // constructor for ButtonFrame
    ButtonFrame(String title)throws IOException 
    {
        super( title );                     // invoke the JFrame constructor
        setLayout( new FlowLayout() );      // set the layout manager

        button = new JButton("add device"); // construct a JButton
        add(button); // add the button to the JFrame
        button.addActionListener(new ButtonListener());
        button=new JButton("remove device");
        add(button);
        button.addActionListener(new ButtonListener());
        button=new JButton("view status");
        add(button);
        button.addActionListener(new ButtonListener());
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );    
    }

  }
class Add_remove_device
{
    public static void main ( String[] args ) throws IOException
    {
        ButtonFrame frm = new ButtonFrame("Welcome admin");

        frm.setSize( 400, 300 );     
        frm.setVisible( true );   
    }
}
