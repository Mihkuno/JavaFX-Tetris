## About
This is my first ambitious game project that took a lot of time to finish
> This repository is made for practice, and must not be used for any commercial purposes... why? cuz the fonts and audio aren't mine :<

## Features
The game is filled with simple transitions and a minimalist design.

## Controls
- w : rotate peice
- a : move left
- s : move down
- d : move right
- spacebar : hard drop
- shift : hold (under construction)

## Game Mechanics: Level, Scoring, and Speed

- speed starts at 1
- speed is increased by plus x0.25 per level 
- level is increased every lines of (10 + (5 * level)) 
- score is increased by plus (0.01 * level) for every s (spacebar is looped)
- score is increased by plus (0.5 * level) for every new block creation (including hold)
- score is increased by plus (5 * (comboCounter + 1)) for each drop (combo is added by 1 since it starts at 0)
