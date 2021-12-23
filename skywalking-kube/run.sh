cd oap-server
docker build -t 192.168.60.137:8888/sidecar/skywalking-oap-server:latest ./
docker push 192.168.60.137:8888/sidecar/skywalking-oap-server:latest
cd .. && cd ui
docker build -t 192.168.60.137:8888/sidecar/skywalking-ui:latest ./
docker push 192.168.60.137:8888/sidecar/skywalking-ui:latest
