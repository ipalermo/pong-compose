# Android Skills Challenge


**Goal** Implement a very simple game similar to the 1972 game 'pong'

## How to complete the task:

Using Kotlin & compose, complete as many of the user stories below as possible, in any order (although we have tried to order them in a pragmatic order for implementation). You may spend as long as you like on the task, although we expect a few hours to be more than sufficient - we do not expect candidates to give up days of their time. We deliberately include many use cases so that it is clear that you are not expected to 'finish'.


The solution you build should be buildable with Android Studio and installable on a test phone. We will use an android 10 phone with 4G of ram to test it.  Although this task is specified as a simple game, we would prefer the solution NOT be implemented by pulling in a game engine. However, apart from that, you may use any libraries/frameworks/tools which are installable with gradle, or which you otherwise provide installation instructions for; 
Google and Stack Overflow are absolutely allowed/encouraged. 

The solution can include supporting documentation, diagrams or pseudo-code as you see fit to support the code produced. If applicable, please consider portability in your solution; this may be reviewed by someone running android studio on a different OS/platform from you.

Given this is an open challenge, there is no expectation of receiving “complete” solutions - this is designed to understand your approach to engineering and overall confidence in technology, not to produce a working production application. Rather, you should be prepared to discuss your choices in an interview.


## Context and definitions

The 1972 game pong is described in its [Wikipedia article](https://en.wikipedia.org/wiki/Pong)
Most of the functionality can be seen in this image:

![Pong animation from wikipedia](https://upload.wikimedia.org/wikipedia/commons/6/62/Pong_Game_Test2.gif)

Each player controls a bat at the side of the screen. 


## User stories:


As a user, I would like to play the game with a friend, with a phone  or tablet lying flat on a table between us. - DONE

As a user, I need to control my bat my touching the left and right corners of the phone nearest to me. Touching the left corner should make my bat
move left, the right corner should make it move right, at constant speed. When I am not touching, the bat should not move. The other player
should be able to control their bat with the other corners of the screen. - DONE

As a user, I want the game to wait to start until I indicate I am ready (by touching the screen). - There is a PLAY screen before starting the game: DONE

As a user, I want to be able to bounce the ball off my bat.  - DONE

As a user, I want to be able to score when my opponent misses the ball,  and for it to fall off the screen past their bat (on the top or bottom of the screen). - NOT DONE

As a user, I want to be able to bounce the ball off the left and right sides of the screen - DONE

As a user, I want to be able to see how many times I have scored. - NOT DONE

As a user, I would like to hear a sound when I hit the ball with my bat (See [sfxr.me](https://sfxr.me/) for some useful sounds). NOT DONE

As a user, I would like to affect the speed and direction of the ball based on where the ball hits my bat. - DONE (not affecting the speed, only direction)

As a user, I would like to hear a sound when I score. NOT DONE

As a user, I would like to hear a warning sound which soudns 'more serious' (eg louder, or more dissonant) the closer I get to missing the ball. NOT DONE

As a user, I would like to be able to use the game in a horizontal or vertical orientation. - DONE

As a user, I would like the game constants (bat speed, ball speed, bat length etc) to adapt based on the orientation, so that the game
has a similar hardness - NOT DONE

As a user, I would like the game constants (bat speed, ball speed, bat length etc) to adapt based on the screen size, so that the game
has a similar hardness - NOT DONE

As a user, I would like a to be able to replace the bat with an image of my choice - NOT DONE

As a user, I would like a to be able to replace the ball with an image of my choice - NOT DONE

As a user, I would like replacement images to affect the bat/ball contact dynamics. - NOT DONE

As a user, I would like an option to play with another user on the same local network. - NOT DONE

## How to submit your task

Please commit all work to this repo and email the person managing your recruitment process when you have completed the task to the level you are happy with.

Please include clear instructions on how to run the project locally in this README, if there are any special instructions beyond building and running with Android
studio.

## How to run the task

Download the zip or clone the repository and open the project in Android studio. Run a gradle sync and then build to debug device

## What we’re looking for

Firstly, we don’t expect you to complete every part of the task, but at least a good portion that allows us to see how you code and how you work.

Think about how your code is going to be viewed by others, aim to ensure it’s readable if anyone else were to continue the project after you completed the user stories you did. We’d like to see your work committed to git as if you were working on any other work project.
