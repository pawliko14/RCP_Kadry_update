package WB;
import java.awt.EventQueue;
import java.awt.Image;
import WB.MenuAdmin;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.sql.*;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Login {

	JFrame frame;

	/**
	 * Launch the application.
	 */
	private static boolean Admin;
	private static String User;
	private static String Pass;
	
	public static String getPass() {
		return Pass;
	}

	public static void setPass(String pass) {
		Pass = pass;
	}

	public static String getUser() {
		return User;
	}

	public static void setUser(String user) {
		User = user;
	}

	public static boolean getAdmin(){
		return Admin;
	}
	
	public static void setAdmin(boolean admin){
		Admin=admin;
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	Connection connection=null;
	private JTextField userField;
	private JPasswordField passwordField;
	private JLabel lblNewLabel_2;
	
	/**
	 * Create the application.
	 */
	public Login() {
		initialize();
		connection=RCPdatabaseConnection.dbConnector("user0", "1234"); // pierwsze polaczenie z baza jest na user0 (nie ma zadnego konta w programie na tym userze). 
																	   //Sprawdza wtedy co to jest za uzytkownik i ustawia go (zamyka polaczenie usera0, a otwiera na odpowiednim dla uzytkownika - user2 lub user3) do kolejnych polaczen
	}
	

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		frame = new JFrame("Login");
		frame.setResizable(false);
		frame.getContentPane().setBackground(new Color(51, 153, 204));
		frame.setBounds(100, 100, 445, 344);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnLogin = new JButton("");
		
		Image img1 = new ImageIcon(this.getClass().getResource("/BlueArrow.png")).getImage();
		btnLogin.setIcon(new ImageIcon(img1));
		btnLogin.setBounds(146, 177, 128, 76);
		btnLogin.setBorder(BorderFactory.createEmptyBorder());
		btnLogin.setContentAreaFilled(false);
		btnLogin.setFocusable(false);
		
		
		
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NextWindow();
			}
		});
		btnLogin.setBounds(153, 193, 128, 85);
		frame.getContentPane().add(btnLogin);
		
		JLabel lblNewLabel = new JLabel("User:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setBounds(10, 93, 86, 28);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Password:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_1.setBounds(10, 152, 86, 17);
		frame.getContentPane().add(lblNewLabel_1);
		
		userField = new JTextField();
		userField.setToolTipText("Wpisz nazwe uzytkownika");
		userField.setBounds(110, 88, 206, 42);
		frame.getContentPane().add(userField);
		userField.setColumns(10);
		
		userField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_ENTER){
					NextWindow();
				}
			}
		});
		
		passwordField = new JPasswordField();
		passwordField.setToolTipText("Wpisz swoje haslo");
		passwordField.setEchoChar('*');
		passwordField.setBounds(110, 141, 206, 42);
		frame.getContentPane().add(passwordField);
		
		passwordField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_ENTER){
					NextWindow();
				}
			}
		});
		
		
		JLabel lblLogin = new JLabel("Log in");
		lblLogin.setFont(new Font("Century", Font.BOLD, 24));
		lblLogin.setBounds(170, 11, 146, 50);
		frame.getContentPane().add(lblLogin);
		
		lblNewLabel_2 = new JLabel("");
		Image img = new ImageIcon(this.getClass().getResource("/panda.png")).getImage();
		lblNewLabel_2.setIcon(new ImageIcon(img));
		lblNewLabel_2.setBounds(-410, -499, 1489, 1047);
		frame.getContentPane().add(lblNewLabel_2);
		
		
	}
	
	public void NextWindow(){
		try {
			String query="select * from EmployeePasswords where username=? and password=?";
			PreparedStatement pst=connection.prepareStatement(query);
			pst.setString(1, userField.getText());
			pst.setString(2, passwordField.getText());
			
			ResultSet rs=pst.executeQuery();
			
			int count=0;
			while(rs.next())
			{
				count=count+1;
				setAdmin(rs.getBoolean("ADMIN"));
				setUser(rs.getString("DBUser"));
				setPass(rs.getString("DBPass"));
				System.out.println(getUser());
				System.out.println(getPass());
			}
			if(count==1)
			{
				if(getAdmin()){
					rs.close();
					pst.close();
					frame.dispose();
					connection=RCPdatabaseConnection.dbConnector(getUser(), getPass());
					MenuAdmin mojeMenu = new MenuAdmin(connection);
					mojeMenu.setVisible(true);
				}else{
					rs.close();
					pst.close();
					frame.dispose();
					connection=RCPdatabaseConnection.dbConnector(getUser(), getPass());
					MenuUser mojeMenu = new MenuUser(connection);
					mojeMenu.setVisible(true);
				}
			}
				
			else if(count>1)
			{
				JOptionPane.showMessageDialog(null, "Duplicate Username and password");
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Username and password is not correct!");
			}
			
			pst.close();
		}catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, e);
			
		}
		
		
	}
	
}
