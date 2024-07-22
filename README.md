# Agile

![](https://img.shields.io/badge/agile-dev-blue#id=rjaDQ&originHeight=20&originWidth=66&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=) ![Static Badge](https://img.shields.io/badge/author-Huang%20Z.Y.-blue)
![](https://img.shields.io/github/languages/top/huangzy1218/agile#id=g42nJ&originHeight=20&originWidth=86&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=) ![](https://img.shields.io/github/last-commit/huangzy1218/agile#id=dQ6Hu&originHeight=20&originWidth=140&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)

## Project Introduction

Agile is an RBAC enterprise rapid development platform based on Spring Cloud, Spring Boot, and OAuth2, and supports both
microservice architecture and monolithic architecture.
It mainly includes system management, file management, business platform, code generation and other functions, provides
production-level practices for Spring Authorization Server and supports multiple security authorization modes.

```java
agile
├── agile-boot -- Standalone Mode Launcher [9999]
├── agile-auth -- Authorization Service Provider [3000]
└── agile-common -- System Common Module
    ├── agile-common-excel -- Process Excel Files
    ├── agile-common-bom -- Global Dependency Management
    ├── agile-common-core -- Common Utilities Core Package
    ├── agile-common-datasource -- Dynamic Data Source Package
    ├── agile-common-log -- Logging Service
    ├── agile-common-oss -- File Upload Utilities
    ├── agile-common-mybatis -- MyBatis Extensions
    ├── agile-common-seata -- Distributed Transactions
    ├── agile-common-security -- Security Utilities
    ├── agile-common-swagger -- API Documentation
    ├── agile-common-feign -- Feign Extensions
    └── agile-common-xss -- XSS Security Package
├── agile-register -- Nacos Server [8848]
├── agile-gateway -- Spring Cloud Gateway [9999]
└── agile-upms -- General User Permission Management Module
    ├── agile-upms-api -- General User Permission Management System Public API Module
    └── agile-upms-biz -- General User Permission Management System Business Processing Module [4000]
├── agile-monitor -- Service Monitoring [5001]
├── agile-codegen -- Graphical Code Generator [5002]
└── agile-quartz -- Scheduled Task Management [5007]
```

## Technology Selection

| **Technology** | **Description**                                          | **Official website**                                                               |
|----------------|----------------------------------------------------------|------------------------------------------------------------------------------------|
| SpringBoot     | Web application development framework                    | [https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot)   |
| SpringCloud    | Build some of the common patterns in distributed systems | [https://spring.io/projects/spring-cloud](https://spring.io/projects/spring-cloud) |

