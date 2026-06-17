---
type: android-doc
area: data-networking
tags:
  - android
  - retrofit
  - room
  - datastore
  - repository
  - networking
aliases:
  - Data and Networking
  - Android Networking
updated: 2026-05-29
---

# Data and Networking

## Purpose

Documents Retrofit, repository, DTO, mapper, local persistence, token/session, and backend-contract conventions.

## Data Flow

Remote data should generally flow like this:

```text
Retrofit ApiService -> DTO -> data mapper -> domain model -> use case -> ViewModel -> UI model/state
```

Local data should generally flow like this:

```text
Room/DataStore entity/value -> data mapper -> domain model -> use case -> ViewModel
```

## Retrofit Rules

- Retrofit services belong in `data.remote.api`.
- Request/response DTOs belong in `data.remote.dto`.
- Retrofit services should not be injected into ViewModels.
- API response wrappers should match backend contracts.
- Verify endpoint paths before building new features on stale assumptions.

Potential contract verification targets from current inspection:

- `PlateApiService.searchPlateByPath`
- `LocationApiService` endpoints
- placeholder detail destinations in discover/messages graphs

## Repository Rules

Repository interfaces belong in `domain.repository`.

Repository implementations belong in `data.repository`.

A repository implementation may:

- call Retrofit services
- call Room/DataStore/local data sources
- use data mappers
- wrap responses in project result types
- coordinate cache/session side effects when appropriate

A repository interface should expose domain models/results, not DTOs.

## DTO Rules

DTOs should stay in the data layer.

Do not expose DTOs to:

- ViewModels
- Screens
- UiState
- domain use cases, unless the project has a deliberate internal exception

## Mapper Rules

| Mapping | Location |
| --- | --- |
| DTO → domain | `data.mapper` |
| Room entity → domain | `data.mapper` or local mapper package |
| domain → UI | `presentation` feature mapper/helper |
| UI input → request | ViewModel/use case or dedicated request mapper if non-trivial |

Handle nullability explicitly. Avoid `!!` in mappers unless there is a strict contract and a clear failure path.

## Result/Error Rules

- Use the project’s `AppResult`/`AppError` style consistently.
- Convert HTTP/network exceptions into app errors in data/repository layer.
- ViewModels should resolve user-facing messages via existing error resolver utilities where available.
- Do not display raw backend exception messages directly unless the API contract guarantees they are user-safe.

## Auth and Token Rules

- Access/refresh token storage should remain centralized.
- Token refresh behavior should remain in networking/session infrastructure, not individual screens.
- Session-expired effects should be handled centrally where possible.
- Logout should clear local session state consistently.

## Room/DataStore Rules

- Room entities are local persistence models, not UI models.
- DataStore should be used for small key-value app/session preferences.
- Do not use DataStore for large relational data.
- Keep DAO queries persistence-focused; business rules belong in use cases/repositories.

## Build Config and Environments

Local endpoint constants are acceptable during development, but production-ready code should move base URLs to BuildConfig fields/flavors when build files are available.

Cleartext traffic should be development-only unless intentionally required.

## Contract Sync Rule

When backend API behavior changes:

1. Update Retrofit service.
2. Update request/response DTOs.
3. Update data mapper.
4. Update repository implementation.
5. Update use case/ViewModel if behavior changes.
6. Update relevant docs.
7. Add/update tests where possible.
