package com.mefy.platemate.architecture

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.name
import org.junit.Assert.fail
import org.junit.Test

class PresentationMappingBoundaryTest {

    private val forbiddenUiModelConstructionRegex = Regex("""\b\w+UiModel\s*\(""")
    private val forbiddenMapperMethodDefinitionRegex = Regex("""\bfun\s+map\w+\s*\(""")

    @Test
    fun viewModels_mustNotConstructUiModels_orDefineRawMappingMethods() {
        val featuresDirectory = sourceRoot().resolve("presentation").resolve("features")
        val violations = mutableListOf<String>()

        Files.walk(featuresDirectory).use { paths ->
            paths.filter { path ->
                Files.isRegularFile(path) &&
                    path.name.endsWith("ViewModel.kt")
            }.forEach { file ->
                Files.readAllLines(file).forEachIndexed { index, line ->
                    val trimmed = line.trim()
                    if (
                        forbiddenUiModelConstructionRegex.containsMatchIn(trimmed) ||
                        forbiddenMapperMethodDefinitionRegex.containsMatchIn(trimmed)
                    ) {
                        violations += "${file.toString().replace('\\', '/')}:${index + 1} -> $trimmed"
                    }
                }
            }
        }

        if (violations.isNotEmpty()) {
            fail(
                buildString {
                    appendLine("ViewModel mapping boundary violations detected:")
                    append(violations.joinToString(separator = "\n"))
                }
            )
        }
    }

    private fun sourceRoot(): Path {
        val candidates = listOf(
            Paths.get("src/main/java/com/mefy/platemate"),
            Paths.get("app/src/main/java/com/mefy/platemate")
        )

        return candidates.firstOrNull { Files.exists(it) }
            ?: error("Could not resolve source root from known candidates.")
    }
}
