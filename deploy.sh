./gradlew clean shadowJar
gcloud app deploy ./build/libs/wave-mapper-1.0-SNAPSHOT.jar
