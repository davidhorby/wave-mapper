curl -m 70 -X POST https://europe-west2-analytics-springernature.cloudfunctions.net/sg-http-to-bucket \
-H "Authorization: bearer $(gcloud auth print-identity-token)" \
-H "Content-Type: application/json" \
-d '{ "hello":"dave"}'