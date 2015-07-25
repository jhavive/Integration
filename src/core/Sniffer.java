package core;
import java.util.ArrayList; 
import java.util.List;  
import org.jnetpcap.Pcap;  
import org.jnetpcap.PcapIf;  
import org.jnetpcap.packet.PcapPacket;  
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.protocol.network.Ip4;
import java.sql.*;
public class Sniffer 
{
    public static void main(String[] args)throws Exception
        {  
            
            Connection conn=null;
            try
            {
                try 
                {
                    Class.forName("com.mysql.jdbc.Driver");
                    conn=(Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root","");
                }
                catch (ClassNotFoundException ex) 
                {
                    System.out.println("Error in db");
                }        
                List<PcapIf> alldevs = new ArrayList<>(); // Will be filled with NICs  
                StringBuilder errbuf = new StringBuilder(); // For any error msgs
                /*************************************************************************** 
                * First get a list of devices on this system 
                **************************************************************************/  
                int r = Pcap.findAllDevs(alldevs, errbuf);  
                if (r == Pcap.NOT_OK || alldevs.isEmpty()) 
                {  
                    System.err.printf("Can't read list of devices, error is %s", errbuf.toString());  
                    return;  
                }
                System.out.println("Network devices found:");  
                int i = 0;  
                for (PcapIf device : alldevs) 
                {  
                    String description =(device.getDescription() != null) ? device.getDescription(): "No description available";  
                    System.out.printf("#%d: %s [%s]\n", i++, device.getName(), description);  
                }
                PcapIf device=alldevs.get(0); // We know we have atleast 1 device  
                System.out.printf("\nChoosing '%s' on your behalf:\n",(device.getDescription() != null) ? device.getDescription(): device.getName());  
                /*************************************************************************** 
                 * Second we open up the selected device 
                 **************************************************************************/  
                int snaplen=64 * 1024;           // Capture all packets, no trucation  
                int flags=Pcap.MODE_PROMISCUOUS; // capture all packets  
                int timeout=1;           // 10 seconds in millis  
                Pcap pcap=Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);  
                if (pcap == null) 
                {  
                    System.err.printf("Error while opening device for capture: "+ errbuf.toString());  
                    return;  
                }  
                /*************************************************************************** 
                 * Third we create a packet handler which will receive packets from the 
                 * libpcap loop. 
                 **************************************************************************/  
                
                PcapPacketHandler<String> jpacketHandler;
                jpacketHandler = new PcapPacketHandler<String>()
                {
                    public void nextPacket(PcapPacket packet, String user)
                    {
                        Ip4 ip=new Ip4();
                        byte[] dIP,sIP;
                        if (packet.hasHeader(ip))
                        {
                            dIP=packet.getHeader(ip).destination();
                            sIP=packet.getHeader(ip).source();
                            String sourceIP=org.jnetpcap.packet.format.FormatUtils.ip(sIP);
                            String destinationIP=org.jnetpcap.packet.format.FormatUtils.ip(dIP);
                            System.out.println(sourceIP+" "+destinationIP);
                            Connection conn=null;
                            try
                            {  
                                try 
                                {
                                    Class.forName("com.mysql.jdbc.Driver");
                                    conn=(Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root","");
                                } 
                                catch (ClassNotFoundException ex) 
                                {
                                    System.out.println("Error in db");
                                }
                                String query;
                                Statement stmt1=(Statement)conn.createStatement(); 
                                query="INSERT INTO idle ( source , destination ) VALUES (\'"+sourceIP+"\',\'"+destinationIP+"\')";
                                stmt1.executeUpdate(query);
                                query="INSERT INTO log ( source , destination ) VALUES (\'"+sourceIP+"\',\'"+destinationIP+"\')";
                                stmt1.executeUpdate(query);

                                conn.close();
                            }
                            catch(SQLException s)
                            {
                                System.out.println(s.getMessage());
                            }  
                        }
                    }
                };/*************************************************************************** 
                 * Fourth we enter the loop and tell it to capture 100 packets. The loop 
                 * method does a mapping of pcap.datalink() DLT value to JProtocol ID, which 
                 * is needed by JScanner. The scanner scans the packet buffer and decodes 
                 * the headers. The mapping is done automatically, although a variation on 
                 * the loop method exists that allows the programmer to sepecify exactly 
                 * which protocol ID to use as the data link type for this pcap interface. 
                 **************************************************************************/  
                long startTime1 = System.nanoTime();
                createlog ob=new createlog();
                for(i=0;i<10;i++)
                {
                    long startTime2 = System.nanoTime();
                    String query;
                    Statement stmt1=(Statement)conn.createStatement(); 
                    query="truncate table log";
                    stmt1.executeQuery(query);
                    pcap.loop(10, jpacketHandler,"");  
                    long endTime2 = System.nanoTime();
                    System.out.println("Took "+(endTime2 - startTime2) + " ns");
                    ob.check_dest();
                }
                long endTime1 = System.nanoTime();
                System.out.println("Took "+(endTime1 - startTime1) + " ns");
                ob.idle_host();
                /*************************************************************************** 
                * Last thing to do is close the pcap handle 
                **************************************************************************/  
                pcap.close();
                conn.close();
            }
            catch(SQLException s)
            {
                System.out.println(s.getMessage());
            }
        }  
    }  