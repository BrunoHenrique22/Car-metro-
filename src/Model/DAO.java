package Model;

import java.sql.Connection;
import java.sql.DriverManager;

public class DAO {
//Processo e ligar o banco de dados ao app aq no java
	
	//Variáveis para setar o banco de dados
	private String driver = "com.mysql.cj.jdbc.Driver";
	private String url = "jdbc:mysql://10.26.49.103:3306/carometro";
	private String user = "dba"; 
	private String password = "123@senac"; 
	
	
	//objeto (JDBC) usado para conectar o banco
	private Connection con; 
	
	/**
	 * Conexão
	 * @return con
	 */
	
	public Connection conectar() {
		try {
			
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);
			return con;
		} catch (Exception e) {
			System.out.println(e);
			return null; 
		}
	}
}
