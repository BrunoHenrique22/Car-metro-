package View;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.mysql.cj.jdbc.Blob;

import Model.DAO;

public class Carometro extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtId;
	private JTextField txtAluno;
	private JLabel lblFoto;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Carometro dialog = new Carometro();
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
	public Carometro() {
		setModal(true);
		getContentPane().setBackground(Color.BLACK);
		setTitle("Carômetro - Alunos");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Carometro.class.getResource("/img/favicon.png")));
		setBounds(100, 100, 488, 411);
		getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("ID:");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setBounds(10, 31, 25, 14);
		getContentPane().add(lblNewLabel);

		JLabel lblAlunoa = new JLabel("Aluno(a):");
		lblAlunoa.setForeground(Color.WHITE);
		lblAlunoa.setBounds(108, 31, 54, 14);
		getContentPane().add(lblAlunoa);

		txtId = new JTextField();
		txtId.setBounds(33, 28, 55, 20);
		getContentPane().add(txtId);
		txtId.setColumns(10);

		txtAluno = new JTextField();
		txtAluno.setHorizontalAlignment(SwingConstants.LEFT);
		txtAluno.setColumns(10);
		txtAluno.setBounds(162, 28, 163, 20);
		getContentPane().add(txtAluno);

		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buscar();
			}
		});
		btnBuscar.setBounds(357, 27, 89, 23);
		getContentPane().add(btnBuscar);

		lblFoto = new JLabel("");
		lblFoto.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblFoto.setBounds(108, 93, 256, 256);
		getContentPane().add(lblFoto);

		btnDeletar = new JButton("Deletar ");
		btnDeletar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deletar();
			}
		});
		btnDeletar.setBounds(373, 325, 89, 23);
		getContentPane().add(btnDeletar);

		btnDeletar = new JButton("Deletar");
		btnDeletar.setBounds(374, 326, 89, 23);
		getContentPane().add(btnDeletar);

	}// Fim do Construtor

	DAO dao = new DAO();
	private JButton btnDeletar;

	private void buscar() {
		String read = "select * from alunos where nome =?";

		try {
			Connection con = dao.conectar();
			PreparedStatement pst = con.prepareStatement(read);
			pst.setString(1, txtAluno.getText());
			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				txtId.setText(rs.getString(1));

				// Ler o Binário e converter para imagens
				Blob blob = (Blob) rs.getBlob(3);

				// Nesse caso é necessario criar um vetor (array) para poder armazenar os bytes
				// da imagem, se converte a int pois é numero inteiro.
				byte[] img = blob.getBytes(1, (int) blob.length());

				// "papel" que vai "imprimir a imagem
				BufferedImage imagem = null;

				try {
					// renderizar a imagem (desenhar a foto (pixels) no papel
					imagem = ImageIO.read(new ByteArrayInputStream(img));

				} catch (Exception e) {
					System.out.println(e);
				}

				// setar a imagem no JLabel
				ImageIcon icone = new ImageIcon(imagem);
				Icon foto = new ImageIcon(icone.getImage().getScaledInstance(lblFoto.getWidth(), lblFoto.getHeight(),
						Image.SCALE_SMOOTH));
				lblFoto.setIcon(icone);

			} else {
				JOptionPane.showMessageDialog(null, "Aluno(a) Não Foi Cadastrado!!!");

			}

			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}

	}// FIM DO BUSCAR A FOTO

	private void deletar() {
		int confirma = JOptionPane.showConfirmDialog(null, "Deseja Excluir Esse Aluno(a)", "Excluir Aluno(a)!!",
				JOptionPane.YES_NO_OPTION);

		if (confirma == JOptionPane.YES_NO_OPTION) {

			String delete = "delete from alunos where id = ?";

			try {

				Connection con = dao.conectar();

				PreparedStatement pst = con.prepareStatement(delete);
				pst.setString(1, txtId.getText());

				int confirmaExcluir = pst.executeUpdate();

				if (confirmaExcluir == 1) {
					JOptionPane.showMessageDialog(null, "Aluno Excluido");
					limpar();
				}

				con.close();

			} catch (Exception e) {
				System.out.println(e);

			}

		}

	}// FIM DO EXCLUIR

	private void limpar() {
		txtId.setText(null);
		txtAluno.setText(null);
		// para limpar imagem
		lblFoto.setIcon(null);

	}
}
// FIM DA VIDA
