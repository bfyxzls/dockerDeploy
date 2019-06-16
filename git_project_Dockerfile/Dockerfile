FROM ubuntu

# Update aptitude with new repo
RUN apt-get update

# Install software 
RUN apt-get install -y git python-virtualenv

WORKDIR /app
# Clone the conf files into the docker container
RUN git clone https://bfyxzls:gemini123@github.com/bfyxzls/eurekaServer.git
