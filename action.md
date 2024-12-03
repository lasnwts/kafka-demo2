# GIT: 
https://github.com/lasnwts/kafka-demo/blob/master/action.md
# 1. Запуск из каталога lesson1 docker-compous
# 2. Подключение к сеансу броека
docker exec -ti kafka /usr/bin/bash
# 3. В сеансе
cd /usr/bin/
# 4. Запускаем просмотр топиков 
./kafka-topics --list --bootstrap-server localhost:9092
# 5. Создаем топики
./kafka-topics --create --topic vowels --bootstrap-server localhost:9092
./kafka-topics --create --topic consonants --bootstrap-server localhost:9092
# 6. Отправляем сообщения в топик vowels
./kafka-console-producer --topic vowels --bootstrap-server localhost:9092
>test 1
>hello word
# 7. Отправляем сообщения в топик consonants
./kafka-console-producer --topic consonants --bootstrap-server localhost:9092
> test2








