package device;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Sneha
 */
public class ButtonListener implements ActionListener {
  ButtonListener() {
  }

   public JTextField txt;
    @Override
  public void actionPerformed (ActionEvent e)  
    {
        if (e.getActionCommand().equals("add device")) 
        {
            add_device ob=new add_device(this);
            JFrame f=new JFrame("Add Device");
            f.setLayout(new GridLayout(2, 1));
            JLabel l=new JLabel("enter mac id:");
            f.add(l);
            JLabel mac_id= new JLabel("", JLabel.LEFT);        
            mac_id.setText("enter mac id:");
            f.add(mac_id);
            txt=new JTextField(20);
            f.add(txt);
            JButton b=new JButton("submit");
            b.addActionListener(ob);
            f.add(b);
            f.setSize(300,300);
            f.pack();
            f.setVisible(true);
        }
        
        
        if (e.getActionCommand().equals("remove device"))
        {
            remove_device ob=new remove_device(this);
            JFrame f=new JFrame("Remove Device");
            f.setLayout(new GridLayout(2, 1));
            JLabel l=new JLabel("enter ip address:");
            f.add(l);
            txt=new JTextField(20);
            f.add(txt);
            JButton b=new JButton("submit");
            b.addActionListener(ob);
            f.add(b);
            f.setSize(300,300);
            f.pack();
            f.setVisible(true);
        }
        
        if (e.getActionCommand().equals("view status"))
        {
            view_status ob=new view_status();
            JFrame f=new JFrame("Status of network devices");
            f.setSize(400, 300);
            ob.check_status();
            
            f.setVisible(true);
        }
    }
}
    
