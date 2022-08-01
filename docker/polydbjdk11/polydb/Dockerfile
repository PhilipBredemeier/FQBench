FROM ubuntu:latest

ENV DEBIAN_FRONTEND noninteractive
ENV JAVA_HOME /usr/lib/jvm/java-11-openjdk-amd64/
ENV USER root

RUN apt-get update \
    && apt-get install -y --no-install-recommends \
        openjdk-11-jdk \
        net-tools \
        curl \
        netcat \
        gnupg \
        libsnappy-dev \
        git \
        maven \
    && rm -rf /var/lib/apt/lists/*

RUN update-alternatives --set java /usr/lib/jvm/java-11-openjdk-amd64/bin/java
