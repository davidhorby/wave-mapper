export MAPS_API_KEY=`gcloud secrets versions access 1 --secret="mapsApiKey"`
export MET_OFFICE_API_KEY=`gcloud secrets versions access 1 --secret="MetOfficeApiKey"`
docker-compose up
