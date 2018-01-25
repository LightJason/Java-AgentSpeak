FROM lightjason/docker:jdk


# --- configuration section ----------------------
ENV DOCKERIMAGE_AGENTSPEAK_VERSION HEAD


# --- content configuration section --------------
RUN git clone https://github.com/LightJason/AgentSpeak.git /tmp/agentspeak
RUN cd /tmp/agentspeak && git checkout $DOCKERIMAGE_AGENTSPEAK_VERSION
RUN cd /tmp/agentspeak && mvn install -DskipTests
