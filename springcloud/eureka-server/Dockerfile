FROM fabric8/java-jboss-openjdk8-jdk:1.4
ENV JAVA_APP_JAR="app.jar"
ENV JAVA_MAX_MEM_RATIO=40
ENV AB_OFF ""
#ADD  --chown=jboss:jboss http://154.206.59.84:8090/eurekaServer-0.0.2.jar /deployments/app.jar
ADD  --chown=jboss:jboss jar/eurekaServer-0.0.2.jar /deployments/app.jar