# Agile

![](https://img.shields.io/badge/agile-dev-blue#id=rjaDQ&originHeight=20&originWidth=66&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=) ![](https://img.shields.io/github/languages/top/huangzy1218/agile#id=g42nJ&originHeight=20&originWidth=86&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=) ![](https://img.shields.io/github/last-commit/huangzy1218/agile#id=dQ6Hu&originHeight=20&originWidth=140&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)

## Project Introduction

Agile is an RBAC enterprise rapid development platform based on Spring Cloud, Spring Boot, and OAuth2, and supports both
microservice architecture and monolithic architecture.
It mainly includes system management, file management, business platform, code generation and other functions, provides
production-level practices for Spring Authorization Server and supports multiple security authorization modes.

```java
agile
        ├── agile-launch--Monolithic mode launcher[9999]
        ├── agile-auth--Authorization service provider[3000]
        └── agile-common--System common module
     ├── agile-common-bom--Global dependency management control
             ├── agile-common-core--Core package of common utility classes
             ├── agile-common-datasource--Dynamic datasource package
             ├── agile-common-log--Logging service
             ├── agile-common-oss--File upload utility class
             ├── agile-common-mybatis--MyBatis extension encapsulation
             ├── agile-common-seata--Distributed transaction
             ├── agile-common-security--Security utility class
             ├── agile-common-swagger--Interface document
             ├── agile-common-feign--Feign extension encapsulation
             ├── agile-common-excel--EasyExcel encapsulation
             └── agile-common-xss--XSS security encapsulation
             ├── agile-register--Nacos Server[8848]
             ├── agile-gateway--Spring Cloud Gateway gateway[9999]
             └── agile-upms--Common user permission management module
             └── agile-upms-api--Common user permission management system public API module
             └── agile-upms-biz--Common user permission management system business processing module[4000]
             └── agile-visual
             └── agile-monitor--Service monitoring[5001]
             ├── agile-codegen--Graphical code generation[5002]
             └── agile-quartz--Timing task management platform[5007]
```

## Technology Selection

| **Technology** | **Description**                                          | **Official website**                                                               |
|----------------|----------------------------------------------------------|------------------------------------------------------------------------------------|
| SpringBoot     | Web application development framework                    | [https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot)   |
| SpringCloud    | Build some of the common patterns in distributed systems | [https://spring.io/projects/spring-cloud](https://spring.io/projects/spring-cloud) |

