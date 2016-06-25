package server;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.JOptionPane;


public class AdminIndex  extends JFrame{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private JButton  button1;
	private JTextField txtLeft;
	private JButton button2;
	private JTextField txtLeft1;
	private String string="";
	private JButton tbutton1;
	private JButton tbutton2;
	private JButton tbutton3;
	
	public String UserAccess() throws Exception{
		Class.forName("com.mysql.jdbc.Driver");
    	Connection con = DriverManager.getConnection("jdbc:mysql://195.134.65.254:3306/sdi0600255","sdi0600255","75701523");
    	PreparedStatement statement = con.prepareStatement("SELECT setcounter, getcounter FROM user");
		ResultSet result = statement.executeQuery();
		int sum_set = 0;
		int sum_get = 0;
		while(result.next()){
			sum_set+=result.getInt(1);
			sum_get+=result.getInt(2);
		}
		if(sum_set!=0 && sum_get!=0){
			return("Users have set data with frequency: " +sum_set+ "and have requested map data with frequency: "+sum_get);
		}
		else return("Users haven't accessed yet.");		
	}
	
	
	
	public String AreaInfo(String position) throws Exception{
		int R=2;
		String[] part = position.split(",");
		String positionX=part[0];
		String positionY=part[1];
		Class.forName("com.mysql.jdbc.Driver");
    	Connection con = DriverManager.getConnection("jdbc:mysql://195.134.65.254:3306/sdi0600255","sdi0600255","75701523");
    	PreparedStatement statement3 = con.prepareStatement("SELECT COUNT(id) FROM PoiID");
		ResultSet result2 = statement3.executeQuery();	
		int i=0;
		while(result2.next()){
			i=result2.getInt(1);
		}
		if(i!=0){
			String[] positionsX = new String[i];
			String[] positionsY = new String[i];
			PreparedStatement statement4 = con.prepareStatement("SELECT PositionX, PositionY FROM PoiID");
			ResultSet result4 = statement4.executeQuery();
			int j=0;
			while(result4.next()){
				positionsX[j] = result4.getString(1);
				positionsY[j] = result4.getString(2);					
				j++;
			}
			
			
			int[] x2 = new int[i];
			int[] y2 = new int[i];
			for(int k=0;k<i;k++){
				x2[k] = Integer.parseInt(positionsX[k]);
    			y2[k] = Integer.parseInt(positionsY[k]);
			}
			int x1 = Integer.parseInt(positionX);
			int y1 = Integer.parseInt(positionY);

			int counter=0;
			for(int k=0;k<i;k++){
				if(Math.sqrt(Math.pow((x2[k]-x1), 2) + Math.pow((y2[k]-y1), 2)) <= R){
					counter++;
				}
			}
					
			String[] newX = new String[counter];
			String[] newY = new String[counter]; 
			int l=0;
			for(int k=0;k<i;k++){
				if(Math.sqrt(Math.pow((x2[k]-x1), 2) + Math.pow((y2[k]-y1), 2)) <= R){
					newX[l] = "" + x2[k];
					newY[l] = "" + y2[k];
					l++;
				}
				
			}
			//newX[k] ola ta X ths vashs mesa se aktina R
			//newY[k] ola ta Y ths vashs mesa se aktina R
			int poicounter=0;
			int getcounter=0;
			for(int k=0;k<counter;k++){
				PreparedStatement newstat = con.prepareStatement("SELECT POI_counter FROM PoiID WHERE PositionX=('"+newX[k]+"') && PositionY=('"+newY[k]+"') ");
				ResultSet newres = newstat.executeQuery();
				
				while(newres.next()){
					poicounter+=newres.getInt(1);
				}
				PreparedStatement stat2 = con.prepareStatement("SELECT getcounter FROM user WHERE username=(SELECT username FROM PoiID WHERE PositionX=('"+newX[k]+"') && PositionY=('"+newY[k]+"'))");
				ResultSet res2 = stat2.executeQuery();
				
				while(res2.next()){
					getcounter+=res2.getInt(1);
				}
				
			}
			
			if(poicounter==0 && getcounter==0) return("Noone has declared a POI in this area.");
			else if(poicounter!=0 && getcounter==0) 
				return("POI(s) in this area that has/have been declared: "+poicounter+". No getMapData used in this area.");
			else if(poicounter!=0 && getcounter!=0){
				return("POI(s) in this area that has/have been declared: "+poicounter+". No of getMapData(s) used: "+getcounter);
			}
    	
		}
		return("No POIs declared.");
		
	}
	
	
	
	public String UserSet() throws Exception{
		
		Class.forName("com.mysql.jdbc.Driver");
    	Connection con = DriverManager.getConnection("jdbc:mysql://195.134.65.254:3306/sdi0600255","sdi0600255","75701523");
    	PreparedStatement statement = con.prepareStatement("SELECT setcounter FROM user");
		ResultSet result = statement.executeQuery();
		int sum_set = 0;
		while(result.next()){
			sum_set+=result.getInt(1);
		}
		if(sum_set!=0)
			return("Users have set data with frequency: " +sum_set);
		
		else 
			return("Users haven't set data yet.");		
		
		
	}
public String DeleteUser(String username_insert) throws Exception{
		
		String username=username_insert;
		Class.forName("com.mysql.jdbc.Driver");
    	Connection con = DriverManager.getConnection("jdbc:mysql://195.134.65.254:3306/sdi0600255","sdi0600255","75701523");
    	Statement statement = con.createStatement(); 
    	String sql= "SELECT username FROM user WHERE username=('"+username+"')";
        ResultSet rs=statement.executeQuery(sql); 
        
        if(rs.next()){
        	String checkuser=rs.getString(1);
        	if(checkuser.equals(username)){
        		String sql2 = "DELETE FROM user WHERE username = '"+username+"' ";
        		statement.executeUpdate(sql2);
        		return("User Deleted!");
        	}       	
        }
        else return("Username does not exist in database.");

		
		return("User could not be deleted.");
	}

public String UserInfo(String username_insert) throws Exception{
		
		String username=username_insert;
		int getcounter=0;
		int setcounter=0;
		String password="NULL";
		Class.forName("com.mysql.jdbc.Driver");
    	Connection con = DriverManager.getConnection("jdbc:mysql://195.134.65.254:3306/sdi0600255","sdi0600255","75701523");
    	
    	PreparedStatement statement0 = con.prepareStatement("SELECT username FROM user WHERE username=('"+username+"')");
        ResultSet rs=statement0.executeQuery();       
        if(rs.next()){
        	String checkuser=rs.getString(1);
        	if(checkuser.equals(username)){
        		PreparedStatement statement = con.prepareStatement("SELECT password,setcounter,getcounter FROM user WHERE username=('"+username+"')"); 
        		ResultSet result = statement.executeQuery();
        		while(result.next()){
        			password=result.getString(1);
        			setcounter=result.getInt(2);
        			getcounter=result.getInt(3);
        		}		
        		PreparedStatement statement5 = con.prepareStatement("SELECT setcounter, getcounter FROM user");
        		ResultSet result0 = statement5.executeQuery();
        		int sum_set = 0;
        		int sum_get = 0;
        		while(result0.next()){
        			sum_set+=result0.getInt(1);
        			sum_get+=result0.getInt(2);
        		}
        		
        		double set_per = 0;
        			if(sum_set!=0) set_per = setcounter/sum_set * 100;
        		double get_per = 0;
        			if(sum_get!=0) get_per = getcounter/sum_get * 100;
        		PreparedStatement statement2 = con.prepareStatement("SELECT name, type, PositionX, PositionY FROM PoiID WHERE username=('"+username+"')"); 
        		ResultSet result2 = statement2.executeQuery();
        		StringWriter sw = new StringWriter();
        		while(result2.next()){      			
        			int length=0;
					String x="name=" +result2.getString(1) + " and type:"+ result2.getString(2) + " at position X=" +result2.getString(3)+" and Y=" +result2.getString(3)+"\n";	
					length=x.length();
					sw.write(x, 0, length);
        		}
        		
        		return("Username: "+username+" password: " +password+ " setMonitorData percentage: " + set_per + "% getMapData percentage: " +get_per +"%  \n POI(s): " +sw.toString());
        	}
        }
        else return("Username you entered does not exist");
		
		return("Something went wrong");
}
	
	public AdminIndex() {
		
		//super("Admin Index");
		//setLayout(new FlowLayout());
		
        createGui();
        
        thehandler handler = new thehandler();
        thedeletehandler deletehandler = new thedeletehandler();
        theusersethandler usersethandler = new theusersethandler();
        theuseraccesshandler  useraccesshandler = new theuseraccesshandler();
        theareahandler areahandler = new theareahandler();
        button1.addActionListener(handler);
        button2.addActionListener(deletehandler);
        tbutton1.addActionListener(useraccesshandler);
        tbutton2.addActionListener(usersethandler);
        tbutton3.addActionListener(areahandler);
        
    }

    private void createGui() {
    	JPanel panel1 = new JPanel(new GridBagLayout());
    	JPanel panel2 = new JPanel(new GridBagLayout()); 
    	
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Statistics", panel1);
        tabbedPane.addTab("User Options",panel2);
        add(tabbedPane);
        
        addButton1(panel1);
        addButton2(panel2);	

    }

    private void addButton1(JPanel panel) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.gridx = 0;
        c.insets = new Insets(1, 5, 1, 10);
        c.weightx=0.3; 
        tbutton1 = new JButton("User Access Frequency");
        panel.add(tbutton1,c);
        
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.3;
        tbutton2 = new JButton("User Data Insert Frequency");
        panel.add(tbutton2,c);
       
        c.gridx = 2;
        c.weightx = 0.3;
        tbutton3 = new JButton("Area Statistics");
        panel.add(tbutton3,c);
        
        
    }
    
    
    private void addButton2(JPanel panel) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.gridx = 0;
        c.insets = new Insets(1, 5, 1, 10);
        c.weightx=0.3; 
        button1 = new JButton("Show User Info");
        panel.add(button1,c);
        
        c.gridx = 1;
        c.fill = GridBagConstraints.VERTICAL;
        c.weightx = 0.1;
        button2 = new JButton("Delete User");
        panel.add(button2,c);
       
        
    }
    
    
    private class thehandler implements ActionListener{

		public void actionPerformed(ActionEvent event){
			
			if(event.getSource() == button1){
				JFrame frame = new JFrame("User Info");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				JPanel panOuter = new JPanel(new BorderLayout());
				JPanel panLeft = new JPanel(new BorderLayout());
				panLeft.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				
				JPanel panBottom = new JPanel(); // default is FlowLayout
				panBottom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

				JPanel panInput = new JPanel(new BorderLayout());
				panInput.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				JPanel panConsole = new JPanel(new BorderLayout());

				panInput.add(panLeft, BorderLayout.WEST);
				panInput.add(panBottom, BorderLayout.SOUTH);

				panOuter.add(panInput, BorderLayout.NORTH);
				panOuter.add(panConsole, BorderLayout.CENTER);

				JLabel lblLeft = new JLabel("Enter Username and Press Enter", JLabel.CENTER);

				txtLeft = new JTextField(10);


				JTextArea txtConsole = new JTextArea(5, 10);

				panLeft.add(lblLeft, BorderLayout.NORTH);
				panLeft.add(txtLeft, BorderLayout.CENTER);



				panConsole.add(txtConsole, BorderLayout.CENTER);

				frame.setContentPane(panOuter);
				frame.pack();
				frame.setVisible(true); 			
			
				thehandler2 handler2 = new thehandler2();
		        txtLeft.addActionListener(handler2);
			}
			
			
			
		}
    	
		
    	
    }
    
    private class thehandler2 implements ActionListener{

		public void actionPerformed(ActionEvent event){
			
			String result="";
			if(event.getSource() == txtLeft){
				string=String.format("%s", event.getActionCommand());
			
				try {
					result=UserInfo(string);
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
			
			//System.out.println("result: " +result);
			JOptionPane.showMessageDialog(null,result);
		}
    
    }
    
    
    private class thedeletehandler implements ActionListener{

		public void actionPerformed(ActionEvent event){
			
			if(event.getSource() == button2){
				JFrame frame = new JFrame("Delete User");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				JPanel panOuter = new JPanel(new BorderLayout());
				JPanel panLeft = new JPanel(new BorderLayout());
				panLeft.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				
				JPanel panBottom = new JPanel(); // default is FlowLayout
				panBottom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

				JPanel panInput = new JPanel(new BorderLayout());
				panInput.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				JPanel panConsole = new JPanel(new BorderLayout());

				panInput.add(panLeft, BorderLayout.WEST);
				panInput.add(panBottom, BorderLayout.SOUTH);

				panOuter.add(panInput, BorderLayout.NORTH);
				panOuter.add(panConsole, BorderLayout.CENTER);

				JLabel lblLeft = new JLabel("Enter Username and Press Enter", JLabel.CENTER);

				txtLeft1 = new JTextField(10);


				JTextArea txtConsole = new JTextArea(5, 10);

				panLeft.add(lblLeft, BorderLayout.NORTH);
				panLeft.add(txtLeft1, BorderLayout.CENTER);



				panConsole.add(txtConsole, BorderLayout.CENTER);

				frame.setContentPane(panOuter);
				frame.pack();
				frame.setVisible(true); 			
			
				deletehandler2 dhandler2 = new deletehandler2();
		        txtLeft1.addActionListener(dhandler2);
			}
			
			
			
		}
    	
		
		
		private class deletehandler2 implements ActionListener{

			public void actionPerformed(ActionEvent event){
				
				String result="";
				if(event.getSource() == txtLeft1){
					string=String.format("%s", event.getActionCommand());
				
					try {
						result=DeleteUser(string);
					} catch (Exception e) {
						
						e.printStackTrace();
					} 
				}
				
				//System.out.println("result: " +result);
				JOptionPane.showMessageDialog(null,result);
			}
	    
	    }
    	
    }
    
    private class theusersethandler implements ActionListener{

		public void actionPerformed(ActionEvent event){
			String result="";
			if(event.getSource() == tbutton2){
				
				try {
					result=UserSet();
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				
			}
			JOptionPane.showMessageDialog(null,result);
				
		}
    }
    
    
    private class theuseraccesshandler implements ActionListener{

		public void actionPerformed(ActionEvent event){
			String result="";
			if(event.getSource() == tbutton1){
				
				try {
					result=UserAccess();
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				
			}
			JOptionPane.showMessageDialog(null,result);
				
		}
    }
    
    
    private class theareahandler implements ActionListener{

		public void actionPerformed(ActionEvent event){
			
			if(event.getSource() == tbutton3){
				JFrame frame = new JFrame("Area Information");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				JPanel panOuter = new JPanel(new BorderLayout());
				JPanel panLeft = new JPanel(new BorderLayout());
				panLeft.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				
				JPanel panBottom = new JPanel(); // default is FlowLayout
				panBottom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

				JPanel panInput = new JPanel(new BorderLayout());
				panInput.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				JPanel panConsole = new JPanel(new BorderLayout());

				panInput.add(panLeft, BorderLayout.WEST);
				panInput.add(panBottom, BorderLayout.SOUTH);

				panOuter.add(panInput, BorderLayout.NORTH);
				panOuter.add(panConsole, BorderLayout.CENTER);

				JLabel lblLeft = new JLabel("Enter x,y and press enter", JLabel.CENTER);

				txtLeft1 = new JTextField(10);


				JTextArea txtConsole = new JTextArea(5, 10);

				panLeft.add(lblLeft, BorderLayout.NORTH);
				panLeft.add(txtLeft1, BorderLayout.CENTER);



				panConsole.add(txtConsole, BorderLayout.CENTER);

				frame.setContentPane(panOuter);
				frame.pack();
				frame.setVisible(true); 			
			
				theareahandler2 ahandler = new theareahandler2();
		        txtLeft1.addActionListener(ahandler);
			}
			
			
			
		}
    	
		
		
		private class theareahandler2 implements ActionListener{

			public void actionPerformed(ActionEvent event){
				
				String result="";
				if(event.getSource() == txtLeft1){
					string=String.format("%s", event.getActionCommand());

					
					try {
						result=AreaInfo(string);
					} catch (Exception e) {
						
						e.printStackTrace();
					} 
				}
				//System.out.println("result: " +result);
				JOptionPane.showMessageDialog(null,result);
			}
	    
	    }
    	
    }
    
}
    
  