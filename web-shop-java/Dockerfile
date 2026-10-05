FROM eclipse-temurin:25-jdk-noble AS build
ARG MAVEN_VERSION=3.9.16
WORKDIR /build
RUN apt-get update && apt-get install -y --no-install-recommends curl ca-certificates tar \
    && curl -fsSL "https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz" -o /tmp/maven.tar.gz \
    && tar -xzf /tmp/maven.tar.gz -C /opt \
    && ln -s "/opt/apache-maven-${MAVEN_VERSION}/bin/mvn" /usr/local/bin/mvn \
    && rm -rf /var/lib/apt/lists/* /tmp/maven.tar.gz
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B clean package

FROM eclipse-temurin:25-jdk-noble
ARG TOMCAT_VERSION=11.0.26
ENV CATALINA_HOME=/opt/tomcat \
    PATH="/opt/tomcat/bin:${PATH}"
RUN apt-get update && apt-get install -y --no-install-recommends curl ca-certificates tar \
    && curl -fsSL "https://archive.apache.org/dist/tomcat/tomcat-11/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.tar.gz" -o /tmp/tomcat.tar.gz \
    && mkdir -p /opt/tomcat \
    && tar -xzf /tmp/tomcat.tar.gz --strip-components=1 -C /opt/tomcat \
    && printf '\norg.apache.catalina.core.ContainerBase.[Catalina].[localhost].handlers = 2localhost.org.apache.juli.AsyncFileHandler,java.util.logging.ConsoleHandler\n' >> /opt/tomcat/conf/logging.properties \
    && rm -rf /opt/tomcat/webapps/* /var/lib/apt/lists/* /tmp/tomcat.tar.gz
COPY --from=build /build/target/ROOT.war /opt/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
