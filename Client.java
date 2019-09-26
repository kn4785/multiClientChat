import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.Thread;

/**
 * @author Kevin Liu, Blake Wesel, Kim Ngo, Coleman Link, Austin Trumble
 * 
 * @version Lab 07
 *
 * Class which 1) creates GUI which holds chat and enter messages
 *             2) writes out text in JTextField to server class
 *             3) reads server text and writes it as a JLabel in a JGrid column
 */
 
 // ipconfig
 // 129.21.92.242
 // 129.21.92.197
 
public class Client extends Thread
{
   private Socket s = null;
   private JTextArea jtaChat;
   private JTextField jtfEnter;
   private OutputStream out;
   private InputStream in;
   private PrintWriter pout;
   private BufferedReader bin;
   
   /*
    *
    *Constructor creates GUI but doesn't connect to any server yet
    *
    */
   public Client()
   {
      try
      {
         JFrame jfClient = new JFrame("Let's Chat");
         jfClient.setLayout(new BorderLayout());
         
         JMenuBar jmbClient = new JMenuBar();
         jfClient.setJMenuBar(jmbClient);
         
         JMenu jmMenu = new JMenu("Menu");
         jmbClient.add(jmMenu);
         
         JMenuItem jmiConnect = new JMenuItem("Connect to Server");
         jmMenu.add(jmiConnect);

         jmiConnect.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent ae)
            {
               connectServer();
            }
         });         
         
         jtaChat = new JTextArea("", 20, 20);
         jfClient.add(jtaChat,BorderLayout.CENTER);
         
         JPanel jpEnter = new JPanel();
         jpEnter.setLayout(new GridLayout(1,1));
         jfClient.add(jpEnter,BorderLayout.SOUTH);
         
         jtfEnter = new JTextField("");
         jpEnter.add(jtfEnter);
         
         JButton jbEnter = new JButton("Send Message");
         jpEnter.add(jbEnter);
         
         jbEnter.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent ae)
            {
               sendMessage();
            }
         });
         
         jfClient.pack();
         jfClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         jfClient.setVisible(true); 
         jfClient.setLocationRelativeTo(null);
         
      }
      catch( ArrayIndexOutOfBoundsException aioobe ) 
      {
 	   	System.out.println("ArrayIndexOutOfBoundsException: " + aioobe);
 	   }
      catch( NullPointerException npe ) 
      {
 	   	System.out.println("NullPointerException: " + npe);
 	   }
   }
  
   /*
    *
    * connectServer() connects to server typed into the JTextfield and runs thread
    *
    */ 
   public void connectServer()
   {
      try
      {
         s = new Socket(jtfEnter.getText(), 16789);
         out = s.getOutputStream();
         in = s.getInputStream();
         pout = new PrintWriter(out);
         bin = new BufferedReader(new InputStreamReader(in));
         
         start();
      }
      catch(UnknownHostException uhe) 
      {
	   	System.out.println("no host");
	   	uhe.printStackTrace();
	   }
      catch(IOException ioe)
	   {
	   	System.out.println("IO error");
	   	ioe.printStackTrace();
	   }
   }
   
   /*
    *
    * sends message and flushes PrintWriter
    *
    */
   public void sendMessage()
   {
      pout.println(jtfEnter.getText());
      pout.flush();
   }

   /*
    *
    * starts reading from server
    *
    */
   public void run() 
   {
      try
      { 
         String text;
         while ((text = bin.readLine()) != null)
         {
            jtaChat.append(text + "\n");
            jtaChat.update(jtaChat.getGraphics());
            System.out.println(text);
         }
      }
      // if the server disconnects, then the program 
      catch (SocketException se)
      {
         jtaChat.append("Server Disconnected. \n");
         jtaChat.update(jtaChat.getGraphics());
         System.out.println("Server Disconnected.");
      }
      catch(IOException ioe)
	   {
	      System.out.println("IO error");
	    	ioe.printStackTrace();
	   }
   }
   
   /*
    *
    * main method runs Client
    *
    */
   public static void main(String[] args)
   {
      Client client = new Client();
   }
}