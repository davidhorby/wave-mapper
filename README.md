Data Point Site List

http://datapoint.metoffice.gov.uk/public/data/val/wxmarineobs/all/xml/sitelist?res=3hourly&key=1e9e146a-63d3-4b42-b145-b581a45d4b8a

Wave URL

"http://datapoint.metoffice.gov.uk/public/data/val/wxmarineobs/all/xml/162304?res=3hourly&key=1e9e146a-63d3-4b42-b145-b581a45d4b8a"

### Setup Application Default Credentials (ADC)
gcloud auth login --update-adc

# Check it is the correct project
gcloud config get-value project

# Set the region
gcloud config set functions/region europe-west1

# Deploy the app
gcloud app deploy ./build/libs/wave-mapper-1.0-SNAPSHOT.jar

# Stream logs from the command line by running
gcloud app logs tail -s default

# View application in the web browser run
gcloud app browse

