# Maze Generation Algorithms

Java implementation of two maze generation algorithms:
- [Depth-first search](https://en.wikipedia.org/wiki/Maze_generation_algorithm#Randomized_depth-first_search)
- [Wilson's Algorithm](https://en.wikipedia.org/wiki/Maze_generation_algorithm#Wilson's_algorithm)

Produces mazes of arbitrary sizes: larger mazes will take increasingly longer to generate.

## Depth first search

- Tends to produce long corridors
- Is not a true uniformly random sample over all mazes.
- Substantially faster than Wilson's algorithm.

## Wilson's algorithm
- Relies on loop-erased random walks.
- Produces a uniform sample over all possible mazes.
- Tends to produce complex, difficult to solve mazes.

Included below are examples in ascending order of size, the maze entrance is at the top left pixel and the exit is at the bottom right in all cases. Try them out!

Depth first search            |  Wilson's algorithm
:-------------------------:|:-------------------------:
![16x16](https://github.com/Jazz-Coding/MazeGenerationAlgorithms/assets/52354702/9c2bfc6a-f5bc-4428-9cf2-eb33d4de47ee)  |  ![16x16](https://github.com/Jazz-Coding/MazeGenerationAlgorithms/assets/52354702/6443a61e-4973-4680-b8b1-3462af387775)
|16x16|16x16|
![64x64](https://github.com/Jazz-Coding/MazeGenerationAlgorithms/assets/52354702/50a10681-16ce-47ac-9547-b7915ea37129) | ![64x64](https://github.com/Jazz-Coding/MazeGenerationAlgorithms/assets/52354702/07229b98-aba8-4fe2-805a-fd86705721ca)
|64x64|64x64|
![256x256](https://github.com/Jazz-Coding/MazeGenerationAlgorithms/assets/52354702/43e790b4-85ee-4666-87a0-01e8dc405d1e) | ![256x256](https://github.com/Jazz-Coding/MazeGenerationAlgorithms/assets/52354702/0925d11b-dee0-4827-bb4f-010f1c7f4f09)
|256x256|256x256|


## Running the program
Included in the [release](https://github.com/Jazz-Coding/MazeGenerationAlgorithms/releases) page is the project JAR file. Run with Java Runtime Environment (JRE) >=14.0.
The user interface should appear as follows:

![gui_example](https://github.com/Jazz-Coding/MazeGenerationAlgorithms/assets/52354702/270fd109-e6fc-49ae-bb2f-4252f78b5e91)

