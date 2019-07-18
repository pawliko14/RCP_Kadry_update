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

public class MenuAdmin extends JFrame {

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
					MenuAdmin frame = new MenuAdmin(connection);
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
	public MenuAdmin(final Connection connection) {
		
		Image img = new ImageIcon(this.getClass().getResource("/BackgroundImage.jpg")).getImage();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Menu");
		setBounds(100, 100, 408, 605);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblMenu = new JLabel("Menu");
		lblMenu.setHorizontalAlignment(SwingConstants.CENTER);
		lblMenu.setFont(new Font("Century", Font.BOLD, 24));
		
		JButton btnNewButton = new JButton("Wy\u015Bwietl ostatnie wej\u015Bcia/wyj\u015Bcia");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OstatnieWejsciaWyjscia500 okno = new OstatnieWejsciaWyjscia500(connection);
				okno.setVisible(true);
			}
		});
		btnNewButton.setToolTipText("Wy\u015Bwietla tabel\u0119 ostatnich 500 przej\u015B\u0107 przez bramk\u0119");
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JButton btnDodajUsunKarte = new JButton("Dodaj/usu\u0144 kart\u0119");
		btnDodajUsunKarte.setToolTipText("<html>Pozwala doda\u0107 now\u0105 kart\u0119 pracownikowi lub usun\u0105\u0107 star\u0105 kart\u0119 (UWAGA: je\u015Bli pracownik zgubi\u0142 kart\u0119, nale\u017Cy j\u0105 zablokowa\u0107, nie usuwa\u0107!)</html>");
		btnDodajUsunKarte.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DaneKarty okno = new DaneKarty(connection);
				okno.setVisible(true);
			}
		});
		btnDodajUsunKarte.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JButton btnZablokujodblokujKart = new JButton("Zablokuj/odblokuj kart\u0119");
		btnZablokujodblokujKart.setToolTipText("Pozwala zablokowa\u0107/odblokowa\u0107 kart\u0119. Zablokowane karty spowoduj\u0105 w\u0142\u0105czenie alarmu na portierni");
		btnZablokujodblokujKart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ZablokujOdblokujKarte okno = new ZablokujOdblokujKarte(connection);
				okno.setVisible(true);
			}
		});
		btnZablokujodblokujKart.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JButton btnNadajodbierzPrawoDo = new JButton("Nadaj/odbierz prawo do pobrania kluczy");
		btnNadajodbierzPrawoDo.setToolTipText("Pozwala okre\u015Bli\u0107 jakie klucze mo\u017Ce pobra\u0107 dany pracownik. Opcja mo\u017Ce by\u0107 te\u017C wykorzystana do innych wiadomo\u015Bci nt. pracownika");
		btnNadajodbierzPrawoDo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NadajOdbierzPrawoDoKluczy okno = new NadajOdbierzPrawoDoKluczy(connection);
				okno.setVisible(true);
			}
		});
		btnNadajodbierzPrawoDo.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JButton btnPodmieKartGocia = new JButton("Eksport do CSV");
		btnPodmieKartGocia.setToolTipText("Umo\u017Cliwia eksport pliku tekstowego akceptowanego przez Symphoni\u0119");
		btnPodmieKartGocia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EksportDoCSV3 okno = new EksportDoCSV3(connection);
				okno.setVisible(true);
			}
		});
		btnPodmieKartGocia.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JButton btnOznaczKartJako = new JButton("Oznacz kart\u0119 jako kart\u0119 go\u015Bcia");
		btnOznaczKartJako.setToolTipText("Pozwala oznaczy\u0107 kart\u0119 jako kart\u0119 go\u015Bcia");
		btnOznaczKartJako.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OznaczOdznaczKarteGoscia okno = new OznaczOdznaczKarteGoscia(connection);
				okno.setVisible(true);
			}
		});
		btnOznaczKartJako.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JButton btnWywietlWszystkieWejciawyjcia = new JButton("Wy\u015Bwietl wszystkie wej\u015Bcia/wyj\u015Bcia");
		btnWywietlWszystkieWejciawyjcia.setToolTipText("Wy\u015Bwietla tabel\u0119 wszystkich wej\u015B\u0107 i wyj\u015B\u0107 przez bramk\u0119 ");
		btnWywietlWszystkieWejciawyjcia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WszystkieWejsciaWyjscia okno = new WszystkieWejsciaWyjscia(connection);
				okno.setVisible(true);
			}
		});
		btnWywietlWszystkieWejciawyjcia.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JButton btnPointlessButton = new JButton("Podgl\u0105d tabel");
		btnPointlessButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PodgladTabel okno = new PodgladTabel(connection);
				okno.setVisible(true);
			}
		});
		btnPointlessButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JButton btnTabelaObecnoci = new JButton("Tabela obecno\u015Bci");
		btnTabelaObecnoci.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TabelaObecnosci okno = new TabelaObecnosci(connection);
				okno.setVisible(true);
			}
		});
		btnTabelaObecnoci.setToolTipText("Wy\u015Bwietla kto by\u0142 na zak\u0142adzie we wskazanym przedziale czasu");
		btnTabelaObecnoci.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JButton btnTabelaSugerowanychSp = new JButton("Tabela sugerowanych spoznionych");
		btnTabelaSugerowanychSp.setToolTipText("Pokazuje wejscia z wczoraj i dzisiaj, ktore sa pomiedzy 5:51-6:00, 6:21-6:30, 6:51-7:00, 7:21-7:30, 7:51-8:00");
		btnTabelaSugerowanychSp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TabelaSugerowanychSpoznionych okno = new TabelaSugerowanychSpoznionych(connection);
				okno.setVisible(true);
			}
		});
		btnTabelaSugerowanychSp.setFont(new Font("Tahoma", Font.BOLD, 12));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblMenu, GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
						.addComponent(btnWywietlWszystkieWejciawyjcia, GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
						.addComponent(btnOznaczKartJako, GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
						.addComponent(btnNadajodbierzPrawoDo, GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
						.addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE))
					.addGap(8))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(8)
					.addComponent(btnPodmieKartGocia, GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnZablokujodblokujKart, GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
					.addGap(8))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(8)
					.addComponent(btnDodajUsunKarte, GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnTabelaObecnoci, GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnTabelaSugerowanychSp, GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnPointlessButton, GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
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
					.addComponent(btnPodmieKartGocia, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnDodajUsunKarte, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNadajodbierzPrawoDo, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnZablokujodblokujKart, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnOznaczKartJako, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnWywietlWszystkieWejciawyjcia, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnTabelaObecnoci, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnTabelaSugerowanychSp, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnPointlessButton, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(18, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(-420, 0, 893, 621);
		lblNewLabel.setIcon(new ImageIcon(img));
		contentPane.add(lblNewLabel);
	}
}
