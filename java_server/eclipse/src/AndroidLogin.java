import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.security.SecureRandom;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.commons.net.ntp.*;
import org.apache.commons.validator.routines.*;

import beans.*;

public class AndroidLogin extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html; charset=utf-8");

		PrintWriter out=response.getWriter();

		out.println("<html><title>Unsupported Method</title><body>doGet is not supported.</body></html>");

		return;

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String id=(String)request.getParameter("id");
		String pw=(String)request.getParameter("pw");

		String ipAddr=request.getRemoteAddr();

		InetAddressValidator ipAddrValidator=InetAddressValidator.getInstance();

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
		java.sql.Timestamp datetime=null;

		try {

			NTPUDPClient ntpClient = new NTPUDPClient();
			InetAddress inetAddress = InetAddress.getByName("pool.ntp.org");
			TimeInfo ntpTimeInfo = ntpClient.getTime(inetAddress);
			long ntpTime = ntpTimeInfo.getMessage().getTransmitTimeStamp().getTime();
			datetime = new java.sql.Timestamp(ntpTime);
			ntpClient.close();

		} catch (UnknownHostException uhe) {

			datetime = new java.sql.Timestamp(calendar.getTimeInMillis());

		}

		String doLogin="";

		try {

			if(SQLRows.query("users", "user_list","where `id`=? and `pw`=SHA2(?, 512)", id, pw) == 1)
			{

				SecureRandom generator = new SecureRandom();
				String SessionKey;

				do {

					SessionKey=new BigInteger(130, generator).toString(32);

				}
				while(SQLRows.query("users", "user_sessions", "where `session`=SHA2(?, 512)", SessionKey) == 1);


				if(ipAddrValidator.isValidInet4Address(ipAddr) == true) {

					if(SQLRows.query("users", "user_sessions", "where `id`=? and `ip_ver`=4 and INET6_NTOA(`ip_v4`)=?", id, ipAddr) == 1)
					{

						Connection SQLSession=SQL.connect("users");
						PreparedStatement statement=SQLSession.prepareStatement("update user_sessions set `session`=SHA2(?, 512), `last`=? where `id`=? and `ip_ver`=4 and `ip_v4`=INET6_ATON(?) and `ip_v6`=INET6_ATON('0:0:0:0:0:0:0:0');");
						statement.setString(1, SessionKey);
						statement.setTimestamp(2, datetime);
						statement.setString(3, id);
						statement.setString(4, ipAddr);

						statement.executeUpdate();
						SQL.disconnect(SQLSession);

					}
					else
					{

						Connection SQLSession=SQL.connect("users");
						PreparedStatement statement=SQLSession.prepareStatement("insert into user_sessions(`id`, `ip_ver`, `ip_v4`, `ip_v6`, `last`, `session`) values(?, 4 , INET6_ATON(?), INET6_ATON('0:0:0:0:0:0:0:0'), ?, SHA2(?, 512));");
						statement.setString(1, id);
						statement.setString(2, ipAddr);
						statement.setTimestamp(3, datetime);
						statement.setString(4, SessionKey);

						statement.executeUpdate();
						SQL.disconnect(SQLSession);

					}

					doLogin += SessionKey;

				}
				else if(ipAddrValidator.isValidInet6Address(ipAddr) == true) {

					if(SQLRows.query("users", "user_sessions", "where `id`=? and `ip_ver`=6 and INET6_NTOA(`ip_v6`)=?", id, ipAddr) == 1)
					{

						Connection SQLSession=SQL.connect("users");
						PreparedStatement statement=SQLSession.prepareStatement("update user_sessions set `session`=SHA2(?, 512), `last`=? where `id`=? and `ip_ver`=6 and `ip_v4`=INET6_ATON('0.0.0.0') and `ip_v6`=INET6_ATON(?);");
						statement.setString(1, SessionKey);
						statement.setTimestamp(2, datetime);
						statement.setString(3, id);
						statement.setString(4, ipAddr);

						statement.executeUpdate();
						SQL.disconnect(SQLSession);

					}
					else
					{

						Connection SQLSession=SQL.connect("users");
						PreparedStatement statement=SQLSession.prepareStatement("insert into user_sessions(`id`, `ip_ver`, `ip_v4`, `ip_v6`, `last`, `session`) values(?, 6 , INET6_ATON('0.0.0.0'), INET6_ATON(?), ?, SHA2(?, 512));");
						statement.setString(1, id);
						statement.setString(2, ipAddr);
						statement.setTimestamp(3, datetime);
						statement.setString(4, SessionKey);

						statement.executeUpdate();
						SQL.disconnect(SQLSession);

					}

					doLogin += SessionKey;

				}
				else {

					doLogin="Failed";

				}

			}
			else
			{

				doLogin="Failed";

			}


		} catch (ClassNotFoundException | SQLException e) {

			e.printStackTrace();
			doLogin="Failed";

		}

		response.setStatus(HttpServletResponse.SC_OK);

		PrintWriter out=response.getWriter();

		out.write(doLogin);
		out.flush();
		out.close();

		return;

	}

}
