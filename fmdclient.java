
import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.*;

public class fmdclient  implements Runnable,ListSelectionListener,ActionListener ,KeyListener,WindowListener{
static String nameofclient;
	static JFrame jf,f;
static	JLabel jl;
static	JTextArea jta;
static	JTextField jtf;
	static JScrollPane jsp;
	static LinkedHashSet<String> lhs= new LinkedHashSet<String>();
	static String strmain;

static HashMap <String , JFrame> hmframe = new HashMap<String ,JFrame>();
static HashMap <String , JLabel> hmlabel = new HashMap<String ,JLabel>();
static HashMap <String , JTextArea> hmta = new HashMap<String ,JTextArea>();
static HashMap <String , JTextField> hmtf = new HashMap<String ,JTextField>();
	
  // The client socket
  private static Socket clientSocket = null;
  // The output stream
  static PrintStream os = null;
  // The input stream
  static DataInputStream is = null;

  private static BufferedReader inputLine = null;
  private static boolean closed = false;
  static	JList list;
  static	DefaultListModel listModel;        
  static	JFrame jfmini = new JFrame();
  static JButton jb = new JButton("Send");
  static	JLabel jlmini = new JLabel();
  static	JTextArea jtamini = new JTextArea();
  static	JTextField jtfmini= new JTextField();
  static	JScrollPane jspmini2 = new JScrollPane(jtamini);
  static	JPanel jp = new JPanel();
  static	JPanel jp2= new JPanel();
static int index=0;
static HashMap<String , Integer> hmindex = new HashMap<String,Integer>();
static HashMap< Integer,String > hmindexname = new HashMap<Integer,String >();
static ArrayList<String> als = new ArrayList<String>();
  
  public static void main(String[] args) {

    // The default port.
    int portNumber = 2222;
    // The default host.
    String host = "localhost";
String clientname;

System.out.println("inside client");
listModel = new DefaultListModel();
list = new JList(listModel);
list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//list.addListSelectionListener(new fmdclient());
jfmini.setLayout(new BorderLayout());
jfmini.add(jlmini,BorderLayout.NORTH);
jfmini.add(jp,BorderLayout.CENTER);
jp.setLayout(new GridLayout(1,2));
jtamini.setEditable(false);
jp.add(jspmini2);
jp.add(list);
jp2.setLayout(new GridLayout(1,2));
jtfmini.addKeyListener(new fmdclienthelp());
jp2.add(jtfmini);
jp2.add(jb);
jb.addActionListener(new fmdclient());
jfmini.add(jp2,BorderLayout.SOUTH);
jfmini.setSize(220,250);
jfmini.setVisible(true);
jfmini.addWindowListener(new fmdclient());
System.out.println(host);
InetAddress addr=null;
try {
		 addr=InetAddress.getByName("localhost");
	} catch (UnknownHostException e1) {
		e1.printStackTrace();
	}
    /* * Open a socket on a given host and port. Open input and output streams.
     */
    try {
      clientSocket = new Socket(addr,2299);
      inputLine = new BufferedReader(new InputStreamReader(System.in));
      os = new PrintStream(clientSocket.getOutputStream());
      is = new DataInputStream(clientSocket.getInputStream());
    
      // can use BufferedReader instead of datainputstream reader works on character
    }  catch (IOException e) {
    	e.printStackTrace();
      System.err.println("Couldn't get I/O for the connection to the host "
          + host);
    }
 if (clientSocket != null && os != null && is != null) {
      try {

        /* Create a thread to read from the server. */
        new Thread(new fmdclient()).start();
      } catch (Exception e) {
      	e.printStackTrace(); System.err.println("IOException:  " + e);
      }
    }
  }
  public static void createFrame()
  {
	  f = new JFrame();
	  jf = new JFrame();
	  jta= new JTextArea();
	  jta.setEditable(false);
	  jtf = new JTextField();
	  jtf.addKeyListener(new fmdclient());
	  jl = new JLabel();
	  jl.setText(nameofowner);
	  jsp = new JScrollPane(jta);
	  jf.setLayout(new BorderLayout());
	  jf.add(jl,BorderLayout.NORTH);
	  jf.add(jsp,BorderLayout.CENTER);
	  jf.add(jtf,BorderLayout.SOUTH);
	  jf.setSize(200,230);
	  jf.setVisible(false);
	  jf.addWindowListener(new WindowAdapter()
	  {
	  	public void WindowClosing(WindowEvent we)
	  	{
	  	jf.setVisible(false);	
	  	}
	  });
	  jl.setText(newlogin);
	 als.add(newlogin);  
	  hmframe.put(newlogin, jf);
	  hmta.put(newlogin, jta);
	  hmtf.put(newlogin,jtf);
	  hmlabel.put(newlogin,jl);
	  hmindex.put(newlogin, index);

	  hmindexname.put( index,newlogin);
	  index++;
	}
  public void deupdateList()
  {
	  System.out.println(hmframe.size()+"size of frame "+hmta.size()+"textarea size ");
	System.out.println(offlinename+"name of customer going offline");
	hmframe.get(offlinename).setVisible(false);
  hmframe.remove(offlinename);
  hmta.remove(offlinename);
  hmtf.remove(offlinename);
  hmlabel.remove(offlinename);
 hmindexname.remove(hmindex.get(offlinename));
 hmindex.remove(offlinename);
 listModel.removeAllElements();
 for(int lis= 0;lis<=index;lis++)
 {
	 if(hmindexname.containsKey(lis))
	 {
listModel.addElement(hmindexname.get(lis));
	 }
 }
  // als.r
  /*for(int ii =0;ii<index;ii++)
  {
  	if(als.get(ii).equals(sending))
  	{if(ii==0)
  		list.setSelectedIndex(ii+1);
  	else
  		list.setSelectedIndex(ii-1);
  	}
  }*/
  }
  public  void updateList()
  {
	 // list.removeAll();
	 System.out.println(newlogin);
	  listModel.addElement(newlogin);
	  
	  list.addListSelectionListener(this);
	}
  
static String receiving,sending,message,newlogin,nameofowner,offlinename;
  public void run() {
	  int i=0;
    String response;
  System.out.println("this is starting");
    // getting the name of owner
    	try
  {
	nameofowner = is.readLine();
	System.out.println("name of owner is received"+nameofowner);
    	 	}catch(Exception e)
    {
    	e.printStackTrace();
    }
   
    
    
    try {
      while ((response = is.readLine()) != null) 
  {System.out.println(response.substring(0,5)+"this is substring ::: this is message"+response);
    	  
    	  if(response.substring(0,5).equals("98997"))
    	  {
    		  System.out.println("inside creation");
    	  newlogin = response.substring(response.indexOf("7")+1,response.length());
    		// frame creation for new user
    	  System.out.println(newlogin+"this is the name of new login");
    	createFrame();
  	  	updateList();
    	  }else if(response.substring(response.indexOf("*%!")+3,response.length()).equals("/END"))
    	  {
    		  System.out.println("inside th deupdation");
    		  offlinename =  response.substring(response.indexOf("@#^")+3,response.indexOf("*%!"));
    		 System.out.println("offlinename"+offlinename);
    		  deupdateList();
    		  
    	  }
    	  else if(response.substring(0, response.indexOf("@#^")).equals("forall")){
    		  System.out.println("for group message");
    		  receiving = response.substring(0, response.indexOf("@#^"));// name of owner is null error 
    		  sending= response.substring(response.indexOf("@#^")+3,response.indexOf("*%!"));
    		  message=response.substring(response.indexOf("*%!")+3,response.length());
              jtamini.setText(jtamini.getText()+"\n"+sending+":"+message);
    	  }
    	  
    	  
    	  else{
 receiving = response.substring(0, response.indexOf("@#^"));// name of owner is null error 
sending= response.substring(response.indexOf("@#^")+3,response.indexOf("*%!"));
message=response.substring(response.indexOf("*%!")+3,response.length());
//frame to make appear// in case of reply instantly we have to select that cell in jList
for(int ii =0;ii<=index;ii++)
{list.setSelectedValue(sending,true);			

for(int j=0;j<=index;j++)
{
	if(hmindex.containsKey(j))
	{System.out.println(hmindexname.get(j)+"this is name");
		hmframe.get(hmindexname.get(j)).setVisible(false);
	System.out.println("selectde and false ");
	}
}
}
hmframe.get(sending).setVisible(true); // it will have name of that user in online list 
hmta.get(sending).setText(hmta.get(sending).getText()+"\n"+sending+":"+message);
    	  }

if (response.indexOf("*** Bye") != -1)
          break;
      }
      closed = true;
    } catch (IOException e) {
      System.err.println("IOException:  " + e);
  	e.printStackTrace();    }
  }
public void keyPressed(KeyEvent e) {
if(e.getKeyCode()==e.VK_ENTER)
{System.out.println("name of owner"+nameofowner);
System.out.println("to whom message is being send"+list.getSelectedValue().toString()+"name of owner"+nameofowner+"this is text from text field"+hmtf.get(list.getSelectedValue().toString()).getText());
os.println(list.getSelectedValue().toString()+"@#^"+nameofowner+"*%!"+hmtf.get(list.getSelectedValue().toString()).getText()); // here it is is printing what i will write in cmd
hmta.get(list.getSelectedValue().toString()).setText(hmta.get(list.getSelectedValue().toString()).getText()+"\n"+"me:"+hmtf.get(list.getSelectedValue().toString()).getText());
//String end ="\END";
if(hmtf.get(list.getSelectedValue().toString()).getText().equals("/END"))
{
jtf.setVisible(false);
jtf.disable();
System.exit(1);
}
hmtf.get(list.getSelectedValue().toString()).setText("");

}
}
public void valueChanged(ListSelectionEvent lse)
{
if(lse.getValueIsAdjusting()==false)
{
	System.out.println(list.getSelectedValue()+"this is inside list listener");
for(int i=0;i<als.size();i++)
{
if(hmframe.containsKey(als.get(i)))	
{
	hmframe.get(als.get(i)).setVisible(false);
}else{
	
}
}
System.out.println(list.getSelectedIndex()+"this is index");
if(hmindex.containsKey(list.getSelectedValue()))
{hmframe.get(list.getSelectedValue()).setVisible(true);}
}
}
public void actionPerformed(ActionEvent arg0) {
	
}
@Override
public void keyReleased(KeyEvent arg0) {
	// TODO Auto-generated method stub
	
}
@Override
public void keyTyped(KeyEvent arg0) {
	// TODO Auto-generated method stub
	
}
@Override
public void windowActivated(WindowEvent arg0) {
	// TODO Auto-generated method stub
	
}
@Override
public void windowClosed(WindowEvent arg0) {
	// TODO Auto-generated method stub
  	System.out.println("this is si");
	jf.setVisible(false);	
	
}
@Override
public void windowClosing(WindowEvent arg0) {
	// TODO Auto-generated method stub
	System.out.println("this is si");
	os.println("anybody"+"@#^"+nameofowner+"*%!"+"/END"); // here it is is printing what i will write in cmd
	System.exit(1);
}
@Override
public void windowDeactivated(WindowEvent arg0) {
	// TODO Auto-generated method stub
	
}
@Override
public void windowDeiconified(WindowEvent arg0) {
	// TODO Auto-generated method stub
	
}
@Override
public void windowIconified(WindowEvent arg0) {
	// TODO Auto-generated method stub
	
}
@Override
public void windowOpened(WindowEvent arg0) {
	// TODO Auto-generated method stub
	
}
}