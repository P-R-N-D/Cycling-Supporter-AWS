# Repository Agent Instructions

Read `docs/CONTEXT.md` first. It is the canonical AI-facing source of truth for this repository.

Short guardrails:

- This repository contains Android source under `android/android-studio/`, Tomcat source under `tomcat/eclipse/`, checked-in artifacts `android/app.apk` and `tomcat/server.war`, and documentation assets.
- Do not commit secrets, API keys, passwords, tokens, sessions, real server credentials, or real user location data.
- Do not modify checked-in APK/WAR artifacts, endpoint contracts, DB schema/dynamic table policy, or runtime credentials unless explicitly requested and approved.
- Do not describe proposed future work as current implementation.
- Do not run destructive commands or operate against production systems/real external services.
- Treat README/Notion/portfolio content as supporting context, not proof that source exists.

Mandatory AI documentation sync: before completing every task, check whether `docs/CONTEXT.md` or related AI-facing docs need updates. If the task affects behavior, architecture, APIs, data, config, artifacts, security/privacy, deployment, tests, or modernization status, update the AI docs in the same change. If no update is needed, state why in the final summary or PR.

Nested `AGENTS.md` files may add stricter instructions for their directory and must not weaken `docs/CONTEXT.md`.
