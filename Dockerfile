FROM alpine:3.7


# --- configuration section ----------------------
ENV DOCKERIMAGE_AGENTSPEAK_VERSION HEAD

ENV DOCKERIMAGE_MAVEN_VERSION 3.5.2
ENV DOCKERIMAGE_GLIBC_VERSION 2.26-r0
ENV DOCKERIMAGE_JAVA_DOWNLOAD http://download.oracle.com/otn-pub/java/jdk/8u152-b16/aa0333dd3019491ca4f6ddbe78cdb6d0/jdk-8u152-linux-x64.tar.gz


# --- dependencies section -----------------------
RUN wget -O /etc/apk/keys/sgerrand.rsa.pub https://raw.githubusercontent.com/sgerrand/alpine-pkg-glibc/master/sgerrand.rsa.pub
RUN wget -O /tmp/glibc.apk https://github.com/sgerrand/alpine-pkg-glibc/releases/download/$DOCKERIMAGE_GLIBC_VERSION/glibc-$DOCKERIMAGE_GLIBC_VERSION.apk
RUN wget -O /tmp/glibc-bin.apk https://github.com/sgerrand/alpine-pkg-glibc/releases/download/$DOCKERIMAGE_GLIBC_VERSION/glibc-bin-$DOCKERIMAGE_GLIBC_VERSION.apk
RUN wget -O /tmp/glibc-i18n.apk https://github.com/sgerrand/alpine-pkg-glibc/releases/download/$DOCKERIMAGE_GLIBC_VERSION/glibc-i18n-$DOCKERIMAGE_GLIBC_VERSION.apk

RUN wget -O /tmp/maven.tar.gz http://archive.apache.org/dist/maven/maven-3/$DOCKERIMAGE_MAVEN_VERSION/binaries/apache-maven-$DOCKERIMAGE_MAVEN_VERSION-bin.tar.gz && mkdir -p /opt/maven && tar --strip 1 -zxvf /tmp/maven.tar.gz -C /opt/maven
RUN wget -O /tmp/java.tar.gz --no-check-certificate -c --header "Cookie: oraclelicense=accept-securebackup-cookie" $DOCKERIMAGE_JAVA_DOWNLOAD && mkdir -p /opt/java && tar --strip 1 -zxvf /tmp/java.tar.gz -C /opt/java

RUN apk --no-cache update &&\
    apk --no-cache upgrade &&\
    apk --no-cache add git ca-certificates /tmp/glibc.apk /tmp/glibc-bin.apk /tmp/glibc-i18n.apk
RUN /usr/glibc-compat/bin/localedef -i en_US -f UTF-8 en_US.UTF-8


# --- machine configuration section --------------
ENV JAVA_HOME /opt/java
ENV PATH /opt/maven/bin:$JAVA_HOME/bin:$PATH

RUN git clone https://github.com/LightJason/AgentSpeak.git /tmp/agentspeak
RUN cd /tmp/agentspeak && git checkout $DOCKERIMAGE_AGENTSPEAK_VERSION
RUN cd /tmp/agentspeak && mvn install -DskipTests

RUN rm -rf /tmp/*
