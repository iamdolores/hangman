# Word Guessing Game

This version of the word guessing game Hangman has a command line interface and is written in java.  The game uses "secret words" from the following [word dictionary API](http://app.linkedin-reach.io/words).  

The player can complete the "secret word" by making letter or word guesses.  If she makes the `MAX_GUESSES_ALLOWED` (in this case 6) of incorrect guesses, she loses.  If the player guesses the word before losing, she earns points based on the difficulty of the word and the number of wrong guesses she made according to the following formula: 

`ROUND_POINTS = GUESSES_REMAINING * DIFFICULTY_LEVEL`,

where `GUESSES_REMAINING = MAX_GUESSES_ALLOWED - WRONG_GUESSES_MADE`.

The player enters a name and difficulty level for each round.  Each player's name must be unique and there are 10 levels of difficulty, integers between 1 and 10.  The game allows the players to play multiple rounds and accumulate points.

The game is set to keep track of a maximum of ten players.  It stores the player information in a file so that it can be used across game sessions.  It keeps the top 9 players based on points and replaces the 10th player with the most recent player.  

The game also keeps track of the words used from the word dictionary API in a file so that the game can use new "secret words" across game sessions.  Once all the words for a difficulty level have been used, the game resets and recycles the words for each difficulty level individually.

Happy word guessing!

## Getting Started

### Prerequisites
This program runs on java version:
```
java 10.0.1 2018-04-17
Java(TM) SE Runtime Environment 18.3 (build 10.0.1+10)
Java HotSpot(TM) 64-Bit Server VM 18.3 (build 10.0.1+10, mixed mode)
```
### Download and Run
Create a directory structure for the source files in your working directory:
```
mkdir src
mkdir src/hangman
```
Download and put the java source files in `src/hangman`.  

Compile the source files from your working directory:
```
javac src/hangman/*.java
```
Create a directory structure for the class files in your working directory:
```
mkdir bin
mkdir bin/hangman
```
Copy the class files to 'bin/hangman' from your working directory:
```
cp sr/hangman/*.class bin/hangman/
```
Run the commandline program:
```
java hangman/hangman
```
## Authors
* **Veronica Mayorga**

## Acknowledgements
Thanks to REACH Linkedin program for the challenge for the [word dictionary API](http://app.linkedin-reach.io/words) and the coding challenge! :smile: :+1: :+1: 






