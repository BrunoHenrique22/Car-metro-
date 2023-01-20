package View;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import Model.DAO;

public class Cadastro extends JDialog {
	private JTextField txtAluno;
	private JLabel lblFoto;

	// Criar um objeto (global) para obter o fluxo de bytes (imagem)
	// FileInputStream -> Classe Modelo responsálvel pela entrada de dados binários
	private FileInputStream fis;
	// Criar uma variável global para armazenar o tamanho de bytes da imagem
	private int tamanho;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Cadastro dialog = new Cadastro();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the dialog.
	 */
	public Cadastro() {
		setTitle("Carômetro - Cadastrar Aluno");
		setModal(true);
		getContentPane().setBackground(Color.BLACK);
		getContentPane().setLayout(null);

		JLabel lblAluno = new JLabel("Aluno(a):");
		lblAluno.setForeground(Color.WHITE);
		lblAluno.setBounds(34, 34, 52, 14);
		getContentPane().add(lblAluno);

		txtAluno = new JTextField();
		txtAluno.setBounds(101, 31, 252, 20);
		getContentPane().add(txtAluno);
		txtAluno.setColumns(10);

		JButton btnSelecionar = new JButton("Selecionar Foto");
		btnSelecionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selecionar();
			}
		});
		btnSelecionar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnSelecionar.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnSelecionar.setBounds(159, 82, 131, 23);
		getContentPane().add(btnSelecionar);

		lblFoto = new JLabel("");
		lblFoto.setBorder(new LineBorder(Color.WHITE));
		lblFoto.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblFoto.setBounds(97, 142, 256, 256);
		getContentPane().add(lblFoto);

		JButton btnSalvar = new JButton("");
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				salvar();
			}
		});
		btnSalvar.setBorder(null);
		btnSalvar.setBackground(Color.BLACK);
		btnSalvar.setIcon(new ImageIcon(Cadastro.class.getResource("/img/save.png")));
		btnSalvar.setToolTipText("Salvar");
		btnSalvar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnSalvar.setAlignmentX(0.5f);
		btnSalvar.setBounds(373, 338, 64, 64);
		getContentPane().add(btnSalvar);
		setIconImage(Toolkit.getDefaultToolkit().getImage(Cadastro.class.getResource("/img/favicon.png")));
		setBounds(100, 100, 484, 504);

	}// Fim do Construtor

	DAO dao = new DAO();

	private void selecionar() {

		// JFileChooser -> classe modelo que gera um explorador de arquivos
		JFileChooser jfc = new JFileChooser();
		// A Linha abaixo modifica o título do explorador de arquivos
		jfc.setDialogTitle("Selecionar Arquivo");
		// A linha abaixo cria um filtro para escolher determminados tipos de arquivos.
		jfc.setFileFilter(new FileNameExtensionFilter("Arquivo de imagens (*.PNG,*.JPG,*.JPEG", "png", "jpg", "jpeg"));
		// showOpenDialog(this); -> ele abre o explorador de arquivos
		// int resultado -> serve para saber se o usúario selecionou o arquivo ou não.
		int resultado = jfc.showOpenDialog(this);
		// se o usuário escolher uma opção, setar a JLabel
		if (resultado == JFileChooser.APPROVE_OPTION) {

			// tratamento de exceção
			try {
				// A Linha abaixo "pega" o arquivo
				fis = new FileInputStream(jfc.getSelectedFile());
				// a linha abaixo obtém o tamanho do arquivo
				tamanho = (int) jfc.getSelectedFile().length();
				// convertendo o arquivo e setando a largura e altura (preencher o espaço
				// completo da imagem)
				Image foto = ImageIO.read(jfc.getSelectedFile()).getScaledInstance(lblFoto.getWidth(),
						lblFoto.getHeight(), Image.SCALE_SMOOTH);
				// setar a JLabel com a Foto
				lblFoto.setIcon(new ImageIcon(foto));
				lblFoto.updateUI();

			} catch (Exception e) {
				System.out.println(e);

			}
		}
	}// Fim DO Selecionar Foto

	private void salvar() {

		if (txtAluno.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Preencha o Nome Do(a) Aluno(a)");
			txtAluno.requestFocus();

		} else {
			String insert = "insert into alunos (nome,foto) values (?,?)";

			try {
				Connection con = dao.conectar();
				PreparedStatement pst = con.prepareStatement(insert);
				pst.setString(1, txtAluno.getText());

				// setar o banco de dados com imagens (poder armazenar coisas lá)
				//fis (arquivo de imagem no formato binário)
				//tamanho (tamanho da imagem em bytes) 
				pst.setBlob(2, fis, tamanho);
				int confirma = pst.executeUpdate();

				if (confirma == 1) {
					JOptionPane.showMessageDialog(null, "Aluno(a) Cadastrado Com Sucesso!!");
					limpar();

				} else {
					JOptionPane.showMessageDialog(null, "Aluno(a) Não Foi Cadastrado!!!");
					limpar();

					con.close();

				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}

	}// Fim do Salvar

	private void limpar() {
		txtAluno.setText(null);
		// para limpar imagem
		lblFoto.setIcon(null);
	}
}// FIM DA VIDA
