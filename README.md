# Maze Generation &amp; Solver
UNFINISHED: Maze Generation &amp; Solver: Automatically generate a maze with user inputting size and solve the maze using Breadth-First Search (BFS) and Depth-First Search (DFS)

## License
All parts of Maze Generation &amp; Solver are free to use and abuse under the open-source MIT license.

## Acknowledgement
Maze Generation &amp; Solver was created by [Nhat Nguyen](https://github.com/nguyen-nhat) and [Jasmine Mai](https://github.com/jasminemai97).

## Example 
```
Input the size for the maze: 10

+   +---+---+---+---+---+---+---+---+---+
|   |                           |       |
+   +---+---+---+   +---+   +   +   +   +
|   |           |   |   |   |       |   |
+   +   +---+   +   +   +   +---+---+---+
|       |       |   |   |               |
+---+---+   +---+   +   +---+---+---+   +
|       |   |               |           |
+   +---+   +   +---+---+---+   +---+   +
|   |       |       |       |       |   |
+   +   +---+---+---+   +   +   +   +   +
|   |                   |   |   |   |   |
+   +---+---+---+---+---+   +---+   +   +
|                   |   |       |   |   |
+   +---+---+---+   +   +---+   +   +   +
|       |               |       |   |   |
+   +   +---+---+---+   +   +---+   +   +
|   |               |   |           |   |
+   +---+---+---+   +---+---+---+---+   +
|               |                       |
+---+---+---+---+---+---+---+---+---+   +
String representation of the generated maze

+   +---+---+---+---+---+---+---+---+---+
| 0 |                           |       |
+   +---+---+---+   +---+   +   +   +   +
| 1 | 4   5   6 |   |   |   |       |   |
+   +   +---+   +   +   +   +---+---+---+
| 2   3 | 8   7 |   |   |               |
+---+---+   +---+   +   +---+---+---+   +
|       | 9 |               | 3   4   5 |
+   +---+   +   +---+---+---+   +---+   +
|   | 1   0 |       | 7   8 | 1   0 | 6 |
+   +   +---+---+---+   +   +   +   +   +
|   | 2   3   4   5   6 | 9 | 2 | 9 | 7 |
+   +---+---+---+---+---+   +---+   +   +
|                   |   | 0   1 | 8 | 8 |
+   +---+---+---+   +   +---+   +   +   +
|       |               | 3   2 | 7 | 9 |
+   +   +---+---+---+   +   +---+   +   +
|   |               |   | 4   5   6 | 0 |
+   +---+---+---+   +---+---+---+---+   +
|               |                     1 |
+---+---+---+---+---+---+---+---+---+   +
String representation of DFS
```
