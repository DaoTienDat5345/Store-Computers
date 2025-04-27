package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class QLdatabase {
	private static final String DB_ServerName = "DESKTOP-BSBAAI0";
	private static final String DB_login = "sa";
	private static final String DB_password = "nhan123";
	private static final String DB_DatabaseName = "ComputerStores";
	
	public static Connection getConnection() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String DB_url = "jdbc:sqlserver://" + DB_ServerName + ":1433;databaseName=" + DB_DatabaseName + 
	                ";encrypt=true;trustServerCertificate=true";
			return DriverManager.getConnection(DB_url, DB_login, DB_password);
			} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
				e.printStackTrace();
			}
		
		return null;
	}

	public Connection connect() {
		// TODO Auto-generated method stub
		return null;
	}
}
