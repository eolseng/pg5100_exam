FROM adoptopenjdk/openjdk11:alpine

COPY target/pg5100_exam-exec.jar .

CMD java -jar pg5100_exam-exec.jar \
    --spring.datasource.username="postgres"  \
    --spring.datasource.password  \
    --spring.datasource.url="jdbc:postgresql://localhost:5432/postgres"
