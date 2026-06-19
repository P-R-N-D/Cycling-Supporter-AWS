# Cycling Supporter AWS Code Map

This AI-facing map helps agents navigate the repository. Use `docs/CONTEXT.md` for canonical guardrails and status labels, and `docs/ARCHITECTURE.md` for architecture boundaries.

## Android app shell, session, and navigation

- Paths: `android/android-studio/app/src/main/java/com/example/vntek/cycling/MainActivity.java`, `LoginLayout.java`, `CreateAccountLayout.java`, layout/menu resources under `android/android-studio/app/src/main/res/`.
- Responsibility: app entry/navigation shell, login/session-aware UI flow, account creation, drawer/menu resources.
- Check when changing: session propagation, endpoint callers, UI navigation assumptions, placeholder/stub screens, permissions, and related backend contracts.
- Related AI docs: `docs/CONTEXT.md`, `docs/ARCHITECTURE.md`, `android/AGENTS.md`.

## Android API configuration

- Paths: `android/android-studio/app/src/main/java/com/example/vntek/cycling/ApiConfig.java`, `android/android-studio/local.properties.template`, `android/android-studio/app/build.gradle`.
- Responsibility: build-config-backed API base URL and TMap app key access, endpoint URL construction, template-only local configuration guidance.
- Check when changing: do not hardcode real endpoints or keys; do not change Gradle/dependency versions unless explicitly requested; update AI docs if config names or endpoint construction changes.
- Related AI docs: `docs/CONTEXT.md`, `android/AGENTS.md`.

## Android location and ride submission

- Paths: `android/android-studio/app/src/main/java/com/example/vntek/cycling/SpeedLayout.java`, `PoiGuideLayout.java`.
- Responsibility: location-aware ride/status screens, weather/current-location related calls, and `SendLocation` submission flows.
- Check when changing: location permissions, lifecycle/background behavior, battery, retry/offline behavior, privacy, request parameters, and backend response handling.
- Related AI docs: `docs/CONTEXT.md`, `docs/ARCHITECTURE.md`, `android/AGENTS.md`, `tomcat/AGENTS.md`.

## Android record retrieval and WebView

- Paths: `android/android-studio/app/src/main/java/com/example/vntek/cycling/RecordLayout.java`.
- Responsibility: user/session lookup and WebView-based ride-record retrieval via backend/JSP flow.
- Check when changing: `SessionToUserInfo`, `ShowPathInput`, JSP routing, form encoding, WebView behavior, and privacy implications.
- Related AI docs: `docs/CONTEXT.md`, `docs/ARCHITECTURE.md`, `android/AGENTS.md`, `tomcat/AGENTS.md`.

## Tomcat endpoint mappings

- Paths: `tomcat/eclipse/WebContent/WEB-INF/web.xml`.
- Responsibility: servlet and JSP URL mappings including `AndroidLogin`, `AndroidRegister`, `HttpsConnectionStatus`, `SendLocation`, `WeatherCurrent`, `RoadMatchToRoads`, `SessionToUserInfo`, `ShowPathInput`, and `show_path`.
- Check when changing: Android callers, endpoint paths, request parameters, response formats, JSP routing, and explicit approval requirements.
- Related AI docs: `docs/CONTEXT.md`, `docs/ARCHITECTURE.md`, `tomcat/AGENTS.md`.

## Tomcat authentication and session handling

- Paths: `tomcat/eclipse/src/AndroidLogin.java`, `AndroidRegister.java`, `SessionToID.java`, `SessionToUserInfo.java`, related SQL helpers under `tomcat/eclipse/src/beans/`.
- Responsibility: login/register/session-to-user flows and DB access supporting those flows.
- Check when changing: session behavior, credential handling, response formats, Android parsing, SQL safety, and no real secrets/sessions in docs or code.
- Related AI docs: `docs/CONTEXT.md`, `tomcat/AGENTS.md`.

## Tomcat ride-record storage

- Paths: `tomcat/eclipse/src/SendLocation.java`, SQL helpers under `tomcat/eclipse/src/beans/`.
- Responsibility: receive ride-location submissions, resolve session/user context, and write path-record data using current DB assumptions.
- Check when changing: request parameter names, dynamic table policy, SQL identifiers, privacy/location data handling, Android callers, and response contract.
- Related AI docs: `docs/CONTEXT.md`, `docs/ARCHITECTURE.md`, `tomcat/AGENTS.md`.

## Tomcat ride-record retrieval and JSP

- Paths: `tomcat/eclipse/src/ShowPathInput.java`, `tomcat/eclipse/WebContent/show_path.jsp`.
- Responsibility: render/select stored ride records and display selected path data through JSP/map flow.
- Check when changing: JSP routing, form parameters, HTML escaping, JavaScript escaping, SQL identifiers, TMap JavaScript configuration, and WebView compatibility.
- Related AI docs: `docs/CONTEXT.md`, `docs/ARCHITECTURE.md`, `tomcat/AGENTS.md`.

## Tomcat DB and config utility classes

- Paths: `tomcat/eclipse/src/beans/AppConfig.java`, `SQL.java`, `SQLSelect.java`, `SQLInsert.java`, `SQLUpdate.java`, `SQLDelete.java`, `SQLRows.java`, `tomcat/eclipse/env.template`.
- Responsibility: runtime environment variable access, JDBC URL construction, external API URL construction, encoding/escaping helpers, and SQL helper operations.
- Check when changing: environment variable names, no real credentials, SQL identifier validation, external API key handling, and AI docs for config/security changes.
- Related AI docs: `docs/CONTEXT.md`, `tomcat/AGENTS.md`.

## Documentation and artifacts

- Paths: `README.md`, `docs/README.ko.md`, `docs/README.en.md`, `docs/assets/`, `docs/CONTEXT.md`, `docs/ARCHITECTURE.md`, `docs/CODE-MAP.md`, `android/app.apk`, `tomcat/server.war`.
- Responsibility: human-facing portfolio docs, AI-facing docs, visual assets, and checked-in APK/WAR artifacts.
- Check when changing: keep human-facing README and AI-facing instructions separate; do not modify APK/WAR unless explicitly requested; classify current/proposed/portfolio facts accurately.
- Related AI docs: `docs/CONTEXT.md`, `AGENTS.md`, `.github/pull_request_template.md`.
