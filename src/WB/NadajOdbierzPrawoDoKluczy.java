package WB;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;


public class NadajOdbierzPrawoDoKluczy extends JFrame {

	private JPanel contentPane;
	private JTextField txtNrBonu;
	private JTextField textField_2;
	private JTextField txtPrawoDoKluczy;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */

	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Connection connection = RCPdatabaseConnection.dbConnector("tosia", "1234");
				try {
					NadajOdbierzPrawoDoKluczy frame = new NadajOdbierzPrawoDoKluczy(connection);
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	/**
	 * Create the frame.
	 */
	public NadajOdbierzPrawoDoKluczy(final Connection connection) {
		
		setBackground(Color.WHITE);
		setTitle("Nadanie/odberanie uprawnien do kluczy");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 467, 360);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblMojeMenu = new JLabel("Nadaj/odbierz prawo do kluczy");
		lblMojeMenu.setHorizontalAlignment(SwingConstants.CENTER);
		lblMojeMenu.setFont(new Font("Century", Font.BOLD, 24));
		
		// co jeœli u¿ytkownik jest adminem
		
		if (Login.getAdmin()==true)
		{
		
		}
		
		textField_2 = new JTextField();
		textField_2.setHorizontalAlignment(SwingConstants.CENTER);
		textField_2.setColumns(10);
		textField_2.grabFocus();
		textField_2.requestFocus();
		
		JButton btnNewButton = new JButton("Modyfikuj uprawnienia");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Statement st3 = null;
				ResultSet rs3 = null;
				try {

				st3 = connection.createStatement();
				rs3 = st3.executeQuery("SELECT id_karty FROM cards_name_surname_nrHacoSoft WHERE id_karty='"+textField_2.getText()+"'");	
				
				if(rs3.next()){
					
					Statement st = null;
					ResultSet rs = null;
					try {
	
						st = connection.createStatement();
						rs = st.executeQuery("SELECT id_karty, nr_kluczy FROM keys_permissions WHERE id_karty='"+textField_2.getText()+"'");
						
						if(rs.next()){
							rs.close();
							st.close();
							
							try{
								
								String query="UPDATE keys_permissions SET nr_kluczy='"+textField_1.getText()+"' WHERE id_karty='"+textField_2.getText()+"'";
								PreparedStatement pst=connection.prepareStatement(query);
								pst.execute();
								pst.close();
								
							}catch(SQLException e3){
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null, "Sprobuj jeszcze raz - baza jest zajeta");
								e3.printStackTrace();
							}
							
						}else{
							rs.close();
							st.close();
							
							try{
								
								String query="INSERT INTO keys_permissions (id_karty, nr_kluczy) VALUES ('"+textField_2.getText()+"', '"+textField_1.getText()+"')";
								PreparedStatement pst=connection.prepareStatement(query);
								pst.execute();
								pst.close();
								
							}catch(SQLException e3){
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null, "Sprobuj jeszcze raz - baza jest zajeta");
								e3.printStackTrace();
							}
						}
					
					} catch (SQLException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				}else{
					rs3.close();
					st3.close();
					JOptionPane.showMessageDialog(null, "Nie ma takiego numeru karty");
				}
			}catch(SQLException e4){
				// TODO Auto-generated catch block
				e4.printStackTrace();
			}
				
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		txtPrawoDoKluczy = new JTextField();
		txtPrawoDoKluczy.setText("PRAWO DO KLUCZY");
		txtPrawoDoKluczy.setHorizontalAlignment(SwingConstants.CENTER);
		txtPrawoDoKluczy.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtPrawoDoKluczy.setEditable(false);
		txtPrawoDoKluczy.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setHorizontalAlignment(SwingConstants.CENTER);
		textField_1.setColumns(10);
		
		JButton btnSprawdzUprawnienia = new JButton("Sprawdz uprawnienia");
		btnSprawdzUprawnienia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Statement st = null;
				ResultSet rs = null;
				try {

				st = connection.createStatement();
				rs = st.executeQuery("SELECT id_karty FROM cards_name_surname_nrHacoSoft WHERE id_karty='"+textField_2.getText()+"'");	
				
					if(rs.next())
					{
						rs.close();
						st.close();
						
						Statement st2 = null;
						ResultSet rs2 = null;
						try {

						st2 = connection.createStatement();
						rs2 = st2.executeQuery("SELECT nr_kluczy FROM keys_permissions WHERE id_karty='"+textField_2.getText()+"'");	
						
							if(rs2.next())
							{
								String uprawnienia = rs2.getString("nr_kluczy");
								textField_1.setText(uprawnienia);
							}else{
								textField_1.setText("");
							}
							
							rs2.close();
							st2.close();
						
						} catch (SQLException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();

						}
						
					}else{
						rs.close();
						st.close();
						JOptionPane.showMessageDialog(null, "Nie ma takiego numeru karty");
					}
				
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();

				}
			}
		});
		btnSprawdzUprawnienia.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		txtNrBonu = new JTextField();
		txtNrBonu.setText("NR KARTY");
		txtNrBonu.setHorizontalAlignment(SwingConstants.CENTER);
		txtNrBonu.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtNrBonu.setEditable(false);
		txtNrBonu.setColumns(10);
		
		textField_2.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_ENTER){
					Statement st = null;
					ResultSet rs = null;
					try {

					st = connection.createStatement();
					rs = st.executeQuery("SELECT id_karty FROM cards_name_surname_nrHacoSoft WHERE id_karty='"+textField_2.getText()+"'");	
					
						if(rs.next())
						{
							rs.close();
							st.close();
							
							Statement st2 = null;
							ResultSet rs2 = null;
							try {

							st2 = connection.createStatement();
							rs2 = st2.executeQuery("SELECT nr_kluczy FROM keys_permissions WHERE id_karty='"+textField_2.getText()+"'");	
							
								if(rs2.next())
								{
									String uprawnienia = rs2.getString("nr_kluczy");
									textField_1.setText(uprawnienia);
								}else{
									textField_1.setText("");
								}
								
								rs2.close();
								st2.close();
							
							} catch (SQLException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();

							}
							
						}else{
							rs.close();
							st.close();
							JOptionPane.showMessageDialog(null, "Nie ma takiego numeru karty");
						}
					
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();

					}
				}
			}
		});
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(19)
					.addComponent(lblMojeMenu, GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(txtNrBonu, GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
					.addGap(0)
					.addComponent(textField_2, GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(txtPrawoDoKluczy, GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
					.addComponent(textField_1, GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(113)
					.addComponent(btnSprawdzUprawnienia, GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
					.addGap(111))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(117)
					.addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
					.addGap(107))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(40)
					.addComponent(lblMojeMenu, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
					.addGap(36)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(txtNrBonu, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnSprawdzUprawnienia, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(txtPrawoDoKluczy, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(28, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);	
	
		textField_2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
		
			}
			});
		
		
		}
	}
