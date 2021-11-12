FROM adoptopenjdk/openjdk11:alpine
EXPOSE 9005
ADD target/auth-0.0.1-SNAPSHOT.jar oms-auth-0.0.1-SNAPSHOT.jar
RUN mkdir -p /var/log/oms
ENV env=""
ENV username=""
ENV password=""
ENV signKey=""
ENV refreshKey=""
ENV authKey=""
ENV dbUrl=""
ENV logpath=""
CMD java -jar -Denv=${env} -Ddb.url=${dbUrl} -Ddb.username=${username} -Ddb.password=${password} -Djwt.signing.key=${signKey} -Djwt.refreshtoken.signing.key=${refreshKey} -Djwt.authorities.key=${authKey} -Dlogging.file.path=${logpath} oms-auth-0.0.1-SNAPSHOT.jar
