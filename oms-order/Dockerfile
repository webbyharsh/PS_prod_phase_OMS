FROM adoptopenjdk/openjdk11:alpine
EXPOSE 9003
ADD target/oms-order-0.0.1-SNAPSHOT.jar oms-order-0.0.1-SNAPSHOT.jar
RUN mkdir -p /var/log/oms
ENV env=""
ENV username=""
ENV password=""
ENV dbUrl=""
ENV accesskey=""
ENV orderUrl=""
ENV fillUrl=""
ENV logpath=""
ENV exchangeUrl=""
CMD java -jar -Denv=${env} -Ddb.url=${dbUrl} -Ddb.username=${username} -Ddb.password=${password} -Dkey=${accesskey} -Dorder.ack.api=${orderUrl} -Dfill.api=${fillUrl} -Dexchange.server.url=${exchangeUrl} -Dlogging.file.path=${logpath} oms-order-0.0.1-SNAPSHOT.jar
