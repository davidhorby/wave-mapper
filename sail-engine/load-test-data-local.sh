curl -m 70 -X POST http://localhost:8080/sg-http-to-bucket \
-H "Authorization: bearer $(gcloud auth print-identity-token)" \
-H "Content-Type: application/json" \
-d '{
      "id": "1",
      "name": "Geraldine",
      "geoLocation": {
        "lat": 47.17,
        "lon": -8.05
      },
      "pieceType": "SHARK"
    }'