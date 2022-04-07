# Unlenen Mancala Game

## Introduction
- The mancala games are a family of two-player turn-based strategy board games played with small stones, beans, or seeds and rows of holes or pits in the earth, a board or other playing surface. The objective is usually to capture all or some set of the opponent's pieces.

## Game Rules

### Game Play
The player who begins with the first move picks up all the stones in any of his
own six pits, and sows the stones on to the right, one in each of the following
pits, including his own big pit. No stones are put in the opponents' big pit. If the
player's last stone lands in his own big pit, he gets another turn. This can be
repeated several times before it's the other player's turn.

### Capturing Stones
During the game the pits are emptied on both sides. Always when the last stone
lands in an own empty pit, the player captures his own stone and all stones in the
opposite pit (the other player’s pit) and puts them in his own (big or little?) pit.

### Winning 
The game is over as soon as one of the sides runs out of stones. The player who
still has stones in his pits keeps them and puts them in his big pit. The winner of
the game is the player who has the most stones in his big pit.



# For Developers

Game has 2 main components. 

## Mancala Backend Application

### Architecture
- Backend is Spring Boot application which provides REST APIs for Frontend application.
- Written in Java 17
- Supports multiple game sessions concurrently
- Container friendly architecture

#### Code Base
##### Rest API 
- unlenen.mancala.be.controller.MancalaController
##### Game Service
- unlenen.mancala.be.service.MancalaService
##### Game Board JSON Entity
- unlenen.mancala.be.model.MancalaBoard
##### Player Board JSON Entity
- unlenen.mancala.be.model.PlayerBoard
#####  Rule Manager
- unlenen.mancala.be.model.move.AbstractMove
##### JUnit Tests
- unlenen.mancala.be.gameTests.GameTest
### Requirements
- Java 17
- Maven 3.8
- Internet connection for downloading maven packages

### Compile
```
 mvn install
```
### Run
```
java -jar target/mancalaBackend-1.0.jar --server.port=8080
```
### Docker Compile
```
docker build . -t unlenen/mancala:1.0
```

### Docker Run
```
docker run --name mancalaServer -d -p 8080:8080 unlenen/mancala:1.0
```

### Testing
```
mvn test
```

## Mancala Frontend Application

### Architecture
- Frontend is a classic HTML application which uses JQuery API for connection to Mancala Backend and CSS for creating board structure

### Requirements
- Nginx or Apache Web Server

### Run

- Update Mancala Backend Application 'URL_MANCALA_API' variable at mancalaFrontend/js/mancala.js 
```
URL_MANCALA_API = "http://MANCALA-BACKEND-SERVER-IP:8080/mancala/v1"
```
- Open index.html on your Javascript supported browser( Firefox , Chrome etc..)

#### Production
- Install Web Server ( Nginx , Apache )
- Copy all source code to web folder
- Open http://WEB-SERVER-IP/ URL  on your favorite browser


