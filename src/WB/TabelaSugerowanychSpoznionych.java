package WB;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Timer;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import net.proteanit.sql.DbUtils;


public class TabelaSugerowanychSpoznionych extends JFrame {

	private JPanel contentPane;
	private JButton odswiez;
	private JTable table;
	private Timer timer;

	/**
	 * Launch the application.
	 */

	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Connection connection = RCPdatabaseConnection.dbConnector("tosia", "1234");
				try {
					TabelaSugerowanychSpoznionych frame = new TabelaSugerowanychSpoznionych(connection);
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
	public TabelaSugerowanychSpoznionych(final Connection connection) {
		
		setBackground(Color.WHITE);
		setTitle("Sugerowani spoznieni");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 579, 563);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblMojeMenu = new JLabel("Sugerowani spoznieni");
		lblMojeMenu.setHorizontalAlignment(SwingConstants.CENTER);
		lblMojeMenu.setFont(new Font("Century", Font.BOLD, 24));
		
		// co jeœli u¿ytkownik jest adminem
		
		if (Login.getAdmin()==true)
		{
		
		}
		
		JScrollPane scrollPane = new JScrollPane();
		
		table = new JTable();
		table.setBackground(Color.WHITE);
		table.setAutoCreateRowSorter(true);
		scrollPane.setViewportView(table);
//						Image img3 = new ImageIcon(this.getClass().getResource("/PdfIcon_mini.png")).getImage();
//						WylaczAlarm.setIcon(new ImageIcon(img3));
		
		ShowTable(connection, table);
						
		odswiez = new JButton("Od\u015Bwie\u017C tabel\u0119");
		odswiez.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ShowTable(connection,table);
			}
		});
		odswiez.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(odswiez, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 533, Short.MAX_VALUE)
						.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 533, Short.MAX_VALUE)
						.addComponent(lblMojeMenu, GroupLayout.DEFAULT_SIZE, 533, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(45)
					.addComponent(lblMojeMenu, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 315, GroupLayout.PREFERRED_SIZE)
					.addGap(26)
					.addComponent(odswiez, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(40, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
		
		}
	
	public void ShowTable(Connection connection, JTable table)
	{
		Instant now = Instant.now(); //current date
		Instant nowMinusOneDay = now.minus(Duration.ofDays(1));
		Instant nowPlusOneDay = now.plus(Duration.ofDays(1));
		Date dzis = Date.from(now);
		Date wczoraj = Date.from(nowMinusOneDay);
		Date jutro = Date.from(nowPlusOneDay);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		String dzisFormat = dateFormat.format(dzis);
		String wczorajFormat = dateFormat.format(wczoraj);
		String jutroFormat = dateFormat.format(jutro);
		
		System.out.println(dateFormat.format(dzis));
		System.out.println(dateFormat.format(wczoraj));
		System.out.println(dateFormat.format(jutro));
		
		try {
			String query="SELECT access.id_karty, cards_name_surname_nrhacosoft.nazwisko_imie, access.data "
					+ "FROM access LEFT JOIN cards_name_surname_nrhacosoft "
					+ "ON access.id_karty = cards_name_surname_nrhacosoft.id_karty "
					+ "WHERE access.data BETWEEN '"+wczorajFormat+"' AND '"+jutroFormat+"%"+"' AND access.akcja='wejscie' "
					+ "AND CASE WHEN SUBSTRING(access.data,12,5) BETWEEN '05:51' AND '06:00' THEN 'spozniony' "
					+ "WHEN SUBSTRING(access.data,12,5) BETWEEN '06:21' AND '06:30' THEN 'spozniony' "
					+ "WHEN SUBSTRING(access.data,12,5) BETWEEN '06:51' AND '07:00' THEN 'spozniony' "
					+ "WHEN SUBSTRING(access.data,12,5) BETWEEN '07:21' AND '07:30' THEN 'spozniony' "
					+ "WHEN SUBSTRING(access.data,12,5) BETWEEN '07:51' AND '08:00' THEN 'spozniony' "
					+ "ELSE 'ok' END = 'spozniony' ORDER BY access.data DESC";
			
			System.out.println(query);
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
