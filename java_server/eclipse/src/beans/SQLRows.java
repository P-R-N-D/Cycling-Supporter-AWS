package beans;
import java.sql.*;

public final class SQLRows {
	
	private static Connection SQLSession=null;
	
	private static void connect(String database) throws ClassNotFoundException, SQLException {
		
		SQLSession = SQL.connect(database);
		
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
