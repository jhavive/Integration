package core;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.*;
public class createlog 
{
   void check_dest()throws Exception 
   {
        Connection conn=null;
        String[][] table=new String[2][100];
        int count=0,i=0,count1=0,v=0;
        String ip="",mac="",query="";
        try
        {
            try 
            {
                Class.forName("com.mysql.jdbc.Driver");
                conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root","");
            } 
            catch (ClassNotFoundException ex) 
            {
            }
            query="select * from log";
            Statement stmt1 = (Statement)conn.createStatement();
            ResultSet c1 = stmt1.executeQuery(query); 
            while(c1.next())
            {
                table[0][i]=c1.getString("source");
                table[1][i]=c1.getString("Destination");
                i++;
            }
            for(i=0;(i<table[0].length)&&(table[0][i]!=null);i++)
            {
                query="select count(*) from blockedIP where ip=\'"+table[1][i]+"\'";
                stmt1 = (Statement)conn.createStatement();
                c1 = stmt1.executeQuery(query); 
                while(c1.next())
                {
                    count=c1.getInt(1);
                }
                if(count>0)
                {
                    System.out.println("looking for mac_id");
                    String mac_id="";
                    ProcessBuilder builder;
                    builder = new ProcessBuilder("netsh", "dhcp","server","scope","192.168.1.0","show","clients");
                    builder.redirectErrorStream(true);
                    Process p = builder.start();
                    BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));System.out.println("1");
                    String line;
                    for(int i1=0;i1<=7;i1++)
                    {
                        r.readLine(); 
                    }
                    while (true) 
                    {
                        line = r.readLine();
                        if (line.length()==0) { System.out.println("1");break; }
                        System.out.println("line="+line.length()+" "+line);
                        int c=0;
                        int j=0;
                        while(j<line.length()-1)
                        {
                            while(line.charAt(j)!=' ')
                            {
                                ip=ip+line.charAt(j);
                                j++;
                            }
                            System.out.println(" "+ip);
                            if(ip.equals(table[0][i]))
                            {
                                while(line.charAt(j)==' ' || line.charAt(j)=='-')
                                {
                                    j++;
                                }
                                while(line.charAt(j)!=' ')
                                {
                                    j++;
                                }
                                while(line.charAt(j)==' ')
                                {
                                    j++;
                                }
                                while(line.charAt(j)==' ' || line.charAt(j)=='-')
                                {
                                    j++;
                                }
                                while(line.charAt(j)!=' ')
                                {   
                                    mac=mac+line.charAt(j);
                                    j++;
                                }
                                System.out.println(" "+mac);
                                v=1;
                                break;
                            }
                            else
                            {
                                ip="";
                                break;
                            }
                        }
                        if(v==1)
                            break;
                    }
                    query="select count(*) from ip_mactable where mac_id=\'"+mac+"\'";
                    ResultSet rs1=stmt1.executeQuery(query);
                    int count2=0;
                    while(rs1.next())
                    {
                        count2=rs1.getInt(1);
                    }
                    if(count2==0)
                    {
                       query="INSERT INTO ip_mactable ( mac_id , ip , count , flag ) VALUES (\'"+mac+"\',\'"+ip+"\',0,0)";
                       stmt1.executeUpdate(query);
                       System.out.println("inserted");
                    }
                    else
                    {
                        query="update ip_mactable set ip=\'"+ip+"\'"+"where mac_id=\'"+mac+"\'";
                        stmt1.executeUpdate(query);
                        System.out.println("ip mac table updated");
                    }
                    query="select count from ip_mactable where mac_id=\'"+mac+"\'";
                    ResultSet rs2=stmt1.executeQuery(query);
                    while(rs2.next())
                    {
                        count1=rs2.getInt(1);
                        System.out.println("count from ip_mactable="+count1);
                    }
                    count1=count1+1;
                    query="update ip_mactable set count=\'"+count1+"\'"+"where mac_id=\'"+mac+"\'";   
                    stmt1.executeUpdate(query);
                    System.out.println("ip mac count table updated"); 
                    /*if(count1==3)
                    {
                      query="update ip_mactable set flag =1 where mac_id=\'"+mac+"\'";  
                      stmt1.executeUpdate(query); 
                      System.out.println("executed");
                      conn.close();ProcessBuilder b;
                      b = new ProcessBuilder("netsh","dhcp","server","scope","192.168.1.0","delete","lease"+table[0][i]);
                      b.redirectErrorStream(true);
                      Process pr = b.start();
                      BufferedReader br = new BufferedReader(new InputStreamReader(pr.getInputStream()));
                      String input = null;
                      while(true)
                        {
                            input=br.readLine();
                            if(input==null)
                            {
                                break;
                            }
                            System.out.println(input.length()+" "+ input);
                        }
                    }*/
                }
            }
        }
        catch(SQLException s)
        {
           System.out.println("exception abc"); 
        }
    }
   void idle_host()throws Exception
   {
       int i,j=0;
       ArrayList<String> l=new ArrayList();
       ArrayList<String> idle=new ArrayList();
       try
       {
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
                        l.add(x1);
                        x1="";
                        if(input.charAt(j)==' ' &&(j<input.length()-1))
                        {
                            break;
                        }
                    }
                }
               for(i=0;i<=l.size()-1;i++)
               {
                    String s= l.get(i);
                    Connection conn=null;
                        try
                        {  
                            try 
                            {
                                Class.forName("com.mysql.jdbc.Driver");
                                conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/nms", "root","");
                            } 
                            catch (ClassNotFoundException ex) 
                            {
                            }
                            String query;
                            //query="truncate table idle_host";
                            Statement stmt1 = (Statement)conn.createStatement(); 
                            //stmt1.executeUpdate(query);
                            //conn.close();
                            query="select count(*) as a from idle where source='"+l.get(i)+"' and destination <> '___.___.___.255' or destination <> '224.___.___.___'";
                            //stmt1 = (Statement)conn.createStatement(); 
                            ResultSet rs1=stmt1.executeQuery(query);
                            while(rs1.next())
                                if(rs1.getInt(1)<0)
                                {
                                     idle.add(s);
                                     /*try
                                     {
                                        ProcessBuilder b1;
                                        b1 = new ProcessBuilder("netsh","dhcp","server","scope","194.168.1.0","remove","lease",idle.get(i));
                                        b.redirectErrorStream(true);
                                        Process pr1 = b1.start();
                                        BufferedReader br1 = new BufferedReader(new InputStreamReader(pr1.getInputStream()));
                                        br1.readLine(); 
                                    }
                                     catch (IOException ex1) 
                                     {}*/
                                    
                            System.out.println(idle);
                            conn.close();
                        }
                        }
                        catch(SQLException s123)
                        {
                            System.out.println("123");
                        }
                
                    }
            }
            catch (IOException ex) 
            {
                
            }
   }
}