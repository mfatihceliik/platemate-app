# Data & Domain Guidelines

This document establishes the boundaries and responsibilities for the Data and Domain layers in PlateMate.

## Data Layer Rules
* **DTOs & Entities:** The `data` layer owns DTOs (Data Transfer Objects for network) and Entities (for Room/Local). See `CLIENT_DTO_CONTRACT.md` for specific structures.
* **Mappers:** DTOs and Entities MUST NOT leak to the `domain` or `presentation` layers. They must be mapped to pure Domain models before leaving the Data layer. Mappers live in `data/mapper`.
* **Repository Implementations:** The `data/repository` package implements the repository interfaces defined in the `domain` layer.
* **Network & Local Caching:** The data layer decides whether to fetch from the network or local cache. It handles all backend pagination, token interception, and socket connections.

## Domain Layer Rules
* **Domain Models:** Pure Kotlin data classes representing the core business entities. They have no annotations (`@SerializedName`, `@Entity`).
* **Repository Contracts:** Interfaces that define the required data operations (e.g., `interface PlateRepository`).
* **UseCases:**
  * **Existing feature pattern wins:** Look at how similar features are implemented and follow that.
  * **Default for user-facing features:** UseCases are the default choice for any new feature that involves business logic, validation, or complex data combining.
  * **Pass-through exception:** Only in truly meaningless pass-through scenarios (e.g., `repository.getName()`) can the repository interface be called directly from the ViewModel. If logic expands later, refactor to a UseCase immediately.

*See also: [[architecture-overview]], [[error-result-handling]]*
