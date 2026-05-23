# Startup Jank Triage (Search)

This project includes debug-only startup performance markers and optional macrobenchmark scaffolding.

## Markers

- `StartupTrace` logs:
  - `app_start`
  - `search_route_enter`
  - `search_first_frame`
- `StartupJankMonitor` logs jank frames with current route context.

## Debug Toggles

In `app/build.gradle.kts`:

- `ENABLE_STARTUP_JANK_MONITORING`
- `ENABLE_FIREBASE_ANALYTICS_COLLECTION`

Default behavior:

- `debug`: monitor on, analytics collection off
- `release`: monitor off, analytics collection on

## Suggested Comparison Matrix

Run each scenario with at least 10 cold starts:

1. `debug` + Android Studio attached (baseline worst case)
2. `debug` standalone run (Live Edit / inspector features off)
3. `release` (or release-like profileable build)

Then compare with emulator resources:

1. 2 GB RAM / 1 core
2. 4 GB RAM / 2+ cores

## Macrobenchmark Scaffold

- Test: `StartupSearchMacrobenchmarkTest`
- Metrics:
  - `StartupTimingMetric`
  - `FrameTimingMetric`

The test is marked `@Ignore` so it does not run by default.

