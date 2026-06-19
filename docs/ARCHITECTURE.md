# Cycling Supporter AWS Architecture Map

This is an AI-facing architecture map. Use `docs/CONTEXT.md` as the canonical source for status labels, guardrails, and documentation sync requirements.

## Current architecture boundaries

- `CURRENT_CODE`: Android app source under `android/android-studio/` provides the mobile UI shell, session-aware screens, navigation/search flows, location display/tracking, ride-location submission, and WebView-based ride-record retrieval.
- `CURRENT_CODE`: Java Servlet/JSP Tomcat backend source under `tomcat/eclipse/` handles servlet endpoints, session/user lookup, ride-record storage/retrieval flows, external API proxy-style calls, and JSP rendering for stored paths.
- `CURRENT_CODE`: MySQL-oriented data flow is implemented through JDBC helper classes under `tomcat/eclipse/src/beans/`; current code uses user/session tables and path-record storage assumptions.
- `CURRENT_CODE`: TMap/external APIs are used by the Android app and backend configuration paths for map/search/routing/weather/road matching roles, depending on the source area.
- `CURRENT_CODE`: WebView/JSP ride-record retrieval is part of the current app/server flow: Android posts user-derived data into backend record selection, and JSP renders selected paths.
- `CURRENT_ARTIFACT`: `android/app.apk` and `tomcat/server.war` are checked-in artifacts and must not be treated as automatically reproducible without verification.
- `PORTFOLIO_SCOPE`: BLE/Arduino embedded device integration and bike light control are described in README/portfolio material, but this first pass did not confirm embedded firmware/protocol source in the repository.

## Android app responsibility

The Android app is responsible for user-facing screens, login/session use, navigation/search UI, TMap display, location-aware ride flows, ride-location submission to backend endpoints, and WebView/JSP-based record retrieval.

When changing Android behavior, check endpoint paths, request parameter names, response parsing, session handling, permissions, location/background behavior, battery impact, retry/offline behavior, and privacy assumptions.

## Java Servlet/JSP Tomcat backend responsibility

The Tomcat backend is responsible for servlet endpoint handling, login/register/session-related flows, user info lookup, ride-location storage, record retrieval entry points, JSP path visualization, and backend-side calls/configuration for external services.

When changing backend behavior, check `tomcat/eclipse/WebContent/WEB-INF/web.xml`, servlet classes, JSP files, Android callers, response formats, session behavior, SQL identifiers, and dynamic table assumptions.

## MySQL data responsibility

The repository confirms MySQL-oriented data access through JDBC helper classes and backend code. The current docs describe user/session/path-record flows, and README notes that a normalized `rides` / `ride_points` model is a long-term improvement.

Do not change DB schema, dynamic table policy, or table naming assumptions without explicit approval. If approved, update AI-facing docs and Android/backend contract notes in the same change.

## TMap and external API role

TMap/external APIs support map, route/search, road matching, JavaScript map rendering, and weather-related flows as referenced by Android code and backend configuration utilities. Keys and URLs must remain externalized; do not hardcode real keys or production endpoints.

## WebView/JSP ride-record retrieval role

The current ride-record retrieval path includes Android WebView usage and backend/JSP rendering. Treat this as an active contract between Android, servlet parameters, JSP routing, SQL lookup, and map rendering. Be careful with HTML escaping, JavaScript string escaping, URL/form encoding, and SQL identifiers.

## AWS-oriented deployment documentation/artifact scope

The repository README and assets describe an AWS-oriented deployment structure. Checked-in artifacts include APK/WAR files. Treat detailed IaC, CI/CD, monitoring, backup, rollback, and production runbook content as `PROPOSED` unless concrete checked-in implementation files are added later.

## Current vs proposed architecture boundaries

- Current: Android Java app + Java Servlet/JSP Tomcat backend + MySQL-oriented data flow + TMap/external API integration + WebView/JSP record display + checked-in APK/WAR artifacts.
- Proposed: JSON API separation, normalized `rides` / `ride_points`, Android modernization, stronger config/secret management, IaC, CI/CD, monitoring, backup/rollback, and embedded protocol hardening.

Do not present proposed architecture as current implementation.

## Change-impact notes

- Android changes may impact backend endpoints, parameter names, response parsing, permissions, privacy, and AI docs.
- Backend/API changes may impact Android callers, WebView/JSP flows, sessions, DB assumptions, and AI docs.
- Data changes may impact record storage/retrieval, dynamic tables, JSP rendering, modernization status, and AI docs.
- Deployment/artifact changes may impact build assumptions, checked-in APK/WAR handling, configuration, security, and AI docs.
