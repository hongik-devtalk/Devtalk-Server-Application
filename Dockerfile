# 도커 이미지 생성을 위한 설정 파일
FROM openjdk:17-jdk
LABEL authors="hongik-devtalk-server"
EXPOSE 8080
ARG JAR_FILE=build/libs/*.jar
ARG SPRING_PROFILE=dev
# dev 개발 환경으로 설정

COPY ${JAR_FILE} devtalk-springboot.jar
ENV spring_profile=${SPRING_PROFILE}

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/devtalk-springboot.jar", "--spring.profiles.active=${spring_profile}"]