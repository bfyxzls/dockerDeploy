 docker run -it -d -p 24224:24224 -v /root/fluentd/fluentd.conf:/fluentd/etc/fluentd.conf -e FLUENTD_CONF=fluentd.conf  fluent-es:latest 

# docker run --log-driver=fluentd --log-driver=fluentd --log-opt=fluentd-address=fluentd.i-counting.cn:24224  --log-opt=tag=your-application your-application