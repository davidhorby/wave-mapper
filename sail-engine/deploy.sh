 gcloud functions deploy sg-bucket-to-ds  \
 --runtime java17 \
 --trigger-bucket gs://sail-game-data \
 --allow-unauthenticated \
 --memory 512MB \
 --source=build/libs
