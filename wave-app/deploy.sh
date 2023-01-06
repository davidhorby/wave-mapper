../gradlew shadowJar

gcloud app deploy ./build/libs/wave-app.jar --appyaml=./app.yaml
