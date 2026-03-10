package beans;
import java.sql.*;

public final class SQLRows {
	
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
		
		Statement statement=SQLSession.createStatement();

		ResultSet rs = statement.executeQuery("SELECT * from " + table + " " + conditions + ";");
		
		if( rs == null || rs.isBeforeFirst() == false )
		{
			
			result=0;

		}
		else
		{
			
			result=0;

			while(rs.next())
			{
				
				result++;
				
			}
			
		}
		
		disconnect();
		
		return result;
		
	}
	
	public static int query(String database, String table) throws ClassNotFoundException, SQLException{
		
		int result=-1;
		
		connect(database);
		
		Statement statement=SQLSession.createStatement();

		ResultSet rs = statement.executeQuery("SELECT * from " + table + ";");
		
		if( rs == null || rs.isBeforeFirst() == false )
		{
			
			result=0;

		}
		else
		{
			
			result=0;

			while(rs.next())
			{
				
				result++;
				
			}
			
		}
		
		disconnect();
		
		return result;
		
	}
	
}