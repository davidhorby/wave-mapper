Data Point Site List

http://datapoint.metoffice.gov.uk/public/data/val/wxmarineobs/all/xml/sitelist?res=3hourly&key=<metofficekey>

Wave URL

http://datapoint.metoffice.gov.uk/public/data/val/wxmarineobs/all/xml/162304?res=3hourly&key=<metofficekey>

# The Wave Mapper App

## Pre-requisites
gcloud

gcloud auth configure-docker

#### Run locally
./gradlew wave-app:run

### Run in docker
./run-in-docker.sh

# Deploying the application in the cloud

#### Setup Application Default Credentials (ADC)
```bash
gcloud auth login --update-adc
```

#### Check it is the correct project
```bash
gcloud config get-value project
```
#### Set the project of not correct
```bash
gcloud config set project analytics-springernature
```

#### Set the region
```bash
gcloud config set functions/region europe-west2
```

#### Build the deployable jar (from top of repo)
```bash
../gradlew shadowJar
```

#### Deploy the app
```bash
gcloud app deploy ./build/libs/wave-app.jar --appyaml=./app.yaml
```

#### Stream logs from the command line by running
```bash
gcloud app logs tail -s default
```

#### View application in the web browser run
```bash
gcloud app browse
```

## Swagger
http://localhost/openapi/index.html

### Debugging
````bash
gcloud app --project analytics-springernature instances disable-debug
````
