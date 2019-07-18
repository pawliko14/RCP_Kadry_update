package WB;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.proteanit.sql.DbUtils;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Color;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.JTextArea;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;


public class OznaczOdznaczKarteGoscia extends JFrame {

	private JPanel contentPane;
	private JTextField txtNrBonu;
	private JTextField textField_KartaGoscia;

	/**
	 * Launch the application.
	 */

	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Connection connection = RCPdatabaseConnection.dbConnector("tosia", "1234");
				try {
					OznaczOdznaczKarteGoscia frame = new OznaczOdznaczKarteGoscia(connection);
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
	public OznaczOdznaczKarteGoscia(final Connection connection) {
		
		setBackground(Color.WHITE);
		setTitle("Oznacz karte goscia");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 467, 360);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblMojeMenu = new JLabel("Oznacz/odznacz kart\u0119 go\u015Bcia");
		lblMojeMenu.setHorizontalAlignment(SwingConstants.CENTER);
		lblMojeMenu.setFont(new Font("Century", Font.BOLD, 24));
		
		// co jeœli u¿ytkownik jest adminem
		
		if (Login.getAdmin()==true)
		{
		
		}
		
		txtNrBonu = new JTextField();
		txtNrBonu.setText("NR KARTY");
		txtNrBonu.setHorizontalAlignment(SwingConstants.CENTER);
		txtNrBonu.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtNrBonu.setEditable(false);
		txtNrBonu.setColumns(10);
		
		textField_KartaGoscia = new JTextField();
		textField_KartaGoscia.setHorizontalAlignment(SwingConstants.CENTER);
		textField_KartaGoscia.setColumns(10);
		textField_KartaGoscia.grabFocus();
		textField_KartaGoscia.requestFocus();
		
		JButton btnDodajKarteGoscia = new JButton("Dodaj kart\u0119 go\u015Bcia");
		btnDodajKarteGoscia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (textField_KartaGoscia.getText().isEmpty()){
					JOptionPane.showMessageDialog(null, "Nie wprowadzono nr karty");
				}else{
					Statement st = null;
					ResultSet rs = null;
					try {
						st = connection.createStatement();
						rs = st.executeQuery("SELECT id_karty FROM guests WHERE id_karty='"+textField_KartaGoscia.getText()+"'");
						if(rs.next()){
							rs.close();
							st.close();
							JOptionPane.showMessageDialog(null, "Ta karta jest ju¿ oznaczona jako karta goœcia");
						}else{
							rs.close();
							st.close();
							try{
								String query="INSERT INTO guests (id_karty) VALUES ('"+textField_KartaGoscia.getText()+"')";
								PreparedStatement pst=connection.prepareStatement(query);
								pst.execute();
								pst.close();
							}catch(SQLException e){
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null, "Sprobuj jeszcze raz - baza jest zajeta");
								e.printStackTrace();
							}
							
						}
					}catch(SQLException e3){
						// TODO Auto-generated catch block
						e3.printStackTrace();
					}
				}
			}
		});
		btnDodajKarteGoscia.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JButton btnUsunKarteGoscia = new JButton("Usu\u0144 kart\u0119 go\u015Bcia");
		btnUsunKarteGoscia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (textField_KartaGoscia.getText().isEmpty()){
					JOptionPane.showMessageDialog(null, "Nie wprowadzono nr karty");
				}else{
					int YesNo = JOptionPane.showConfirmDialog(null, "Czy na pewno chcesz usunac karte goscia?", "Usuwanie karty goscia", JOptionPane.YES_NO_OPTION);
					if(YesNo == JOptionPane.YES_OPTION){
						PreparedStatement pst =null;	
						try {	
							String query = "DELETE FROM guests WHERE id_karty ='"+textField_KartaGoscia.getText()+"'";
							pst = connection.prepareStatement(query);
							pst.execute();
							pst.close();
							textField_KartaGoscia.setText("");
						}catch (SQLException e3) {
							// TODO Auto-generated catch block
							JOptionPane.showMessageDialog(null, "Sprobuj jeszcze raz - baza jest zajeta");
							e3.printStackTrace();
		
						}
					}else{
						//nic
					}
				}
			}
		});
		btnUsunKarteGoscia.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(19)
					.addComponent(lblMojeMenu, GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(txtNrBonu, GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(btnDodajKarteGoscia, GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
							.addGap(6)))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(textField_KartaGoscia, GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
						.addComponent(btnUsunKarteGoscia, GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(40)
					.addComponent(lblMojeMenu, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
					.addGap(36)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(txtNrBonu, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(textField_KartaGoscia, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnDodajKarteGoscia, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnUsunKarteGoscia, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE))
					.addGap(56))
		);
		contentPane.setLayout(gl_contentPane);	
		
		}

	}
