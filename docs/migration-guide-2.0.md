# Documentify 2.0 Migration Guide

This guide helps you upgrade an application from Documentify 1.4.x to 2.0.

Documentify 2.0 moves to the Spring Boot 4 generation (Spring Framework 7, Spring REST Docs 4, Jackson 3).
It is released through milestones (`2.0.0-M1`, ...) before the GA release, so this guide is updated with each milestone.
Changes that only apply from a specific milestone are marked as such.

If you need to stay on Spring Boot 3, keep using Documentify 1.4.x.

## Before You Start

### Upgrade to Spring Boot 4 first

Documentify 2.0 only works with Spring Boot 4. Upgrade your application to Spring Boot 4 before upgrading Documentify,
following the official [Spring Boot 4.0 Migration Guide](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Migration-Guide).

### Review the system requirements

| | 1.4.x | 2.0.x |
|---|---|---|
| Spring Boot | 3.x | 4.0.x |
| Spring Framework | 6.x | 7.0.x |
| Spring REST Docs | 3.0.x | 4.0.x |
| Jackson | 2.x (`com.fasterxml.jackson`) | 3.x (`tools.jackson`) |
| Java | 21+ | 21+ |
| Kotlin | 2.0+ | 2.2+ |
| Gradle | 8.x | 8.14+ or 9.x (as required by Spring Boot 4) |

Documentify 2.0 is built and tested with Gradle 9.1.

## Upgrading Documentify

Update the version of the starter and the Gradle plugin:

```kotlin
plugins {
    id("io.github.bgmsound.documentify") version "2.0.0-M1"
}

dependencies {
    implementation("io.github.bgmsound:documentify-starter-mvc:2.0.0-M1")
    testImplementation("org.springframework.restdocs:spring-restdocs-webtestclient")
}
```

`spring-restdocs-webtestclient` still has to be declared explicitly. When the Spring Boot dependency management is applied,
its version (4.0.x) is managed for you.

## Breaking Changes

### `codec(...)` now takes a Jackson 3 `JsonMapper`

The standalone environment's `codec(...)` accepted a Jackson 2 `com.fasterxml.jackson.databind.ObjectMapper`.
It now accepts a Jackson 3 `tools.jackson.databind.json.JsonMapper`, which is what Spring Framework 7's JSON codecs and
message converters use.

Before (1.4.x):

```kotlin
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.module.kotlin.KotlinModule

private val objectMapper = ObjectMapper().apply {
    propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
    registerModules(KotlinModule.Builder().build())
}

standalone(provider) {
    controller(api)
    codec(objectMapper)
}
```

After (2.0):

```kotlin
import tools.jackson.databind.PropertyNamingStrategies
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.KotlinModule

private val jsonMapper = JsonMapper.builder()
    .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
    .addModule(KotlinModule.Builder().build())
    .build()

standalone(provider) {
    controller(api)
    codec(jsonMapper)
}
```

Jackson 3 mappers are immutable, so configure them through `JsonMapper.builder()` instead of mutating an existing instance.
Jackson 3 also writes dates as ISO-8601 strings by default and supports `java.time` without a separate module.

### Jackson 3 instead of Jackson 2 on the classpath

Documentify 1.4.x brought `jackson-databind` 2.x and `jackson-datatype-jsr310` in transitively.
Documentify 2.0 depends on Jackson 3 (`tools.jackson.core:jackson-databind`) instead, and exposes it as an API dependency
because `codec(...)` uses it.

If your tests relied on Jackson 2 being provided by Documentify, declare it yourself or migrate that code to Jackson 3.

### Spring Boot starters are no longer pulled in transitively

Documentify 1.4.x brought `spring-boot-starter-web` and `spring-boot-starter-validation` into your test classpath through its
OpenAPI dependency, even in WebFlux applications. Documentify 2.0 no longer does this.

If your tests depended on those starters only through Documentify, declare them explicitly.

## Changes in Spring Framework 7 that affect your tests

`documentation(...)` returns Spring's `WebTestClient.BodyContentSpec` as before, so the assertions you chain onto it follow
Spring Framework 7.

- The Hamcrest `Matcher` variants of `jsonPath(...).value(...)` are deprecated for removal. Use `isEqualTo(...)` or the
  `Consumer` variants instead:

  ```kotlin
  documentation("test-get-api") {
      // ...
  }.jsonPath("$.testField").isEqualTo("test")
  ```

## OpenAPI Output

In 2.0.0-M1, the OpenAPI document is generated the same way as in 1.4.x (`openapi3 { ... }`, `./gradlew openapi3`),
and the output is equivalent.

- Schemas without an explicit name get an automatically generated, hash-based name in `components.schemas`.
  These generated names may differ from those produced by 1.4.x. Do not rely on them; name your schemas explicitly
  if you reference them from other tools.

## Upcoming Changes

Later 2.0 milestones replace the current OpenAPI conversion layer with Documentify's own OpenAPI generation pipeline.
This is expected to change how the OpenAPI document is configured and generated (the `openapi3 { ... }` extension and the
related Gradle tasks). The details will be added to this guide as those milestones are released.
