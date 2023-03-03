curl -m 70 -X POST http://localhost:8080/sg-http-to-bucket \
-H "Authorization: bearer $(gcloud auth print-identity-token)" \
-H "Content-Type: application/json" \
-d '{
      "id": "5678",
      "name": "Bert",
      "geoLocation": {
        "lat": 49.17,
        "lon": -5.05
      },
      "pieceType": "SHARK"
    }'