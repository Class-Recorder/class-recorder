FROM ubuntu

#Non interactive installation
#and common installations
ENV DEBIAN_FRONTEND noninteractive
RUN apt-get update && apt-get install -y --no-install-recommends software-properties-common
RUN rm -rf /var/lib/apt/lists/*

#Install java and ffmpeg
RUN \
  echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && \
  add-apt-repository -y ppa:webupd8team/java && \
  add-apt-repository ppa:mc3man/xerus-media && \
  apt-get update && \
  apt-get install -y --no-install-recommends oracle-java8-installer ffmpeg && \
  rm -rf /var/lib/apt/lists/* && \
  rm -rf /var/cache/oracle-jdk8-installer

VOLUME /tmp
ADD /target/classrecorder-0.1.0.jar myapp.jar
RUN sh -c 'touch /myapp.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/myapp.jar"]



