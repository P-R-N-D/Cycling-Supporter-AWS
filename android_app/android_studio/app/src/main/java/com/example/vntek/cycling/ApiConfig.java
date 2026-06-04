package com.example.vntek.cycling;

import android.net.Uri;

import java.util.Map;

public final class ApiConfig {

    private ApiConfig() {
    }

    public static String apiBaseUrl() {
        return required(BuildConfig.API_BASE_URL, "API_BASE_URL");
    }

    public static String endpoint(String path) {
        String baseUrl = trimTrailingSlash(apiBaseUrl());
        String cleanPath = trimLeadingSlash(path);

        return baseUrl + "/" + cleanPath;
    }

    public static String endpoint(String path, Map<String, String> queryParams) {
        Uri.Builder builder = Uri.parse(endpoint(path)).buildUpon();

        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            builder.appendQueryParameter(entry.getKey(), entry.getValue());
        }

        return builder.build().toString();
    }

    public static String tmapAppKey() {
        return required(BuildConfig.TMAP_APP_KEY, "TMAP_APP_KEY");
    }

    private static String required(String value, String name) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException(name + " is required. See local.properties.template.");
        }

        return value.trim();
    }

    private static String trimTrailingSlash(String value) {
        while (value.endsWith("/")) {
            value = value.substring(0, value.length() - 1);
        }

        return value;
    }

    private static String trimLeadingSlash(String value) {
        while (value.startsWith("/")) {
            value = value.substring(1);
        }

        return value;
    }
}
