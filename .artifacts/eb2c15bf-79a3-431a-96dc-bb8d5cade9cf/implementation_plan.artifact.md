# Fix NullPointerException in ReportType mapping

The application is crashing with a `NullPointerException` when mapping `ReportTypeDto` to `ReportType` in `DiscoveryMapper`. This happens because `ReportTypeDto` has non-nullable fields (like `severity`), but the JSON response from the server might contain `null` for these fields, which GSON allows during deserialization but Kotlin's runtime checks reject when passed to a non-nullable constructor parameter.

## User Review Required

> [!NOTE]
> I am making all fields in `ReportTypeDto` nullable and providing safe defaults in `DiscoveryMapper`. This is a standard practice for robust network data mapping.

## Proposed Changes

### Data Layer

#### [MODIFY] [ReportTypeDto.kt](file:///C:/Users/mfatihceliik/Desktop/platemate_application/Android/PlateMate/app/src/main/java/com/mefy/platemate/data/remote/dto/report/ReportTypeDto.kt)
- Change all fields to nullable `String?`, `Int?`, etc.
- This ensures that GSON can safely deserialize even if fields are missing or null in the JSON.

#### [MODIFY] [DiscoveryMapper.kt](file:///C:/Users/mfatihceliik/Desktop/platemate_application/Android/PlateMate/app/src/main/java/com/mefy/platemate/data/mapper/DiscoveryMapper.kt)
- Update `mapTopReport` to use null-safe calls and provide default values when mapping to the domain model `ReportType`.
- Use `.orEmpty()` for strings and `?: 0` for integers.

## Verification Plan

### Automated Tests
- Run existing mapper tests:
  `./gradlew :app:testDebugUnitTest --tests "com.mefy.platemate.data.mapper.DtoMappersTest"`
- I will add a new test case to `DtoMappersTest` (or a new test file) specifically for null values in `ReportTypeDto` to verify the fix.

### Manual Verification
- The user can verify that the crash no longer occurs when navigating to the Discovery screen where these reports are displayed.
