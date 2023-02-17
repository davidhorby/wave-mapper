 ../gradlew shadowJar

 gcloud config set project analytics-springernature

 gcloud config set functions/region europe-west2

 gcloud functions deploy sg-bucket-to-ds  --entry-point com.dhorby.gcloud.BucketToDSFunc \
 --runtime java17 \
 --trigger-bucket gs://sail-game-data \
 --allow-unauthenticated \
 --memory 512MB \
 --source=build/libs

