import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.AppConfig;

public class WeatherCurrent extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		proxyWeather(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		proxyWeather(request, response);
	}

	private void proxyWeather(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.setCharacterEncoding("UTF-8");

		String city = request.getParameter("city");
		String county = request.getParameter("county");
		String village = request.getParameter("village");

		if (isEmpty(city) || isEmpty(county) || isEmpty(village)) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "city, county, and village are required.");
			return;
		}

		Map<String, String> params = new LinkedHashMap<>();
		params.put("city", city);
		params.put("county", county);
		params.put("village", village);

		HttpURLConnection connection = null;

		try {
			connection = (HttpURLConnection) new URL(AppConfig.weatherCurrentUrl(params)).openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(10000);

			writeUpstreamResponse(connection, response);
		} catch (IllegalStateException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	private boolean isEmpty(String value) {
		return value == null || value.trim().isEmpty();
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
