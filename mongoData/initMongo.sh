bash -c "/wait-for-it.sh 127.0.0.1:3306 -t 10 -- mongoimport -v --db flights --collection flight_info --type json --file /exportDb.json" &
exec mongod --bind_ip_all