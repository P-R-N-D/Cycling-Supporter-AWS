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
		return query(database, table, conditions, new Object[0]);
	}

	public static int query(String database, String table, String conditions, Object... parameters) throws ClassNotFoundException, SQLException{

		int result=-1;

		connect(database);

		PreparedStatement statement=SQLSession.prepareStatement("SELECT * from " + AppConfig.quotedIdentifier(table) + " " + conditions + ";");
		bindParameters(statement, parameters);

		ResultSet rs = statement.executeQuery();

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

		ResultSet rs = statement.executeQuery("SELECT * from " + AppConfig.quotedIdentifier(table) + ";");

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

	private static void bindParameters(PreparedStatement statement, Object... parameters) throws SQLException {
		for(int i=0; i < parameters.length; i++)
		{

			statement.setObject(i+1, parameters[i]);

		}
	}

}
