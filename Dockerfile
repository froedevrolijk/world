FROM hseeberger/scala-sbt:eclipse-temurin-11.0.14.1_1.6.2_2.13.8
WORKDIR /www/app
COPY ./ ./
RUN sbt compile
EXPOSE 9000
CMD sbt run