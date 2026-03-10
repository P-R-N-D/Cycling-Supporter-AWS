import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.json.JSONObject;

import beans.*;

public class SessionToUserInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html; charset=utf-8");
		
		PrintWriter out=response.getWriter();
		
		out.println("<html><title>Unsupported Method</title><body>doGet is not supported.</body></html>");
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8"); 
		
		String session=(String)request.getParameter("session");
		
		ArrayList<ArrayList<Object>> result=null;
		
		try {
			
			String id =	SessionToID.query(session);
			
			ArrayList<String> columns=new ArrayList<String>();
			
			columns.add("`id`");
			columns.add("`name`");
			columns.add("`email`");
			
			result=SQLSelect.query(columns, "users", "user_list", "where id = '" + id + "';");
		
		} catch (ClassNotFoundException | SQLException e) {
			
			e.printStackTrace();
		}
		
		String id=(String)result.get(0).get(0);
		String name=(String)result.get(0).get(1);
		String email=(String)result.get(0).get(2);
	
		JSONObject jsonResponse=new JSONObject();
		
		jsonResponse.put("id", id);
		jsonResponse.put("name", name);
		jsonResponse.put("email", email);
		
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/json; charset=utf-8"); 
		
		PrintWriter out=response.getWriter();
		
		out.write(jsonResponse.toString());
		out.flush();
		out.close();
		
	}

}
