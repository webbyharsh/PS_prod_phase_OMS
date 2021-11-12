FROM adoptopenjdk/openjdk11:alpine
EXPOSE 9006
ADD target/oms-exchange-0.0.1-SNAPSHOT.jar oms-exchange-0.0.1-SNAPSHOT.jar
RUN mkdir -p /var/log/oms
ENV orderUrl=""
ENV fillUrl=""
ENV logpath=""
CMD java -jar -Dorder.ack.api=${orderUrl} -Dfill.api=${fillUrl} -Dlogging.file.path=${logpath} oms-exchange-0.0.1-SNAPSHOT.jar