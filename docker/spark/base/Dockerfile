FROM ubuntu:latest

ENV DEBIAN_FRONTEND noninteractive
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64/

ENV HIVE_VERSION 3.1.2
ENV HIVE_URL https://archive.apache.org/dist/hive/hive-$HIVE_VERSION/apache-hive-$HIVE_VERSION-bin.tar.gz

ENV SPARK_VERSION=2.3.0
#ENV SPARK_VERSION=3.1.1
ENV HADOOP_VERSION=2.7
#ENV SPARK_URL https://dist.apache.org/repos/dist/release/spark/spark-$SPARK_VERSION/spark-$SPARK_VERSION-bin-hadoop$HADOOP_VERSION.tgz
ENV SPARK_URL https://archive.apache.org/dist/spark/spark-$SPARK_VERSION/spark-$SPARK_VERSION-bin-hadoop$HADOOP_VERSION.tgz

ENV PG_URL https://jdbc.postgresql.org/download/postgresql-42.2.16.jar
ENV MDB_URL https://repo1.maven.org/maven2/org/mariadb/jdbc/mariadb-java-client/2.6.0/mariadb-java-client-2.6.0.jar
ENV EXA_URL https://maven.exasol.com/artifactory/exasol-releases/com/exasol/exasol-jdbc/7.0.0/exasol-jdbc-7.0.0.jar

ENV SPARK_HOME /opt/spark
ENV HIVE_HOME /opt/hive
ENV USER root
ENV PATH $SPARK_HOME/bin/:$PATH
ENV PATH $HIVE_HOME/bin:$PATH


RUN apt-get update \
    && apt-get install -y --no-install-recommends \
        openjdk-8-jdk \
        net-tools \
        curl \
        netcat \
        gnupg \
        libsnappy-dev \
        nano \
    && rm -rf /var/lib/apt/lists/*

#RUN curl -O https://dist.apache.org/repos/dist/release/spark/KEYS
#RUN gpg --import KEYS

RUN set -x \
    && curl -fSL "$SPARK_URL" -o /tmp/spark.tgz \
#    && curl -fSL "$SPARK_URL.asc" -o /tmp/spark.tgz.asc \
#    && gpg --verify /tmp/spark.tgz.asc \
    && tar -xvzf /tmp/spark.tgz -C /opt/ \
    && mv /opt/spark-$SPARK_VERSION-bin-hadoop$HADOOP_VERSION /opt/spark
#    && rm /tmp/spark.tgz*

RUN set -x \
    && curl -fSL "$HIVE_URL" -o /tmp/hive.tar.gz \
    #&& curl -fSL "$HIVE_URL.asc" -o /tmp/hive.tar.gz.asc \
    #&& gpg --verify /tmp/hive.tar.gz.asc \
    && tar -xvf /tmp/hive.tar.gz -C /opt/ \
    && mv /opt/apache-hive-$HIVE_VERSION-bin /opt/hive \
    && rm /tmp/hive.tar.gz*

RUN curl -fSL "$PG_URL" -o $SPARK_HOME/jars/postgresql-jdbc.jar
RUN curl -fSL "$MDB_URL" -o $SPARK_HOME/jars/mariadb-jdbc.jar
RUN curl -fSL "$EXA_URL" -o $SPARK_HOME/jars/exasol-jdbc.jar

COPY entrypoint.sh /entrypoint.sh
RUN chmod a+x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]