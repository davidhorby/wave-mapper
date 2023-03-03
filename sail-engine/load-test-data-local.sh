curl -m 70 -X POST http://localhost:8080/sg-http-to-bucket \
-H "Authorization: bearer $(gcloud auth print-identity-token)" \
-H "Content-Type: application/json" \
-d '{
      "id": "a12345678",
      "name": "Barbera",
      "geoLocation": {
        "lat": 44.17,
        "lon": -0.05
      },
      "pieceType": "SHARK"
    }'