package com.mefy.platemate.architecture

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.name
import org.junit.Assert.fail
import org.junit.Test

class LayerBoundaryTest {

    @Test
    fun domain_mustNotImportPresentation() {
        assertNoForbiddenImports(
            directory = sourceRoot().resolve("domain"),
            forbiddenPrefix = "com.mefy.platemate.presentation"
        )
    }

    @Test
    fun presentation_mustNotImportData() {
        assertNoForbiddenImports(
            directory = sourceRoot().resolve("presentation"),
            forbiddenPrefix = "com.mefy.platemate.data"
        )
    }

    private fun assertNoForbiddenImports(
        directory: Path,
        forbiddenPrefix: String
    ) {
        val violations = mutableListOf<String>()
        Files.walk(directory).use { paths ->
            paths.filter { Files.isRegularFile(it) && it.name.endsWith(".kt") }
                .forEach { file ->
                    Files.readAllLines(file).forEachIndexed { index, line ->
                        val trimmed = line.trim()
                        if (trimmed.startsWith("import ") && trimmed.contains(forbiddenPrefix)) {
                            violations += "${file.toString().replace('\\', '/')}:${index + 1} -> $trimmed"
                        }
                    }
                }
        }

        if (violations.isNotEmpty()) {
            fail(
                buildString {
                    appendLine("Forbidden layer imports detected:")
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
