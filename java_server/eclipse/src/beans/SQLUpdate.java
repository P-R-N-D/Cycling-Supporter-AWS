package beans;
import java.sql.*;
import java.util.ArrayList;

public final class SQLUpdate {
	
	private static Connection SQLSession=null;
	
	private static void connect(String database) throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		SQLSession = DriverManager.getConnection("jdbc:mysql://sql.ccjb4squbrhs.ap-northeast-2.rds.amazonaws.com:3306/" + database + "?serverTimezone=Asia/Seoul", "root", "1Shot1Kill.");
		
	}
	
	private static void disconnect() throws SQLException {
		
		SQLSession.close();
		
		SQLSession=null;
		
	}
	
	public static int query(ArrayList<String> columns, ArrayList<Object> values, String database, String table, String conditions) throws ClassNotFoundException, SQLException{

		connect(database);
		
		int result=-1;
		
		String query="UPDATE " + table + " set ";
		
		if(columns.size() != values.size())
		{
			
			result=-1;
			
		}
		else
		{
			
			for(int i=0; i < columns.size(); i++)
			{
				
				if(i>0)
				{
					
					query += ", ";
					
				}
				
				query += (columns.get(i) + " = ?");
				
			}
			
			query += (" " + conditions + " limit 1;");
			
			PreparedStatement statement=SQLSession.prepareStatement(query);
			
			for(int i=0; i < values.size(); i++)
			{
				
				statement.setObject( i+1 , values.get(i));
				
			}	
			
			result=statement.executeUpdate();
			
		}
				
		disconnect();
		
		return result;
		
	}

}
