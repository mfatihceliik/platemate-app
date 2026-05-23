package com.mefy.platemate.architecture

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.name
import org.junit.Assert.fail
import org.junit.Test

class PresentationErrorHandlingBoundaryTest {

    private val forbiddenTypeBranchRegex = Regex("""\bis\s+AppError\b""")
    private val forbiddenRawFieldErrorRegex = Regex("""\berror\.fieldErrors\b""")
    private val forbiddenUiTextExtensionRegex = Regex("""\btoUiText\s*\(""")
    private val forbiddenRawThrowableUiTextRegex =
        Regex("""UiText\.Dynamic\(\s*[^)]*(error|throwable|exception)\??\.message""")

    @Test
    fun presentationFeatures_mustNotBranchOnAppErrorType_orReadRawFieldErrors_orUseToUiTextExtension() {
        val featuresDirectory = sourceRoot().resolve("presentation").resolve("features")
        val violations = mutableListOf<String>()

        Files.walk(featuresDirectory).use { paths ->
            paths.filter { Files.isRegularFile(it) && it.name.endsWith(".kt") }
                .forEach { file ->
                    Files.readAllLines(file).forEachIndexed { index, line ->
                        val trimmed = line.trim()
                        if (
                            forbiddenTypeBranchRegex.containsMatchIn(trimmed) ||
                            forbiddenRawFieldErrorRegex.containsMatchIn(trimmed) ||
                            forbiddenUiTextExtensionRegex.containsMatchIn(trimmed)
                        ) {
                            violations += "${file.toString().replace('\\', '/')}:${index + 1} -> $trimmed"
                        }
                    }
                }
        }

        if (violations.isNotEmpty()) {
            fail(
                buildString {
                    appendLine("Raw AppError handling in presentation/features is forbidden:")
                    append(violations.joinToString(separator = "\n"))
                }
            )
        }
    }

    @Test
    fun presentation_mustNotCreateUiTextDynamicFromRawThrowableMessages() {
        val sourceRoot = sourceRoot().resolve("presentation")
        val scanDirectories = listOf(
            sourceRoot.resolve("features"),
            sourceRoot.resolve("common")
        )
        val violations = mutableListOf<String>()

        scanDirectories.forEach { directory ->
            Files.walk(directory).use { paths ->
                paths.filter { Files.isRegularFile(it) && it.name.endsWith(".kt") }
                    .forEach { file ->
                        Files.readAllLines(file).forEachIndexed { index, line ->
                            val trimmed = line.trim()
                            if (forbiddenRawThrowableUiTextRegex.containsMatchIn(trimmed)) {
                                violations += "${file.toString().replace('\\', '/')}:${index + 1} -> $trimmed"
                            }
                        }
                    }
            }
        }

        if (violations.isNotEmpty()) {
            fail(
                buildString {
                    appendLine("Raw throwable/error messages must not be converted to UiText.Dynamic:")
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
