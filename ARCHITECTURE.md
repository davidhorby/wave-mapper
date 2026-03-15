# Wave Mapper — Architecture Diagram

```mermaid
flowchart TD
    Browser["🌐 Browser\nHandlebars UI + Google Maps"]

    subgraph GCP["☁️ Google Cloud Platform  (analytics-springernature · europe-west2)"]

        subgraph AppEngine["App Engine — wave-app (Jetty :8080)"]
            direction TB

            subgraph Routes["PolyHandler — WaveServiceRoutes"]
                HTTP["HttpRoutes\nGET /  ·  POST /\nGET /data  ·  GET /map\nGET /ping  ·  GET /api"]
                WS["WsRoutes\n/move  ·  /start  ·  /clear\n/reset  ·  /post  ·  /message"]
            end

            subgraph Hex["Hexagonal Core"]
                WP["«port»\nWavePort"]
                SP["«port»\nStoragePort"]
                WA["«adapter»\nWaveAdapter"]
                SA["«adapter»\nStorageAdapter"]
            end

            WH["WaveHandlers"]
            RA["RaceActions\nstart · move · clear\nreset · add · delete"]
            WavePage["WavePage\n(ViewModel → Handlebars)"]
        end

        subgraph Shared["shared module"]
            DSC["DataStoreClient\n(Storable)"]
            GD["GeoDistance\n(Haversine formula)"]
            NAV["sailMove()\n(bearing + speed → new GeoLocation)"]
            CONST["Constants\n(secret resolver)"]
        end

        subgraph Domain["domain module"]
            Models["PieceLocation  ·  PieceType\nGeoLocation  ·  WaveLocation\nPlayer  ·  Site  ·  Bearing"]
        end

        DS[("Cloud Datastore\nkind: PIECE_LOCATION\nfields: id · name · location · type")]
        SM["🔐 Secret Manager\nmapsApiKey\nmapsApiKeyServer\nMetOfficeApiKey"]
    end

    MetOffice["🌊 Met Office DataPoint API\nXML marine buoy observations\n(site list + wave readings)"]
    GMaps["🗺️ Google Maps API\n(geocoding + map tiles)"]

    %% Browser ↔ App
    Browser -- "HTTP GET / → rendered map page" --> HTTP
    Browser -- "HTTP POST / → add piece" --> HTTP
    Browser -- "WebSocket (real-time race events)" --> WS

    %% HTTP routing
    HTTP --> WH
    WH --> WP
    WP --> WA
    WA -- "getWavePage()\ngetWaveData()" --> SA
    WA -- "getLocationData()" --> GMapsClient["GoogleMapsClientApi"]
    WA -- "getSiteList()" --> MOClient["MetOfficeClient"]
    WA --> WavePage

    %% WebSocket routing
    WS --> RA
    RA --> SP
    SP --> SA
    RA -- "move()" --> NAV

    %% Storage
    SA --> DSC
    DSC --> DS

    %% Distance calculation
    SA --> GD

    %% External APIs
    MOClient -- "XML site list + observations" --> MetOffice
    GMapsClient --> GMaps

    %% Secrets
    CONST -- "RUN_WITH_LOCAL_KEYS=false" --> SM

    %% Domain used everywhere
    Models -. "data models" .-> SA
    Models -. "data models" .-> RA
```

## Game Piece Types

| Type | Role |
|------|------|
| `BOAT` | Player vessels — moved each tick via `sailMove()` |
| `SHARK` | Hazard pieces on the map |
| `PIRATE` | Hazard pieces on the map |
| `START` | Newport, Rhode Island (41.29°N, 71.19°W) |
| `FINISH` | Lisbon (38.41°N, 9.09°W) |
| `WAVE` | Met Office buoy observation positions |

## Key Data Flows

1. **Page load** — `GET /` → `WaveAdapter.getWavePage()` fetches all `PieceLocation` entities from Datastore + live Met Office buoy positions → serialised to Google Maps JSON → rendered into `WavePage.hbs`
2. **Race tick** — WebSocket `/move` → `RaceActions.move()` → reads all `BOAT` pieces from Datastore → applies `sailMove()` (bearing + speed navigation) → writes updated positions back to Datastore → pushes new map state to browser over WS
3. **Race start** — `/start` → moves all boats to `START` position, adds `START`/`FINISH` markers
4. **Wave data** — `GET /data` → `MetOfficeClient.getSiteList()` → parses XML from Met Office DataPoint API → returns `WaveLocation` list

