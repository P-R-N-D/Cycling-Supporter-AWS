package beans;
import java.sql.*;

public final class SQLDelete {
	
	private static Connection SQLSession=null;
	
	private static void connect(String database) throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		SQLSession = DriverManager.getConnection("jdbc:mysql://sql.ccjb4squbrhs.ap-northeast-2.rds.amazonaws.com:3306/" + database + "?serverTimezone=Asia/Seoul", "root", "1Shot1Kill.");
		
	}
	
	private static void disconnect() throws SQLException {
		
		SQLSession.close();
		
		SQLSession=null;
		
	}
	
	public static int query(String database, String table, String conditions) throws ClassNotFoundException, SQLException{
		
		int result=-1;
		
		connect(database);
		
		PreparedStatement statement=SQLSession.prepareStatement("DELETE from " + table + " " + conditions + ";");
		
		result=statement.executeUpdate();
		
		disconnect();
		
		return result;
		
	}
	
}