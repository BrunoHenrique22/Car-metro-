package View;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Model.DAO;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.Cursor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Main extends JFrame {

	private JPanel contentPane;
	private JLabel lblStatus;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
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
	public Main() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				status();
				data();
			}
		});
		setTitle("Carômetro");
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/img/favicon.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 442, 321);
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(Color.RED);
		panel.setBounds(0, 225, 426, 57);
		contentPane.add(panel);
		panel.setLayout(null);

		lblStatus = new JLabel("");
		lblStatus.setIcon(new ImageIcon(Main.class.getResource("/img/dboff.png")));
		lblStatus.setForeground(Color.WHITE);
		lblStatus.setBounds(384, 13, 32, 32);
		panel.add(lblStatus);

		lblData = new JLabel("");
		lblData.setFont(new Font("Arial", Font.PLAIN, 12));
		lblData.setForeground(Color.BLACK);
		lblData.setBounds(22, 13, 205, 32);
		panel.add(lblData);

		JButton btnAdd = new JButton("");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Cadastro cadastro = new Cadastro();
				cadastro.setVisible(true);
			}
		});
		btnAdd.setToolTipText("Adicionar Foto");
		btnAdd.setBackground(Color.BLACK);
		btnAdd.setIcon(new ImageIcon(Main.class.getResource("/img/foto.png")));
		btnAdd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnAdd.setBounds(29, 35, 128, 128);
		contentPane.add(btnAdd);

		JButton btnCarometro = new JButton("");
		btnCarometro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Carometro carometro = new Carometro();
				carometro.setVisible(true);
			}
		});
		btnCarometro.setIcon(new ImageIcon(Main.class.getResource("/img/students.png")));
		btnCarometro.setToolTipText("Carômetro");
		btnCarometro.setBackground(Color.BLACK);
		btnCarometro.setBounds(235, 35, 128, 128);
		contentPane.add(btnCarometro);
	}// Fim do Construtor

	DAO dao = new DAO();
	private JLabel lblData;

	private void status() {
		try {

			Connection con = dao.conectar();
			if (con == null) {
				lblStatus.setIcon(new ImageIcon(Main.class.getResource("/img/dboff.png")));

			} else {
				lblStatus.setIcon(new ImageIcon(Main.class.getResource("/img/dbon.png")));

			}
			con.close();

		} catch (Exception e) {
			System.out.println(e);
		}
	}// Fim do Status

	private void data() {
		Date data = new Date();
		DateFormat formatador = DateFormat.getDateInstance(DateFormat.FULL);
		lblData.setText(formatador.format(data));

	}
}// Fim Da Vida
