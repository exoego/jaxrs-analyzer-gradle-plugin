package functional

import java.io.File
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.io.TempDir
import kotlin.test.Test
import kotlin.test.BeforeTest

class JaxRsAnalyzerPluginFunctionalTest {
    @TempDir
    lateinit var testProjectDir: File

    lateinit var buildFile: File
    lateinit var resourceFile: File
    lateinit var appFile: File

    @BeforeTest
    fun setup() {
        buildFile = File(testProjectDir, "build.gradle.kts")
        buildFile.writeText(
            """
            plugins {
                id("java")
                id("io.github.grimmjo.jaxrs-analyzer") version "0.4"
            }

            apply(plugin = "java")
            apply(plugin = "io.github.grimmjo.jaxrs-analyzer")

            repositories {
                mavenCentral()
            }

            dependencies {
                implementation("javax:javaee-api:7.0")
            }

        """.trimIndent()
        )

        val srcDir = File(testProjectDir, "src/main/java/sample")
        srcDir.mkdirs()
        resourceFile = File(srcDir, "CoffeeResource.java")
        resourceFile.writeText(/*language=java*/ """
            package sample;
            import javax.ws.rs.*;

            @Path("/coffee")
            public class CoffeeResource {
                @GET
                public String getCoffee() {
                    return "That's a coffee";
                }

                @POST
                public String createCoffee(String coffee) {
                    return "That's a " + coffee;
                }
            }
        """.trimIndent()
        )

        appFile = File(srcDir, "JaxApp.java")
        appFile.writeText(/*language=java*/ """
            package sample;

            import javax.ws.rs.ApplicationPath;
            
            @ApplicationPath("")
            public class JaxApp {}
        """.trimIndent())
    }

    @Test
    fun worksWithNoArguments() {
        buildFile.appendText("""
            jaxRsAnalyzer {
                backend.set(listOf("swagger", "plaintext", "asciidoc"))
                domain.set("localhost:8080")
                schemes.set(listOf("http", "https"))
                renderTags.set(true)
                tagPathOffset.set(0)
                outputDirectory.set("jaxrs-analyzer")
            }
        """.trimIndent())

        val result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments("analyze")
            .withPluginClasspath()
            .build()
        assert(result.task(":analyze")?.outcome == TaskOutcome.SUCCESS)
    }
}
