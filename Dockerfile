FROM hmcts/cnp-java-base:openjdk-8u191-jre-alpine3.9-2.0.1

COPY build/libs/rpa-dg-template-management-api.jar /opt/app/

CMD ["rpa-dg-template-management-api.jar"]

EXPOSE 8080
