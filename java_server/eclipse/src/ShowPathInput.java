import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

import beans.*;

public class ShowPathInput extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html; charset=utf-8");
		
		PrintWriter out=response.getWriter();
		
		out.println("<html><title>Unsupported Method</title><body>doGet is not supported.</body></html>");
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8"); 
		
		String show_path_input_userid=(String)request.getParameter("show_path_input_userid");

		ArrayList<String> columns=new ArrayList<String>();

		columns.add("`record`");

		ArrayList<ArrayList<Object>> rs=null;

		try {

			rs=SQLSelect.query(columns, "users", "user_path_records", "where `id`='" + show_path_input_userid + "' ORDER BY `time` ASC");

		} catch (ClassNotFoundException | SQLException e) {

			e.printStackTrace();
		}
		
		
		response.setContentType("text/html; charset=utf-8");
		
		PrintWriter out=response.getWriter();
		
		out.println("<html>");
		out.println("<meta charset='UTF-8'>");
		out.println("<title>주행 정보 조회</title>");
		out.println("<head>");
		out.println("</head>");
		out.println("<body>");
		out.println("<div align='center'>");
		out.println("<H3>주행 정보를 선택하세요</H3>");
		out.println("<form action='./show_path.jsp' method='post'><div align='center'>");
		out.println("<select name='path_record'>");
		
		for(int i=0; i < rs.size(); i++)
		{
			
			out.println("<option value='" + ((String) rs.get(i).get(0)) + "'>" + (String)rs.get(i).get(0) + "</option>");
			
		}
		
		out.println("</select>");
		out.println("<input type='submit' value='확인' />");
		out.println("</form>");
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");
		
	}

}
