package beans;
import java.sql.*;
import java.util.ArrayList;

public final class SQLSelect {

	private static Connection SQLSession=null;
	
	private static void connect(String database) throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		SQLSession = DriverManager.getConnection("jdbc:mysql://sql.ccjb4squbrhs.ap-northeast-2.rds.amazonaws.com:3306/" + database + "?serverTimezone=Asia/Seoul", "root", "1Shot1Kill.");
		
	}
	
	private static void disconnect() throws SQLException {
		
		SQLSession.close();
		
		SQLSession=null;
		
	}
	
	public static ArrayList<ArrayList<Object>> query(ArrayList<String> columns, String database, String table, String conditions) throws ClassNotFoundException, SQLException{

		connect(database);
		
		Statement statement=SQLSession.createStatement();
		
		String query="SELECT ";
		
		ArrayList<ArrayList<Object>> result=new ArrayList<ArrayList<Object>>();
		
		for(int i=0; i < columns.size(); i++)
		{
			
			if(i>0)
			{
				
				query += ", ";
				
			}
			
			query += columns.get(i);
			
		}
		
		query += (" from " + table + " " + conditions +";");

		ResultSet rs = statement.executeQuery(query);
		
		if( rs == null || rs.isBeforeFirst() == false )
		{
			
			result=null;

		}
		else
		{
			
			while(rs.next())
			{
				
				ArrayList<Object> rowTable=new ArrayList<Object>();
				
				for(int i=0; i < columns.size(); i++)
				{
					
					rowTable.add(rs.getObject(i+1));
					
				}
				
				result.add(rowTable);
				
			}
			
		}
		
		disconnect();
		
		return result;
		
	}
	
	public static ArrayList<ArrayList<Object>> query(ArrayList<String> columns, String database, String table) throws ClassNotFoundException, SQLException{

		connect(database);
		
		Statement statement=SQLSession.createStatement();
		
		String query="SELECT ";
		
		ArrayList<ArrayList<Object>> result=new ArrayList<ArrayList<Object>>();
		
		for(int i=0; i < columns.size(); i++)
		{
			
			if(i>0)
			{
				
				query += ", ";
				
			}
			
			query += columns.get(i);
			
		}
		
		query += (" from " + table +";");

		ResultSet rs = statement.executeQuery(query);
		
		if( rs == null || rs.isBeforeFirst() == false )
		{
			
			result=null;

		}
		else
		{
			
			while(rs.next())
			{
				
				ArrayList<Object> rowTable=new ArrayList<Object>();
				
				for(int i=0; i < columns.size(); i++)
				{
					
					rowTable.add(rs.getObject(i+1));
					
				}
				
				result.add(rowTable);
				
			}
			
		}
		
		disconnect();
		
		return result;
		
	}
	
}
