#!/usr/bin/env bash

gcloud config set project local

gcloud beta emulators datastore start --data-dir=./.datastore-docker --host-port 0.0.0.0:8081


