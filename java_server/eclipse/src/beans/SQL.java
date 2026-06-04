package beans;
import java.sql.*;

public class SQL {
	
	public static Connection connect(String database) throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");

		Connection SQLSession = DriverManager.getConnection(AppConfig.dbUrl(database), AppConfig.dbUser(), AppConfig.dbPassword());
		
		return SQLSession;
		
	}
	
	public static void disconnect(Connection SQLSession) throws SQLException {
		
		SQLSession.close();
		
		SQLSession=null;
		
	}
	
	
}
