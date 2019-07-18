package WB;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

public class PodgladTabel extends JFrame {

	private JPanel contentPane;
	private JFrame frame;
	public static int a;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Connection connection = RCPdatabaseConnection.dbConnector("tosia", "1234");
				try {
					PodgladTabel frame = new PodgladTabel(connection);
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
	public PodgladTabel(final Connection connection) {
		
		Image img = new ImageIcon(this.getClass().getResource("/BackgroundImage.jpg")).getImage();
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Podglad tabel");
		setBounds(100, 100, 408, 314);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblMenu = new JLabel("Podgl\u0105d tabel");
		lblMenu.setHorizontalAlignment(SwingConstants.CENTER);
		lblMenu.setFont(new Font("Century", Font.BOLD, 24));
		
		JButton btnNewButton = new JButton("Tabela aktywnych kart");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PodgladDaneKart okno = new PodgladDaneKart(connection);
				okno.setVisible(true);
			}
		});
		btnNewButton.setToolTipText("Wy\u015Bwietla tabel\u0119 ostatnich 500 przej\u015B\u0107 przez bramk\u0119");
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JButton btnDodajUsunKarte = new JButton("Tabela kart zablokowanych");
		btnDodajUsunKarte.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PodgladTabeliKartZablokowanych okno = new PodgladTabeliKartZablokowanych(connection);
				okno.setVisible(true);
			}
		});
		btnDodajUsunKarte.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JButton btnZablokujodblokujKart = new JButton("Tabela kart go\u015Bci");
		btnZablokujodblokujKart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PodgladTabeliGosci okno = new PodgladTabeliGosci(connection);
				okno.setVisible(true);
			}
		});
		btnZablokujodblokujKart.setFont(new Font("Tahoma", Font.BOLD, 12));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblMenu, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
						.addComponent(btnZablokujodblokujKart, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
						.addComponent(btnDodajUsunKarte, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
						.addComponent(btnNewButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE))
					.addGap(8))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(6)
					.addComponent(lblMenu, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnDodajUsunKarte, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnZablokujodblokujKart, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(63, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(-420, -100, 893, 621);
		lblNewLabel.setIcon(new ImageIcon(img));
		contentPane.add(lblNewLabel);
	}
}
