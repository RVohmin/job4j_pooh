## Проект job4j_pooh

В этом проекте реализован аналог асинхронной очереди RabbitMQ.
Приложение запускает Socket и ждем клиентов.
Клиенты могут быть двух типов: отправители (publisher), получатели (subscriver).
В качестве протокола будет использовать HTTP. Сообщения в формате JSON.
Существуют два режима: queue, topic.

#### Режим Queue.
Отправитель посылает сообщение с указанием очереди.
Получатель читает первое сообщение и удаляет его из очереди. 
Если приходят несколько получателей, то они читают из одной очереди. 
Уникальное сообщение может быть прочитано, только одним получателем.

Пример запросов.

POST /queue
{
  "queue" : "weather",
  "text" : "temperature +18 C"
}
 
GET /queue/weather
{
  "queue" : "weather",
  "text" : "temperature +18 C"
}

#### Режим Topic.
Отправитель посылает сообщение с указанием темы.
Получатель читает первое сообщение и удаляет его из очереди.
Если приходят несколько получателей, то они читают отдельные очереди.

POST /topic

```
{
  "topic" : "weather",
  "text" : "temperature +18 C"
}
```

 
GET /topic/weather
```
{
  "topic" : "weather",
  "text" : "temperature +18 C"
}
```
В проекте использована библиотека json.org