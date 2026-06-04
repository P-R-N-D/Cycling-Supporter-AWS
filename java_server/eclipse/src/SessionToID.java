import java.sql.*;
import java.util.*;

import beans.*;

public final class SessionToID {
	
	public static String query(String session) throws ClassNotFoundException, SQLException{
		
		String result=null;
		
		if (SQLRows.query("users", "user_sessions", "where session=SHA2(?, 512)", session) == 1)
		{
			
			ArrayList<String> columns=new ArrayList<String>();
			
			columns.add("`id`");
			
			ArrayList<ArrayList<Object>> rs=SQLSelect.query(columns, "users", "user_sessions", "where session=SHA2(?, 512)", session);
			
			result=(String)rs.get(0).get(0);
			
		}
		
		return result;
		
	}
	
}
