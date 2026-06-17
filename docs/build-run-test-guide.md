# Build, Run, and Test Guide

Agents must use these commands to verify their changes before completing a task.

## Windows Commands

Run from project root:

```powershell
.\gradlew.bat assembleDebug
.\gradlew.bat testDebugUnitTest
.\gradlew.bat lintDebug
```

Run a specific test class:

```powershell
.\gradlew.bat testDebugUnitTest --tests "*LoginUseCaseTest"
```

## macOS / Linux Commands

Run from project root:

```bash
./gradlew assembleDebug
./gradlew testDebugUnitTest
./gradlew lintDebug
```

Run a specific test class:

```bash
./gradlew testDebugUnitTest --tests "*LoginUseCaseTest"
```

## If Commands Cannot Be Run

If Android SDK, Gradle, or local environment is unavailable:

* do not guess the result,
* do not claim the project builds,
* report the exact command that should be run locally,
* explain what could not be verified.

## What to do on failure?
* **Compile Error:** Check imports, typo errors, or signature mismatches between Interface/Implementation. Fix immediately.
* **Test Failure:** If a test breaks because you changed expected behavior, update the test. If it breaks unintentionally, revert your implementation change.
* **Lint Warning:** Fix obvious issues. Ignore minor warnings if they require large refactors, but note them in the output.
