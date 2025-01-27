package io.github.bgmsound.documentify.reactive.environment

import io.github.bgmsound.documentify.core.environment.AbstractStandaloneContextEnvironment
import io.github.bgmsound.documentify.core.specification.schema.document.DocumentSpec
import io.github.bgmsound.documentify.reactive.ReactiveDocumentContextEnvironment
import io.github.bgmsound.documentify.reactive.emitter.EmitterFactory
import io.github.bgmsound.documentify.reactive.emitter.ReactiveDocumentEmitter
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver

class StandaloneReactiveContextEnvironment private constructor(
) : AbstractStandaloneContextEnvironment<StandaloneReactiveContextEnvironment>(), ReactiveDocumentContextEnvironment {
    private val argumentResolvers = mutableListOf<HandlerMethodArgumentResolver>()

    fun argumentResolver(argumentResolver: HandlerMethodArgumentResolver): StandaloneReactiveContextEnvironment {
        this.argumentResolvers.add(argumentResolver)
        return this
    }

    fun argumentResolvers(vararg argumentResolvers: HandlerMethodArgumentResolver): StandaloneReactiveContextEnvironment {
        this.argumentResolvers.addAll(argumentResolvers)
        return this
    }

    fun argumentResolvers(argumentResolvers: List<HandlerMethodArgumentResolver>): StandaloneReactiveContextEnvironment {
        this.argumentResolvers.addAll(argumentResolvers)
        return this
    }

    override fun buildEmitter(
        provider: RestDocumentationContextProvider,
        documentSpec: DocumentSpec,
    ): ReactiveDocumentEmitter {
        val webTestClient = WebTestClient
            .bindToController(*controllers.toTypedArray())
            .controllerAdvice(*controllerAdvices.toTypedArray())
            .argumentResolvers { configurer ->
                configurer.addCustomResolver(*argumentResolvers.toTypedArray())
            }
            .configureClient()
            .filter(WebTestClientRestDocumentation.documentationConfiguration(provider))
            .build()
        return EmitterFactory.createReactiveEmitter(provider, documentSpec, webTestClient)
    }

    companion object {
        fun controller(controller: Any): StandaloneReactiveContextEnvironment {
            return StandaloneReactiveContextEnvironment().controller(controller)
        }

        fun controllers(vararg controllers: Any): StandaloneReactiveContextEnvironment {
            return StandaloneReactiveContextEnvironment().controllers(*controllers)
        }

        fun controllers(controllers: List<Any>): StandaloneReactiveContextEnvironment {
            return StandaloneReactiveContextEnvironment().controllers(controllers)
        }
    }
}