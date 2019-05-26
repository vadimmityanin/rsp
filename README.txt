This is Rock, Paper, Scissors game.
The main idea is based on Markov chains(https://en.wikipedia.org/wiki/Markov_chain),
current implementation keeps track of all states within [ROCK,PAPER,SCISSORS] transitions and counters the most probable
(based on previous application user games). If no statistics is available (when new user starts using application) application
uses random.

Prerequisites:
Java 11, Apache Maven 3.x+
This is a maven project, to build it run: mvn clean install
it builds RSP-1.0-SNAPSHOT.jar

To run tests separately: mvn test

Built artifact that can be run from console as simple as
java -jar RSP-1.0-SNAPSHOT.jar
or as this is SpringBoot implementation you can run this project
from your favourite IDE via com.mityanin.rsp.ApplicationRunner class

Launching this application start application server on 8080 port (can be changed in application.properties)

Example request:
http://localhost:8080/api/v1/rsp?username=Lena&entity=PAPER
Response:
{
  "result" : "LOSS",
  "serverPick" : "SCISSORS",
  "yourPick" : "PAPER",
  "statistics" : {
    "name" : "Lena",
    "wins" : 1,
    "losses" : 7,
    "draws" : 1
  }
}

NOTE: this is rather conceptual/prototype implementation with the following simplification/TODOs:
1)Embedded DB usage instead of ful-fledged one
2)DB scripts for tables + schema migration tools
3)Rather naive AAA implementation (utilizing request parameter for user identification)
4)Test coverage to be enhanced
5)Basic exception handling/translation

