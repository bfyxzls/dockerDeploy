FROM fabric8/java-jboss-openjdk8-jdk:1.4

ENV JAVA_APP_JAR="app.jar"
ENV JAVA_MAX_MEM_RATIO=100
ENV AB_OFF ""
#ADD --chown=jboss:jboss https://github.com/bfyxzls/lindJar/blob/master/configServer-0.0.2.jar /deployments/app.jar
ADD --chown=jboss:jboss configServer-0.0.2.jar /deployments/app.jar
