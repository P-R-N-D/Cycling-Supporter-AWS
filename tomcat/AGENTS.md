# Tomcat/Backend Agent Instructions

Read the root `AGENTS.md` and `docs/CONTEXT.md` first. These Tomcat/backend-specific rules apply to files under `tomcat/` and must not weaken global rules.

- `tomcat/eclipse/` is the Java/Tomcat server source project.
- `tomcat/server.war` is a checked-in artifact; do not modify it unless explicitly requested.
- Treat `web.xml` as the endpoint mapping reference if present.
- Keep DB credentials and external API keys in runtime environment variables/templates only.
- Do not write real DB credentials, API keys, passwords, tokens, or sessions.
- Do not change endpoint paths, request parameters, response formats, session behavior, DB schema, dynamic table policy, or JSP routing without explicit approval.
- If backend API contracts change, update Android callers and AI-facing docs in the same change.
- Do not send state-changing requests to production systems.
- Be careful with HTML, JavaScript, SQL identifiers, and WebView/JSP flows.

Mandatory AI documentation sync: before completing backend work, check whether backend/API/data/config/security/deployment assumptions in AI-facing docs changed. Update those docs in the same change, or state why no AI doc update was needed.
