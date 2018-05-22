#!/bin/sh

echo "Executing Maven to build the apps..."
mvn clean package

if [ $? -gt 0 ]
then
    echo "\nMaven process was unsuccesful! Please check the errors :("
    exit 1;
fi

echo "\nMaven process finished!"

echo "\nCreating Docker images..."

docker build -t adidas/config-server:1.0.0 ./config-server
if [ $? -gt 0 ]
then
    echo "\nconfig-server image creation failed. Please check the errors :("
    exit 1;
fi

docker build -t adidas/eureka-server:1.0.0 ./eureka-server
if [ $? -gt 0 ]
then
    echo "\neureka-server image creation failed. Please check the errors :("
    exit 1;
fi

docker build -t adidas/security-server:1.0.0 ./security-server
if [ $? -gt 0 ]
then
    echo "\nsecurity-server image creation failed. Please check the errors :("
    exit 1;
fi

docker build -t adidas/zuul-server:1.0.0  ./zuul-server
if [ $? -gt 0 ]
then
    echo "\nzuul-server image creation failed. Please check the errors :("
    exit 1;
fi

docker build -t adidas/flight-data:1.0.0  ./flight-data
if [ $? -gt 0 ]
then
    echo "\nflight-data image creation failed. Please check the errors :("
    exit 1;
fi

docker build -t adidas/flight-info:1.0.0 ./flight-info
if [ $? -gt 0 ]
then
    echo "\nflight-info image creation failed. Please check the errors :("
    exit 1;
fi

docker-compose up --no-start

echo "\nDocker images and its containers created!"
echo "\nStart by starting the containers in the following order"
echo "\nmongodbflights => config-server => eureka-server => security-server => zuul-server"
echo "\nThen start flight-data and flight-info. You can start just flight-info to see the Hystrix fallback acting."