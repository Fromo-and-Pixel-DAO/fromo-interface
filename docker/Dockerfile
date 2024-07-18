FROM  eclipse-temurin:17-jre
MAINTAINER fromo

# volume
VOLUME /usr/local/application/fromo_run
# create directory
RUN mkdir -p /usr/local/application/fromo_run
# set work directory
WORKDIR /usr/local/application/fromo_run
# copy jar
COPY ./froopylandinterface-0.0.1-SNAPSHOT.jar /usr/local/application/fromo_run/froopylandinterface-0.0.1-SNAPSHOT.jar
# start
ENTRYPOINT ["java","-jar","froopylandinterface-0.0.1-SNAPSHOT.jar"]