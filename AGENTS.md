# AGENTS.md — wave-mapper

## Project Overview
A sailing race game layered on real-world ocean wave data. Boats, sharks, and pirates race across real Met Office marine observation buoy positions. The main app is deployed to Google App Engine; data-loading routines run as Google Cloud Functions (`sail-engine`).

GCP project: `analytics-springernature` (europe-west2).

## Module Structure
| Module | Role |
|---|---|
| `domain` | Pure Kotlin data models only (`PieceLocation`, `GeoLocation`, `WaveLocation`, `PieceType`, `Player`, etc.) — no dependencies |
| `shared` | Shared business logic: `DataStoreClient`, `GeoDistance`, `WaveServiceFunctions`, secrets, env settings. Used by both `wave-app` and `sail-engine`. |
| `wave-app` | Main http4k web application (Jetty/App Engine). Hexagonal ports/adapters, HTTP + WebSocket routes, Handlebars UI. |
| `sail-engine` | Google Cloud Functions for loading data into Firestore/Cloud Storage. |

## Critical Developer Commands
```bash
# Run locally (starts on :8080)
./gradlew :wave-app:run

# Build deployable fat jar
./gradlew wave-app:shadowJar          # → wave-app/build/libs/wave-app-all.jar

# Run all tests
./gradlew test

# Deploy to App Engine
./gradlew :wave-app:appengineDeploy
# or: gcloud app deploy ./wave-app/build/libs/wave-app-all.jar --appyaml=./wave-app/src/main/appengine/app.yaml

# Start local Datastore emulator (required for integration tests)
docker compose -f docker-compose-datastore.yml up
# then: export DATASTORE_EMULATOR_HOST=localhost:8081
```

## Architecture Patterns

### Hexagonal (Ports & Adapters)
`wave-app` ports live in `port/` (e.g., `StoragePort`, `WavePort`) — these are the contracts. Adapters in `adapter/` wire ports to real implementations:
- `StorageAdapter` implements `StoragePort` via `DataStoreClient` (from `shared`)
- `WaveAdapter` implements `WavePort`, composing `StorageAdapter`, `GoogleMapsClientApi`, `MetOfficeClient`

### http4k Idioms
- **Lenses** for all request/response parsing: `Body.auto<WaveLocation>().toLens()`, `WsMessage.auto<AddPieceWsMessage>().toLens()` — see `Helpers.kt`
- **PolyHandler** in `WaveServiceRoutes` combines `HttpRoutes` and `WsRoutes` (HTTP + WebSocket on the same server)
- **Events** pipeline wired at startup in `WaveMapperHttp4kApp.main` using `EventFilters.AddTimestamp().then(...).then(AutoMarshallingEvents(Jackson))`
- OpenAPI contract served at `/api` via `http4k-contract`

### WebSocket Routes
Real-time race actions at `/move`, `/start`, `/clear`, `/reset`, `/post`, `/message`. Stateful sockets stored as `var` on the `WsRoutes` object.

## Environment Variables & Secrets
```
RUN_WITH_LOCAL_KEYS=true        # use env vars below instead of GCP Secret Manager
MAPS_API_KEY=...
MAPS_API_SERVER_KEY=...
MET_OFFICE_API_KEY=...
ARTIFACTORY_USERNAME=...        # required for Gradle dependency resolution
ARTIFACTORY_PASSWORD=...
```
In production, `Constants.kt` fetches secrets from **GCP Secret Manager** (`AccessSecretVersion`) using secret names: `mapsApiKey`, `mapsApiKeyServer`, `MetOfficeApiKey`.

## Testing Conventions
- **Test marker interfaces** in `TestTags.kt`: `IntegrationTest` (`@Tag("integration")`) and `FunctionalTest` (`@Tag("functional")`)
- **In-memory Datastore** via `DataStoreExtension` (JUnit 5 extension from `shared/testFixtures`): use `@ExtendWith(DataStoreExtension::class)` or `@RegisterExtension val server = DataStoreExtension().builder().build()`. Resets between tests; consistency set to 1.0.
- **Shared test data** in `shared/src/testFixtures` — `TestData` object with canonical `PieceLocation`, `WaveLocation`, etc. Add `testImplementation(testFixtures(project(":shared")))` to use.
- All test suites use JUnit Jupiter (`useJUnitPlatform()` globally in root `build.gradle.kts`).

## External Integrations
| Service | Purpose | Config |
|---|---|---|
| Met Office DataPoint API | Marine observation XML (site list + wave readings) | `MET_OFFICE_API_KEY` |
| Google Maps | Map rendering in UI | `MAPS_API_KEY` / `MAPS_API_SERVER_KEY` |
| Google Cloud Datastore | Game state persistence (`EntityKind.PIECE_LOCATION`) | ADC / `DATASTORE_EMULATOR_HOST` locally |
| GCP Secret Manager | API keys in production | `accessSecretVersion("mapsApiKey")` |

## Key Files
- `wave-app/src/main/kotlin/.../WaveMapperHttp4kApp.kt` — app entry point
- `wave-app/src/main/kotlin/.../routes/WaveServiceRoutes.kt` — top-level routing composition
- `shared/src/main/kotlin/.../wavemapper/Constants.kt` — secret/env resolution logic
- `shared/src/main/kotlin/.../external/storage/DataStoreClient.kt` — Datastore CRUD
- `shared/src/testFixtures/.../external/junit/DataStoreExtension.kt` — in-memory Datastore for tests
- `gradle/libs.versions.toml` — all dependency versions (version catalog)

