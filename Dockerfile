ARG BUILD_HOME=/app-spring-ioc
FROM gradle:jdk17 as build-image
ARG BUILD_HOME
ENV APP_HOME=$BUILD_HOME
WORKDIR $APP_HOME
COPY --chown=gradle:gradle build.gradle settings.gradle $APP_HOME/
COPY --chown=gradle:gradle src $APP_HOME/src
RUN gradle --no-daemon build

FROM tomcat:10.1.17
ARG BUILD_HOME
ENV APP_HOME=$BUILD_HOME
RUN mv /usr/local/tomcat/webapps /usr/local/tomcat/webapps2
RUN mv /usr/local/tomcat/webapps.dist /usr/local/tomcat/webapps
COPY --from=build-image $APP_HOME/build/libs/*.war /usr/local/tomcat/webapps/service.war
EXPOSE 8086
CMD ["catalina.sh", "run"]