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

/**
 *
 * @author Administrator
 */
public class remove_device implements ActionListener {
       String ip=null;
       ButtonListener obj;
    public remove_device(ButtonListener a) {
         obj=a;
    }
    @Override
     public void actionPerformed (ActionEvent e) 
    { 
        try
        {   
            ip=obj.txt.getText();
            System.out.println(ip);
            int i,j=0;
            String mac_id="";
            ProcessBuilder b;
            b = new ProcessBuilder("netsh","dhcp","server","scope","192.168.1.0","show","reservedip");
            b.redirectErrorStream(true);
            Process pr = b.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String input = null;
            for(i=0;i<=6;i++)
            {
                br.readLine(); 
            }
            i=0;String x1="";
            while(true)
            {
                input=br.readLine();
                if(input==null)
                {
                    break;
                }
                if(input.length()==0)
                    break;
                //System.out.println(input.length()+" "+ input);
                
                j=4;
                while(j<input.length()-1)
                {
                    
                    while(input.charAt(j)!=' ')
                    {   
                        x1=x1+input.charAt(j);
                        j++;
                    }
                    System.out.println("1 "+x1);
                    System.out.println("2 "+ip);
                    if(x1.equals(ip))
                    {
                        
                        while(j<input.length()-1)
                        {
                            if((input.charAt(j)!=' '))
                            {mac_id=mac_id+input.charAt(j);}
                            j++;
                        }
                        
                        System.out.println(mac_id);
                        break;
                    }
                    else
                    {
                        x1="";break;
                    }
                }
            }
            ProcessBuilder builder;
            builder = new ProcessBuilder("netsh", "dhcp","server","scope","192.168.1.0","delete","reservedip",ip,mac_id);
            builder.redirectErrorStream(true);
            Process p;
            p = builder.start();
            BufferedReader bfr = new BufferedReader(new InputStreamReader(p.getInputStream()));//System.out.println("1");
            String line;
            while (true) 
            {
                line = bfr.readLine();//System.out.println("1");
                if (line == null) 
                { 
                    break; //System.out.println("1");}
                }
                System.out.println(line);
            } 
        }
        catch (IOException ec)
        {
            System.out.println("exception");
        }
    }
    
    }

