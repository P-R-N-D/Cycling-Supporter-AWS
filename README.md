# Cycling Supporter AWS
**Demonstration**: https://youtu.be/G39ZnHDm7jI

Cycling Supporter AWS is a two-part project:

- **Android client** in `android_app/android_studio`
- **Java servlet web server** in `java_server/eclipse`

## Repository Structure

```text
.
├── architecture.svg
├── android_app
│   ├── android_studio
│   │   ├── app
│   │   │   ├── libs
│   │   │   └── src
│   │   │       ├── androidTest
│   │   │       ├── main
│   │   │       └── test
│   │   ├── build.gradle
│   │   ├── gradle
│   │   │   └── wrapper
│   │   ├── gradle.properties
│   │   ├── gradlew
│   │   ├── gradlew.bat
│   │   └── settings.gradle
│   └── app.apk
├── java_server
│   ├── eclipse
│   │   ├── WebContent
│   │   │   ├── META-INF
│   │   │   └── WEB-INF
│   │   │       └── lib
│   │   └── src
│   │       └── beans
│   └── image.war
└── README.md
```

## Components

### 1) Android App (`android_app/android_studio`)

- Android application source code and resources.
- Gradle wrapper and module configuration for Android Studio.
- Includes legacy external dependency JAR (`app/libs/com.skt.Tmap.jar`).

### 2) Java Server (`java_server/eclipse`)

- Eclipse dynamic web project (Servlet + JSP based).
- Java servlet sources are under `src/`.
- Web resources and deployment descriptors are under `WebContent/`.

## Artifacts Included in This Repository

- `architecture.svg`: service architecture diagram.
- `android_app/app.apk`: built Android application package.
- `java_server/image.war`: built Java web application archive.

## Quick Start

### Android

1. Open `android_app/android_studio` in Android Studio.
2. Sync Gradle.
3. Build and run the `app` module on an emulator or device.

### Java Server

1. Import `java_server/eclipse` as an Eclipse Dynamic Web Project.
2. Deploy to an Apache Tomcat server.
3. Access mapped endpoints defined in `WEB-INF/web.xml`.

## Notes

- This codebase is maintained with minimal behavioral changes to preserve the original implementation.
- Generated intermediate outputs should not be committed.
