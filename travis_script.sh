#!/bin/sh
npm run build
docker build -t cruizba/class-recorder builds/class-recorder-teacher-pc-server