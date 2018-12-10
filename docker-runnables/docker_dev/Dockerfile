FROM ubuntu

# Versions
ENV NODE_VERSION=11.0.0 \
    NPM_VERSION=6.4.1 \
    IONIC_VERSION=4.5.0 \
    CORDOVA_VERSION=8.1.2
# Android

ENV ANDROID_SDK_URL="https://dl.google.com/android/repository/tools_r25.2.5-linux.zip" \
    ANDROID_HOME="/opt/android" \
    ANDROID_BUILD_TOOLS_VERSION=27.0.0 \
    ANDROID_APIS="android-10,android-15,android-16,android-17,android-18,android-19,android-20,android-21,android-22,android-23,android-24,android-25,android-26"

#Path
ENV PATH $PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools:$ANDROID_HOME/build-tools/$ANDROID_BUILD_TOOLS_VERSION

###### Basic stuff
RUN apt-get update \ 
    && apt-get install -y --no-install-recommends software-properties-common \
    curl gnupg ant gradle wget \
    && rm -rf /var/lib/apt/lists/*

####### Ffmpeg
RUN apt-get update \ 
    && apt-get install -y --no-install-recommends ffmpeg libxrender1 libxtst6 \
	libxi6 pulseaudio socat alsa-utils \
    && rm -rf /var/lib/apt/lists/*

####### Jdk 8
RUN apt-get update \ 
    && apt-get install -y openjdk-8-jdk \
    && rm -rf /var/lib/apt/lists/*

####### Maven
RUN apt-get update \ 
    && apt-get install -y maven \
    && rm -rf /var/lib/apt/lists/*

####### Node, npm, cordova and ionic
RUN apt-get update \
    && curl -sL https://deb.nodesource.com/setup_11.x | bash \
    && apt-get install -y nodejs \
    && npm install -g npm@"$NPM_VERSION" \
    && npm install -g cordova@"$CORDOVA_VERSION" ionic@"$IONIC_VERSION" \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /opt
####### Android sdk
RUN dpkg --add-architecture i386 && \
    apt-get update && \
    apt-get install -y libncurses5:i386 libstdc++6:i386 zlib1g:i386 && \
    # Installs Android SDK
    mkdir android && cd android && \
    wget -O tools.zip ${ANDROID_SDK_URL} && \
    unzip tools.zip && rm tools.zip && \
    echo y | android update sdk -a -u -t platform-tools,${ANDROID_APIS},build-tools-${ANDROID_BUILD_TOOLS_VERSION} && \
    chmod a+x -R $ANDROID_HOME && \
    chown -R root:root $ANDROID_HOME && \
    # Clean up
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/* && \
    apt-get autoremove -y && \
    apt-get clean

ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64
ENV PATH $JAVA_HOME/bin:$PATH
ENV DEV_DIR /class-recorder

WORKDIR $DEV_DIR