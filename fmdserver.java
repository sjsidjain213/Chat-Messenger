import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/*
 * A chat server that delivers public and private messages.
 */
public class fmdserver {

  private static ServerSocket serverSocket = null;
  private static Socket clientSocket = null;
 private static final int maxClientsCount = 20;
  private static final clientThread[] threads = new clientThread[maxClientsCount];
  private static HashMap <Integer,clientThread> hm = new HashMap<Integer,clientThread>();
  static int count=0;
static LinkedHashSet <String>lhs = new LinkedHashSet<String>();
static HashMap<String,JFrame> hmtrans = new HashMap<String,JFrame>();  
//server ml=  new server();
public static LinkedHashSet<String> nametoclient()
{
System.out.println("nametoclient");
return lhs;  
}

public static void main(String args[]) {
   int portNumber = 2299;

    try {
      serverSocket = new ServerSocket(portNumber);
    } catch (IOException e) {
      System.out.println(e);
    }
    while (true) {
      try {
        clientSocket = serverSocket.accept();
       
        int i = 0;
        for (i = 0; i < maxClientsCount; i++) {
          if (threads[i] == null) {
            threads[i] = new clientThread(clientSocket, hm);
hm.put(count,threads[i]);       
count++;          
threads[i].start();
            break;
          }
        }
        if (i == maxClientsCount) {
          PrintStream os = new PrintStream(clientSocket.getOutputStream());
          os.println("Server too busy. Try later.");
          os.close();
          clientSocket.close();
        }
      } catch (IOException e) {
        System.out.println(e);
      }
    }
  }


}

/*
 * The chat client thread. This client thread opens the input and the output
 * streams for a particular client, ask the client's name, informs all the
 * clients connected to the server about the fact that a new client has joined
 * the chat room, and as long as it receive data, echos that data back to all
 * other clients. When a client leaves the chat room this thread informs also
 * all the clients about that and terminates.
 */
class clientThread extends Thread{

	// for server
  private DataInputStream is = null;
  private PrintStream os = null;
 private Socket clientSocket = null;
 //static private clientThread[] threads;
 static private int maxClientsCount;
 
// for setting correct connection and storing information
 static LinkedHashSet <String > lhs = new LinkedHashSet<String>();
//static ArrayList <String> alname = new ArrayList<String>();
static HashMap<String,Integer> hmindex = new HashMap<String,Integer>();
static HashMap<Integer,String> hmindexname = new HashMap<Integer,String>();
private static HashMap <Integer,clientThread> hmmain = new HashMap<Integer,clientThread>();


//variables
static String name;
static String line;
static String forall;
String str;
static int index=-1;
String sending,receiving,message,response;
JFrame jff;      

public clientThread(Socket clientSocket,HashMap <Integer,clientThread> hm) 
{
    this.clientSocket = clientSocket;
    hmmain = hm;
   // maxClientsCount = threads.length;
  index++;
  System.out.println("this is in constructor"+"this is index"+index+"this is noof threads lemngth"+this.maxClientsCount);
}
public void run() {
   // int maxClientsCount = this.maxClientsCount;
    //clientThread[] threads = this.threads;
    try {
      is = new DataInputStream(clientSocket.getInputStream());// each time new input an doutput stream is created
      os = new PrintStream(clientSocket.getOutputStream());

      
      lhs.add("");
      lhs.add(null);
      while(!lhs.add(str))
	  	{
	  		str=JOptionPane.showInputDialog(jff,"ente", "ss",JOptionPane.INFORMATION_MESSAGE);
	  	
	  	}
    
  	name=str;
    System.out.println("this is the name"+name+lhs.size()+"this is size");
    
	if(!hmindex.containsKey(str))
  {
  hmindex.put(str, index);
  hmindexname.put(index,str);
  System.out.println("this is for index : "+index+" for name : "+str);	
 // alname.add(name);
  }
	System.out.println("name of frame owner"+name);
	os.println(name);  
	System.out.println(name+"name of frame owner");

	//telling all the online client about this new user
    for(int i =0;i<index;i++)
    {System.out.println("inside loop for new name transfer");
    if(hmindex.containsKey(hmindexname.get(i)))
    {	
    if(hmmain.get(i)!=this)
    	{System.out.println("inside thread allotment"+name+":::::::::"+i);
    		hmmain.get(i).os.println("98997"+name);// transfer the name of client to clientclass +// encrupt this message with a  number ex : 97997 
    os.println("98997"+hmindexname.get(i));
    	System.out.println(hmindexname.get(i)+"checking alname");
    	}
    }
    else{
    	System.out.println("blah blah blah");
    }
    }
    

      while (true) {
        response=is.readLine();
// identifying from which client the message had been send
        receiving = response.substring(0, response.indexOf("@#^"));
        sending= response.substring(response.indexOf("@#^")+3,response.indexOf("*%!"));
        message=response.substring(response.indexOf("*%!")+3,response.length());
System.out.println("inside while true"+"name of receiver"+receiving+"this is message"+message);
//checking for group message
forall=response.substring(0,response.indexOf("@#^"));
System.out.println(forall+"this is eligibity for group chat");
 if(response.substring(response.indexOf("*%!")+3,response.length()).equals("/END"))
{	System.out.println("inside for end **********");
int grop = hmindex.get(sending);		
for(int i=0;i<=index;i++)
	{
		if(hmindex.containsKey(hmindexname.get(i)))
	{	if(grop==i)
	{
		System.out.println("blah blah blah ***"+i);
	}
	else{
       hmmain.get(i).os.println(response);
	}
}
else{
	}	
	}

System.out.println("this is entering for removal of client");
	System.out.println(hmmain.size()+"main siz e"+hmindex.size()+"this is index size :: lhs size"+lhs.size()+"this is grp"+grop);

hmindex.remove(sending);
hmindexname.remove(grop);
lhs.remove(sending);
hmmain.remove(grop);
System.out.println(hmmain.size()+"main siz e"+hmindex.size()+"this is index size :: lhs size"+lhs.size());
}
 else if(forall.equals("forall"))
{
	System.out.println("inside for all");
for(int i=0;i<=index;i++)
{  int grop = hmindex.get(sending);	
	if(hmindex.containsKey(hmindexname.get(i)))
	{	if(grop==i)
		{
			System.out.println("blah blah blah ***"+i);
		}
		else{
	       hmmain.get(i).os.println(response);
		}
	}
	else{
		}
	}
}
// this all is for personal chat
//setting up the correct connection

else {   
	int setindex = hmindex.get(receiving);
       System.out.println("this is setindex"+setindex);
      hmmain.get(setindex).os.println(response);
}
//if client is going offline    	

      }
      
    } catch (IOException e) {
//   e.printStackTrace();
    }
  }

}
