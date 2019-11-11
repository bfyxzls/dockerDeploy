# Docker image of elasticsearch with smartcn tokenizer
# VERSION 2.4.6
# Author: SenWang
 
#基础镜像使用elasticsearch:6.7.2
FROM elasticsearch:6.7.2
 
#作者
MAINTAINER SenWang <wangsen961225@163.com>
 
#es插件目录
ENV ES_PLUGINS_PATH /usr/share/elasticsearch/plugins
 
#定义存放smartcn分词器文件的目录
ENV SMARTCN_SRC_COMPILE_PATH /opt/smartcn_build
 
#创建存放smartcn分词器文件的目录
RUN mkdir $SMARTCN_SRC_COMPILE_PATH && \
 
#进入编译smartcn分词器文件的目录
cd $SMARTCN_SRC_COMPILE_PATH && \
 
#下载smartcn包
wget https://download.elastic.co/elasticsearch/release/org/elasticsearch/plugin/analysis-smartcn/2.4.6/analysis-smartcn-2.4.6.zip && \
 
#解压源码包
unzip analysis-smartcn-2.4.6 -d  analysis-smartcn&& \
 
#构建成功后，将文件移动到插件目录
mv analysis-smartcn $ES_PLUGINS_PATH/

#构建 docker build -t elasticsearch-with-smartcn:6.7.2 .