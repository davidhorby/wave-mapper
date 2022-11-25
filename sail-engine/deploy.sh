 gcloud functions deploy dhorby-test2-gcp-tmp --entry-point com.dhorby.gcloud.CanvasFunc \
 --runtime java17 \
 --trigger-bucket gs://dhorby-function-test \
 --allow-unauthenticated \
 --memory 512MB
