# 📝 Documentify
Documentify allows easy and convenient creation of OpenAPI specification documents through Kotlin DSL, making users from the complexity of traditional RestDocs. 
It combines the advantages of both Swagger and RestDocs for efficient and intuitive document management.

## Installation and Getting Started
### Installation
Add the following dependency to your `build.gradle.kts` file:
```kotlin
dependencies {
    implementation("io.github.bgmsound:documentify-core:${version}")
}
```

### Getting Started
Add the following code to your test class:
```kotlin
@Test
fun documentationGetApi() {
    `when`(testService.test()).thenReturn("hi")
    documentation("test-get-api") {
        information {
            summary("test get api")
            description("this is test get api")
            tag("test")
        }
        requestLine(Method.GET, "/api/test/{path}") {
            pathVariable("path", "path", "path")
        }
        responseBody {
            field("testField1", "path", "path")
            field("testField2", "message", "hi")
        }
    }
}
```