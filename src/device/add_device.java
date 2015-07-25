/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package device;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sneha
 */
public class add_device implements ActionListener {

String mac_id=null;
 ButtonListener obj;
    
    public add_device(ButtonListener a) 
    {
        obj=a;
    }
   @Override
     public void actionPerformed (ActionEvent e) 
    {
        
        mac_id=obj.txt.getText();
        
        System.out.println(mac_id);
        ProcessBuilder builder;
        builder = new ProcessBuilder("netsh","dhcp","server","scope","193.168.1.0","show","iprange");
        builder.redirectErrorStream(true);
        Process p;
        int i=0,j=0;
        String start_ip="";//to store starting ip address
        String end_ip=""; 
        ArrayList<String> l=new ArrayList();
        int count=0;
        String str1="",str2="";
        int x,y;
        try 
        {
            p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while (true) 
            {
                line = r.readLine();
                line=r.readLine();
                line=r.readLine();
                line=r.readLine();
                line=r.readLine();
                line=r.readLine();
                line=r.readLine();
                if (line == null) { break; }
                System.out.println(line);
                while(line.charAt(i)!='1' &&(line.charAt(i)!='\n') )
                {
                    i++;
                }
                do
                {
                    start_ip=start_ip+line.charAt(i);
                    i++;
                }while(line.charAt(i)!=' ');
                System.out.println("start_ip is:"+start_ip);
                while(line.charAt(i)!='1'&&(line.charAt(i)!='\n'))
                {
                    i++;
                }
                do
                {
                    end_ip=end_ip+line.charAt(i);
                    i++;
                }while(line.charAt(i)!=' ');
                System.out.println("end_ip is:"+end_ip);
            }
            i=0;
            while(i<start_ip.length()-1)
            {
                 if(start_ip.charAt(i)=='.')
                     count++;
                 if(count==3)
                      str1=str1+start_ip.charAt(i+1);
                 i++;
             }
            i=0;count=0;
             while(i<end_ip.length()-1)
             {
                 if(end_ip.charAt(i)=='.')
                     count++;
                 if(count==3)
                      str2=str2+end_ip.charAt(i+1);
                 i++;
             }
             System.out.println(str1+' '+str2);
             x=Integer.parseInt(str1);
             y=Integer.parseInt(str2);
             for(i=x;i<=y;i++)
             {
                 l.add("193.168.1."+i);
                 j++;
             }
             for(i=0;i<l.size();i++)
             {
                System.out.println(l.get(i));
             }
            ProcessBuilder b;
            b = new ProcessBuilder("netsh","dhcp","server","scope","192.168.1.0","show","client");
            b.redirectErrorStream(true);
            Process pr = b.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String input = null;
            for(i=0;i<=7;i++)
            {
                br.readLine(); 
            }
            i=0;
            while(true)
            {
                input=br.readLine();
                if(input==null)
                {
                    break;
                }
                System.out.println(input.length()+" "+ input);
                int c=0;
                j=0;
                while(j<input.length()-1)
                {
                    String x1="";
                    while(input.charAt(j)!=' ')
                    {   
                        x1=x1+input.charAt(j);
                        j++;
                    }
                    System.out.println(" "+x1);
                    l.remove(x1);
                    x1="";
                    if(input.charAt(j)==' ' &&(j<input.length()-1))
                    {
                        break;
                    }
                }
            }
            for(i=0;i<=l.size()-1;i++)
            {
                System.out.println("abc "+l.get(i));
            } 
        }
        catch (IOException ex) 
        {
            Logger.getLogger(add_device.class.getName()).log(Level.SEVERE, null, ex);
        }
        try
        {
            System.out.println(l.get(0));
            builder = new ProcessBuilder("netsh", "dhcp","server","scope","192.168.1.0","add","reservedip",l.get(0),mac_id);
            builder.redirectErrorStream(true);
            p = builder.start();
            BufferedReader bfr = new BufferedReader(new InputStreamReader(p.getInputStream()));System.out.println("1");
            String line;
            while (true) 
            {
                line = bfr.readLine();System.out.println("1");
                if (line == null) { System.out.println("1");break; }
                System.out.println(line);
            }
        }
        catch (IOException ec)
        {
            System.out.println("exception");
        }
    }
}


