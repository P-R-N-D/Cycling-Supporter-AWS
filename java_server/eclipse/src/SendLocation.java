import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import beans.*;

public class SendLocation extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
	protected String name;
	protected String session;
	protected String id;
	protected double latitude;
	protected double longitude;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html; charset=utf-8");
		
		PrintWriter out=response.getWriter();
		
		out.println("<html><title>Unsupported Method</title><body>doGet is not supported.</body></html>");
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		try {

			getParameter(request);
			
			id=SessionToID.query(session);
			
			Connection SQLSession1 = SQL.connect("users");
			Statement statement1=SQLSession1.createStatement();
			statement1.executeUpdate("INSERT INTO `user_path_records` (`id`, `record`) SELECT '" + id + "', '" + name + "' FROM DUAL WHERE NOT EXISTS (SELECT * from `user_path_records` WHERE id='" + id + "' AND record='" + name + "');");
			SQL.disconnect(SQLSession1);
			
			Connection SQLSession2 = SQL.connect("path_records");
			Statement statement2=SQLSession2.createStatement();
			statement2.execute("CREATE TABLE if not exists `" + name + "` (`lat` double NOT NULL, `long` double NOT NULL, `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, UNIQUE KEY `time` (`time`));");
			SQL.disconnect(SQLSession2);
			
			ArrayList<String> columns = new ArrayList<String>();
			columns.add("`lat`");
			columns.add("`long`");
			
			ArrayList<Object> values = new ArrayList<Object>();
			values.add(latitude);
			values.add(longitude);
			
			SQLInsert.query(columns, values, "path_records", "`" + name + "`");
		
		} catch (ClassNotFoundException | SQLException e) {

			e.printStackTrace();
		}
		
		PrintWriter out=response.getWriter();
		
		out.write(id);
		out.flush();
		out.close();
		
	}
	
	protected void getParameter(HttpServletRequest request) throws ClassNotFoundException, SQLException
	{
		
		session=(String)request.getParameter("session");
		name=(String)request.getParameter("name");
		latitude=(double)Double.parseDouble((String)request.getParameter("lat"));
		longitude=(double)Double.parseDouble((String)request.getParameter("long"));
		
	}

}
