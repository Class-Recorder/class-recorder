#!/bin/sh
xhost +local: 
echo "Opened xhost to local connections"
export AUDIO_GROUP_CR=$(getent group audio | cut -d: -f3)
echo "Created AUDIO_GROUP_CR environment variable to ${AUDIO_GROUP_CR}"
echo ${MYSQL_URL_CR}
echo ${YOUTUBE_CLIENT_ID}
echo ${YOUTUBE_SECRET}
# docker-compose up