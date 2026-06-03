# 📝 Documentify
[![Kotlin](https://img.shields.io/badge/kotlin-2.0.0-blue.svg?logo=kotlin)](http://kotlinlang.org)
![Latest Release](https://img.shields.io/github/v/release/BGMSound/documentify)
[![Apache 2.0 license](https://img.shields.io/badge/License-APACHE%202.0-green.svg?logo=APACHE&style=flat)](https://opensource.org/licenses/Apache-2.0)
<br>
Documentify allows easy and convenient creation of OpenAPI specification documents through Kotlin DSL, making users from the complexity of traditional RestDocs. 
It combines the advantages of both Swagger and RestDocs for efficient and intuitive document management.

## Installation and Getting Started
### Installation

This library requires `spring-restdocs-webtestclient` to be declared explicitly
in your project, as it is no longer transitively provided.

```kotlin
dependencies {
    testImplementation("org.springframework.restdocs:spring-restdocs-webtestclient")
}
```

Add the following dependency to your `build.gradle.kts` file:
<br><br>
`MVC`
```kotlin
dependencies {
    implementation("io.github.bgmsound:documentify-starter-mvc:${version}")
}
```
`WebFlux`
```kotlin
dependencies {
    implementation("io.github.bgmsound:documentify-starter-reactive:${version}")
}
```

### Getting Started
First, make your test class implements `Documentify` by `Documentify.new()`. and set up the test environment like this:
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
`MVC Example`
```kotlin
webApplicationContext(provider, context)
mockMvc(provider, mockMvc)
```

`Reactive Example`
```kotlin
applicationContext(provider, context)
webTestClient(provider, webTestClient)
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

Additional validation of the mock response generated during the tests for document creation is also possible.
```kotlin
@Test
fun documentationGetApi() {
    documentation("test-get-api") {
        information {
            summary("test get api")
            description("this is test get api")
            tag("test")
        }
        requestLine(Method.GET, "/api/test/{path}")
        responseBody {
            field("testField", "test", "test")
        }
    }.jsonPath("$testField").value("test")
}
```

### Generate OpenAPI Specification
After setting up the test environment and writing the test code, run the test.
The OpenAPI specification document will be generated in the `build/generated-snippets` directory.

First, apply documentify plugin to your `build.gradle.kts` file *(need gradle plugin portal)*:
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
---
[[click to see more sample code]](https://github.com/BGMSound/documentify/tree/main/documentify-sample)

## Documentify Development Story
If you want to check out the development story of Documentify, please refer to the [blog post](https://bgmsound.medium.com/documentify-선언형-rest-docs-dsl-제작기-0a09f651be2c).

## License
documentify is Open Source software released under the [Apache 2.0 license](https://www.apache.org/licenses/LICENSE-2.0.html).
