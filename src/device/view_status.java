/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package device;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Administrator
 */
public class view_status 
{
    public void check_status()
    {
        try{
            int i,j;
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
            i=0;String ip="";
            while(true)
            {
                input=br.readLine();
                if(input==null)
                {
                    System.out.println("no reserved ips in the network");
                    break;
                }
                if(input.length()==0)
                    break;
                System.out.println(input.length()+" "+ input);
                
                j=4;
                while(j<input.length()-1)
                {
                    
                    while(input.charAt(j)!=' ')
                    {   
                        ip=ip+input.charAt(j);
                        j++;
                    }
                    System.out.println("ip="+ip); 
                    ProcessBuilder pb;
                    pb=new ProcessBuilder("cmd.exe", "/c", "ping -n 1 "+ip);
                    pb.redirectErrorStream(true);
                    Process p =pb.start();
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String str;
                    br1.readLine();
                    
                    if(br1.readLine()!=null)
                    {
                        str=br1.readLine();
                        System.out.println(str);
                        if(str.contains("unreachable" ) ||str.contains("timed out" ) )
                        {
                            System.out.println("device not working");
                            ip="";
                            break;
                        }
                        else
                        {
                            System.out.println("Device working properly");
                            ip="";
                            break;
                        }
                        
                     }
                    else
                    {
                        break;
                    }
                } 
            }
        }
        catch (IOException ec)
        {
            System.out.println("exception");
        }
}
}
