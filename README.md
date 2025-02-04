# 📝 Documentify
Documentify allows easy and convenient creation of OpenAPI specification documents through Kotlin DSL, making users from the complexity of traditional RestDocs. 
It combines the advantages of both Swagger and RestDocs for efficient and intuitive document management.

## Installation and Getting Started
### Installation
Add the following dependency to your `build.gradle.kts` file:
<br><br>
`MVC`
```kotlin
dependencies {
    implementation("io.github.bgmsound:documentify-mvc:${version}")
}
```
`WebFlux`
```kotlin
dependencies {
    implementation("io.github.bgmsound:documentify-reactive:${version}")
}
```
 > Latest version : **1.0.0**

### Getting Started
First, make your test class extends `Documentify`. and set up the test environment like this:
```kotlin
@BeforeEach
fun setUp(provider: RestDocumentationContextProvider) {
    testService = mockk()
    standalone(provider) {
        controllers(TestController(testService))
    }
}
```
You can also set up the test environment with an application context or an auto-configured MockMvc (or WebTestClient). 
<br><br>
`Mvc Example`
```kotlin
webApplicationContext(provider, context)
mockMvc(provider, context)
```

`Reactive Example`
```kotlin
applicationContext(provider, context)
webTestClient(provider, context)
```

And add the following code to your test class:
```kotlin
@BeforeEach
fun setUp(provider: RestDocumentationContextProvider) {
    standalone(provider) {
        controllers(TestController(testService))
    }
}

@Test
fun documentationGetApi() {
    every { testService.test() } returns SampleResponse("path", "test")
    
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
            field("testField2", "message", "test")
        }
    }
}
```

### Generate OpenAPI Specification
After setting up the test environment and writing the test code, run the test.
The OpenAPI specification document will be generated in the `build/generated-snippets` directory.

First, apply documentify plugin to your `build.gradle.kts` file:
```kotlin
plugins {
    id("io.github.bgmsound.documentify") version "${version}"
}
```
Then, write openapi configuration in your `build.gradle.kts` file:
```kotlin
openapi3 { 
    title = "Sample API"
    description = "This is a sample API documentation."
    version = "0.0.1"
    format = "yaml"
}
```

Finally, run the following command:
```shell
./gradlew openapi3
./gradlew openapi
```

you can also create Postman collection by running the following command:
```shell
./gradlew postman
```
=======<br>
[more sample code](https://github.com/BGMSound/documentify/tree/main/documentify-sample) 
