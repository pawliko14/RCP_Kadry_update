package WB;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import net.proteanit.sql.DbUtils;

// biblioteki do generowania PDF

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


public class TabelaObecnosci extends JFrame {
	
	private static String USER_PASS = "LISTABHP";

	private static String OWNER_PASS = "IamTheKing";
	
	private Timer timer;
	private Timer timer2;

	private JPanel contentPane;
	private JTable table;
	private ArrayList<String> KartyGosciDoPodmiany = new ArrayList<String>();
	
	private JTable table_1;
	private static boolean czy_kliknieto;
	private int licznik_klikniec;
	private DefaultTableModel model; // model do tabeli z aktualnymi wyjsciami
	
	private List<String> lista_pracownikow;
	private List<String> lista_pracownikow_obecnych;
	private List<String> lista_pracownikow_obecnych_ID;
	private List<String> lista_pracownikow_obecnych_Data;
	private JTable table_2;
	private JButton Folder_button;

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
		setTitle("Tabela obecnosci");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1018, 625);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblMojeMenu = new JLabel("Tabela obecnosci");
		lblMojeMenu.setHorizontalAlignment(SwingConstants.CENTER);
		lblMojeMenu.setFont(new Font("Century", Font.BOLD, 24));
		
		// co jeœli u¿ytkownik jest adminem
		
		if (Login.getAdmin()==true)
		{
		
		}
		
		JLabel lblNewLabel = new JLabel((String) null);
		
		JLabel lblTabelaHistoriiEksportw = new JLabel("Wszyscy pracownicy");
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
		table_1 = new JTable();		
		
	
	
		table_1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
			}
		});
		table_1.setBackground(Color.WHITE);
		table_1.setAutoCreateRowSorter(true);
		table_1.addMouseListener(new MouseAdapter() {

			});
		scrollPane_1.setViewportView(table_1);

/////////////////////
			
		Folder_button = new JButton("Otworz folder");
		Folder_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					Desktop.getDesktop().open(new File("\\\\192.168.90.203\\Common\\BHP"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		Folder_button.setEnabled(false);
	
		
		JButton buttonPodgladTabeli = new JButton("Odswiezyc Tabele");
		czy_kliknieto = false;
		
		
		buttonPodgladTabeli.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				generate( connection);
			
			}
		});
		buttonPodgladTabeli.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JLabel lblObecniPracownicy = new JLabel("Obecni pracownicy");
		lblObecniPracownicy.setHorizontalAlignment(SwingConstants.CENTER);
		lblObecniPracownicy.setFont(new Font("Dialog", Font.BOLD, 18));
		
		JLabel lblPracownikNieobecny = new JLabel("Pracownik nieobecny\r\n");
		
		JButton btnNewButton = new JButton("");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnNewButton.setForeground(Color.RED);
		btnNewButton.setBackground(Color.RED);
		btnNewButton.setEnabled(false);
		
		JButton Export_button = new JButton("Export PDF");
		Export_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					exportPdf();
					Folder_button.setEnabled(true);
					JOptionPane.showMessageDialog(null, "Wyexportowano");
				} catch (FileNotFoundException | DocumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		JScrollPane scrollPane_2 = new JScrollPane();
		
		JLabel lblOstanieWejscia = new JLabel("Ostanie Wejscia");
		lblOstanieWejscia.setHorizontalAlignment(SwingConstants.CENTER);
		lblOstanieWejscia.setFont(new Font("Dialog", Font.BOLD, 18));
		

		
		
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(39)
							.addComponent(lblMojeMenu, GroupLayout.DEFAULT_SIZE, 943, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblNewLabel))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 362, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 203, GroupLayout.PREFERRED_SIZE)
							.addGap(48)
							.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(buttonPodgladTabeli, GroupLayout.DEFAULT_SIZE, 972, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(Export_button)
							.addGap(18)
							.addComponent(Folder_button)
							.addPreferredGap(ComponentPlacement.RELATED, 599, Short.MAX_VALUE)
							.addComponent(btnNewButton)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblPracownikNieobecny, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(104)
					.addComponent(lblObecniPracownicy)
					.addGap(127)
					.addComponent(lblTabelaHistoriiEksportw)
					.addGap(153)
					.addComponent(lblOstanieWejscia, GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
					.addGap(72))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(Export_button)
							.addComponent(Folder_button))
						.addComponent(lblPracownikNieobecny))
					.addGap(7)
					.addComponent(lblMojeMenu, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel)
						.addComponent(buttonPodgladTabeli, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblTabelaHistoriiEksportw, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblObecniPracownicy, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblOstanieWejscia, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
					.addGap(13)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
							.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)))
					.addContainerGap())
		);
		
		table_2 = new JTable();
		scrollPane_2.setViewportView(table_2);
		
		contentPane.setLayout(gl_contentPane);	
		
		
		class RemindTask extends TimerTask {
	        public void run() {
				generate( connection);

	        }
	    }
		
		timer = new Timer();
		timer.schedule(new RemindTask(), 1000, 40000);
		
		/////////////////
		
		class RemindTask2 extends TimerTask {
	        public void run() {
				last200rekords(connection);

	        }
	    }
		
		timer2 = new Timer();
		timer2.schedule(new RemindTask2(), 1000, 15000);
		
		/////
		
		}
	
	public void last200rekords(Connection connection )
	{
		ArrayList<String> listaKartZablokowanych = new ArrayList<String>();
		ArrayList<String> listaKartPoOdswiezeniu = new ArrayList<String>();
		
		try {
			String query="SELECT access.id_karty, access.akcja, access.data, cards_name_surname_nrHacoSoft.nazwisko_imie FROM"
					+ " access LEFT OUTER JOIN cards_name_surname_nrHacoSoft ON access.id_karty=cards_name_surname_nrHacoSoft.id_karty LEFT OUTER JOIN keys_permissions ON access.id_karty=keys_permissions.id_karty "
					+ "ORDER BY access.data desc LIMIT 200";
			PreparedStatement pst=connection.prepareStatement(query);
			ResultSet rs=pst.executeQuery();
			table_2.setModel(DbUtils.resultSetToTableModel(rs));
			pst.close();
			rs.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<String> Porownaj_listy()
	{
	
        Collection<String> similar = new HashSet<String>( lista_pracownikow );
        Collection<String> different = new HashSet<String>();
        different.addAll( lista_pracownikow );
        different.addAll( lista_pracownikow_obecnych );

        similar.retainAll( lista_pracownikow_obecnych );
        different.removeAll( similar );
                      
        List theList = new ArrayList(different);
        return theList;

	}
	
	// Tabela WSZYSTKICH PRACOWNIKOW ( na podstawie ostatnich 3 miesiecy)
	public void ShowTable(Connection connection, JTable table)
	{
		
		
		// arraye ktore nie sa uzywane, trzeba sprawdzic pozniej do czego to powinnno sluzyc
		
		//ArrayList<String> listaKartGosci = new ArrayList<String>();
		//ArrayList<String> listaKartZTemporary2Export = new ArrayList<String>();
	

					// wyswietla tabele
					// jesli nie okreslono nr karty, to zwroci wszystkie, a jesli okreslono, to tylko wyniki dla danej karty
						try {
							// zamiana z dnia 24.06.2019 zeby zapytanie bylo bardziej dokladne					

							
							String query = "select fat.cards_name_surname_nrhacosoft.nazwisko_imie\r\n" + 
									"from fat.access\r\n" + 
									"left join fat.cards_name_surname_nrhacosoft\r\n" + 
									"on fat.access.id_karty = fat.cards_name_surname_nrhacosoft.id_karty\r\n" + 
									"where fat.cards_name_surname_nrhacosoft.nazwisko_imie like '%%'\r\n" + 
									"and fat.access.`data` between  DATE_ADD(now(), INTERVAL -40 day) and now()\r\n" + 
									"and fat.cards_name_surname_nrhacosoft.nazwisko_imie not like '%GOSC%'\r\n" + 
									"group by fat.access.id_karty\r\n" + 
									"order by fat.cards_name_surname_nrhacosoft.nazwisko_imie asc";
							
							//String query="SELECT cards_name_surname_nrhacosoft.nazwisko_imie, access.id_karty, access.akcja, access.data FROM access LEFT JOIN cards_name_surname_nrhacosoft ON access.id_karty=cards_name_surname_nrhacosoft.id_karty WHERE access.data BETWEEN '"+dataODString+"' AND '"+dataDOString+"' AND access.akcja<>'nowy_pracownik'";
							PreparedStatement pst=connection.prepareStatement(query);
							ResultSet rs=pst.executeQuery();
							table.setModel(DbUtils.resultSetToTableModel(rs));
							
							table.getColumnModel().getColumn(0).setPreferredWidth(100);

					    //    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

							
							pst.close();
							rs.close();
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}			
	
	

public void ShowAnotherTable(Connection connection, JTable table_1)
{	
				// jesli nie okreslono nr karty, to zwroci wszystkie, a jesli okreslono, to tylko wyniki dla danej karty
			
	lista_pracownikow = new ArrayList<String>(); // wszyscy pracownicy z ostatnich 40 dni
	lista_pracownikow_obecnych = new ArrayList<String>(); 	// pracownicy ktorzy aktualnie sa w FAT
	lista_pracownikow_obecnych_ID = new ArrayList<String>(); 
	lista_pracownikow_obecnych_Data = new ArrayList<String>(); 
	
					try {
	/*								
						String query = "select  fat.access.id_karty, Substring(fat.access.`data`,11,17) as \"godzina wejscia\", \r\n" + 
								"fat.cards_name_surname_nrhacosoft.nazwisko_imie\r\n" + 
								"from fat.access\r\n" + 
								"left join fat.cards_name_surname_nrhacosoft\r\n" + 
								"on fat.access.id_karty = fat.cards_name_surname_nrhacosoft.id_karty\r\n" + 
								"where fat.cards_name_surname_nrhacosoft.nazwisko_imie like '%%'\r\n" + 
								"and fat.access.`data` like concat(curdate(), \"%\")\r\n" + 
								"and fat.access.akcja = 'wejscie'\r\n" + 
								"group by id_karty";
			
			*/
				
						/* query ktore wybiera wszystkich ludzi ktorzy wchodzili od ostatnich 40 - rzeczywisci pracownicy */
						
						String query = "select  fat.cards_name_surname_nrhacosoft.nazwisko_imie\r\n" + 
								"from fat.access\r\n" + 
								"left join fat.cards_name_surname_nrhacosoft\r\n" + 
								"on fat.access.id_karty = fat.cards_name_surname_nrhacosoft.id_karty\r\n" + 
								"where fat.cards_name_surname_nrhacosoft.nazwisko_imie like '%%'\r\n" + 
								"and fat.access.`data` between  DATE_ADD(now(), INTERVAL -40 day) and now()\r\n" + 
								"and fat.cards_name_surname_nrhacosoft.nazwisko_imie not like '%GOSC%'\r\n" + 
								"group by fat.access.id_karty\r\n" + 
								"order by fat.cards_name_surname_nrhacosoft.nazwisko_imie";
						
						
						
						//String query="SELECT cards_name_surname_nrhacosoft.nazwisko_imie, access.id_karty, access.akcja, access.data FROM access LEFT JOIN cards_name_surname_nrhacosoft ON access.id_karty=cards_name_surname_nrhacosoft.id_karty WHERE access.data BETWEEN '"+dataODString+"' AND '"+dataDOString+"' AND access.akcja<>'nowy_pracownik'";
						PreparedStatement pst=connection.prepareStatement(query);
						ResultSet rs=pst.executeQuery();
						
						while(rs.next())
						{							
							String imie = rs.getString(1);
							lista_pracownikow.add(imie);
						}
						  
					     
					   List<String> ListaTestowa = new ArrayList<String>();  
					   	int index = 0;
					   	
					   	 model = (DefaultTableModel) table_1.getModel();
					   	
					   	// usunicie poprzednich rekordow jesli bylo wiecej klikniec:
					   	int rows = model.getRowCount(); 
					   	for(int i = rows - 1; i >=0; i--)
					   	{
					   	   model.removeRow(i); 
					   	}
					   	
					   	if(czy_kliknieto == true) // tylko jedno klikniecie - pierwsze sie liczy
					   	{
						    
						 // Create a couple of columns 
						    model.addColumn("ID");
							model.addColumn("ID-karty"); 
							model.addColumn("Data wejscia"); 
							model.addColumn("Nazwisko"); 
							
							table_1.getColumnModel().getColumn(0).setPreferredWidth(15);
							table_1.getColumnModel().getColumn(1).setPreferredWidth(40);
							table_1.getColumnModel().getColumn(2).setPreferredWidth(40);
							table_1.getColumnModel().getColumn(3).setPreferredWidth(160);
					  	}
					   	
					     for(int i = 0 ; i <lista_pracownikow.size();i++)
					     {
							 //   System.out.println("duppa: " + lista_pracownikow.get(i)); 

					    	 
					    	 String query2 = "select  fat.access.id_karty, fat.access.akcja, SUBSTRING(fat.access.`data`, 12, 9),\r\n" + 
					    	 		"fat.cards_name_surname_nrhacosoft.nazwisko_imie\r\n" + 
					    	 		"from fat.access\r\n" + 
					    	 		"left join fat.cards_name_surname_nrhacosoft\r\n" + 
					    	 		"on fat.access.id_karty = fat.cards_name_surname_nrhacosoft.id_karty\r\n" + 
					    	 		"where fat.cards_name_surname_nrhacosoft.nazwisko_imie = '"+lista_pracownikow.get(i)+"'\r\n" + 
					    	 		"and `data` like concat(curdate() , '%')\r\n" + 
					    	 		"order by fat.access.`data` desc limit 1";
					    	 
					    	 
					    	 
								PreparedStatement pst2=connection.prepareStatement(query2);
								ResultSet rs2=pst2.executeQuery();
								int problem = 0 ;
								
								while(rs2.next())
								{		
								 //   System.out.println("DZIALA"); 

									String id_karty = rs2.getString(1);
									String akcja = rs2.getString(2);
									String data = rs2.getString(3);
									String nazwisko = rs2.getString(4);

									if(akcja.equals("wejscie"))
									{
										ListaTestowa.add(nazwisko);
										index++;
																			
										// Append a row 
										model.addRow(new Object[]{index,id_karty,data,nazwisko});
																				
										lista_pracownikow_obecnych.add(nazwisko); // dodanie nazwiska do listy 
										lista_pracownikow_obecnych_ID.add(id_karty);
										lista_pracownikow_obecnych_Data.add(data);

									}	

		
								}

					     }
												
						pst.close();
						rs.close();
															
						
					} catch (Exception e) {
						e.printStackTrace();
					}						
}

public void exportPdf() throws FileNotFoundException, DocumentException
{
	// funkcja ma wyexportowac dane o aktualnej ilosci pracownikow do folderu na serwerze, 
	// gdzie koles z BHP bedzie mogl sobie te dane zobaczyc
	DateFormat dateFormat0 = new SimpleDateFormat("dd-MM-YYYY");
	Date date0 = new Date();
	
	File theDir = new File("\\\\192.168.90.203\\Common\\BHP\\" + dateFormat0.format(date0).toString());
	// if the directory does not exist, create it
	if (!theDir.exists()) {
	    try{
	        theDir.mkdir();
	    } 
	    catch(SecurityException se){
	        //handle it
	    	System.out.println("Blad w tworzeniu folderu z listami");
	    }  
	}
	
	
	// funkcja ma wyexportowac dane o aktualnej ilosci pracownikow do folderu na serwerze, 
	// gdzie koles z BHP bedzie mogl sobie te dane zobaczyc
	
	DateFormat dateFormat = new SimpleDateFormat("HH;mm;ss");
	Date date = new Date();
	//System.out.println(dateFormat.format(date)); //12:08:43
	
	
	String destination_file = "Raport_Obecnosci_export ";
	
//	String DEST = "\\\\192.168.90.203\\Common\\BHP\\"+destination_file + dateFormat.format(date).toString() +".pdf";

	String DEST = theDir+"\\"+destination_file + dateFormat.format(date).toString() +".pdf";
	   Document doc  = new Document();
		PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(DEST));
	//   PdfWriter.getInstance(doc, new FileOutputStream(DEST));
	   
	   //zabezpiecznie haslem	   
	   
		writer.setEncryption(USER_PASS.getBytes(), OWNER_PASS.getBytes(),PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128);
	   
	   doc.open();
	   doc.add(new Paragraph("Wykaz pracownikow, ktorzy sa obecni w firmie",FontFactory.getFont(FontFactory.TIMES_ROMAN,11, Font.ITALIC, BaseColor.BLACK)));
	   doc.add(new Paragraph("\n"));
	   doc.add(new Paragraph("Raport dla dnia:",FontFactory.getFont(FontFactory.TIMES_ROMAN,11, Font.ITALIC, BaseColor.BLACK)));
	   doc.add(new Paragraph(new Date().toString(),FontFactory.getFont(FontFactory.TIMES_ROMAN,11, Font.ITALIC, BaseColor.BLACK)));
	   doc.add(new Paragraph("\n"));
	
	   
	      PdfPTable table = new PdfPTable(4);
	      table.setWidths(new float[] {1,2,2,5}); // relatywne szerokosci - 1 podstawa - 2 2razy wiekszy - 5 5razy wiekszy

	        // header row:
	        table.addCell("Nr");
	        table.addCell("ID-karty");
	        table.addCell("godzina wejscia");
	        table.addCell("Pracownik");

	        table.setHeaderRows(1);
	        
	    	PdfPCell table_cell0;
	    	PdfPCell table_cell1;
	        PdfPCell table_cell2;
	        PdfPCell table_cell3;

	        
	        int c1= 255, c2=255, c3=255;
	        
	
	        for(int i = 0 ; i < lista_pracownikow_obecnych.size();i++)
	        {
	        	table_cell0 = new PdfPCell(new Phrase(Integer.toString(i+1)));
	        	table_cell0.setBackgroundColor(new BaseColor(c1, c2, c3)); 
	
	            table.addCell(table_cell0);
	        	
				table_cell1 = new PdfPCell(new Phrase(lista_pracownikow_obecnych_ID.get(i)));
				table_cell1.setBackgroundColor(new BaseColor(c1, c2, c3)); 
	
	            table.addCell(table_cell1);
	            
	
	            table_cell2 = new PdfPCell(new Phrase(lista_pracownikow_obecnych_Data.get(i)));
				table_cell2.setBackgroundColor(new BaseColor(c1, c2, c3)); 
	
	            table.addCell(table_cell2);
	              
	            
	            table_cell3= new PdfPCell(new Phrase(lista_pracownikow_obecnych.get(i)));
				table_cell3.setBackgroundColor(new BaseColor(c1, c2, c3)); 
	
	            table.addCell(table_cell3);
	        }
            
            try {
       				doc.add(table);
       			} catch (DocumentException e) {
       				// TODO Auto-generated catch block
       				e.printStackTrace();
       			}
	   
	   doc.close();
	
}

public void generate(Connection connection)
{
	licznik_klikniec++;
	if(licznik_klikniec == 1)
	{
		czy_kliknieto = true;
	}
	else
	{
		czy_kliknieto = false;
	}
	ShowAnotherTable(connection,table_1);
	ShowTable(connection, table);
	
	final List<String> roznica = Porownaj_listy();
	
	
	
	table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
	    @Override
	    public Component getTableCellRendererComponent(JTable table,
	            Object value, boolean isSelected, boolean hasFocus, int row, int col) {
	
	        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
	        
	       // mainIndex++;
	        String status = (String)table.getModel().getValueAt(row, 0);
	        boolean prawda = false;
	        for(int i = 0 ; i < roznica.size();i++)
	        {
	        	
	        	if(roznica.get(i).equals(status)) 
	        	{
	        		prawda = true;
	        	}
	        }
	         						        	  
	        	  // wykrzaczy bo bedzie niezgodnosc w ilosci danych
		        if (prawda){
		        	   setBackground(Color.RED);
			            setForeground(Color.WHITE);
		        } else 
		        {
		        	setBackground(table.getBackground());
			        setForeground(table.getForeground());		                
		        }
		        table.revalidate(); // Repaint all the component (all Cells).
		        
	        return this;
	    }   
	});
	
	}
}








