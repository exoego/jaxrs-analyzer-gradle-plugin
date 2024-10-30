package io.github.grimmjo.jaxrs_analyzer

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * JaxRsAnalyzerPlugin
 * @author grimmjo
 */
class JaxRsAnalyzerPlugin : Plugin<Project> {
    override fun apply(project: Project) {

        val jaxRsAnalyzerExtension = project.extensions.create("jaxRsAnalyzer", JaxRsAnalyzerExtension::class.java)

        project.tasks.register("analyze", JaxRsAnalyserTask::class.java) {
            dependsOn += project.tasks.named("classes")
            backend.set(jaxRsAnalyzerExtension.backend)
            outputFileBaseName.set(jaxRsAnalyzerExtension.outputFileBaseName)
            schemes.set(jaxRsAnalyzerExtension.schemes)
            domain.set(jaxRsAnalyzerExtension.domain)
            renderTags.set(jaxRsAnalyzerExtension.renderTags)
            tagPathOffset.set(jaxRsAnalyzerExtension.tagPathOffset)
            outputDirectory.set(project.buildDir.resolve(jaxRsAnalyzerExtension.outputDirectory.get()))
        }
    }
}
