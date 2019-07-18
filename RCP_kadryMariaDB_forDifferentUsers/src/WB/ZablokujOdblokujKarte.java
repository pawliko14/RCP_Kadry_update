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


public class ZablokujOdblokujKarte extends JFrame {

	private JPanel contentPane;
	private JTextField txtNrBonu;
	private JTextField textField_2;

	/**
	 * Launch the application.
	 */

	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Connection connection = RCPdatabaseConnection.dbConnector("tosia", "1234");
				try {
					ZablokujOdblokujKarte frame = new ZablokujOdblokujKarte(connection);
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
	public ZablokujOdblokujKarte(final Connection connection) {
		
		setBackground(Color.WHITE);
		setTitle("Blokowanie kart");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 467, 360);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblMojeMenu = new JLabel("Zablokuj/odblokuj kart\u0119");
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
		
		textField_2 = new JTextField();
		textField_2.setHorizontalAlignment(SwingConstants.CENTER);
		textField_2.setColumns(10);
		textField_2.grabFocus();
		textField_2.requestFocus();
		
		JButton btnNewButton = new JButton("Zablokuj kart\u0119");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (textField_2.getText().isEmpty()){
					
					JOptionPane.showMessageDialog(null, "Nie wprowadzono nr karty");
					
				}else{
					
					int YesNo = JOptionPane.showConfirmDialog(null, "Czy na pewno chcesz zablokowaæ kartê?", "Blokowanie karty", JOptionPane.YES_NO_OPTION);
					if(YesNo == JOptionPane.YES_OPTION){
						PreparedStatement pst =null;	
						try {	
						String query = "INSERT INTO cards_blocked (id_karty) VALUES ('"+textField_2.getText()+"')";
						pst = connection.prepareStatement(query);
						pst.execute();
						pst.close();
						}catch (SQLException e3) {
							// TODO Auto-generated catch block
							JOptionPane.showMessageDialog(null, "Sprobuj jeszcze raz - baza jest zajeta");
							e3.printStackTrace();
		
						}
						textField_2.setText("");
					}else{
						
					}
				}
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JButton btnOdblokujKart = new JButton("Odblokuj kart\u0119");
		btnOdblokujKart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (textField_2.getText().isEmpty()){
			
					JOptionPane.showMessageDialog(null, "Nie wprowadzono nr karty");
					
				}else{
					PreparedStatement pst =null;	
					try {	
						String query = "DELETE FROM cards_blocked WHERE id_karty ='"+textField_2.getText()+"'";
						pst = connection.prepareStatement(query);
						pst.execute();
						pst.close();
					}catch (SQLException e3) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, "Sprobuj jeszcze raz - baza jest zajeta");
						e3.printStackTrace();
	
					}
					textField_2.setText("");
				}
				
			}
		});
		btnOdblokujKart.setFont(new Font("Tahoma", Font.BOLD, 12));
		
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
							.addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
							.addGap(6)))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(textField_2, GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
						.addComponent(btnOdblokujKart, GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE))
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
						.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnOdblokujKart, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE))
					.addGap(56))
		);
		contentPane.setLayout(gl_contentPane);	
	
		textField_2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
		
			}
			});
		
		
		}

	}
