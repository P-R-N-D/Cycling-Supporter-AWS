# Cycling Supporter AWS Agent Context

## Canonical status

This file is the canonical AI-facing source of truth for this repository. Future AI agents must read this file before changing repository behavior, architecture, API contracts, data flow, build/test assumptions, artifacts, security/privacy assumptions, deployment assumptions, or modernization documentation.

Human-facing README files remain separate from AI-facing instruction files. Do not turn this file into a portfolio README.

## Project purpose

Cycling Supporter AWS is a full-stack cycling navigation/support project. Repository-confirmed scope includes Android app source, Java Servlet/JSP Tomcat backend source, MySQL-oriented data flow, documentation assets, and checked-in APK/WAR artifacts with AWS-oriented deployment documentation.

## Current repository baseline

- `CURRENT_CODE`: Android source project lives under `android/android-studio/`.
- `CURRENT_CODE`: Java/Tomcat server source project lives under `tomcat/eclipse/`.
- `CURRENT_CODE`: Tomcat endpoint mappings are declared in `tomcat/eclipse/WebContent/WEB-INF/web.xml` when present.
- `CURRENT_CODE`: Android API URL and TMap key access are centralized through `ApiConfig` and Gradle-generated build config values.
- `CURRENT_CODE`: Backend DB and external API settings are read from environment variables through `beans.AppConfig`.
- `CURRENT_ARTIFACT`: `android/app.apk` is a checked-in APK artifact. Do not assume it is reproducible from the current source without verifying.
- `CURRENT_ARTIFACT`: `tomcat/server.war` is a checked-in WAR artifact. Do not assume it is reproducible from the current source without verifying.
- `PORTFOLIO_SCOPE`: README/demo material describes BLE/Arduino embedded device integration and bike light control, but no embedded firmware/protocol source has been confirmed in this repository during this first AI documentation pass.
- `PROPOSED`: JSON API separation, `rides` / `ride_points` normalization, Android modernization, IaC, CI/CD, monitoring, backup/rollback, and embedded protocol hardening are future work unless current checked-in code proves otherwise.

## Scope classification

Use these labels in AI-facing documentation and PR notes:

- `CURRENT_CODE`: directly confirmed by current checked-in source code or configuration.
- `CURRENT_ARTIFACT`: checked-in artifact such as APK/WAR, without assuming full build reproducibility.
- `PORTFOLIO_SCOPE`: described in README/Notion/portfolio/demo material but not directly confirmed by repository source code.
- `PROPOSED`: modernization direction or future improvement.
- `UNKNOWN`: not confirmed; do not guess.

## Source-of-truth hierarchy

For current implementation facts:

1. Current checked-in source code and configuration.
2. Checked-in repository docs.
3. Notion/portfolio documentation.
4. Proposed modernization notes.

For AI-agent instructions:

1. Direct user/developer task instructions.
2. `docs/CONTEXT.md`.
3. More specific nested `AGENTS.md` files.
4. Thin bridge files and generated suggestions.

Treat Notion/portfolio documentation as supporting context only. It is not proof that source files or production behavior exist in this repository.

## AI-facing document map

- `docs/CONTEXT.md`: canonical AI-agent context, labels, guardrails, validation expectations, and documentation sync rule.
- `docs/ARCHITECTURE.md`: concise AI-facing architecture boundaries and change-impact notes.
- `docs/CODE-MAP.md`: path-oriented code navigation map for future agents.
- `AGENTS.md`: root bridge for agents; read this file and then `docs/CONTEXT.md`.
- `CLAUDE.md`: thin bridge for Claude-style agents.
- `.github/copilot-instructions.md`: thin bridge for GitHub Copilot.
- `.cursor/rules/cycling-supporter.mdc`: thin bridge for Cursor.
- `android/AGENTS.md`: Android-specific guardrails for files under `android/`.
- `tomcat/AGENTS.md`: Tomcat/backend-specific guardrails for files under `tomcat/`.
- `.github/pull_request_template.md`: PR checklist, including AI documentation sync.

## Mandatory AI documentation sync

AI-facing documentation is not a one-time setup artifact.

Every time an AI agent works on this repository, it must check whether the task changes or invalidates any AI-facing documentation. Before completing any task, review the relevant AI-facing documents.

If the task changes repository behavior, architecture, APIs, endpoint contracts, environment variables, data model, build/test commands, artifact handling, security/privacy assumptions, deployment assumptions, or modernization status, update the relevant AI-facing docs in the same change.

If no AI-facing documentation update is needed, the agent must explicitly say so in the final summary or PR description and explain why.

A task is not complete if it leaves AI-facing documentation stale.

## Global guardrails

- Do not write or commit secrets, API keys, passwords, tokens, sessions, or real server credentials.
- Do not log, document, or commit real user location data.
- Do not modify checked-in APK/WAR artifacts unless explicitly requested.
- Do not change DB schema, dynamic table policy, endpoint names, request parameter names, or response contracts without explicit approval.
- Do not describe proposed future work as current implementation.
- Do not run destructive commands.
- Do not operate against production systems or real external services.
- Treat Notion/portfolio documentation as supporting context, not as proof that source code exists.
- Keep human-facing README files separate from AI-facing instruction files.

## Change-impact rules

- Android changes: check API endpoints, request parameters, response parsing, WebView/JSP flows, permissions, lifecycle, background execution, battery, retry/offline behavior, and privacy impact.
- Backend changes: check `web.xml`, servlet names, endpoint paths, request parameters, response formats, session behavior, SQL identifiers, dynamic table policy, JSP routing, and Android callers.
- Data changes: do not alter schema assumptions or dynamic table behavior without explicit approval; update AI docs if approved changes occur.
- Deployment/config changes: do not add real endpoints or credentials; update AI docs when environment variable names, artifact handling, or deployment assumptions change.
- Documentation changes: distinguish `CURRENT_CODE`, `CURRENT_ARTIFACT`, `PORTFOLIO_SCOPE`, `PROPOSED`, and `UNKNOWN`.

## Required validation

For documentation-only changes, do not run full Android or Tomcat builds unless an explicit repository instruction requires it. Preferred validation:

1. `git status --short`.
2. Confirm only documentation/instruction files changed.
3. Run a markdown/whitespace sanity check if available.
4. `git diff --check`.
5. Best-effort search for newly added real secrets, API keys, passwords, tokens, sessions, or real location data.

For code changes, run the smallest relevant tests/checks available and update AI-facing docs when behavior or assumptions change.

## Future AI-facing documentation roadmap

Do not create the full second-pass documentation set unless needed for a future task. Candidate future AI-facing docs:

- `docs/API-CONTRACT.md`
- `docs/DATA-MODEL.md`
- `docs/CONFIGURATION.md`
- `docs/BUILD-AND-ARTIFACTS.md`
- `docs/TESTING.md`
- `docs/SECURITY-AND-PRIVACY.md`
- `docs/DEPLOYMENT.md`
- `docs/MODERNIZATION.md`

## Completion criteria

A task is complete only when:

- The requested change is implemented within scope.
- No prohibited files or artifacts were changed.
- Required validation was run or skipped with an explicit reason.
- AI-facing documentation was updated when needed, or the final summary/PR explains why no update was needed.
- Proposed future work is not presented as current implementation.
