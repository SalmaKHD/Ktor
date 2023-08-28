package com.salmakhd.plugins

import freemarker.cache.ClassTemplateLoader
import freemarker.core.HTMLOutputFormat
import io.ktor.server.application.*
import io.ktor.server.freemarker.*

fun Application.configureTemplating() {
    install(FreeMarker)  { // let FreeMarker know that templates will be available in templates directory
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
        // add output format
        outputFormat = HTMLOutputFormat.INSTANCE
    }
}