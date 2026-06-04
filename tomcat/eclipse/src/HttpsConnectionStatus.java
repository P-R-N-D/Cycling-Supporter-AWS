import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class HttpsConnectionStatus extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("text/html; charset=utf-8");
		
		PrintWriter out=response.getWriter();
		
		out.println("<html>");
		out.println("<title>HTTPS 상태</title>");
		out.println("<body>");
		out.println("HTTPS Enabled: " + request.isSecure());
		out.println("</body>");
		out.println("</html>");

		out.flush();
		out.close();
		
		return;
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		response.setStatus(HttpServletResponse.SC_OK);
		
		PrintWriter out=response.getWriter();
		
		out.write("HTTPS Enabled: " + request.isSecure());
		out.flush();
		out.close();
		
		return;
		
	}

}
