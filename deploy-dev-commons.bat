echo off
echo -----------------------------------------
echo *** Deploy commons-service on dev ***
echo -----------------------------------------
call cd commons-service
call mvn clean
call mvn clean package -DskipTests
call az login --service-principal -u 10b7f7bc-a96d-4330-b87e-b2eb37c498ea -p F6Y~Qa3swRhPvEr7zR0R47zD-l94weMr~. --tenant 1b10882f-6873-45a5-b4c6-c484fca221ed
call az account set -s 2ff14b67-0b59-4366-b97d-8ce42bb79c87
call az spring app deploy -n dev-commons-service -g SIDevelopmentBE -s sidevspring --artifact-path target/COMMONS-SERVICE-0.0.1-SNAPSHOT.jar
call cd..
