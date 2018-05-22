FROM mongo
COPY exportDb.json /exportDb.json
COPY initMongo.sh /initMongo.sh
ADD https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh /wait-for-it.sh
RUN chmod +x /initMongo.sh /wait-for-it.sh
CMD /initMongo.sh
EXPOSE 27017