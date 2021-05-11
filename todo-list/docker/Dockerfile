FROM mongo:4.2
RUN echo "rs.initiate({'_id':'rs0','members':[{'_id':0,'host':'127.0.0.1:27017'}]});" > /docker-entrypoint-initdb.d/replica-init.js
CMD [ "--bind_ip_all", "--replSet", "rs0" ]
