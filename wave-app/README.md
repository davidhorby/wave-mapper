Data Point Site List

http://datapoint.metoffice.gov.uk/public/data/val/wxmarineobs/all/xml/sitelist?res=3hourly&key=<metofficekey>

Wave URL

http://datapoint.metoffice.gov.uk/public/data/val/wxmarineobs/all/xml/162304?res=3hourly&key=<metofficekey>

# The Wave Mapper App

## Pre-requisites
gcloud

#### Run locally
./gradlew wave-app:run

### Run in docker
./run-in-docker.sh

# Deploying the application in the cloud

#### Setup Application Default Credentials (ADC)
gcloud auth login --update-adc

#### Check it is the correct project
gcloud config get-value project

#### Set the project of not correct
gcloud config set project analytics-springernature

#### Set the region
gcloud config set functions/region europe-west1

#### Build the deployable jar (from top of repo)
./gradlew wave-app:shadowJar

#### Deploy the app
gcloud app deploy ./wave-app/build/libs/wave-app.jar --appyaml=./wave-app/app.yaml

#### Stream logs from the command line by running
gcloud app logs tail -s default

#### View application in the web browser run
gcloud app browse

