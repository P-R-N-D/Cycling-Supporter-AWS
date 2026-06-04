import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.AppConfig;

public class RoadMatchToRoads extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		String coords = request.getParameter("coords");
		if (coords == null || coords.trim().isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "coords is required.");
			return;
		}

		Map<String, String> params = new LinkedHashMap<>();
		params.put("responseType", defaultValue(request.getParameter("responseType"), "1"));
		params.put("coords", coords);

		HttpURLConnection connection = null;

		try {
			byte[] body = AppConfig.formEncode(params).getBytes("UTF-8");

			connection = (HttpURLConnection) new URL(AppConfig.tmapRoadMatchUrl()).openConnection();
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(10000);
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			connection.setRequestProperty("Content-Length", String.valueOf(body.length));

			OutputStream outputStream = connection.getOutputStream();
			outputStream.write(body);
			outputStream.close();

			writeUpstreamResponse(connection, response);
		} catch (IllegalStateException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	private String defaultValue(String value, String defaultValue) {
		if (value == null || value.trim().isEmpty()) {
			return defaultValue;
		}

		return value;
	}

	private void writeUpstreamResponse(HttpURLConnection connection, HttpServletResponse response) throws IOException {
		int statusCode = connection.getResponseCode();
		InputStream stream = statusCode >= 400 ? connection.getErrorStream() : connection.getInputStream();

		response.setStatus(statusCode);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=UTF-8");

		if (stream != null) {
			response.getWriter().write(readStream(stream));
		}
	}

	private String readStream(InputStream stream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
		StringBuilder result = new StringBuilder();
		String line;

		while ((line = reader.readLine()) != null) {
			result.append(line);
		}

		return result.toString();
	}
}
