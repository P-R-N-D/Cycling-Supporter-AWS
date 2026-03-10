package beans;
import java.sql.*;

public class SQL {
	
	public static Connection connect(String database) throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection SQLSession = DriverManager.getConnection("jdbc:mysql://sql.ccjb4squbrhs.ap-northeast-2.rds.amazonaws.com:3306/" + database + "?serverTimezone=Asia/Seoul", "root", "1Shot1Kill.");
		
		return SQLSession;
		
	}
	
	public static void disconnect(Connection SQLSession) throws SQLException {
		
		SQLSession.close();
		
		SQLSession=null;
		
	}
	
	
}
