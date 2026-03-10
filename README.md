# Cycling Supporter AWS

> Full-stack cycling navigation platform with an Android client, a Java backend, and an AWS-oriented deployment architecture.

## Overview

Cycling Supporter AWS is an end-to-end service project built around a cycling navigation use case. It combines a native Android application, a Java web backend, and a cloud deployment architecture into one integrated service flow.

The repository is intended to show how mobile interaction, backend processing, and deployment design can be structured as one product rather than as isolated components.

## Demo

- Video demo: https://youtu.be/G39ZnHDm7jI

## Architecture

<p align="center">
  <img src="architecture.svg" alt="Cycling Supporter AWS architecture diagram" width="1024" />
</p>

The repository includes the architecture diagram used to explain the overall service topology and deployment flow.

## Project Scope

This project covers:

- an Android client for user-facing cycling navigation workflows
- a Java servlet-based backend for service logic
- AWS-oriented deployment design for service delivery
- packaged build artifacts for demonstration and presentation

## Technology Stack

### Client
- Android Studio
- Android application project
- TMap API integration

### Backend
- Java
- Servlet / JSP
- Apache Tomcat
- Eclipse Dynamic Web Project

### Infrastructure
- AWS deployment architecture
- backend service hosting and delivery design
- database-connected service topology
- cloud distribution and certificate-aware deployment planning

## Repository Structure

```text
.
├── architecture.svg
├── android_app
│   ├── android_studio
│   └── app.apk
├── java_server
│   ├── eclipse
│   └── image.war
└── README.md
```

## Included Artifacts

- `architecture.svg` — service architecture diagram
- `android_app/app.apk` — packaged Android application
- `java_server/image.war` — packaged Java web application

## Local Setup

### Android application
1. Open `android_app/android_studio` in Android Studio.
2. Sync Gradle dependencies.
3. Build and run the `app` module on an emulator or Android device.

### Java backend
1. Import `java_server/eclipse` into Eclipse as a Dynamic Web Project.
2. Configure Apache Tomcat.
3. Deploy the backend project to Tomcat.
4. Update environment-specific settings as needed.

## Why This Repository Matters

Cycling Supporter AWS demonstrates a full-stack service perspective across:

- mobile application development
- backend service implementation
- cloud deployment architecture
- deployable build artifacts

It is intended to show how application logic, server-side processing, and deployment design can be connected in one practical service project.

## License

This repository is provided for project and portfolio presentation purposes. Please review repository contents before reuse or redistribution.
