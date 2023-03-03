ENV='local' \
DATASTORE_HOST='http://localhost:8081' \
DATASTORE_PROJECT_ID='analytics-springernature' \
../gradlew runFunction -Prun.functionTarget=com.dhorby.gcloud.HttpToBucketFunc -Prun.port=8080