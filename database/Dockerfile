FROM mysql:5.7

# 设置环境变量
ENV MYSQL_ROOT_PASSWORD eRGaQZDNta6M25Ze
ENV MYSQL_DATABASE market
ENV MYSQL_USER market
ENV MYSQL_PASSWORD eRGaQZDNta6M25Ze

# 复制 init.sql 文件到/docker-entrypoint-initdb.d 目录下
COPY init.sql /docker-entrypoint-initdb.d