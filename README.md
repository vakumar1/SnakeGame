# SnakeGame
Snake Game implemented with LibGDX. 
* Play Desktop version of the game: download the SnakeGameDesktop.jar file
    * 'P': play the game
        * Arrow Keys: move snake
    * 'W': watch autonomous player
        * Any arrow key: start game
    * 'Q': end game
    * 'R': return to main menu
* Read source code: go to core/src/com/github/vakumar1/SnakeGame
    * SnakeGame.java: LibGDX Game instance
    * MainMenuScreen.java: LibGDX Screen instance (main menu)
    * GameScreen.java: LibGDX Screen instance (snake game grid)
    * GridGenerator.java: represent and update snake game grid
    * AutonomousPlayer.java: update the snake's direction with greedy approach
    * AutonomousPlayerRunner.java: repeatedly run the autonomous player and output aggregate score statistics

The best score I've seen the Autonomous Player get so far is 377:
![Alt text](snakegame_best_score.png?raw=true "Title")
    
