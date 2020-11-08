FROM openjdk:12-alpine
COPY ./* /app/
WORKDIR /app/
RUN javac -d ./output opt_analyser.java
WORKDIR /app/output