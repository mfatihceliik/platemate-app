# Dependency Injection Guidelines

PlateMate relies on **Hilt** for dependency injection.

## Hilt Modules Overview
Modules are located in the `di/` package and categorized by responsibility to prevent a single massive `AppModule`.

* `NetworkModule`: Provides Retrofit instances, OkHttpClient, Interceptors, and ApiServices.
* `LocalDatabaseModule`: Provides Room Database, DAOs, and DataStore instances.
* `RepositoryModule`: Binds Repository Interfaces (Domain) to their Implementations (Data).
* `DispatcherModule`: Provides Coroutine Dispatchers (`AppDispatchers`) for testing flexibility.
* `SocketModule`: Provides WebSocket and socket-related dependencies.
* `UiErrorModule`: Provides UI-specific error mappers or handlers if applicable.
* `PresentationMapperModule`: Provides UI model mappers.

## DI Rules & Best Practices
1. **Interface Binding:** Use `@Binds` in abstract modules when binding an interface to its implementation (e.g., in `RepositoryModule`). Use `@Provides` when you need to construct the object (e.g., Retrofit setup).
2. **Scoping:** Default to un-scoped (no annotation) unless you explicitly need a single instance across the app lifecycle (`@Singleton`). Avoid overusing `@Singleton` as it keeps objects in memory permanently.
3. **Dispatchers:** **NEVER** hardcode `Dispatchers.IO` or `Dispatchers.Main` inside your classes. Inject `AppDispatchers` provided by `DispatcherModule`. This allows swapping dispatchers out during testing.
4. **ViewModel Injection:** Use `@HiltViewModel` for ViewModels and `@Inject constructor` for their dependencies. Do not pass parameters manually unless using Assisted Injection.
5. **No Static Access:** Avoid global or static references to contexts or dependencies. Everything should be injected.

*See also: [[package-map]], [[testing-guidelines]]*
