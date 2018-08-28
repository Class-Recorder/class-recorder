FROM ubuntu

RUN apt-get update \
	&& apt-get install -y --no-install-recommends software-properties-common \
	&& rm -rf /var/lib/apt/lists/*

RUN apt-get update \
	&& apt-get install -y --no-install-recommends ffmpeg libxrender1 libxtst6 \
	libxi6 pulseaudio socat alsa-utils \
	&& rm -rf /var/lib/apt/lists/*
RUN apt-get update \
	&& apt-get install -y openjdk-8-jdk \
	&& rm -rf /var/lib/apt/lists/*

RUN apt-get update &&\ 
    apt-get install -y firefox \
    && rm -rf /var/lib/apt/lists/*

ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64
ENV PATH $JAVA_HOME/bin:$PATH
ENV APP_DIR /app

WORKDIR $APP_DIR

COPY . $APP_DIR

ENTRYPOINT [ "java", "-Xmx512m", "-jar", "classrecorderpc.jar"]
