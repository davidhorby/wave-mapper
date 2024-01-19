![image](https://github.com/davidhorby/wave-mapper/assets/16700900/39f9e5b9-ea97-49dc-b266-2585339676c5)


Data Point Site List

http://datapoint.metoffice.gov.uk/public/data/val/wxmarineobs/all/xml/sitelist?res=3hourly&key=<metofficekey>

Wave URL

http://datapoint.metoffice.gov.uk/public/data/val/wxmarineobs/all/xml/162304?res=3hourly&key=<metofficekey>


## Prerequisites

Java JDK 17 - note that we do not use the Oracle JDK.

It is easiest to manage the JDK by using SDKMAN!

- Install [SDKMAN!](https://sdkman.io/install)
- `sdk install java 17.0.6-librca`
- `vi ~/.sdkman/etc/config`
    - ensure this line exists in that file: `sdkman_auto_env=true`

Open new terminal. When you cd into a directory with an appropriate `.sdkmanrc` file, sdkman will set the appropriate jdk for you.


### Setup Application Default Credentials (ADC)
```bash
gcloud auth login --update-adc
```
# Check it is the correct project
```bash
gcloud config get-value project
```
# Set the project
```bash
gcloud config set project analytics-springernature
```
# Set the region
```bash
gcloud config set functions/region europe-west2
```
## Install cloud datastore emulator
```bash
gcloud components install cloud-datastore-emulator
```
```bash
gcloud beta emulators datastore start --data-dir=./.datastore-localdb
```
```bash
export DATASTORE_EMULATOR_HOST=localhost:8081
```
To run the datastore emulator in docker
```bash
docker compose -f docker-compose-datastore.yml up
```



### Automatically set the environment variable
```bash
gcloud beta emulators datastore env-init
```
will result in 

export DATASTORE_DATASET=analytics-springernature
export DATASTORE_EMULATOR_HOST=localhost:8081
export DATASTORE_EMULATOR_HOST_PATH=localhost:8081/datastore
export DATASTORE_HOST=http://localhost:8081
export DATASTORE_PROJECT_ID=analytics-springernature


[//]: # ($&#40;gcloud beta emulators datastore env-init&#41;)

## Build and deploy the wave application

### Build the deployable jar
```bash
./gradlew wave-app:clean wave-app:shadowJar
```

### Deploy the app
```bash
gcloud app deploy ./wave-app/build/libs/wave-app.jar --appyaml=./wave-app/app.yaml
```

### Stream logs from the command line by running
```bash
gcloud app logs tail -s default
```

### View application in the web browser run
```bash
gcloud app browse
```

### Build the cloud function jar
```bash
./gradlew wave-app:clean wave-app:shadowJar
```

### Deploy the cloud function jar
```bash
gcloud app deploy ./wave-app/build/libs/wave-app.jar
```

