import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import beans.*;

public class AndroidRegister extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html; charset=utf-8");
		
		PrintWriter out=response.getWriter();
		
		out.println("<html><title>Unsupported Method</title><body>doGet is not supported.</body></html>");
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8"); 
		
		String name=(String)request.getParameter("name");
		String email=(String)request.getParameter("email");
		String id=(String)request.getParameter("id");
		String pw=(String)request.getParameter("pw");
		
		ArrayList<String> columns=new ArrayList<String>();
		
		columns.add("`name`");
		columns.add("`email`");
		columns.add("`id`");
		columns.add("`pw`");
		
		ArrayList<Object> values=new ArrayList<Object>();
		
		values.add(name);
		values.add(email);
		values.add(id);
		values.add("");
		
		try {
			
			if(SQLRows.query("users", "user_list", "where `id`='" + id + "'") == 1)
			{
				
				PrintWriter out=response.getWriter();
				
				out.write("Already Registered");
				out.flush();
				out.close();
				
				return;
				
			}
			
			if(SQLInsert.query(columns, values, "users", "user_list") == 1)
			{
				
				Connection SQLSession=SQL.connect("users");
				PreparedStatement statement=SQLSession.prepareStatement("update user_list set `pw`=SHA2('" + pw + "', 512) where `id`='" + id + "';");
				
				if(statement.executeUpdate() == 1)
				{
					
					response.setStatus(HttpServletResponse.SC_OK);
					
					PrintWriter out=response.getWriter();
					
					out.write("OK");
					out.flush();
					out.close();
					
					SQL.disconnect(SQLSession);
					
				}
				else
				{
					
					PrintWriter out=response.getWriter();
					
					out.write("Failed");
					out.flush();
					out.close();
					
					SQL.disconnect(SQLSession);
					
				}
				
				
			}
			else
			{
				
				PrintWriter out=response.getWriter();
				
				out.write("Failed");
				out.flush();
				out.close();
				
			}
		
		} catch (ClassNotFoundException | SQLException e) {
			
			e.printStackTrace();
		
		}
		
		
	}

}
