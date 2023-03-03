 ../gradlew shadowJar

 gcloud config set project analytics-springernature

 gcloud config set functions/region europe-west2

 gcloud functions deploy sharkfrenzy \
 --env-vars-file ./env-live.yaml \
 --entry-point com.dhorby.gcloud.SharkFrenzyFunc \
 --runtime java17 \
 --trigger-http \
 --allow-unauthenticated \
 --memory 512MB \
 --source=build/libs

