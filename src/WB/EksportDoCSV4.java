package WB;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import java.util.concurrent.TimeUnit;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import net.proteanit.sql.DbUtils;



public class EksportDoCSV4 extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JTable table2;
	private JTable table3;
	private JDateChooser dateChooserDO;
	private JDateChooser dateChooserOD;
	private JLabel lblZnalezioneKartyGocia;
	private JButton btnPodmienKarteGoscia;
	private ArrayList<String> KartyGosciDoPodmiany = new ArrayList<String>();
	private JButton button;
	private JTextField textField;
	private JTextField textField_1;
	private JLabel lblNewLabel_2;
	private JLabel lblNaKart;
	private ArrayList<String> listaKartZTemporary2Export;
	private ArrayList<String> listaDat = new ArrayList<String>();
	private ArrayList<String> listaAkcji = new ArrayList<String>();
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField txtPracownik;
	private JButton buttonPodgladTabeli;
	private JTextField txtFirma;
	private JLabel lblFirma;

	/**
	 * Launch the application.
	 */

	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			Connection connection = RCPdatabaseConnection.dbConnector("tosia", "1234");
			public void run() {
				try {
					EksportDoCSV4 frame = new EksportDoCSV4(connection);
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
	public EksportDoCSV4(final Connection connection) {
		
		setBackground(Color.WHITE);
		setTitle("Eksport do CSV");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 467, 952);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblMojeMenu = new JLabel("Eksport do CSV");
		lblMojeMenu.setHorizontalAlignment(SwingConstants.CENTER);
		lblMojeMenu.setFont(new Font("Century", Font.BOLD, 24));
		
		// co jeœli u¿ytkownik jest adminem
		
		if (Login.getAdmin()==true)
		{
		
		}
		
		button = new JButton("EKSPORTUJ");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int CzyExport = -1;
				CzyExport = prepare2Export(connection, table);
				if(CzyExport==0){
					InsertIntoTable2(connection, table2);
					ShowTable2(connection, table2);
					ExportFunction(connection);
				}else{
					// nie eksportuje
				}
			}
		});
		button.setToolTipText("Eksportuje dane do pliku CSV, kt\u00F3ry mo\u017Cna p\u00F3\u017Aniej zaimportowa\u0107 do Symfonii. Pomini\u0119te zostan\u0105 wpisy z kart go\u015Bci");
		button.setEnabled(false);
		button.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		btnPodmienKarteGoscia = new JButton("PODMIE\u0143");
		btnPodmienKarteGoscia.setEnabled(false);
		btnPodmienKarteGoscia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				listaDat.add("'"+textField_4.getText()+"'");
				listaAkcji.add("'"+textField_3.getText()+"'");
				ReplaceInTable(connection, table);
				ShowTableAfterReplacing(connection, table);
				ShowTable3(connection, table3);
				textField.setText("");
				textField_1.setText("");
				textField_2.setText("");
				textField_3.setText("");
				textField_4.setText("");
			}
		});
		btnPodmienKarteGoscia.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		dateChooserOD = new JDateChooser();
		dateChooserOD.setDateFormatString("yyyy-MM-dd");
		dateChooserDO = new JDateChooser();
		dateChooserDO.setDateFormatString("yyyy-MM-dd");
		
		JLabel lblNewLabel = new JLabel((String) null);
		
		JLabel lblZakresEksportu = new JLabel("Zakres eksportu:");
		lblZakresEksportu.setHorizontalAlignment(SwingConstants.CENTER);
		lblZakresEksportu.setFont(new Font("Century", Font.BOLD, 18));
		
		JLabel lblNewLabel_1 = new JLabel("OD");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		
//		JSpinner timeSpinner = new JSpinner( new SpinnerDateModel());
//		JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm:ss");
//		timeSpinner.setEditor(timeEditor);
//		timeSpinner.setValue(new Date());
		
		JLabel lblDo = new JLabel("DO");
		lblDo.setHorizontalAlignment(SwingConstants.CENTER);
		lblDo.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		textField_2 = new JTextField();
		textField_2.setEditable(false);
		textField_2.setColumns(10);
		
		textField_3 = new JTextField();
		textField_3.setEditable(false);
		textField_3.setColumns(10);
		
		textField_4 = new JTextField();
		textField_4.setEditable(false);
		textField_4.setColumns(10);
		
		lblZnalezioneKartyGocia = new JLabel("Znalezione wpisy na kart\u0119 go\u015Bcia:");
		lblZnalezioneKartyGocia.setEnabled(false);
		lblZnalezioneKartyGocia.setHorizontalAlignment(SwingConstants.CENTER);
		lblZnalezioneKartyGocia.setFont(new Font("Century", Font.BOLD, 18));
		
		JLabel lblTabelaHistoriiEksportw = new JLabel("Podgl\u0105d tabeli eksportowanej");
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
		
		JScrollPane scrollPane_1 = new JScrollPane();
		table2 = new JTable();
		table2.setBackground(Color.WHITE);
		table2.setAutoCreateRowSorter(true);
		table2.addMouseListener(new MouseAdapter() {

			});
		scrollPane_1.setViewportView(table2);
		
		ShowTable2(connection, table2);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		table3 = new JTable();
		table3.setBackground(Color.WHITE);
		table3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try{
					int row = table3.getSelectedRow();
					String karta = (table3.getModel().getValueAt(row,0)).toString();
					String akcja = (table3.getModel().getValueAt(row,1)).toString();
					Date data = (Date) (table3.getModel().getValueAt(row, 2));
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:ss");
					String dateStr = dateFormat.format(data);
					
					textField_2.setText(karta);
					textField_3.setText(akcja);
					textField_4.setText(dateStr);
					textField.setText(karta);
					
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			});
		scrollPane_2.setViewportView(table3);
		
		
		JLabel label = new JLabel("Tabela historii eksport\u00F3w");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Century", Font.BOLD, 18));
		
		buttonPodgladTabeli = new JButton("Podgl\u0105d tabeli");
		buttonPodgladTabeli.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(((JTextField)dateChooserOD.getDateEditor().getUiComponent()).getText()!=null||((JTextField)dateChooserDO.getDateEditor().getUiComponent()).getText()!=null){
					if(!listaDat.isEmpty()){
						listaDat.clear();
					}
					if(!listaAkcji.isEmpty()){
						listaAkcji.clear();
					}
					ShowTable(connection, table);
					ShowTable3(connection, table3);
					button.setEnabled(true);
				}else{
					JOptionPane.showMessageDialog(null, "Nie wypelniono pola daty");
				}
				
			}
		});
		buttonPodgladTabeli.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		lblNewLabel_2 = new JLabel("Podmie\u0144 z karty:");
		lblNewLabel_2.setEnabled(false);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setColumns(10);
		
		lblNaKart = new JLabel("Na kart\u0119:");
		lblNaKart.setEnabled(false);
		lblNaKart.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		textField_1 = new JTextField();
		textField_1.setEnabled(false);
		textField_1.setColumns(10);
		
		JSeparator separator = new JSeparator();
		
		txtPracownik = new JTextField();
		txtPracownik.setToolTipText("wpisz nr karty pracownika");
		txtPracownik.setHorizontalAlignment(SwingConstants.CENTER);
		txtPracownik.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtPracownik.setText("wszyscy");
		txtPracownik.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("Nr pracownika:");
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		
		txtFirma = new JTextField();
		txtFirma.setToolTipText("wpisz nazw\u0119 firmy dla kt\u00F3rej ma nast\u0105pi\u0107 eksport");
		txtFirma.setText("wszystkie");
		txtFirma.setHorizontalAlignment(SwingConstants.CENTER);
		txtFirma.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtFirma.setColumns(10);
		
		lblFirma = new JLabel("Firma:");
		lblFirma.setBackground(Color.LIGHT_GRAY);
		lblFirma.setHorizontalAlignment(SwingConstants.CENTER);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(19)
							.addComponent(lblMojeMenu, GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblNewLabel)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(lblZakresEksportu, GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
										.addComponent(dateChooserOD, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
										.addComponent(lblNewLabel_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE))
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addPreferredGap(ComponentPlacement.UNRELATED)
											.addComponent(lblDo, GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE))
										.addGroup(gl_contentPane.createSequentialGroup()
											.addGap(21)
											.addComponent(dateChooserDO, GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)))))))
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(119)
					.addComponent(buttonPodgladTabeli, GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
					.addGap(110))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblZnalezioneKartyGocia, GroupLayout.DEFAULT_SIZE, 421, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(16)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(textField_2, GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textField_3, GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
							.addGap(7)
							.addComponent(textField_4, GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE))
						.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE))
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(16)
					.addComponent(lblNewLabel_2, GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField, GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNaKart, GroupLayout.PREFERRED_SIZE, 68, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField_1, GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(120)
					.addComponent(btnPodmienKarteGoscia, GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
					.addGap(109))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(17)
					.addComponent(separator, GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(16)
					.addComponent(lblTabelaHistoriiEksportw, GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(16)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane_1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblFirma, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(lblNewLabel_3, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(txtFirma, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
									.addGap(26)
									.addComponent(txtPracownik, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)))
							.addGap(18)
							.addComponent(button, GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE))
						.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
						.addComponent(label, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(40)
					.addComponent(lblMojeMenu, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel)
						.addComponent(lblZakresEksportu, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblDo, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(dateChooserDO, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
						.addComponent(dateChooserOD, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(buttonPodgladTabeli, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblZnalezioneKartyGocia, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_2, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(textField_4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(textField_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(lblNaKart, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(lblNewLabel_2, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnPodmienKarteGoscia, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, 9, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblTabelaHistoriiEksportw, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 166, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(label, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblNewLabel_3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblFirma, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
							.addGap(5)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(txtPracownik, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtFirma, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)))
						.addComponent(button, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE))
					.addGap(5))
		);
		contentPane.setLayout(gl_contentPane);	
		
		}
	// przygotowuje fragment tabeli wejsc/wyjsc do wyeksportowania
	// wycina odpowiedni fragment z tabeli access i wrzuca ja do tabeli temporary2export
	// pobiera tez numery karty gosci z wycietego zakresu i wrzuca je do comboboxa sugerujac, ze dobrze by bylo je podmienic
	public void ShowTable(Connection connection, JTable table)
	{
		ArrayList<String> listaKartGosci = new ArrayList<String>();
		listaKartZTemporary2Export = new ArrayList<String>();
		
		// najpierw usuwa stara liste
		try{
			String query="DELETE FROM temporary2Export";
			PreparedStatement pst=connection.prepareStatement(query);
			pst.execute();
			pst.close();
			
		}catch(SQLException e3){
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Sprobuj jeszcze raz - baza jest zajeta");
			e3.printStackTrace();
		}
		// pobranie dat z datechooserow
			String dataODString;
			String dataDOString;
			Date dataOD = dateChooserOD.getDate();
			Date dataDO = dateChooserDO.getDate();
			DateFormat FD = new SimpleDateFormat("yyyy-MM-dd");
			if(dataOD==null || dataDO==null){
				dataODString = "";
				dataDOString = "";
			}else{
				dataODString = FD.format(dataOD)+" 00:00";
				dataDOString = FD.format(dataDO)+" 23:59";
			}
			
			System.out.println(dataODString);
			System.out.println(dataDOString);
			
			if(dataODString.equals("")||dataDOString.equals("")){
				JOptionPane.showMessageDialog(null, "Nie wype³niono pola daty");
			}else{
				if(dataOD.compareTo(dataDO) > 0){
					JOptionPane.showMessageDialog(null, "Data OD musi byæ przed dat¹ DO");
				}else{
					// przerzuca wybrany fragment tabeli (od daty do daty) access do temporary2Export
					try {
					String query="INSERT INTO temporary2Export select access.id, access.id_karty, access.akcja, REPLACE (access.data, 'H', ' ') AS data "
							+ "FROM access LEFT JOIN cards_name_surname_nrhacosoft ON access.id_karty=cards_name_surname_nrhacosoft.id_karty "
							+ "WHERE data BETWEEN '"+dataODString+"' AND '"+dataDOString+"' AND cards_name_surname_nrhacosoft.nazwisko_imie IS NOT NULL AND access.akcja <>'nowy_pracownik' "
							+ "ORDER BY data DESC";
					PreparedStatement pst=connection.prepareStatement(query);
					pst.execute();
					pst.close();
					
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Sprobuj jeszcze raz - baza jest zajeta");
						e.printStackTrace();
					}
					// wyswietla tabele
					try {
						String query="SELECT id_karty, akcja, data FROM temporary2Export";
						PreparedStatement pst=connection.prepareStatement(query);
						ResultSet rs=pst.executeQuery();
						table.setModel(DbUtils.resultSetToTableModel(rs));
						pst.close();
						rs.close();
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					// pobranie numerow kart z tabeli temporary2Export
					try {
						String query="SELECT * FROM temporary2Export";
						PreparedStatement pst=connection.prepareStatement(query);
						ResultSet rs=pst.executeQuery();
						while (rs.next()){
							listaKartZTemporary2Export.add(rs.getString("id_karty"));
						}
						System.out.println(listaKartZTemporary2Export);
						pst.close();
						rs.close();
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					// pobranie numerow z tabeli guests
					try {
						String query="SELECT id_karty FROM guests";
						PreparedStatement pst=connection.prepareStatement(query);
						ResultSet rs=pst.executeQuery();
						while (rs.next()){
							listaKartGosci.add(rs.getString("id_karty"));
						}
						System.out.println(listaKartGosci);
						pst.close();
						rs.close();
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					// pobranie czesci wspolnej z ciagu listaKartZTemporary2Export i listaKartGosci
					// zmienna listaKartZTemporary2Export zostaje nadpisana czescia wspolna
					// jesli czesc wspolna zawiera jakies elementy, to pojawia sie komunikat, ze znaleziono karte goscia
					// karty goscia zostaja wyswietlone w comboBoxie
					listaKartZTemporary2Export.retainAll(listaKartGosci);
					System.out.println(listaKartZTemporary2Export);
					Set<String> ListaBezDuplikatow = new LinkedHashSet<String>(listaKartZTemporary2Export);
					listaKartZTemporary2Export.clear();
					listaKartZTemporary2Export.addAll(ListaBezDuplikatow);
					System.out.println("Bez duplikatow: "+listaKartZTemporary2Export);
					
					if(!listaKartZTemporary2Export.isEmpty()){
						lblZnalezioneKartyGocia.setEnabled(true);
						btnPodmienKarteGoscia.setEnabled(true);
						button.setEnabled(true);
						lblNewLabel_2.setEnabled(true);
						textField_1.setEnabled(true);
						lblNaKart.setEnabled(true);
					}else{
						lblZnalezioneKartyGocia.setEnabled(false);
						btnPodmienKarteGoscia.setEnabled(false);
						button.setEnabled(false);
						lblNewLabel_2.setEnabled(false);
						textField_1.setEnabled(false);
						lblNaKart.setEnabled(false);
					}
				}
			}
		
	}
	
	// wyswietla tabele z historia eksportow
	public void ShowTable2(Connection connection, JTable table)
	{
		try {
			String query="SELECT * FROM exportHistory ORDER BY ExportPlikuWdniu DESC";
			PreparedStatement pst=connection.prepareStatement(query);
			ResultSet rs=pst.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
			pst.close();
			rs.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// z wybranego zakresu pokazuje wpisy wejsc/wyjsc na karte goscia - sugeruje ich podmiane
	public void ShowTable3(Connection connection, JTable table3)
	{
		try {
			String kartyGosciWaccess = listaKartZTemporary2Export.toString();
			kartyGosciWaccess = kartyGosciWaccess.replace('[', '(');
			kartyGosciWaccess = kartyGosciWaccess.replace(']', ')');
			String DatyPodmienione = listaDat.toString();
			DatyPodmienione = DatyPodmienione.replace('[', '(');
			DatyPodmienione = DatyPodmienione.replace(']', ')');
			String AkcjePodmienione = listaAkcji.toString();
			AkcjePodmienione = AkcjePodmienione.replace('[', '(');
			AkcjePodmienione = AkcjePodmienione.replace(']', ')');
			
			System.out.println("akcje: " + AkcjePodmienione);
			System.out.println("daty: " + DatyPodmienione);
			System.out.println("kartyGosci: " + kartyGosciWaccess);
			
			String query = "";
			if(listaDat.isEmpty()&&listaAkcji.isEmpty()){
				if(listaKartZTemporary2Export.isEmpty()){
					query="SELECT id_karty FROM access WHERE id_karty=12345678900"; // ma nic nie wyswietlic - nie mozna dac query="" lub null
				}else{
					query="SELECT id_karty, akcja, data FROM access USE INDEX (idx_id_karty_and_akcja) WHERE id_karty IN "+kartyGosciWaccess+" AND akcja <> 'nowy_pracownik'";
				}
			}else{
				if(listaKartZTemporary2Export.isEmpty()){
					query="SELECT id_karty FROM access WHERE id_karty=12345678900"; // ma nic nie wyswietlic - nie mozna dac query="" lub null
				}else{
					query="SELECT id_karty, akcja, data FROM access USE INDEX (idx_id_karty_and_akcja) WHERE id_karty IN "+kartyGosciWaccess+" AND akcja <> 'nowy_pracownik' AND NOT (akcja IN "+AkcjePodmienione+" AND data IN "+DatyPodmienione+")";
				}
				System.out.println(query);
			}
			PreparedStatement pst=connection.prepareStatement(query);
			ResultSet rs=pst.executeQuery();
			if (rs.next()){
				table3.setModel(DbUtils.resultSetToTableModel(rs));
			}else{
				table3.setModel(new DefaultTableModel(
						new Object[][] {
						},
						new String[] {
							"Brak wynikow"
						}
					));
			}
			pst.close();
			rs.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// wpisywanie wiersza do tabeli historii eksportow
	public void InsertIntoTable2(Connection connection, JTable table)
	{
		String dataODString;
		String dataDOString;
		//String todayString;
		Date dataOD = dateChooserOD.getDate();
		Date dataDO = dateChooserDO.getDate();
		Date today = new Date();
		DateFormat FD = new SimpleDateFormat("yyyy-MM-dd");
		//todayString = FD.format(today);
		if(dataOD==null || dataDO==null){
			dataODString = "";
			dataDOString = "";
		}else{
			dataODString = FD.format(dataOD);
			dataDOString = FD.format(dataDO);
		}
		
		try {
			String query="INSERT INTO exportHistory (OD, DO) VALUES ('"+dataODString+"', '"+dataDOString+"')";
			PreparedStatement pst=connection.prepareStatement(query);
			pst.execute();
			pst.close();
			
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Sprobuj jeszcze raz - baza jest zajeta");
				e.printStackTrace();
			}
	}
	
	// pokazuje tabele, ktora ma zostac wyeksportowana do CSV
	public void ShowTableAfterReplacing(Connection connection, JTable table){
		try {
			String query="SELECT id_karty, akcja, data FROM temporary2Export";
			PreparedStatement pst=connection.prepareStatement(query);
			ResultSet rs=pst.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
			pst.close();
			rs.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// nadpisanie numeru karty goscia w tabeli, ktora ma zostac wyeksportowana do CSV
	public void ReplaceInTable(Connection connection, JTable table){
		//sprawdzenie czy wpisany nr jest karta goscia
		try {
			String query="SELECT * FROM guests USE INDEX (idx_id_karty) WHERE id_karty='"+textField.getText()+"'";
			PreparedStatement pst=connection.prepareStatement(query);
			ResultSet rs=pst.executeQuery();
			if (!rs.next()){
				JOptionPane.showMessageDialog(null, "Takiego numeru nie ma w tabeli goœci");
			}else{
				pst.close();
				rs.close();
				//sprawdzenie czy wpisany nr jest karta pracownika
				try {
					String query2="SELECT * FROM cards_name_surname_nrHacoSoft USE INDEX (idx_id_karty) WHERE id_karty='"+textField_1.getText()+"'";
					PreparedStatement pst2=connection.prepareStatement(query2);
					ResultSet rs2=pst2.executeQuery();
					if (!rs2.next()){
						JOptionPane.showMessageDialog(null, "Takiego numeru nie ma w tabeli kart pracownikow");
					}else{
						pst2.close();
						rs2.close();
						// podmiana numeru
						if(textField_2.getText().equals("")||textField_3.getText().equals("")||textField_4.getText().equals("")){
							JOptionPane.showMessageDialog(null, "Nie wybrano wiersza z tabeli");
						}else{
							try {
								String query3="UPDATE temporary2Export SET id_karty = "+textField_1.getText()+" WHERE id_karty = "+textField_2.getText()+" AND akcja ='"+textField_3.getText()+"' AND data ='"+textField_4.getText()+"'";
								PreparedStatement pst3=connection.prepareStatement(query3);
								pst3.execute();
								pst3.close();
							}catch (Exception e) {
								JOptionPane.showMessageDialog(null, "Sprobuj jeszcze raz - baza jest zajeta");
								e.printStackTrace();
							}
						}
					}
					pst2.close();
					rs2.close();
				}catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			pst.close();
			rs.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// przygotowuje tabele temporary2Export do eksportu
	// sprawdza czy pominieto podmiane karty goscia (jesli tak, to spyta czy na pewno chcemy zrobic eksport)
	// jesli zostana jakies pozycje z zarejestrowanymi kartami goscia, to zostana usuniete (Symfonia nie moze zobaczyc tych wpisow, bo to nie pracownicy)
	// usuniete zostana tez numery kart, ktore nie maja przypisanych imion i nazwisk pracownikow (filtruje zapisy powstale przy wzbudzeniu sie czytnikow) !!!!!!!!!!!!!!!!!!!!!!!!!
	public int prepare2Export(Connection connection, JTable table)
	{	
		if(!txtPracownik.getText().equals("wszyscy")){
			buttonPodgladTabeli.doClick();
			try {
				String query2="DELETE FROM temporary2Export WHERE id_karty<> "+txtPracownik.getText()+"";
				PreparedStatement pst2=connection.prepareStatement(query2);
				pst2.execute();
				pst2.close();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Sprobuj jeszcze raz - baza jest zajeta");
				e.printStackTrace();
			}
		}else if(!txtFirma.getText().equals("wszystkie")){
			buttonPodgladTabeli.doClick();
			try {
				String query2="DELETE FROM temporary2export WHERE temporary2export.id_karty = ANY (SELECT cards_name_surname_nrhacosoft.id_karty FROM cards_name_surname_nrhacosoft WHERE cards_name_surname_nrhacosoft.firma <> '"+txtFirma.getText()+"' OR cards_name_surname_nrhacosoft.firma IS NULL)";
				PreparedStatement pst2=connection.prepareStatement(query2);
				pst2.execute();
				pst2.close();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Sprobuj jeszcze raz - baza jest zajeta");
				e.printStackTrace();
			}
		}else{
			// nic nie zmieniaj (wyeksportuje zapisy wszystkich pracownikow)
		}
		
		int i = -1;
		String kartyGosciWtemporary2Export = listaKartZTemporary2Export.toString();
		kartyGosciWtemporary2Export = kartyGosciWtemporary2Export.replace('[', '(');
		kartyGosciWtemporary2Export = kartyGosciWtemporary2Export.replace(']', ')');
		try {
			String query = "";
			if(listaKartZTemporary2Export.isEmpty()){
				query="SELECT id_karty FROM access WHERE id_karty=12345678900"; // ma nic nie wyswietlic - nie mozna dac query="" lub null
			}else{
				query="SELECT id_karty, akcja, data FROM temporary2Export USE INDEX (idx_id_karty) WHERE id_karty IN "+kartyGosciWtemporary2Export+"";
			}
			PreparedStatement pst=connection.prepareStatement(query);
			ResultSet rs=pst.executeQuery();
			if(rs.next()){
			    int reply = JOptionPane.showConfirmDialog(null, "W zapisach z bramki znaleziono karty goœcia, które nie zosta³y jeszcze podmienione na karty pracowników. NIE zostan¹ one przeniesione do Symfonii. Czy na pewno nie chcesz ich zamieniæ?", "Karty goœcia w eksportowanej tabeli", JOptionPane.YES_NO_OPTION);
		        if (reply == JOptionPane.YES_OPTION) {
		        	pst.close();
					rs.close();
		            //z temporary2Export zostana usuniete wpisy kartami gosci
					try {
						String query2="DELETE FROM temporary2Export WHERE id_karty IN "+kartyGosciWtemporary2Export+"";
						PreparedStatement pst2=connection.prepareStatement(query2);
						pst2.execute();
						pst2.close();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Sprobuj jeszcze raz - baza jest zajeta");
						e.printStackTrace();
					}
					return i=0;
		        }
		        else {
		        	pst.close();
					rs.close();
		            // nastapi wstrzymanie eksportu, bo uzytkownik jednak chce podmienic wpisy z kart goscia
					return i=-1;
		        }
			}else{
				return i=0;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return i=-1;
		}
	}
	
	//usuwa poprzednio zapisany plik
	//zrzut do pliku txt na //192.168.90.203/Common/EksportRCP/RCPdoSymfonii.txt
	//po zrzucie z bazy do pliku separatorem jest litera 'a' (nie da sie ustawic pustego znaku)
	//nastepnie litera a podmieniana jest przez pusty znak
	public void ExportFunction(Connection connection){
		
		//File file = new File("//192.168.90.203/Logistyka/Tosia/EksportRCP/RCPdoSymfonii.txt");
		File file = new File("//192.168.90.203/Common/EksportRCP/RCPdoSymfonii.txt");
		if(file.exists()){
			if(file.delete()){
				JOptionPane.showMessageDialog(null, "Skasowano poprzedni plik");
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				JOptionPane.showMessageDialog(null, "NIE UDALO SIE SKASOWAC PLIKU");
			}
		}
		
		try {
			String query="SELECT "
					+ "CASE "
					+ "WHEN CHAR_LENGTH(id_karty) = 1 "
					+ "	THEN CONCAT('000000000', id_karty) "
					+ "WHEN CHAR_LENGTH(id_karty) = 2 "
					+ " THEN CONCAT('00000000', id_karty) "
					+ "WHEN CHAR_LENGTH(id_karty) = 3 "
					+ "	THEN CONCAT('0000000', id_karty) "
					+ "WHEN CHAR_LENGTH(id_karty) = 4 "
					+ " THEN CONCAT('000000', id_karty) "
					+ "WHEN CHAR_LENGTH(id_karty) = 5 "
					+ "	THEN CONCAT('00000', id_karty) "
					+ "WHEN CHAR_LENGTH(id_karty) = 6 "
					+ " THEN CONCAT('0000', id_karty) "
					+ "WHEN CHAR_LENGTH(id_karty) = 7 "
					+ " THEN CONCAT('000', id_karty) "
					+ "WHEN CHAR_LENGTH(id_karty) = 8 "
					+ " THEN CONCAT('00', id_karty) "
					+ "WHEN CHAR_LENGTH(id_karty) = 9 "
					+ "	THEN CONCAT('0', id_karty) "
					+ "WHEN CHAR_LENGTH(id_karty) = 10 "
					+ "	THEN CONCAT('', id_karty) "
					+ "ELSE '0000000000' END AS id_karty,  "
					+ "SUBSTRING(data, 1, 4),  "
					+ "SUBSTRING(data, 6, 2),  "
					+ "SUBSTRING(data, 9, 2),  "
					+ "SUBSTRING(data, 12, 2),  "
					+ "SUBSTRING(data, 15, 2),  "
					+ "CASE  "
					+ "WHEN akcja='wejscie'  "
					+ " THEN REPLACE(akcja, 'wejscie', 1) "
					+ "WHEN akcja='wyjscie' "
					+ " THEN REPLACE(akcja, 'wyjscie', 0) END, "
					+ "'1000010000000' AS koncowkaDlaSymfonii  "
					//+ "into OUTFILE '//192.168.90.203/Logistyka/Tosia/EksportRCP/RCPdoSymfonii.txt' FIELDS TERMINATED BY 'a' LINES TERMINATED BY '\r\n' "
					+ "into OUTFILE '//192.168.90.203/Common/EksportRCP/RCPdoSymfonii.txt' FIELDS TERMINATED BY 'a' LINES TERMINATED BY '\r\n' "
					+ "FROM temporary2Export ORDER BY data DESC";
			PreparedStatement pst=connection.prepareStatement(query);
			pst.executeQuery();
			pst.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		// oczekiwanie na pelne zapisanie sie pliku (dopiero jak sie caly zapisze, to lec dalej)
		//File file2 = new File("//192.168.90.203/Logistyka/Tosia/EksportRCP/RCPdoSymfonii.txt");
		File file2 = new File("//192.168.90.203/Common/EksportRCP/RCPdoSymfonii.txt");
		while(!file2.exists()){
			//file2 = new File("//192.168.90.203/Logistyka/Tosia/EksportRCP/RCPdoSymfonii.txt");
			file2 = new File("//192.168.90.203/Common/EksportRCP/RCPdoSymfonii.txt");
		}
		// podmiana 'a' na ''
			System.out.println("aaa");
			//Path path = Paths.get("//192.168.90.203/Logistyka/Tosia/EksportRCP/RCPdoSymfonii.txt");
			Path path = Paths.get("//192.168.90.203/Common/EksportRCP/RCPdoSymfonii.txt");
			Charset cs = StandardCharsets.UTF_8;
			try {
				String content = new String(Files.readAllBytes(path), cs);
				content = content.replaceAll("a", "");
				Files.write(path,  content.getBytes(cs));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
