FROM adoptopenjdk/openjdk11:alpine
EXPOSE 9011
ADD target/oms-fill-0.0.1-SNAPSHOT.jar oms-fill-0.0.1-SNAPSHOT.jar
RUN mkdir -p /var/log/oms
ENV username=""
ENV password=""
ENV dbUrl=""
ENV logpath=""
ENV env=""
CMD java -jar -Denv=${env} -Ddb.url=${dbUrl} -Ddb.username=${username} -Ddb.password=${password} -Dlogging.file.path=${logpath} oms-fill-0.0.1-SNAPSHOT.jar