package WB;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;


import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.hyphenation.TernaryTree.Iterator;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

//import net.proteanit.sql.DbUtils;
import net.proteanit.sql.DbUtils;
//import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JDateChooser;

	public class EdycjaWeWy extends JFrame {

		private JPanel contentPane;
		private JButton odswiez;
		private JTable table;
		private JTable table1;
		private Timer timer;
		private JTextField txtNrPracownika;
		private JTextField textField_nr_pracownika;
		private JButton btnOk;
		private JTextField nic;
		private JButton GenerujPdf;
		private JButton btnUsun;
		private JButton btnEdytuj;
		private JButton btnDodaj;
		private String ImieNazwisko;
		private JTextField Osoba;
		private String data[]; // tabela do przechowania daty do pdf np : 03-01, 03-02 itp
		private List<String> data1; // lista trzymajaca dane do pdf
		private List<String> data2; // lista trzymajaca dane do pdf
		private	JDateChooser dateChooser_od;
		private	JDateChooser dateChooser_do;
		private JButton btnNewButton;
		String DEST = null; // zmienna do lokacji zapisu pliku (linia 655)
		long SumaCzasowPracy = 0;
		private int  rozowy = 0; // 1 jezeli bedzie wiecej niz 10h w pracy
		
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");  // HH - tryb 24h, hh - tryb 12h


		/**
		 * Launch the application.
		 */

		public static void main(String[] args) {
			
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					Connection connection = RCPdatabaseConnection.dbConnector("tosia", "1234");
					try {
						EdycjaWeWy frame = new EdycjaWeWy(connection);
						frame.setVisible(true);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		

		public EdycjaWeWy(final Connection connection) {
			setResizable(false);
			
			setBackground(Color.WHITE);
			setTitle("Rejestracja wejœæ i wyjœæ pracowników");
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setBounds(100, 100, 701, 581);
			contentPane = new JPanel();
			contentPane.setBackground(Color.WHITE);
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			setContentPane(contentPane);
			
			JLabel lblMojeMenu = new JLabel("Okno edycji wejœæ/wyjœæ pracownika");
			lblMojeMenu.setHorizontalAlignment(SwingConstants.CENTER);
			lblMojeMenu.setFont(new Font("Century", Font.BOLD, 24));

			
			nic = new JTextField();
			nic.setEditable(false);
			nic.setColumns(10);
			nic.setVisible(false);
			
			ImieNazwisko = ""; 

			
			Osoba = new JTextField();
			Osoba.setEditable(false);
			Osoba.setColumns(10);
			
			dateChooser_od = new JDateChooser();
			dateChooser_do = new JDateChooser();
			
			dateChooser_od.setDateFormatString("yyyy-MM-dd");
			dateChooser_do.setDateFormatString("yyyy-MM-dd");

		
			// co jeœli u¿ytkownik jest adminem
			
			if (Login.getAdmin()==true)
			{
			
			}
			
			JScrollPane scrollPane = new JScrollPane();    
			final JScrollPane scrollPane_1 = new JScrollPane();
			scrollPane_1.setEnabled(false);
			scrollPane_1.setVisible(false);

			
			
			// przycisk ktory odpowiada za pokazanie qwerendy - to jest dane wejsc pracownika/pracownikow
			btnOk = new JButton("OK");	
			btnOk.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
								
					if(textField_nr_pracownika.getText().equals("")) {
						
					    JOptionPane.showMessageDialog(null, "Podaj nr. karty pracownika");

					}
					else {
					
					ShowTable(connection, table);	
					ShowName(connection, table1);
					
					btnUsun.setVisible(true);
					btnEdytuj.setVisible(true);
					btnDodaj.setVisible(true);
					
					
					nic.setVisible(true);	
					GenerujPdf.setVisible(true);
					
					Osoba.getText();
					
					// PROBA< usunac pozniej
					getDate( connection, table1);
					

					}
				}
			});
			
			
			table = new JTable() {
				
			        @Override
					public java.awt.Component prepareRenderer(TableCellRenderer renderer, int rowIndex,
			                int columnIndex) {
			            JComponent component = (JComponent) super.prepareRenderer(renderer, rowIndex, columnIndex);  
			            
			            String s =  table.getModel().getValueAt(rowIndex,1 ).toString();
			            			           
			            return component;
			        }
				
			};
			
			
			
			
			table.setBackground(Color.WHITE);
			table.setAutoCreateRowSorter(true);
			scrollPane.setViewportView(table);
			
			table1 = new JTable();
			table1.setBackground(Color.WHITE);
			table1.setAutoCreateRowSorter(true);
			table1.setAlignmentX(CENTER_ALIGNMENT);
			scrollPane_1.setAlignmentX(CENTER_ALIGNMENT);
			scrollPane_1.setViewportView(table1);

							
			odswiez = new JButton("Od\u015Bwie\u017C tabel\u0119");
			odswiez.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					ShowTable(connection,table);
				}
			});
			odswiez.setFont(new Font("Tahoma", Font.BOLD, 12));
			
			JTextPane textPane = new JTextPane();
			
			txtNrPracownika = new JTextField();
			txtNrPracownika.setEditable(false);
			txtNrPracownika.setText("Nr. pracownika");
			txtNrPracownika.setColumns(10);
			
			textField_nr_pracownika = new JTextField();
			textField_nr_pracownika.setColumns(10);
					
			
			btnDodaj = new JButton("dodaj");
			btnDodaj.setVisible(false);
			btnDodaj.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
				
					String data1 = JOptionPane.showInputDialog("Wpisz pelna date np: 2018-02-13 09:27:23");
					String wejwyj = JOptionPane.showInputDialog("Podaj czy to wejscie czy wyjscie");
					
					if(data1.length() < 18) {
						 JOptionPane.showMessageDialog(null, "Dlugosc daty jest za krotka- niepowodzenie dodania");
					}
					else {
					
						try {
							Add(connection,table1,wejwyj,data1);
						 JOptionPane.showMessageDialog(null, "Dodano nowy rekord");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}
			});
			
			 btnUsun = new JButton("usun");
			 btnUsun.setVisible(false);
			 btnUsun.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					DefaultTableModel model = (DefaultTableModel) table.getModel();
					
					int row = table.getSelectedRow();
					try {
						
					    int confirmed = JOptionPane.showConfirmDialog(null, 
					            "Czy chcesz usunaæ rekord z bazy danych?", "Wyjdz",
					            JOptionPane.YES_NO_OPTION);

					        if (confirmed == JOptionPane.YES_OPTION) {
							String	id = JOptionPane.showInputDialog("wpisz id");
							
							
							delete(connection, table, id);	
								
							 JOptionPane.showMessageDialog(null, "USUNIETO REKORD " + id);
					        }
					      

							
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			
			JTextArea textArea = new JTextArea();
			
			JTextArea textArea_1 = new JTextArea();
			
			btnEdytuj = new JButton("edytuj");
			btnEdytuj.setVisible(false);
			btnEdytuj.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
				String id = JOptionPane.showInputDialog("Podaj ID do zmiany:");
				String data1 = JOptionPane.showInputDialog("Zmien date wg. schematu: '2018-05-28 09:30:00'");
				String wejwyj = JOptionPane.showInputDialog("zmien na 'wejscie' lub  'wyjscie'");
				
				if(data1.length() < 18 ) {
					 JOptionPane.showMessageDialog(null, "Dlugosc daty jest za krotka- niepowodzenie dodania");
				}
				else {
					update( connection, table1, id, data1, wejwyj);
				
				 	JOptionPane.showMessageDialog(null, "ZMIENIONO REKORD: "+ id);
				}
				
				}
			});
			
			
			GenerujPdf = new JButton("Generuj PDF");
			
			GenerujPdf.setVisible(false);
			GenerujPdf.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
					try {
							if(!((JTextField)dateChooser_od.getDateEditor().getUiComponent()).getText().isEmpty() || !((JTextField)dateChooser_do.getDateEditor().getUiComponent()).getText().isEmpty()) {
							
								if(((JTextField)dateChooser_od.getDateEditor().getUiComponent()).getText().compareTo(((JTextField)dateChooser_do.getDateEditor().getUiComponent()).getText()) > 0) {
									JOptionPane.showMessageDialog(null, "Data OD musi byæ przed dat¹ DO");
								}
								else {
									PROBA(connection,table1);
									btnNewButton.setVisible(true);
								}
						}
						else {
						    JOptionPane.showMessageDialog(null, "Podaj zakres dat");
							 }
						
							
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (DocumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			 btnNewButton = new JButton("Otworz Pdf");
			 btnNewButton.setVisible(false);
			 btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
					try {
						Desktop.getDesktop().open(new java.io.File(DEST));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						}
				}
			});
					
			GroupLayout gl_contentPane = new GroupLayout(contentPane);
			gl_contentPane.setHorizontalGroup(
				gl_contentPane.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_contentPane.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
							.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 660, GroupLayout.PREFERRED_SIZE)
								.addContainerGap())
							.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 8, GroupLayout.PREFERRED_SIZE)
							.addGroup(gl_contentPane.createSequentialGroup()
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
									.addComponent(lblMojeMenu, GroupLayout.DEFAULT_SIZE, 549, Short.MAX_VALUE)
									.addGroup(gl_contentPane.createSequentialGroup()
										.addGap(80)
										.addComponent(textArea_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED, 326, Short.MAX_VALUE)
										.addComponent(textArea, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addGap(137))
									.addGroup(gl_contentPane.createSequentialGroup()
										.addComponent(txtNrPracownika, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(textField_nr_pracownika, GroupLayout.PREFERRED_SIZE, 182, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(textPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addGap(18)
										.addComponent(btnOk, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE))
									.addGroup(gl_contentPane.createSequentialGroup()
										.addGap(50)
										.addComponent(Osoba, GroupLayout.PREFERRED_SIZE, 295, GroupLayout.PREFERRED_SIZE)))
								.addGap(124))
							.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(btnDodaj, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(btnEdytuj, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(btnUsun, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
								.addGap(246)
								.addComponent(btnNewButton)
								.addContainerGap(84, Short.MAX_VALUE))))
					.addGroup(gl_contentPane.createSequentialGroup()
						.addGap(78)
						.addComponent(dateChooser_od, GroupLayout.PREFERRED_SIZE, 161, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(nic, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(dateChooser_do, GroupLayout.PREFERRED_SIZE, 164, GroupLayout.PREFERRED_SIZE)
						.addGap(32)
						.addComponent(GenerujPdf, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(71, Short.MAX_VALUE))
					.addGroup(gl_contentPane.createSequentialGroup()
						.addGap(101)
						.addComponent(odswiez, GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
						.addGap(270))
			);
			gl_contentPane.setVerticalGroup(
				gl_contentPane.createParallelGroup(Alignment.TRAILING)
					.addGroup(gl_contentPane.createSequentialGroup()
						.addComponent(lblMojeMenu)
						.addGap(18)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(textPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtNrPracownika, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(textField_nr_pracownika, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addComponent(btnOk))
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
							.addGroup(gl_contentPane.createSequentialGroup()
								.addGap(18)
								.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE))
							.addGroup(gl_contentPane.createSequentialGroup()
								.addGap(26)
								.addComponent(Osoba, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGap(11)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 223, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
							.addGroup(gl_contentPane.createSequentialGroup()
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
									.addComponent(btnDodaj, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
									.addComponent(btnEdytuj, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
									.addComponent(btnUsun, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)))
							.addGroup(gl_contentPane.createSequentialGroup()
								.addGap(31)
								.addComponent(btnNewButton)))
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
							.addGroup(gl_contentPane.createSequentialGroup()
								.addGap(28)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
									.addComponent(dateChooser_od, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(nic, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(dateChooser_do, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
									.addGroup(gl_contentPane.createSequentialGroup()
										.addGap(53)
										.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
											.addComponent(textArea, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
											.addComponent(textArea_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
									.addGroup(gl_contentPane.createSequentialGroup()
										.addGap(33)
										.addComponent(odswiez))))
							.addGroup(gl_contentPane.createSequentialGroup()
								.addGap(18)
								.addComponent(GenerujPdf, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)))
						.addGap(179))
			);
			contentPane.setLayout(gl_contentPane);
			
			}
		
		public void ShowTable(Connection connection, JTable table)
		{
			
			try {
				// to query Dziala dla WSZYSTKICH mozliwych dat, zakomentowane w razie jakby ponizsze query sie nie sprawdzalo
				String query = "select access.id, access.`data`,access.akcja from access WHERE id_karty='"+textField_nr_pracownika.getText()+"'order by `data` desc";
				
				// to query dziala dla aktualnego miesiaca + ostatni miesiac z kawalkiem( powinno byc rowno 2 miesiace, ale...)
			//	String query = "  select access.id, access.`data`,access.akcja from access WHERE (id_karty='"+textField_nr_pracownika.getText()+"' &&  `data` >=(CURDATE()-INTERVAL 2 MONTH)) order by `data` desc\r\n";
				
				PreparedStatement pst=connection.prepareStatement(query);
				ResultSet rs=pst.executeQuery();
				table.setModel(DbUtils.resultSetToTableModel(rs));

				
				data1 = new ArrayList<String>();
				while(rs.next()) {
					String name = rs.getString(1);
					String name1 = rs.getString(2);
					String name2 = rs.getString(3);


				    data1.add(name);
				    data1.add(name1);
				    data1.add(name2);

				}	
				

				pst.close();
				rs.close();
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		
		public void ShowName(Connection connection, JTable table1)
		{
			String query = null;
			
			try {

				query = "select cards_name_surname_nrhacosoft.nazwisko_imie from cards_name_surname_nrhacosoft where id_karty='"+textField_nr_pracownika.getText()+"'";
				
				PreparedStatement pst=connection.prepareStatement(query);
				ResultSet rs=pst.executeQuery();
				
					while(rs.next())
					{
						ImieNazwisko = rs.getString("nazwisko_imie");
					}			
				Osoba.setText(ImieNazwisko);
				
				table1.setModel(DbUtils.resultSetToTableModel(rs));
				
				pst.close();			
				rs.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
		
		
		public void Add(Connection connection, JTable table1, String akcja ,String data)
		{
			String query = null;
			
			try {
				// aby dobrze dodac MUSI byc pelna data razem z godzina np "2018-12-12 07:19:59"
				query = "insert into proba_fat.access (`id_karty`,`akcja`,`data`) values ('"+textField_nr_pracownika.getText()+"','"+akcja+"','"+data+"')";
				PreparedStatement pst=connection.prepareStatement(query);
				pst.execute();

				pst.close();
		
			} catch (Exception e) {
				e.printStackTrace();
			}	
		

		}
		
		
		public void delete(Connection connection, JTable table1, String delete)
		{
			String query = null;
			
			try {

				query = "delete from proba_fat.access where id = '"+delete+"'";
				PreparedStatement pst=connection.prepareStatement(query);
				pst.execute();
				pst.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}	
			table.repaint();
		}
		
		public void update(Connection connection, JTable table1, String id, String data,String akcja)
		{
			String query = null;


			try {
				query = "update access set `data` = '"+data+"',access.akcja = '"+akcja+"'  where id_karty = '"+textField_nr_pracownika.getText()+"' and id  = '"+id+"'\r\n";								
				PreparedStatement pst=connection.prepareStatement(query);
				pst.execute();
				pst.close();
			
				
			} catch (Exception e) {
				e.printStackTrace();
			}	
			table.repaint();
		}
		
		
		
		
		public void getDate(Connection connection, JTable table1)
		{
			String query = null;
			
			try {
					query = "SELECT SUBSTRING(access.data, 6, 5) AS ExtractString from  access WHERE (id_karty = '"+txtNrPracownika.getText()+"' and `data` BETWEEN '"+((JTextField)dateChooser_od.getDateEditor().getUiComponent()).getText()+"'AND '"+((JTextField)dateChooser_do.getDateEditor().getUiComponent()).getText()+"') order by `data` asc";
					
				PreparedStatement pst=connection.prepareStatement(query);
				ResultSet rs=pst.executeQuery();

				data1 = new ArrayList<String>();

				while(rs.next()) {
					String name = rs.getString(1);
				    data1.add(name);
				}

				pst.close();
				rs.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
		
		
		
		
			PdfPCell table_cell1;
	        PdfPCell table_cell2;
	        PdfPCell table_cell3;
	        PdfPCell table_cell4;
	        PdfPCell table_cell5;
		
	        
	        //GLOWNA FUNKCJA obliczania czasu pracy
		public void PROBA(Connection connection, JTable table1) throws IOException, SQLException, ParseException, DocumentException {
			
			// lokalizacja spersonalizowana, tylko DLA PANI ZOSI Z KADR
	//		DEST = "C://Users/fk03/Desktop/godziny_pracy_pdf/"+ ImieNazwisko +"_"+ ((JTextField)dateChooser_od.getDateEditor().getUiComponent()).getText() +"_"+ ((JTextField)dateChooser_do.getDateEditor().getUiComponent()).getText() +".pdf";

			//lokalizacja testowa
	//		DEST = "C://Users/el08/Desktop/programiki/RCP_moja_przerobka/"+ ImieNazwisko +"_"+ ((JTextField)dateChooser_od.getDateEditor().getUiComponent()).getText() +"_"+ ((JTextField)dateChooser_do.getDateEditor().getUiComponent()).getText() +".pdf";
			
			//lokalizacja jurgen
			DEST = "\\\\dataserver\\common\\RCP_obecnosci\\"+ ImieNazwisko +"_"+ ((JTextField)dateChooser_od.getDateEditor().getUiComponent()).getText() +"_"+ ((JTextField)dateChooser_do.getDateEditor().getUiComponent()).getText() +".pdf";
			Date data = new Date();
			
			String roznica = null;
			String dzien = null;
			String wejscie_ostateczne = null;
			String Wyjscie_ostateczne = null;
			String komentarz = null;
			
			String ilosc_dni_od = ((JTextField)dateChooser_od.getDateEditor().getUiComponent()).getText();
			String ilosc_dni_do = ((JTextField)dateChooser_do.getDateEditor().getUiComponent()).getText();
			
			int zmienna = 0;
			
	       
			 // ile dni ma potem wplyw na ilosc iteracji petli
			int ile_dni= licz_czas_dni(ilosc_dni_od, ilosc_dni_do);
	
			   Document doc  = new Document();
			   PdfWriter.getInstance(doc, new FileOutputStream(DEST));
			   doc.open();
			   doc.add(new Paragraph("RAPORT PDF PRACOWNIKOW",FontFactory.getFont(FontFactory.TIMES_ROMAN,11, Font.ITALIC, BaseColor.BLACK)));
			   doc.add(new Paragraph("\n"));
			   doc.add(new Paragraph("Przedzial czasu: "+((JTextField)dateChooser_od.getDateEditor().getUiComponent()).getText()+" do "+ ((JTextField)dateChooser_do.getDateEditor().getUiComponent()).getText(),FontFactory.getFont(FontFactory.TIMES_ROMAN,11, Font.ITALIC, BaseColor.BLACK)));
			   doc.add(new Paragraph("Pracownik: "+ ImieNazwisko +" Nr. karty: " + textField_nr_pracownika.getText(),FontFactory.getFont(FontFactory.TIMES_ROMAN,11, Font.ITALIC, BaseColor.BLACK)));
			   doc.add(new Paragraph("Data wygenerowania raportu: "+new Date().toString(),FontFactory.getFont(FontFactory.TIMES_ROMAN,11, Font.ITALIC, BaseColor.BLACK)));
			   doc.add(new Paragraph("\n"));
			   
			 
			      PdfPTable table = new PdfPTable(5);
			        float widths[] = new float[] { 6, 6, 6, 6, 6};


			        // header row:
			        table.addCell("Dzien");
			        table.addCell("wejscie");
			        table.addCell("wyjscie");
			        table.addCell("czas w firmie");
			        table.addCell("Komentarz");
			        table.setHeaderRows(1);
			        
			        		      
			      String data_pocz = ((JTextField)dateChooser_od.getDateEditor().getUiComponent()).getText();
			      String data_kon = ((JTextField)dateChooser_do.getDateEditor().getUiComponent()).getText();
			      
			      data_kon = fun_plus_dzien(data_pocz);  // czyli bedzie np : pocz 2018-03-18 , kon 2018-03-19, dzieki temu bedzie sie iterowalo o jeden dzien w przod 
			      			
		for(int x = 0 ;x< ile_dni +1 ; x++) {	
			rozowy = 0; // standartowo nie ma byc rozowego, tylko jezeli za dlugo pracuje
			
			zmienna = (x%2 == 0) ? 1 : 0;
			
		String query = "select SUBSTRING(access.data, 1, 10) as dzien,SUBSTRING(access.data, 11, 20) as godzina,access.akcja from access WHERE (id_karty = '"+textField_nr_pracownika.getText()+"' and `data` BETWEEN '"+data_pocz+  "'  AND '"+data_kon+"') order by `data` asc\r\n";
		// fun_plus_dzien dodaje do podanej daty w parametrze 1 dzien, sprawdzone dziala

			PreparedStatement pst=connection.prepareStatement(query);
			ResultSet rs=pst.executeQuery();
			

	        String s1=null,s2=null,s3=null;
	        
	        // if iteruje dzien po dniu, wiec kiedy jest pusty rekord, albo w ogole nie ma to dodaje  do tabeli rekord pusty (ponizeszego ifa)
         if (!rs.isBeforeFirst()) 
            {
 			data_pocz = fun_plus_dzien(data_pocz);
		    data_kon = fun_plus_dzien(data_kon);
		    

		    SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );   
			Calendar cal = Calendar.getInstance();    
			cal.setTime( dateFormat.parse(data_pocz));    
			cal.add( Calendar.DATE, -1 );    
			dzien=dateFormat.format(cal.getTime());  
		    
			wejscie_ostateczne = "----";
			Wyjscie_ostateczne = "----";
			roznica = "----";
			komentarz = "----";

			dodaj_cell( dzien,  wejscie_ostateczne,  Wyjscie_ostateczne,  roznica,  komentarz, doc, table,zmienna,rozowy);
	        table.flushContent();
		}
		
		else  {
			data_pocz = fun_plus_dzien(data_pocz);
		    data_kon = fun_plus_dzien(data_kon);
		
		    ArrayList<ArrayList<String>> md = new ArrayList<ArrayList<String>>();
		    ArrayList<String> row = null;

		        
		        while(rs.next()){
		            row = new ArrayList<String>();
		            s1 = rs.getString("dzien");
		            s2 = rs.getString("godzina");
		            s3 = rs.getString("akcja");
		            
		           
		            row.add(s1);
		            row.add(s2);
		            row.add(s3);
        
		            md.add(row);	            		           
		           }
		        
		        // DLA PEWNOSCI WIECEJ RAZY przesiac, bez for'a przesiewa tylko raz
		        for(int y = 0; y< 6 ;y++)
		        	md = sprawdz_przesianie(md);

		        
		    	 // glowna funkcja	    	
				dzien = md.get(0).get(0); // zawsze wiadomo bo pierwsza wartosc jest data np 2018-04-03 
							
		    	  ArrayList<Calendar> calendar_array = new ArrayList<Calendar>();  
		    	  
		    	  String godzina_wej = null;
		    	  String godzina_wyj = null;

		    	  Date d1 = null;
		    	  Date d2 = null;
		    	  
		    	  int il_wyjsc = 0;
		    	  int il_wejsc = 0;
		    	  
		    	  int test = 0 ;
		    	  
		    	  // for liczacy ile jest wyjsc i wejsc dla danej petli
		    	  for(int i = 0; i< md.size();i++) {		  
		    		  if(md.get(i).get(2).equals("wejscie"))
		    			  il_wejsc++;
		    		  else  if(md.get(i).get(2).equals("wyjscie"))
		    			  il_wyjsc++;
		    	  }
		    	  
		    	  


		    	  
			    	// kiedy jest tylko 1 rekord i jest to wyjscie
			    	 if(md.get(0).get(2).equals("wyjscie")  && md.size() == 1) { 		
			    	 	 ArrayList<String> proba1 = new ArrayList<String>();

		            	    proba1.add(md.get(0).get(0));
				            proba1.add(md.get(0).get(1)); // ta sama godzina, zeby potem odjac i wyszlo 0
				            proba1.add("wejscie");        
				            md.add(0,proba1); // na sam poczatek dodaje
				            
				            dodaj_cell( dzien,  "-",  md.get(0).get(1),  "-",  "Brak wejscia", doc, table,zmienna,rozowy);
							table.flushContent();
							md.clear();
			    	} 
			    	 // analogicznie, ale kiedy jest tylko jedno wejscie i tyle
			    	 else if(md.get(0).get(2).equals("wejscie")  && md.size() == 1) { 		
			    	 	 ArrayList<String> proba1 = new ArrayList<String>();

		            	    proba1.add(md.get(0).get(0));
				            proba1.add(md.get(0).get(1)); // ta sama godzina, zeby potem odjac i wyszlo 0
				            proba1.add("wejscie");        
				            md.add(0,proba1); // na sam poczatek dodaje
				            
				            dodaj_cell( dzien, md.get(0).get(1),  "-",  "-",  "Brak wyjscia", doc, table,zmienna,rozowy);
							table.flushContent();
							md.clear();
			    	}
		    	
		    	  
		     for(int i = 0 ;i < md.size();i++) {
		   
		    	 //sytuacja najlepsza : sprawdza czy pierwsza rzecza w dniu jest wejscie a ostanie wyjsciem
		    	 	// oraz ze jest tylko 1 wejscie i jedno wyjscie ( size == 2)\
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		    	if(md.get(0).get(2).equals("wejscie")  && md.get(md.size()-1).get(2).equals("wyjscie") && md.size() == 2) {
		    		
	    		//System.out.println("Sytuacja najprostsza");
		    		najprostsza_opcja( md, dzien,  table, zmienna,  doc);

		    	}
		    	// kompletny brak wejsc w ciagu dnia
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		    	else if(il_wejsc == 0){  
		    		
			    		for(int z = 0 ; z < il_wyjsc; z++) {
		    		
			    			 Wyjscie_ostateczne = md.get(z).get(1); // godzina wyjscia
			    			 wejscie_ostateczne  = "-";
			    			 komentarz = "BRAK WEJSC";
			    			         
				              roznica = "-";
				              
				            dodaj_cell( dzien,  wejscie_ostateczne,  Wyjscie_ostateczne,  roznica,  komentarz, doc, table,zmienna,rozowy);
				            table.flushContent();
				            	    
			    		}
			    		 md.clear();
			    		
			    	}
		    	// kompletny brak wyjsc w ciagu dnia
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		    	else if (il_wyjsc == 0) {
		    		
		    		for(int z = 0; z < il_wejsc ; z++) {
		    		 wejscie_ostateczne = md.get(z).get(1); // godzina wejscia
					 Wyjscie_ostateczne = "-"; // godzina wyjscia
					 komentarz = "BRAK WYJSC";
		             
			              roznica = "-";
		             
			              dodaj_cell( dzien,  wejscie_ostateczne,  Wyjscie_ostateczne,  roznica,  komentarz, doc, table,zmienna,rozowy);
			              table.flushContent();
		    		}
				    md.clear();
		    	}
		    	
		    	// jezeli pierwsze to wejscie ostatnie wyjscie i jest parzysta il wej i wyj DYNAMICZNE
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		    	else if(md.get(0).get(2).equals("wejscie")  && md.get(md.size()-1).get(2).equals("wyjscie")&& (md.size() % 2 ) == 0  && il_wejsc == il_wyjsc) {
		    		// TUTAJ WCHODZI
		    		Wyjscie_ostateczne = md.get(md.size()-1).get(1); // skoro ostatnie wyjscie, to md.size()-1
		    						  			    		
		        //		if(md.size() == 2  || md.size() == 4) {
			    //			md =sprawdz_przesianie(md); // tego nie trzeba, ale niech zostanie
			    //			najprostsza_opcja( md, dzien,  table, zmienna,  doc);

		    	//	}
		    	//	else {		    			
		    			wej_wyj_par_tylesamo(il_wejsc,il_wyjsc,wejscie_ostateczne,Wyjscie_ostateczne,md,komentarz,dzien,rozowy,zmienna,doc,table);
		    	//	}
		            md.clear();
		            table.flushContent();

		    	}
		    	
		    	// jezeli pierwsze to wejscie ostatnie wyjscie i jest parzysta il wej i wyj DYNAMICZNE
				/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				else if(md.get(0).get(2).equals("wejscie")  && md.get(md.size()-1).get(2).equals("wyjscie")&& (md.size() % 2 ) == 0  && il_wejsc % 2 == 0 &&  il_wyjsc %2 == 0 && il_wyjsc >= 4) {
				
					Wyjscie_ostateczne = md.get(md.size()-1).get(1); // skoro ostatnie wyjscie, to md.size()-1
					System.out.println("TUTAJ WSZEDL\n");
					
					if(md.size() == 2  || md.size() == 4) {
						md =sprawdz_przesianie( md);
						najprostsza_opcja( md, dzien,  table, zmienna,  doc);					
					}
					
					else {		    			
						wej_wyj_par_tylesamo(il_wejsc,il_wyjsc,wejscie_ostateczne,Wyjscie_ostateczne,md,komentarz,dzien,rozowy,zmienna,doc,table);
					}				
					md.clear();
					table.flushContent();
				
				}
		    	
		    	
		    		// kiedy na poczatku jest wej i na koncu wyj, ale sa 3 akcje i musi byc wiecej wejsc niz wyjsc ( 3 akcje = 2 wejscia 1 wyjscie)
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		    	else if(md.get(0).get(2).equals("wejscie")  && md.get(md.size()-1).get(2).equals("wyjscie")&& (md.size() % 2 ) == 1  && il_wejsc > il_wyjsc) {
		    		
		    		d1 = sdf.parse(md.get(1).get(1));
		    		d2 = sdf.parse(md.get(md.size()-1).get(1));
		    		
		    		
		    		////////////////////////
		    		 wejscie_ostateczne = md.get(1).get(1); // godzina wejscia
					 Wyjscie_ostateczne = md.get(md.size()-1).get(1); // godzina wyjscia
					 komentarz = "-"; // wczesniej bylo "brak wyjscia";
		    		    		
		    		long diff = d2.getTime() - d1.getTime();

		    		SumaCzasowPracy = SumaCzasowPracy + diff;
  		
		    		roznica  = zamien_diff_na_roznica(diff);
	
					dodaj_cell( dzien,  wejscie_ostateczne,  Wyjscie_ostateczne,  roznica,  komentarz, doc, table,zmienna,rozowy);
					 table.flushContent();
	
			    		
			    		////////////////////////
			    		 wejscie_ostateczne = md.get(0).get(1); // godzina wejscia
			    		 
			    		 
			    		 Date d3 = sdf.parse(md.get(0).get(1));
			    		 long diff_proba = d1.getTime() - d3.getTime();

			    			 table.flushContent();
			    			 md.clear();
			    		 

		    	
		    	}
				// kiedy na poczatku jest wej i na koncu wyj, ale sa 3 akcje i musi byc wiecej WYJSC NIZ WEJSCIE ( 3 akcje = 1 wejscia 2 wyjscie)
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		    	else if(md.get(0).get(2).equals("wejscie")  && md.get(md.size()-1).get(2).equals("wyjscie")&& (md.size() % 2 ) == 1  && il_wejsc < il_wyjsc) {
		    		
		    		
		    			// ponizsza czesc jest tylko kiedy jest wej ... wyj , parzysta ilosc wej wyj, oraz ilosc wej i wyj jest taka sama
				  ArrayList<Date> Daty = new ArrayList<>();    
		    	  ArrayList<Long> D11 = new ArrayList<Long>(); 
		    	  ArrayList<Long> Diff = new ArrayList<Long>();
		    	  ArrayList<String> D11_Hours = new ArrayList<String>();

		    	  long finalr = 0;
	            
		            wejscie_ostateczne = md.get(0).get(1); // godzina wejscia, skoro jest pierwsza pierwsza, to nie trza nic iterowac
		            komentarz = "-";
		            Wyjscie_ostateczne = md.get(md.size()-1).get(1);

		            int size2 = md.size();
		            
		        // od 1 zeby porownac potem z-1 i z
		            for(int z = 1 ; z < size2; z++) {

			          
			            // czyli kiedy na poprzednim indexie tez bylo wyjscie
				               if(md.get(z).get(2).equals("wyjscie") && md.get(z-1).get(2).equals("wyjscie")/*&& z% 2 == 0*/){
			            	
			            	 ArrayList<String> proba1 = new ArrayList<String>();
					            System.out.println("czyli kiedy na poprzednim indexie tez bylo wyjscie");

			            	    proba1.add(md.get(z).get(0));
					            proba1.add(md.get(z).get(1)); // ta sama godzina, zeby potem odjac i wyszlo 0
					            proba1.add("wejscie");        
					            md.add(z,proba1); // na sam poczatek dodaje
					            
					            dodaj_cell( dzien,  "-",  md.get(z).get(1),  "-",  "Brak wejscia", doc, table,zmienna,rozowy);
								table.flushContent();
					            
			            }
			             //czyli kiedy wejscie jest na nieparzystym ORAZ kiedy nie jest ostatnie
			             else  if(md.get(z-1).get(2).equals("wejscie") && md.get(z).get(2).equals("wejscie") /*&& z% 2 ==1*/ || (md.get(z-1).get(2).equals("wyjscie") && md.get(z+1).get(2).equals("wejscie")) ){
			            	 
			            	 ArrayList<String> proba3 = new ArrayList<String>();
					            System.out.println("czyli kiedy wejscie jest na nieparzystym ORAZ kiedy nie jest ostatnie");

			            	    proba3.add(md.get(z).get(0));
					            proba3.add(md.get(z).get(1)); // ta sama godzina, zeby potem odjac i wyszlo 0
					            proba3.add("wyjscie");        
					            md.add(z+1,proba3); // na sam poczatek dodaje
					            
					            dodaj_cell( dzien,  "-",  md.get(z).get(1),  "-",  "Brak wejscia", doc, table,zmienna,rozowy);
								table.flushContent();
					                        	 
			             }
			             else {
			            	 // bierze tylko co 3 rekord 1 3 5 , nie bierze 2 pierwszych rekordow, i sprawdza czy rekordy sie roznia, czas pracy np
			            	 if(z %2 == 1 && z >=3 && !(md.get(z-1).get(1)).equals(md.get(z).get(1)) ) {
			            		
					    		d1 = sdf.parse(md.get(z-1).get(1));
					    		d2 = sdf.parse(md.get(z).get(1));
					    		
					    		
					    		 wejscie_ostateczne = md.get(z-1).get(1); // godzina wejscia
								 Wyjscie_ostateczne = md.get(z).get(1); // godzina wyjscia
								 komentarz = "-";
					    		
					    		
					    		
					    		long diff = d2.getTime() - d1.getTime();
					    		
					    		SumaCzasowPracy = SumaCzasowPracy + diff;
					    		
					    		roznica  = zamien_diff_na_roznica(diff);
					    		
								dodaj_cell( dzien,  wejscie_ostateczne,  Wyjscie_ostateczne,  roznica,  komentarz, doc, table,zmienna,rozowy);
								 table.flushContent();
			            	 }	 
			             }
            	
		            }
			        md.clear();

		    	}
		    	
		    	// jezeli pierwsze jest WYJSCIE a i ostatnie te¿ jest WYJSCIE 
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		    	else if(md.get(0).get(2).equals("wyjscie")  && md.get(md.size()-1).get(2).equals("wyjscie")) { 		
		    			// ponizsza czesc jest tylko kiedy jest wej ... wyj , parzysta ilosc wej wyj, oraz ilosc wej i wyj jest taka sama

				  ArrayList<Date> Daty = new ArrayList<>();    
		    	  ArrayList<Long> D11 = new ArrayList<Long>(); 
		    	  ArrayList<Long> Diff = new ArrayList<Long>();
		    	  ArrayList<String> D11_Hours = new ArrayList<String>();

		    	  long finalr = 0;
		            
		           // wejscie_ostateczne = md.get(0).get(1); // godzina wejscia, skoro jest pierwsza pierwsza, to nie trza nic iterowac
		            wejscie_ostateczne = "-"; // bo nie ma poczatkowego wejscia
		            komentarz = "-";
		           // Wyjscie_ostateczne = md.get(md.size()-1).get(1);
		            Wyjscie_ostateczne = "-"; // rowniez nie ma wyjscia na ostatnim miejscu

		            int size2 = md.size();
		            int check1 = 1;
		            
		            	// kiedy na pierwszym miejscu jest wyjscie dodaj na poczatek rekord wejscia z godzina wyjscia ( zeby pozniej roznica = 0 )
		            	if(md.get(0).get(2).equals("wyjscie") && check1 == 1) {
				          
				            ArrayList<String> proba1 = new ArrayList<String>();
				            
				            System.out.println("kiedy na pierwszym miejscu jest wyjscie dodaj na poczatek rekord wejscia z godzina wyjscia");

		            	    proba1.add(md.get(0).get(0));
				            proba1.add(md.get(0).get(1)); // ta sama godzina, zeby potem odjac i wyszlo 0
				            proba1.add("wejscie");        
				            md.add(0,proba1); // na sam poczatek dodaje
				          
				            dodaj_cell( dzien,  "-",  md.get(0).get(1),  "-",  "Brak wejscia", doc, table,zmienna,rozowy);
							table.flushContent();

				            
				            check1 = 0;
		            	}
		            	
		           size2 = md.size();
		            boolean check2= false;
	
		            
		        // od 1 zeby porownac potem z-1 i z
		            for(int z = 1 ; z < size2; z++) {

			          
			            // czyli kiedy na poprzednim indexie tez bylo wyjscie
				               if(md.get(z).get(2).equals("wyjscie") && md.get(z-1).get(2).equals("wyjscie")/*&& z% 2 == 0*/){
			            	
			            	 ArrayList<String> proba1 = new ArrayList<String>();
					            System.out.println("czyli kiedy na poprzednim indexie tez bylo wyjscie");

			            	    proba1.add(md.get(z).get(0));
					            proba1.add(md.get(z).get(1)); // ta sama godzina, zeby potem odjac i wyszlo 0
					            proba1.add("wejscie");        
					            md.add(z,proba1); // na sam poczatek dodaje
					            
					            dodaj_cell( dzien,  "-",  md.get(z).get(1),  "-",  "Brak wejscia", doc, table,zmienna,rozowy);
								table.flushContent();
		            	
			            }
			             //czyli kiedy wejscie jest na nieparzystym ORAZ kiedy nie jest ostatnie
			             else  if(md.get(z-1).get(2).equals("wejscie") && md.get(z).get(2).equals("wejscie") /*&& z% 2 ==1*/ || (md.get(z-1).get(2).equals("wyjscie") && md.get(z+1).get(2).equals("wejscie")) ){
			            	 
			            	 ArrayList<String> proba3 = new ArrayList<String>();
					            System.out.println("czyli kiedy wejscie jest na nieparzystym ORAZ kiedy nie jest ostatnie");

			            	    proba3.add(md.get(z).get(0));
					            proba3.add(md.get(z).get(1)); // ta sama godzina, zeby potem odjac i wyszlo 0
					            proba3.add("wyjscie");        
					            md.add(z+1,proba3); // na sam poczatek dodaje
					            
					            dodaj_cell( dzien, md.get(z).get(1), "-" ,   "-",  "Brak wyjscia", doc, table,zmienna,rozowy);
								table.flushContent();

			            	 
			             }

			             
			             else {
			            	 // bierze tylko co 3 rekord 1 3 5 , nie bierze 2 pierwszych rekordow, i sprawdza czy rekordy sie roznia, czas pracy np
			            	 if(z %2 == 1 && z >=3 && !(md.get(z-1).get(1)).equals(md.get(z).get(1)) ) {
			            		
					    		d1 = sdf.parse(md.get(z-1).get(1));
					    		d2 = sdf.parse(md.get(z).get(1));
					    		
					    		
					    		 wejscie_ostateczne = md.get(z-1).get(1); // godzina wejscia
								 Wyjscie_ostateczne = md.get(z).get(1); // godzina wyjscia
								 komentarz = "-";
					    		
					    		System.out.println("d1: "+d1 + " d2: "+ d2 + "\n");
					    		
					    		long diff = d2.getTime() - d1.getTime();
					    		
					    		SumaCzasowPracy = SumaCzasowPracy + diff;
					    		
					    		
					    		roznica  = zamien_diff_na_roznica(diff);
						                 
								dodaj_cell( dzien,  wejscie_ostateczne,  Wyjscie_ostateczne,  roznica,  komentarz, doc, table,zmienna,rozowy);
								 table.flushContent();
			            	 }	  
			             }

		            	
		            }

		            	size2 = md.size();

		            	// kiedy mamy wejscie a na koniec wyjscie
//			              if(md.get(size2-2).get(2).equals("wejscie") && md.get(size2-1).get(2).equals("wyjscie")) {
//				            	System.out.println("powinno wej: "+md.get(size2-2).get(1) + " powinno wyj " + md.get(size2-1).get(1));
//
//			            		d1 = sdf.parse(md.get(size2-2).get(1));
//					    		d2 = sdf.parse(md.get(size2-1).get(1));
//					    		
//					    		
//					    		 wejscie_ostateczne = md.get(size2-2).get(1); // godzina wejscia
//								 Wyjscie_ostateczne = md.get(size2-1).get(1); // godzina wyjscia
//								 komentarz = "-";
//					    		
//					    		
//					    		
//					    		long diff = d2.getTime() - d1.getTime();
//					    		
//					    		SumaCzasowPracy = SumaCzasowPracy + diff;
//					    		
//					    		roznica  = zamien_diff_na_roznica(diff);
//						                 
//					    		System.out.println(" Czas w firmie: \n"+ roznica);
//								dodaj_cell( dzien,  wejscie_ostateczne,  Wyjscie_ostateczne,  roznica,  komentarz, doc, table,zmienna,rozowy);
//								 table.flushContent();
//			              }

				        md.clear();
		    		
		    	}
		    	// kiedy jest na poczatku WYJSCIE i na koncu WEJSCIE
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		    	else if(md.get(0).get(2).equals("wyjscie")  && md.get(md.size()-1).get(2).equals("wejscie")) { 		
		    			// ponizsza czesc jest tylko kiedy jest wej ... wyj , parzysta ilosc wej wyj, oraz ilosc wej i wyj jest taka sama
		    		
		    		int size2 = 0;
		    		
		    			if(md.size() >= 3) {
					    				
							  ArrayList<Date> Daty = new ArrayList<>();    
					    	  ArrayList<Long> D11 = new ArrayList<Long>(); 
					    	  ArrayList<Long> Diff = new ArrayList<Long>();
					    	  ArrayList<String> D11_Hours = new ArrayList<String>();
			
					    	  long finalr = 0;
					    	  
					            System.out.println("wej: "+ il_wejsc + "\n");
					            System.out.println("wyj: "+ il_wyjsc + "\n");
					            
					           // wejscie_ostateczne = md.get(0).get(1); // godzina wejscia, skoro jest pierwsza pierwsza, to nie trza nic iterowac
					            wejscie_ostateczne = "-"; // bo nie ma poczatkowego wejscia
					            komentarz = "-";
					           // Wyjscie_ostateczne = md.get(md.size()-1).get(1);
					            Wyjscie_ostateczne = "-"; // rowniez nie ma wyjscia na ostatnim miejscu
			
					            size2 = md.size();
					            int check1 = 1;
					                  
					    		md = sprawdz_przesianie(md); // dla pewnosci jeszcze raz przesiane
          
					            	// kiedy na pierwszym miejscu jest wyjscie dodaj na poczatek rekord wejscia z godzina wyjscia ( zeby pozniej roznica = 0 )
					            	if(md.get(0).get(2).equals("wyjscie") && check1 == 1) {
							          
							            ArrayList<String> proba1 = new ArrayList<String>();
			
					            	    proba1.add(md.get(0).get(0));
							            proba1.add(md.get(0).get(1)); // ta sama godzina, zeby potem odjac i wyszlo 0
							            proba1.add("wejscie");        
							            md.add(0,proba1); // na sam poczatek dodaje
							          
							            dodaj_cell( dzien,  "-",  md.get(0).get(1),  "-",  "Brak wejscia", doc, table,zmienna,rozowy);
										table.flushContent();
			
							            
							            check1 = 0;
					            	}
					            	
					           size2 = md.size();
					            boolean check2= false;
				
					            
					        // od 1 zeby porownac potem z-1 i z
					            for(int z = 1 ; z < size2; z++) {
			
						          
						            // czyli kiedy na poprzednim indexie tez bylo wyjscie
							               if(md.get(z).get(2).equals("wyjscie") && md.get(z-1).get(2).equals("wyjscie")/*&& z% 2 == 0*/){
						            	
						            	 ArrayList<String> proba1 = new ArrayList<String>();
			
						            	    proba1.add(md.get(z).get(0));
								            proba1.add(md.get(z).get(1)); // ta sama godzina, zeby potem odjac i wyszlo 0
								            proba1.add("wejscie");        
								            md.add(z,proba1); // na sam poczatek dodaje
								            
								            dodaj_cell( dzien,  "-",  md.get(z).get(1),  "-",  "Brak wejscia", doc, table,zmienna,rozowy);
											table.flushContent();
					            	
						            }
						             //czyli kiedy wejscie jest na nieparzystym ORAZ kiedy nie jest ostatnie
						             else  if(md.get(z-1).get(2).equals("wejscie") && md.get(z).get(2).equals("wejscie") /*&& z% 2 ==1*/  /*|| (md.get(z-1).get(2).equals("wyjscie") && md.get(z+1).get(2).equals("wejscie")) */){
						            	 
						            	 ArrayList<String> proba3 = new ArrayList<String>();
			
						            	    proba3.add(md.get(z).get(0));
								            proba3.add(md.get(z).get(1)); // ta sama godzina, zeby potem odjac i wyszlo 0
								            proba3.add("wyjscie");        
								            md.add(z+1,proba3); // na sam poczatek dodaje
								            
								            dodaj_cell( dzien, md.get(z).get(1), "-" ,   "-",  "Brak wyjscia", doc, table,zmienna,rozowy);
											table.flushContent();
			
						            	 
						             }
						        
						             
						             else {
						            	 // bierze tylko co 3 rekord 1 3 5 , nie bierze 2 pierwszych rekordow, i sprawdza czy rekordy sie roznia, czas pracy np
						            	 if(z %2 == 1 && z >=3 && !(md.get(z-1).get(1)).equals(md.get(z).get(1)) ) {
						            		
								    		d1 = sdf.parse(md.get(z-1).get(1));
								    		d2 = sdf.parse(md.get(z).get(1));
								    		
								    		
								    		 wejscie_ostateczne = md.get(z-1).get(1); // godzina wejscia
											 Wyjscie_ostateczne = md.get(z).get(1); // godzina wyjscia
											 komentarz = "-";
								    		
								    		
								    		
								    		long diff = d2.getTime() - d1.getTime();
								    		
								    		SumaCzasowPracy = SumaCzasowPracy + diff;
								    		
								    		roznica  = zamien_diff_na_roznica(diff);
								    		
											dodaj_cell( dzien,  wejscie_ostateczne,  Wyjscie_ostateczne,  roznica,  komentarz, doc, table,zmienna,rozowy);
											 table.flushContent();
						            	 }	  
						             }
			
					            	
					            }
			
					            	size2 = md.size();
					            	 // jezeli ostatni rekord jest wciaz wejsciem, to dodaj wyjscie i wsio
						              if(md.get(size2-1).get(2).equals("wejscie") ) {
						            	 ArrayList<String> proba2 = new ArrayList<String>();
			
						            	    proba2.add(md.get(size2-1).get(0));
								            proba2.add(md.get(size2-1).get(1)); // ta sama godzina, zeby potem odjac i wyszlo 0
								            proba2.add("wyjscie");        
								            md.add(size2,proba2); // Dodaje na sam koniec
								            
								            dodaj_cell( dzien, md.get(size2).get(1), "-" ,   "-",  "Brak wyjscia", doc, table,zmienna,rozowy);
											table.flushContent();
			
					             }
		    	} 
		    			else {
					    	wejscie_ostateczne = "-";
				    		Wyjscie_ostateczne = md.get(0).get(1);
				    		roznica = "-";
				    		komentarz = "brak wejscia";
				    		dodaj_cell( dzien,  wejscie_ostateczne,  Wyjscie_ostateczne,  roznica,  komentarz, doc, table,zmienna,rozowy);
							 table.flushContent();
						      
		    				
		    				wejscie_ostateczne = md.get(1).get(1);
				    		Wyjscie_ostateczne = "-";
				    		roznica = "-";
				    		komentarz = "brak wyjscia";
				    		dodaj_cell( dzien,  wejscie_ostateczne,  Wyjscie_ostateczne,  roznica,  komentarz, doc, table,zmienna,rozowy);
				    		table.flushContent();
				    		  md.clear();

		    			}
				        md.clear();
		
		    	}
		    	// Kiedy mamy na poczatku WEJSCIE i na koncu WEJSCIE
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		    	else if(md.get(0).get(2).equals("wejscie")  && md.get(md.size()-1).get(2).equals("wejscie") ) {

    				
					  ArrayList<Date> Daty = new ArrayList<>();    
			    	  ArrayList<Long> D11 = new ArrayList<Long>(); 
			    	  ArrayList<Long> Diff = new ArrayList<Long>();
			    	  ArrayList<String> D11_Hours = new ArrayList<String>();
	
			    	  long finalr = 0;
			    	  
			            System.out.println("wej: "+ il_wejsc + "\n");
			            System.out.println("wyj: "+ il_wyjsc + "\n");
			            
			           // wejscie_ostateczne = md.get(0).get(1); // godzina wejscia, skoro jest pierwsza pierwsza, to nie trza nic iterowac
			            wejscie_ostateczne = "-"; // bo nie ma poczatkowego wejscia
			            komentarz = "-";
			           // Wyjscie_ostateczne = md.get(md.size()-1).get(1);
			            Wyjscie_ostateczne = "-"; // rowniez nie ma wyjscia na ostatnim miejscu
	
			            int size2 = md.size();
			            int check1 = 1;
  		 
			    		md = sprawdz_przesianie(md);

			            	// kiedy na pierwszym miejscu jest wyjscie dodaj na poczatek rekord wejscia z godzina wyjscia ( zeby pozniej roznica = 0 )
			            	if(md.get(0).get(2).equals("wyjscie") && check1 == 1) {
					          
					            ArrayList<String> proba1 = new ArrayList<String>();
	
			            	    proba1.add(md.get(0).get(0));
					            proba1.add(md.get(0).get(1)); // ta sama godzina, zeby potem odjac i wyszlo 0
					            proba1.add("wejscie");        
					            md.add(0,proba1); // na sam poczatek dodaje
					          
					            dodaj_cell( dzien,  "-",  md.get(0).get(1),  "-",  "Brak wejscia", doc, table,zmienna,rozowy);
								table.flushContent();
	
					            
					            check1 = 0;
			            	}
			            	
			           size2 = md.size();

			        // od 1 zeby porownac potem z-1 i z
			            for(int z = 1 ; z < size2-1; z++) {
	          
				            // czyli kiedy na poprzednim indexie tez bylo wyjscie
					               if(md.get(z).get(2).equals("wyjscie") && md.get(z-1).get(2).equals("wyjscie")){
				            	
				            	 ArrayList<String> proba1 = new ArrayList<String>();
	
				            	    proba1.add(md.get(z).get(0));
						            proba1.add(md.get(z).get(1)); // ta sama godzina, zeby potem odjac i wyszlo 0
						            proba1.add("wejscie");        
						            md.add(z,proba1); // na sam poczatek dodaje
						            
						            dodaj_cell( dzien,  "-",  md.get(z).get(1),  "-",  "Brak wejscia", doc, table,zmienna,rozowy);
									table.flushContent();
		
						            
				            }
				             //czyli kiedy wejscie jest na nieparzystym ORAZ kiedy nie jest ostatnie
				             else  if(md.get(z-1).get(2).equals("wejscie") && md.get(z).get(2).equals("wejscie") || (md.get(z-1).get(2).equals("wyjscie") && md.get(z+1).get(2).equals("wejscie")) ){
				            	 
				            	 ArrayList<String> proba3 = new ArrayList<String>();
	
				            	    proba3.add(md.get(z).get(0));
						            proba3.add(md.get(z).get(1)); // ta sama godzina, zeby potem odjac i wyszlo 0
						            proba3.add("wyjscie");        
						            md.add(z+1,proba3); // na sam poczatek dodaje
						            
						            dodaj_cell( dzien,  md.get(z).get(1),  "-",  "-",  "Brak wyjscia", doc, table,zmienna,rozowy);
									table.flushContent();
						            
				             }
            
					               
				             else {
				            	 // bierze tylko co 3 rekord 1 3 5 , nie bierze 2 pierwszych rekordow, i sprawdza czy rekordy sie roznia, czas pracy np
				            	
						    		d1 = sdf.parse(md.get(z-1).get(1));
						    		d2 = sdf.parse(md.get(z).get(1));
						    		
						    		
						    		 wejscie_ostateczne = md.get(z-1).get(1); // godzina wejscia
									 Wyjscie_ostateczne = md.get(z).get(1); // godzina wyjscia
									 komentarz = "-";
						    		
						    		
						    		
						    		long diff = d2.getTime() - d1.getTime();
						    		
						    		SumaCzasowPracy = SumaCzasowPracy + diff;
						    		
						    		roznica  = zamien_diff_na_roznica(diff);
						    		
									dodaj_cell( dzien,  wejscie_ostateczne,  Wyjscie_ostateczne,  roznica,  komentarz, doc, table,zmienna,rozowy);
									 table.flushContent();
				            	 	 
				             }
	        
					               
			            }
				           size2 = md.size();

			        	if(md.get(md.size()-1).get(2).equals("wejscie") && check1 == 1) {
					          
				            ArrayList<String> proba1 = new ArrayList<String>();

		            	    proba1.add(md.get(size2-1).get(0));
				            proba1.add(md.get(size2-1).get(1)); // ta sama godzina, zeby potem odjac i wyszlo 0
				            proba1.add("wyjscie");        
				            md.add(size2,proba1); // dodaje na koniec (powinno byc chyba size2-1
				          
				            dodaj_cell( dzien,  md.get(size2-1).get(1),  "-",  "-",  "Brak wyjscia", doc, table,zmienna,rozowy);
							table.flushContent();
 
				            check1 = 0;
		            	}
				        md.clear();

		    	}
		    	//kiedy na poczatku jest WEJSCIE na koncu WYJSCIE oraz liczba rekordow jest parzysta
		    	////////////////////////////////////////////////////////////////////////////////////////////////////////////
		    	else if(md.get(0).get(2).equals("wejscie")  && md.get(md.size()-1).get(2).equals("wyjscie") && md.size() %2 ==0 && il_wejsc > il_wyjsc  && md.size() <= 4 ) {

		    		Wyjscie_ostateczne = md.get(md.size()-1).get(1); // skoro ostatnie wyjscie, to md.size()-1
		    		wejscie_ostateczne = md.get(0).get(1); 	// pierwszy rekord, nie trzeba przesiewac
		    		wej_wyj_par_tylesamo(il_wejsc,il_wyjsc,wejscie_ostateczne,Wyjscie_ostateczne,md,komentarz,dzien,rozowy,zmienna,doc,table);
		    		    		
		            md.clear();
		            table.flushContent();
		    		
		    	}
		    	
		    	//kiedy na poczatku jest WEJSCIE na koncu WYJSCIE oraz liczba rekordow jest psrzysta
		    	////////////////////////////////////////////////////////////////////////////////////////////////////////////
		    	else if(md.get(0).get(2).equals("wejscie")  && md.get(md.size()-1).get(2).equals("wyjscie") && md.size() %2 ==0 && il_wejsc < il_wyjsc  && md.size() <= 4 ) {

		    		Wyjscie_ostateczne = md.get(md.size()-2).get(1); // skoro ostatnie wyjscie, to md.size()-1
		    		wejscie_ostateczne = md.get(0).get(1); 	// pierwszy rekord, nie trzeba przesiewac
		    		wej_wyj_par_tylesamo(il_wejsc,il_wyjsc,wejscie_ostateczne,Wyjscie_ostateczne,md,komentarz,dzien,rozowy,zmienna,doc,table);
		    		    		
		            md.clear();
		            table.flushContent();
		    		
		    	}
		    	
		    	
 	
		    	// jezeli nic nie pasuje do schematu wypisz blad ( np kiedy rekord jest jakis dziwny - nietypowy)
		    	else {
		    		wejscie_ostateczne = "blad";
		    		Wyjscie_ostateczne = "blad";
		    		roznica = "blad";
		    		komentarz = "blad";
		    		dodaj_cell( dzien,  wejscie_ostateczne,  Wyjscie_ostateczne,  roznica,  komentarz, doc, table,zmienna,rozowy);
					 table.flushContent();
				        md.clear();
		    	}
		    		
		     }
		}   
		   	   
		
		}
		
		
		   doc.add(new Paragraph("\n\n"));
		   
		 
		      PdfPTable table2 = new PdfPTable(4);

		        // header row:
		        table2.addCell("Data od");
		        table2.addCell("Data do");
		        table2.addCell("Suma czasu");
		        table2.addCell("Komentarz");
		        table2.setHeaderRows(1);
		        
		        
		        long diffSeconds = SumaCzasowPracy / 1000 % 60;
	             long diffMinutes = SumaCzasowPracy / (60 * 1000) % 60;
	             long diffHours = SumaCzasowPracy / (60 * 60 * 1000) ; // %24
        
	             String strHours = Long.toString(diffHours);
	             String strMinutes = Long.toString(diffMinutes);
	             String strSec = Long.toString(diffSeconds);	
	            String  SumaPracy = dodaj_zero(strHours,strMinutes,strSec);
		        
		        
		        table_cell1 = new PdfPCell(new Phrase(((JTextField)dateChooser_od.getDateEditor().getUiComponent()).getText()));
		        table2.addCell(table_cell1);
		        table_cell1 = new PdfPCell(new Phrase(((JTextField)dateChooser_do.getDateEditor().getUiComponent()).getText()));
		        table2.addCell(table_cell1);
		        table_cell1 = new PdfPCell(new Phrase(SumaPracy));
		        table2.addCell(table_cell1);
		        table_cell1 = new PdfPCell(new Phrase("-"));
		        table2.addCell(table_cell1);
		        
		        doc.add(table2);
		        
		   
		        
		        doc.add(new Paragraph("*Adnotacja ", FontFactory.getFont(FontFactory.TIMES_ROMAN,9, Font.BOLD, BaseColor.BLACK) ));
		        doc.add(new Paragraph("		W przypadku wyjscia z pracy i powrotem miedzy godzina 9:31 a 9:52 " , FontFactory.getFont(FontFactory.TIMES_ROMAN,9, Font.BOLD, BaseColor.BLACK)));
		        doc.add(new Paragraph("		Czas ten dodawany jest do sumy czasu pracy", FontFactory.getFont(FontFactory.TIMES_ROMAN,9, Font.BOLD, BaseColor.BLACK) ));
		        SumaCzasowPracy = 0; // zerowanie, bo kumuluje;
		        
		
	    JOptionPane.showMessageDialog(null, "Raport wygenerowany");
		doc.close();   
			       
	}
		
		// funkcja dodaje do Stringa z data zera, np 2018-9-12 bedzie -> 2018-09-12 itp. Poprawia czytelnsc raportu
		public String dodaj_zero(String strHours, String strMinutes, String strSec) {
		    if(strHours.equals("1") || strHours.equals("2") || strHours.equals("3") || strHours.equals("4") ||
           		 strHours.equals("5") || strHours.equals("6") || strHours.equals("7") || strHours.equals("8") || strHours.equals("9") )
            {
           	 strHours = "0" + strHours;
            }
            if(strMinutes.equals("0") || strMinutes.equals("1") || strMinutes.equals("2") || strMinutes.equals("3") || strMinutes.equals("4") ||
           		 strMinutes.equals("5") || strMinutes.equals("6") || strMinutes.equals("7") || strMinutes.equals("8") || strMinutes.equals("9") )
            {
           	 strMinutes = "0" + strMinutes;
            }
            if(strSec.equals("0") || strSec.equals("1") || strSec.equals("2") || strSec.equals("3") || strSec.equals("4") ||
           		 strSec.equals("5") || strSec.equals("6") || strSec.equals("7") || strSec.equals("8") || strSec.equals("9") )
            {
           	 strSec = "0" + strSec;
            }
           String  roznica = strHours + ":" + strMinutes + ":" + strSec;
            
            return roznica;
		}
		
		// zwraca stringa np godzina 8:45 bedzie 845, potem w innej funkcji cast na Int i porownuje czy np. 8:45 > 9:21 itp
		public String getHoursMinutes(Long a)
		{
			String b = null;
			
	         long diffMinutes = a / (60 * 1000) % 60;
             long diffHours = a / (60 * 60 * 1000) % 24;
             
             
     
             String strHours = Long.toString(diffHours);
             String strMinutes = Long.toString(diffMinutes);
              b = strHours  + strMinutes;
			
			return b;
		}
		
		public void dodaj_cell(String d, String we, String wy, String r, String k, Document doc, PdfPTable table, int zmienna, int rozowy)
		{		
			// jezeli zmienna == 0 - czyli normalnie, to kolor bialy neutralny,
			// jezeli zmienna == 1 - czyli co 2gi row, to kolor szarawy - dla odroznienia
			int c1= 255, c2=255, c3=255;
        	if(zmienna ==1)   	{
        		c1 = 226;
        		c2 = 226;
        		c3= 226;
        	}
        	

			table_cell1 = new PdfPCell(new Phrase(d));
			table_cell1.setBackgroundColor(new BaseColor(c1, c2, c3)); 

            table.addCell(table_cell1);
            

            table_cell2 = new PdfPCell(new Phrase(we));
			table_cell2.setBackgroundColor(new BaseColor(c1, c2, c3)); 

            table.addCell(table_cell2);
              
            
            table_cell3= new PdfPCell(new Phrase(wy));
			table_cell3.setBackgroundColor(new BaseColor(c1, c2, c3)); 

            table.addCell(table_cell3);
            
            
             // rozowy tylko wtedy wystapi kiedy godziny pracy > 9h
            if (rozowy == 1)
        	{
            	  table_cell4 = new PdfPCell(new Phrase(r));
      			table_cell4.setBackgroundColor(new BaseColor(255, 192, 203)); 
                  table.addCell(table_cell4);
        	}
            else if(rozowy == 2) 
            {
            	 table_cell4 = new PdfPCell(new Phrase(r));
       			table_cell4.setBackgroundColor(new BaseColor(181, 181, 255)); 
                   table.addCell(table_cell4);
            }
            else if(rozowy == 3) {
            		table_cell4 = new PdfPCell(new Phrase(r));
        			table_cell4.setBackgroundColor(new BaseColor(207, 41, 41)); 
                    table.addCell(table_cell4);      	
            }
            else {
            	table_cell4 = new PdfPCell(new Phrase(r));
    			table_cell4.setBackgroundColor(new BaseColor(c1, c2, c3)); 
                table.addCell(table_cell4);
            }
            
            table_cell5 = new PdfPCell(new Phrase(k));
			table_cell5.setBackgroundColor(new BaseColor(c1, c2, c3)); 

            table.addCell(table_cell5);


            
	        try {
				doc.add(table);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		
		// funkcja liczaca roznice w dniach dla podanych dwoch dat w Stringach
			public int licz_czas_dni(String od_1, String do_1) throws ParseException {
				
		        SimpleDateFormat sdf_year = new SimpleDateFormat("yyyy-MM-dd");
				
				  Date d1 = null;
		    	  Date d2 = null;
		    	  
		    	  d1 = sdf_year.parse(od_1);
		    	  d2 = sdf_year.parse(do_1);
		    	  
		    	  long diff = d2.getTime() - d1.getTime();
		          long diffDays = diff / (24 * 60 * 60 * 1000);
	
		          String strDays = Long.toString(diffDays);             
		          int roznica = Integer.parseInt(strDays);
		             
		          return  roznica;    	  	
			}
		
			// funkcja dodajaca do podanej daty w stringu + 1 dzien
			public String fun_plus_dzien(String data) throws ParseException {
				String dzien = null;
				
				SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );   
				Calendar cal = Calendar.getInstance();    
				cal.setTime( dateFormat.parse(data));    
				cal.add( Calendar.DATE, 1 );    
				dzien=dateFormat.format(cal.getTime());    
	
				return dzien;
			}
			
			// tutaj jest problem, trzeba dodac dodatkowe rowy!!
			public void wej_wyj_par_tylesamo(int il_wejsc, int il_wyjsc,String wejscie_ostateczne,String Wyjscie_ostateczne,ArrayList<ArrayList<String>> md,String komentarz,String dzien,int rozowy,int zmienna,Document doc ,PdfPTable table ) throws ParseException {
				
				  ArrayList<Date> Daty = new ArrayList<>();    
		    	  ArrayList<Long> D11 = new ArrayList<Long>(); 
		    	  ArrayList<Long> Diff = new ArrayList<Long>();
		    	  ArrayList<String> D11_Hours = new ArrayList<String>();

		    	  long finalr = 0;

		            wejscie_ostateczne = md.get(0).get(1); // godzina wejscia, skoro jest pierwsza pierwsza, to nie trza nic iterowac
		            komentarz = "-";

		    	  for(int z = 0; z < md.size(); z++)
		    	  {		    		
	    			  Daty.add(sdf.parse(md.get(z).get(1)));
	    			  D11.add(Daty.get(z).getTime());
		    	  }
		    	  
		    	  
		    	  //zamienia na godziny zeby pozniej porownac
		    	  for(int z = 0 ; z < D11.size();z++)
		    	  {
		    		  D11_Hours.add(getHoursMinutes(D11.get(z)));
		    	  }
		          

		    	  // z iteruje co 2 od 1 czyli 1 3 5 ... bo ma porownywac 2 kolejne daty bez powtorzen
	            	for(int z = 1; z< md.size(); z+= 2) {
	            			Diff.add(Daty.get(z).getTime() - Daty.get(z-1).getTime());	            	
	            	}
	            	
	            	for(int i = 0 ; i < Diff.size();i++)
	            		System.out.println("DIFF: " + Diff.get(i));
	            	
	            	// sprawdza czas przerwy

	            	for(int z = 1; z< md.size(); z++) {
           			
		            		if (Integer.parseInt(D11_Hours.get(z)) >= 830 && Integer.parseInt(D11_Hours.get(z)) <= 853 && Integer.parseInt(D11_Hours.get(z-1)) >= 830 && Integer.parseInt(D11_Hours.get(z-1)) <= 853) {
		            			long lala1 = (D11.get(z));
		            			long lala2 = (D11.get(z-1));
		            			long roznica2 = 0;		
		            			
		            			roznica2 = lala1 - lala2;
		            			finalr = finalr + roznica2;	
		            			// dodanie roznicy do godzin pracy
		            			SumaCzasowPracy =SumaCzasowPracy + finalr;
		            		}
		            		
	            	}
	            	
	            	
	            	long finalr_2 = Diff.get(1) ;
	            	finalr = Diff.get(0);
	            	
            	for(int i = 0 ; i < Diff.size();i++)
            		SumaCzasowPracy = SumaCzasowPracy + Diff.get(i);
            		
            		//	SumaCzasowPracy = SumaCzasowPracy + finalr;
		    	  
		    
		    	 
		    	  
		    	  		String roznica = zamien_diff_na_roznica(finalr);  	    	  		
		    	  		String roznica_2 = zamien_diff_na_roznica(finalr_2);
		    	  		

				    	
		    	  		
		    	  			//dzien wyjscie_ostateczne oraz wyjscie_ostateczne powinno nie globalne, powinno byc 
		    	  		//	zadeklarowane i wypelnione w t
		    	  		//do testow

		    	  		
		    	  		//dodawanei kolejnego cella, powoduje ze celle sie agreguja przykladowo dodaj cell bedzie 1, 
				    	 // potem dodaj cell bedzie 1 2, potem kolejny bedzie 1 2 3 itd... 
				    	 // trzeba wymyslic jakas metoda usuwania nadmiarowych rowow
		    	  		
	    	  			dodaj_cell( dzien,  md.get(0).get(1),  md.get(1).get(1),  roznica,  komentarz, doc, table,zmienna,rozowy);
						table.flushContent();

	    	  			dodaj_cell( dzien,  md.get(2).get(1),  md.get(3).get(1),  roznica_2,  komentarz, doc, table,zmienna,rozowy);
						table.flushContent();

						table.flushContent();
				        md.clear();

			}
			
			public String zamien_diff_na_roznica(long diff) {
    		
				String roznica = "";
				if(diff > 0) {
    		
		    		 long diffSeconds = diff / 1000 % 60;
		             long diffMinutes = diff / (60 * 1000) % 60;
		             long diffHours = diff / (60 * 60 * 1000) % 24;
		            
		          
		             if(diffHours > 8 && diffHours <= 15) {
		            	  rozowy  = 1;
		             }
		             else if (diffHours < 7) {
	  			    	   rozowy = 2; // #b5b5fc
	  			       }
		             else if (diffHours > 16)
		            	rozowy = 3; 
	


		             String strHours = Long.toString(diffHours);
		             String strMinutes = Long.toString(diffMinutes);
		             String strSec = Long.toString(diffSeconds);
		             
		              roznica = dodaj_zero(strHours,strMinutes, strSec); 
	                 
    			}
    		return roznica;
			}
			
			public ArrayList<ArrayList<String>> sprawdz_przesianie( ArrayList<ArrayList<String>> md) throws ParseException {
			     ArrayList<ArrayList<String>> temp = new ArrayList<ArrayList<String>>();
		            temp = md; // przypisanie arraya md do tymczasowego arraya
		            // METODA ODPOWIADAJACA ZA PRZESIANIE POWOTRZEN
		            
		    		 for(int y = 1; y < md.size(); y++ ) {
		    			 
		    			Date d1 = sdf.parse(md.get(y-1).get(1));
			    		Date d2 = sdf.parse(md.get(y).get(1));
				       	 long diff_proba = d2.getTime() - d1.getTime();

			    		System.out.println("DIFF proba: "+diff_proba);
			    		System.out.println("y-1: "+d1 + " y: " +d2+ "\n");
				       	 	//przesianie, jezeli roznica jest MNIEJSZA niz 30sekund oraz MUSI byc taki sam typ akcji (wej wej wej itp..), dopiero wtedy dodaj wpis
					    		 if(d2.getTime() > d1.getTime() && diff_proba < 30000 && md.get(y).get(2).equals(md.get(y-1).get(2))) {
					    			 System.out.println("trwa usuwanie rekordu ktory sie powiela\n");
					    			 System.out.println(md.get(y).get(1)+ " ");
					    			//md.remove(y); // usunieto rekord powielany		 
						    		temp.remove(y);
					    		 }
		    		 }
		    		 return temp;
			}
			
			public void najprostsza_opcja(ArrayList<ArrayList<String>> md,String dzien, PdfPTable table,int zmienna, Document doc) throws ParseException {

				  System.out.println(md.get(0).get(1) + "a teraz?: "+  sdf.parse(md.get(0).get(1)));
				  
	    		Date d1 = sdf.parse(md.get(0).get(1));
	    		Date d2 = sdf.parse(md.get(md.size()-1).get(1));
	    		
	    		////////////////////////////
	    		
	    		String wejscie_ostateczne = md.get(0).get(1); // godzina wejscia
				String Wyjscie_ostateczne = md.get(md.size()-1).get(1); // godzina wyjscia
				String komentarz = "-";
	    		
	    		long diff = d2.getTime() - d1.getTime();
	    		
	    		SumaCzasowPracy = SumaCzasowPracy + diff;
	    		

	    		String roznica  = zamien_diff_na_roznica(diff);  	

				dodaj_cell( dzien,  wejscie_ostateczne,  Wyjscie_ostateczne,  roznica,  komentarz, doc, table,zmienna,rozowy);
				 table.flushContent();
			        md.clear();
				
			}
			
				
		}
