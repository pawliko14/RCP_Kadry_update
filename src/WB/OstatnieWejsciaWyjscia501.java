package WB;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.proteanit.sql.DbUtils;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Color;
import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.util.Timer;
import java.util.TimerTask;


public class OstatnieWejsciaWyjscia501 extends JFrame implements WindowListener {

	private JPanel contentPane;
	private JButton odswiez;
	private JTable table;
	private Timer timer;
	private boolean stopAlarm = false;

	/**
	 * Launch the application.
	 */

	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Connection connection = RCPdatabaseConnection.dbConnector("tosia", "1234");
				try {
					OstatnieWejsciaWyjscia501 frame = new OstatnieWejsciaWyjscia501(connection);
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
	public OstatnieWejsciaWyjscia501(final Connection connection) {
		
		setBackground(Color.WHITE);
		setTitle("Rejestracja wejœæ i wyjœæ pracowników");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 760, 563);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		addWindowListener(this);
		
		JLabel lblMojeMenu = new JLabel("Rejestracja wej\u015B\u0107/wyj\u015B\u0107");
		lblMojeMenu.setHorizontalAlignment(SwingConstants.CENTER);
		lblMojeMenu.setFont(new Font("Century", Font.BOLD, 24));
		
		final SimpleDateFormat doNazwy = new SimpleDateFormat("yyyy.MM.dd");
		final Calendar date = Calendar.getInstance();
		Image img = new ImageIcon(this.getClass().getResource("/BackgroundImage.jpg")).getImage();
		
		
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
						.addComponent(odswiez, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 714, Short.MAX_VALUE)
						.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 714, Short.MAX_VALUE)
						.addComponent(lblMojeMenu, GroupLayout.DEFAULT_SIZE, 714, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(45)
					.addComponent(lblMojeMenu, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
					.addGap(26)
					.addComponent(odswiez, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
					.addGap(40))
		);
		contentPane.setLayout(gl_contentPane);

		// ustawienie timera
						
		class RemindTask extends TimerTask {
	        public void run() {
	            System.out.println("Odswiezam tabele!!!");
	            ShowTable(connection, table);
	        }
	    }
		
		timer = new Timer();
		timer.schedule(new RemindTask(), 3000, 15000);

		
		}
	
	public void ShowTable(Connection connection, JTable table)
	{
		ArrayList<String> listaKartZablokowanych = new ArrayList<String>();
		ArrayList<String> listaKartPoOdswiezeniu = new ArrayList<String>();
		
		try {
			String query="SELECT access.id_karty, access.akcja, access.data, cards_name_surname_nrHacoSoft.nazwisko_imie, keys_permissions.nr_kluczy FROM"
					+ " access LEFT OUTER JOIN cards_name_surname_nrHacoSoft ON access.id_karty=cards_name_surname_nrHacoSoft.id_karty LEFT OUTER JOIN keys_permissions ON access.id_karty=keys_permissions.id_karty "
					+ "ORDER BY access.data desc LIMIT 500";
			PreparedStatement pst=connection.prepareStatement(query);
			ResultSet rs=pst.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
			pst.close();
			rs.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowClosed(WindowEvent arg0) {
		System.out.println("stop");
		timer.cancel();
	}


	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
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
