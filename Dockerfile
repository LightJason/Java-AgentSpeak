FROM alpine:3.7
MAINTAINER LightJason <info@lightjason.org>

# --- configuration section ----------------------
ENV AGENTSPEAK_VERSION HEAD

ENV MAVEN_VERSION 3.5.2
ENV GLIBC_VERSION 2.26-r0
ENV JAVA_DOWNLOAD http://download.oracle.com/otn-pub/java/jdk/8u152-b16/aa0333dd3019491ca4f6ddbe78cdb6d0/jdk-8u152-linux-x64.tar.gz


# --- dependencies section -----------------------
RUN wget -O /etc/apk/keys/sgerrand.rsa.pub https://raw.githubusercontent.com/sgerrand/alpine-pkg-glibc/master/sgerrand.rsa.pub
RUN wget -O /tmp/glibc.apk https://github.com/sgerrand/alpine-pkg-glibc/releases/download/$GLIBC_VERSION/glibc-$GLIBC_VERSION.apk
RUN wget -O /tmp/glibc-bin.apk https://github.com/sgerrand/alpine-pkg-glibc/releases/download/$GLIBC_VERSION/glibc-bin-$GLIBC_VERSION.apk
RUN wget -O /tmp/glibc-i18n.apk https://github.com/sgerrand/alpine-pkg-glibc/releases/download/$GLIBC_VERSION/glibc-i18n-$GLIBC_VERSION.apk

RUN wget -O /tmp/maven.tar.gz http://archive.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz && mkdir -p /opt/maven && tar --strip 1 -zxvf /tmp/maven.tar.gz -C /opt/maven
RUN wget -O /tmp/java.tar.gz --no-check-certificate -c --header "Cookie: oraclelicense=accept-securebackup-cookie" $JAVA_DOWNLOAD && mkdir -p /opt/java && tar --strip 1 -zxvf /tmp/java.tar.gz -C /opt/java

RUN apk --no-cache update &&\
    apk --no-cache upgrade &&\
    apk --no-cache add git ca-certificates /tmp/glibc.apk /tmp/glibc-bin.apk /tmp/glibc-i18n.apk
RUN /usr/glibc-compat/bin/localedef -i en_US -f UTF-8 en_US.UTF-8


# --- build section ------------------------------
ENV JAVA_HOME /opt/java
ENV PATH /opt/maven/bin:$JAVA_HOME/bin:$PATH

RUN git clone https://github.com/LightJason/AgentSpeak.git /tmp/agentspeak
RUN cd /tmp/agentspeak && git checkout $AGENTSPEAK_VERSION
RUN cd /tmp/agentspeak && mvn install -DskipTests

RUN rm -rf /tmp/*
