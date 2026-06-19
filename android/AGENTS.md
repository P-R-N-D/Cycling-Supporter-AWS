# Android Agent Instructions

Read the root `AGENTS.md` and `docs/CONTEXT.md` first. These Android-specific rules apply to files under `android/` and must not weaken global rules.

- `android/android-studio/` is the Android source project.
- `android/app.apk` is a checked-in artifact; do not modify it unless explicitly requested.
- Keep `API_BASE_URL` and `TMAP_APP_KEY` externalized.
- Do not hardcode real API keys or endpoints.
- Do not upgrade Android Gradle Plugin, Gradle, SDK versions, or dependencies unless explicitly requested.
- If endpoint paths, request parameters, or response parsing change, update relevant backend docs and AI-facing docs.
- If location/background behavior changes, consider permission, lifecycle, background execution, battery, retry/offline behavior, and privacy implications.
- Do not assume stub screens or placeholder features are complete product features.

Mandatory AI documentation sync: before completing Android work, check whether Android/API/config/privacy/build assumptions in AI-facing docs changed. Update those docs in the same change, or state why no AI doc update was needed.
