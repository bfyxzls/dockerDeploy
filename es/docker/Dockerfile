#基础镜像使用elasticsearch:6.7.2
FROM elasticsearch:6.7.2
 
#es插件目录
ENV ES_PLUGINS_PATH /usr/share/elasticsearch/plugins/
ENV ES_PLUGINS_TEMP_PATH /usr/share/elasticsearch/
#进入ik分词器文件的目录
RUN cd $ES_PLUGINS_PATH && mkdir analysis_ik && \

cd $ES_PLUGINS_TEMP_PATH && \
 
#下载analysis-ik包
wget https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v6.7.2/elasticsearch-analysis-ik-6.7.2.zip && \

#解压源码包
unzip elasticsearch-analysis-ik-6.7.2.zip -d  /usr/share/elasticsearch/plugins/analysis_ik

#构建 docker build --no-cache -t elasticsearch-with-smartcn:6.7.2 .