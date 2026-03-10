# Cycling Supporter AWS

> Full-stack cycling navigation platform with an Android client, a Java backend, and an AWS deployment architecture.

## Overview

Cycling Supporter AWS is an end-to-end service project that combines a native Android application, a Java web backend, and an AWS-oriented deployment architecture for route-based cycling support.

The project connects mobile interaction, backend processing, and cloud deployment in one product flow. Instead of treating the client, server, and infrastructure as separate parts, this repository presents them as one integrated service.

## Demo

- Video demo: https://youtu.be/G39ZnHDm7jI

## Architecture

<p align="center">
  <img src="architecture.svg" alt="Cycling Supporter AWS architecture diagram" width="1024" />
</p>

The repository includes the service architecture diagram used to explain the overall deployment topology and system flow.

## Highlights

- Android client for cycling navigation workflows
- Java servlet-based backend service
- AWS deployment architecture for service delivery
- End-to-end project scope across app, backend, and infrastructure
- Packaged deliverables for demonstration and deployment

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

### Cloud / Deployment
- AWS
- Elastic Load Balancing
- Elastic Beanstalk
- RDS
- CloudFront
- ACM
- Docker

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
