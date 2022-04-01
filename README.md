## About
This is my first ambitious game project that took a lot of time to finish
> This repository is made for practice


## Features
The game is filled with simple transitions and a minimalist design.


https://user-images.githubusercontent.com/26486389/161334362-afc88ba8-4a72-45d6-a7aa-ef3abb3cf70f.MP4

https://user-images.githubusercontent.com/26486389/161334370-a7e7c0c1-acea-4e7e-b33c-9a0c5df695ae.MP4


## Controls
- w : rotate peice
- a : move left
- s : move down
- d : move right
- spacebar : hard drop
- shift : hold (under construction)

## Game Mechanics: Level, Scoring, and Speed

- SPEED starts at 1
- SPEED is decreased by 100ms per LEVEL 
- SPEED is increased by 350ms (briefly) for every S control

- LEVEL is increased every line GAIN of GAINUP += (5 + LEVEL)

- SCORE is increased by plus ((3 * LEVEL) * COMBO) for each row clear 
- SCORE is increased by plus (0.01 * LEVEL) for every S control (SPACEBAR is while looped)
- SCORE is increased by plus (0.5 * LEVEL) for every new block creation (excluding hold, except hold start)
