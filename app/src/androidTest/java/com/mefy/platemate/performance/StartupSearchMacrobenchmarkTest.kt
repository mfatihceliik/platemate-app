package com.mefy.platemate.performance

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class StartupSearchMacrobenchmarkTest {

    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Ignore("Run manually during performance triage passes.")
    @Test
    fun startupToSearch_coldStart() {
        benchmarkRule.measureRepeated(
            packageName = "com.mefy.platemate",
            metrics = listOf(
                StartupTimingMetric(),
                FrameTimingMetric()
            ),
            iterations = 10,
            startupMode = StartupMode.COLD,
            compilationMode = CompilationMode.None()
        ) {
            pressHome()
            startActivityAndWait()
        }
    }
}
