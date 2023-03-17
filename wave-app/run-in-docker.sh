export MAPS_API_KEY=`gcloud secrets versions access latest --secret="mapsApiKey"`
export MET_OFFICE_API_KEY=`gcloud secrets versions access latest --secret="MetOfficeApiKey"`
export MAPS_API_SERVER_KEY=`gcloud secrets versions access latest --secret="mapsApiKeyServer"`
docker-compose up
