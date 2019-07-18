package WB;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JDateChooser;

import PDF.Parameters;
import net.proteanit.sql.DbUtils;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Color;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Vector;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerDateModel;


import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;



public class TabelaObecnosci extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JDateChooser dateChooserDO;
	private JDateChooser dateChooserOD;
	private ArrayList<String> KartyGosciDoPodmiany = new ArrayList<String>();
	private JLabel lblZnajdzPoNr;
	private JTextField textField;

	/**
	 * Launch the application.
	 */

	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Connection connection = RCPdatabaseConnection.dbConnector("tosia", "1234");
				try {
					TabelaObecnosci frame = new TabelaObecnosci(connection);
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
	public TabelaObecnosci(final Connection connection) {
		
		setBackground(Color.WHITE);
		setTitle("Tabela obecno\u015Bci");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 467, 602);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblMojeMenu = new JLabel("Tabela obecno\u015Bci");
		lblMojeMenu.setHorizontalAlignment(SwingConstants.CENTER);
		lblMojeMenu.setFont(new Font("Century", Font.BOLD, 24));
		
		// co jeœli u¿ytkownik jest adminem
		
		if (Login.getAdmin()==true)
		{
		
		}
		
		dateChooserOD = new JDateChooser();
		dateChooserOD.setDateFormatString("yyyy-MM-dd HH:mm");
		dateChooserDO = new JDateChooser();
		dateChooserDO.setDateFormatString("yyyy-MM-dd HH:mm");
		
		JLabel lblNewLabel = new JLabel((String) null);
		
		JLabel lblNewLabel_1 = new JLabel("OD");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel lblDo = new JLabel("DO");
		lblDo.setHorizontalAlignment(SwingConstants.CENTER);
		lblDo.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JLabel lblTabelaHistoriiEksportw = new JLabel("Podgl\u0105d tabeli obecno\u015Bci");
		lblTabelaHistoriiEksportw.setHorizontalAlignment(SwingConstants.CENTER);
		lblTabelaHistoriiEksportw.setFont(new Font("Century", Font.BOLD, 18));
		
		JScrollPane scrollPane = new JScrollPane();
		table = new JTable();
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
			}
		});
		table.setBackground(Color.WHITE);
		table.setAutoCreateRowSorter(true);
		table.addMouseListener(new MouseAdapter() {

			});
		scrollPane.setViewportView(table);
		
		JButton buttonPodgladTabeli = new JButton("Podgl\u0105d tabeli");
		buttonPodgladTabeli.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ShowTable(connection, table);
			}
		});
		buttonPodgladTabeli.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		lblZnajdzPoNr = new JLabel("Znajdz po nr karty:");
		lblZnajdzPoNr.setHorizontalAlignment(SwingConstants.CENTER);
		lblZnajdzPoNr.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		textField = new JTextField();
		textField.setColumns(10);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(19)
							.addComponent(lblMojeMenu, GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblNewLabel)))
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblDo, GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
						.addComponent(lblNewLabel_1, GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(dateChooserOD, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(dateChooserDO, GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE))
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblZnajdzPoNr, GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField, GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblTabelaHistoriiEksportw, GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
					.addGap(16))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
					.addGap(16))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(buttonPodgladTabeli, GroupLayout.PREFERRED_SIZE, 213, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(218, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(40)
					.addComponent(lblMojeMenu, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addComponent(dateChooserOD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel)
						.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addComponent(lblDo, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addComponent(dateChooserDO, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addComponent(lblZnajdzPoNr, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(textField, GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(buttonPodgladTabeli, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
					.addGap(25)
					.addComponent(lblTabelaHistoriiEksportw, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
					.addGap(10))
		);
		contentPane.setLayout(gl_contentPane);	
		
		}
	
	public void ShowTable(Connection connection, JTable table)
	{
		ArrayList<String> listaKartGosci = new ArrayList<String>();
		ArrayList<String> listaKartZTemporary2Export = new ArrayList<String>();
	
		// pobranie dat z datechooserow
			String dataODString;
			String dataDOString;
			Date dataOD = dateChooserOD.getDate();
			Date dataDO = dateChooserDO.getDate();
			DateFormat FD = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			if(dataOD==null || dataDO==null){
				dataODString = "";
				dataDOString = "";
			}else{
				dataODString = FD.format(dataOD);
				dataDOString = FD.format(dataDO);
			}
			
			System.out.println(dataODString);
			System.out.println(dataDOString);
			
			if(dataODString.equals("")||dataDOString.equals("")){
				JOptionPane.showMessageDialog(null, "Nie wype³niono pola daty");
			}else{
				if(dataOD.compareTo(dataDO) > 0){
					JOptionPane.showMessageDialog(null, "Data OD musi byæ przed dat¹ DO");
				}else{
					// wyswietla tabele
					// jesli nie okreslono nr karty, to zwroci wszystkie, a jesli okreslono, to tylko wyniki dla danej karty
					if(textField.getText().equals(null) || textField.getText().equals("") || textField.getText().equals("0")){
						try {
							System.out.println(textField.getText());
							String query="SELECT cards_name_surname_nrhacosoft.nazwisko_imie, access.id_karty, access.akcja, access.data FROM access LEFT JOIN cards_name_surname_nrhacosoft ON access.id_karty=cards_name_surname_nrhacosoft.id_karty WHERE access.data BETWEEN '"+dataODString+"' AND '"+dataDOString+"' AND access.akcja<>'nowy_pracownik'";
							PreparedStatement pst=connection.prepareStatement(query);
							ResultSet rs=pst.executeQuery();
							table.setModel(DbUtils.resultSetToTableModel(rs));
							pst.close();
							rs.close();
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else{
						try {
							System.out.println(textField.getText());
							String query="SELECT cards_name_surname_nrhacosoft.nazwisko_imie, access.id_karty, access.akcja, access.data FROM access LEFT JOIN cards_name_surname_nrhacosoft ON access.id_karty=cards_name_surname_nrhacosoft.id_karty WHERE access.data BETWEEN '"+dataODString+"' AND '"+dataDOString+"' AND access.akcja<>'nowy_pracownik' AND access.id_karty = "+textField.getText()+"";
							PreparedStatement pst=connection.prepareStatement(query);
							ResultSet rs=pst.executeQuery();
							table.setModel(DbUtils.resultSetToTableModel(rs));
							pst.close();
							rs.close();
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}
			}
		
	}
	
	}
