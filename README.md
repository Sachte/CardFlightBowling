# Running
To run the application,
1) open as a maven project and right click main and run, for example in IntelliJ
    
or

2) run included jar file:
java -jar target/bowling-0.0.1-SNAPSHOT.jar

# Endpoints
There are 3 endpoints:
POST /bowling/newGame/{gameName}
    creates new Game with game name: gameName
    example: curl -X POST localhost:8080/bowling/newGame/game1
POST /bowling/roll/{gameName}/{roll}
    rolls a score roll for game gameName
    example: curl -X POST localhost:8080/bowling/roll/game1/4
GET /bowling/scoreboard/{gameName}
    retrieves scoreboard for game gameName
    example: curl localhost:8080/bowling/scoreboard/game1
    note: if frameScore is -1, then frame's score isn't finalized yet, for example
    in the case of a strike, but the next frame hasn't finished.

# Project Structure
main:
* BowlingApplication is the main class.
* BowlingController contains the APIs and helper methods to run the business logic.
* BowlingRepository stores active games in a HashMap, this can be substituted for MongoDB etc.

model:
* Game represents a single game of bowling, consists of Frames.
* Frame represents a single frame, consists of Rolls.
* Roll represents an individual roll: an int value.
* Scoreboard represents the information needed to create a scoreboard, including a list of ScoreBoardFrames
with a string moves, and frameScore.

test:
* contains tests for business logic.


# Original Instructions:
# Bowling Scoring Challenge

Design an interface to track the score of a single game of bowling. The interface will accept the result of each ball bowled and calculate the correct score, including spares and strikes. The interface must also indicate if the 10th frame includes a third ball.

The results of ball rolls are input one at a time, sequentially. The interface should assign the scores to the correct frames. Based on the current conditions, the interface should sanitize the inputs to only allow valid scores to be entered. The score should be displayed and updated as the rolls are entered. *Do not* assume all input will be entered at once.

## Example

Input: Strike, 7, Spare, 9, Miss, Strike, Miss, 8, 8, Spare, Miss, 6, Strike, Strike, Strike, 8, 1

| Frame |  1 | 2  | 3  | 4  | 5  | 6  | 7 | 8 | 9 | 10 |
|---|---|---|---|---|---|---|---|---|---|---|
| Input| X  |  7 / |  9 - | X  | - 8  |  8 / |  - 6 |  X | X  |  X 8 1 |
|Score|  20 | 39  |  48 | 66  | 74  |  84 |  90 |  120 | 148  | 167  |

## Bowling Game
* A game of bowling consists of 10 frames, in two balls are rolled per frame to attempt to knock down all ten pins.
* If all ten pins are knocked down in those two turns in the 10th frame, an extra bonus chance is awarded.
* A strike is defined as knocking down all ten pins in the first ball of a frame, signified with an X
* A spare is defined as knocking down all the remaining pins in the second ball of a frame, signified with a /
* A miss is defined as zero pins being knocked down on a roll, signified with a -

## Scoring Rules
* Generally, 1 point per pin knocked down.
* Strike - Score of 10 (for knocking down all ten pins), plus the total of the next two rolls
* Spare - Score of 10, plus the total number of pins knocked down on the next roll only
* Each frame displays the cumulative score up to that point for all complete frames. If a frame has a strike or spare, the score for that frame is not displayed until sufficient subsequent rolls have been input

## Milestones

### Milestone 1 - Data Model
A fully implemented and tested data model for the above.
Should include well-encapsulated interfaces for all classes, properties, and methods of model layer.

### Milestone 2 - Business Logic
Implementation of the "business logic", which includes maintaining the state of a game in progress, keeping track of the current frame, calculating the score, and the logic to step through the game.
The functionality can be demonstrated with hard-coded data or use a basic interface to allow rolls to be entered.

### Milestone 3 - API / UI Implementation
Wrap your business logic in either an API or a Web UI, using any architecture desired.
Provide an interface to enter in scores and a way to view/fetch a scoreboard that correctly tells you what the current score is, what frame you are in, and how many rolls remain in the current frame.

## What we're looking for

Here are some things we will be looking for when assessing your implementation
1) Your code compiles
2) Your score calculation is able to handle a variety of games
3) Your code has 80% test coverage
