# SnakeGame
Snake Game implemented with LibGDX. The game can be actively played by a user, or the user can watch an autonomous player.

## Download
[Snake Game (Desktop)](https://github.com/vakumar1/SnakeGame/raw/master/SnakeGameDesktop.jar)

## Usage
**P** - play the game<br>
**W** - watch autonomous player<br>
**Q** - end game<br>
**R** - return to main menu<br>

## Source
[SnakeGame Source Code](https://github.com/vakumar1/SnakeGame/tree/master/core/src/com/github/vakumar1/SnakeGame)

**SnakeGame.java** - LibGDX Game instance<br>
**MainMenuScreen.java** - LibGDX Screen instance (main menu)<br>
**GameScreen.java** - LibGDX Screen instance (snake game grid)<br>
**GridGenerator.java** - represent and update snake game grid<br>
**AutonomousPlayer.java** - update the snake's direction with greedy approach<br>
**AutonomousPlayerRunner.java** - repeatedly run the autonomous player and output aggregate score statistics<br>

The best score I've seen the Autonomous Player get so far is 377:
![Alt text](snakegame_best_score.png?raw=true "Title")
    
