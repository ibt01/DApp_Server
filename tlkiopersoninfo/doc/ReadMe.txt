curl http://127.0.0.1:8080/yinniper/user/getUserInfo   -H "server_id:serverid1a80eccc-352e-4940-8df0-7a437e972a4a"  -H "server_pwd:9e8dd5c8-25e3-41f1-999b-8742896894a2" -X POST -d '{"idLong":"123456"}' -H "Content-Type: application/json"

curl http://127.0.0.1:8080/yinniper/user/saveUserInfo   -H "server_id:serverid1a80eccc-352e-4940-8df0-7a437e972a4a"  -H "server_pwd:9e8dd5c8-25e3-41f1-999b-8742896894a2" -X POST -d '{"idLong":"123456","personalInfoJson":"eyJ4IjoiMmFjIiwiYSI6IjEiLCJjIjoiMyIsImQiOiIuLi4uLi4ifQ=="}' -H "Content-Type: application/json"


curl http://10.12.8.111:8080/yinniper/user/getDapps  -X GET

curl http://127.0.0.1:8080/yinniper/user/getDapps  -X GET


curl http://127.0.0.1:8080/yinniper/user/getUserInfo   -H "server_id:serverid1a80eccc-352e-4940-8df0-7a437e972a4a"  -H "server_pwd:9e8dd5c8-25e3-41f1-999b-8742896894a2" -X POST -d '{"idLong":"123456"}' -H "Content-Type: application/json"

curl http://127.0.0.1:8080/yinniper/user/saveUserInfo   -H "server_id:serverid1a80eccc-352e-4940-8df0-7a437e972a4a"  -H "server_pwd:9e8dd5c8-25e3-41f1-999b-8742896894a2" -X POST -d '{"idLong":"123456","personalInfoJson":"eyJ4IjoiMmFjIiwiYSI6IjEiLCJjIjoiMyIsImQiOiIuLi4uLi4ifQ=="}' -H "Content-Type: application/json"



