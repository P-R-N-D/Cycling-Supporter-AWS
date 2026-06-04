package beans;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public final class AppConfig {

	private AppConfig() {
	}

	public static String dbUrl(String database) {
		String cleanDatabase = requireDatabaseName(database);

		return "jdbc:mysql://" + requiredEnv("DB_HOST") + ":" + requiredEnv("DB_PORT") + "/" + cleanDatabase
				+ "?serverTimezone=" + optionalEnv("DB_SERVER_TIMEZONE", "Asia/Seoul");
	}

	public static String dbUser() {
		return requiredEnv("DB_USER");
	}

	public static String dbPassword() {
		return requiredEnv("DB_PASSWORD");
	}

	public static String weatherCurrentUrl(Map<String, String> queryParams) {
		return appendQuery(requiredEnv("SK_WEATHER_CURRENT_URL"), withAppKey(queryParams));
	}

	public static String tmapRoadMatchUrl() {
		return appendQuery(requiredEnv("SK_TMAP_ROAD_MATCH_URL"), newQuery("version", "1", "appKey", skOpenApiAppKey()));
	}

	public static String tmapJavascriptUrl() {
		return appendQuery(requiredEnv("SK_TMAP_JS_URL"),
				newQuery("version", "1", "format", "javascript", "appKey", skOpenApiAppKey()));
	}

	public static String jqueryUrl() {
		return requiredEnv("JQUERY_URL");
	}

	public static String optionalEnv(String name, String defaultValue) {
		String value = System.getenv(name);
		if (value == null || value.trim().isEmpty()) {
			return defaultValue;
		}

		return value.trim();
	}

	public static String htmlAttribute(String value) {
		return value.replace("&", "&amp;").replace("\"", "&quot;").replace("'", "&#39;").replace("<", "&lt;")
				.replace(">", "&gt;");
	}

	public static String javascriptString(String value) {
		return value.replace("\\", "\\\\").replace("'", "\\'").replace("\r", "\\r").replace("\n", "\\n");
	}

	public static String formEncode(Map<String, String> params) {
		StringBuilder body = new StringBuilder();

		for (Map.Entry<String, String> param : params.entrySet()) {
			if (body.length() > 0) {
				body.append("&");
			}

			body.append(urlEncode(param.getKey()));
			body.append("=");
			body.append(urlEncode(param.getValue()));
		}

		return body.toString();
	}

	private static String requiredEnv(String name) {
		String value = System.getenv(name);
		if (value == null || value.trim().isEmpty()) {
			throw new IllegalStateException("Missing required environment variable: " + name);
		}

		return value.trim();
	}

	private static String skOpenApiAppKey() {
		return requiredEnv("SK_OPENAPI_APP_KEY");
	}

	private static Map<String, String> withAppKey(Map<String, String> queryParams) {
		queryParams.put("appKey", skOpenApiAppKey());
		queryParams.put("version", "2");

		return queryParams;
	}

	private static Map<String, String> newQuery(String firstKey, String firstValue, String secondKey, String secondValue) {
		Map<String, String> params = new java.util.LinkedHashMap<>();
		params.put(firstKey, firstValue);
		params.put(secondKey, secondValue);

		return params;
	}

	private static Map<String, String> newQuery(String firstKey, String firstValue, String secondKey, String secondValue,
			String thirdKey, String thirdValue) {
		Map<String, String> params = newQuery(firstKey, firstValue, secondKey, secondValue);
		params.put(thirdKey, thirdValue);

		return params;
	}

	private static String appendQuery(String baseUrl, Map<String, String> queryParams) {
		StringBuilder url = new StringBuilder(baseUrl);
		String separator = baseUrl.contains("?") ? "&" : "?";

		for (Map.Entry<String, String> param : queryParams.entrySet()) {
			url.append(separator);
			url.append(urlEncode(param.getKey()));
			url.append("=");
			url.append(urlEncode(param.getValue()));
			separator = "&";
		}

		return url.toString();
	}

	private static String requireDatabaseName(String database) {
		if (database == null || database.matches("[A-Za-z0-9_]+") == false) {
			throw new IllegalArgumentException("Invalid database name: " + database);
		}

		return database;
	}

	private static String urlEncode(String value) {
		try {
			return URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}
}
