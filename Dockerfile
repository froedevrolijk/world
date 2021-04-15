FROM hseeberger/scala-sbt:8u222_1.3.5_2.12.10
WORKDIR /www/app
COPY ./ ./
RUN sbt compile
EXPOSE 9000
CMD sbt run

# https://stackoverflow.com/questions/61522281/sbt-project-not-mounted-to-docker-container-using-docker-compose