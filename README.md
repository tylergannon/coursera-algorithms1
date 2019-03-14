# Code Submissions For Coursera Algorithms 1

Taught by Robert Sedgewick of Princeton.

I highly recommend this course, and I also highly recommend
solving all the problems on your own before checking the code
here.


Included here are:

## Percolation using UnionFind.

Solving the backwash problem was fun.

Percolation.java

## Randomized Queue

Requiring queue operations in constant amortized time,
creating a randomized iterator in linear time, with
_hasNext()_ and _next()_ functions in constant time.

RandomizedQueue.java

## Finding All Sets Of Collinear Points

In a field of _n_ points.

In time proportional to _n_<sup>2</sup>log _n_, using memory expanding
linearly with _n_ plus the number of segments found.

FastCollinearPoints.java

## 8-puzzle Solver In Minimum Number Of Moves

Using A<sup>*</sup> algorithm.

I minimized memory usage by calculating at
runtime the number of bits needed to store the board state,
based on the size of the board, and compressing the board state
into all the bits of an integer array.

There's some pretty fun bitwise manipulation involved.

Board.java
Solver.java

## 2D Binary Search Tree Implementation

Optimized geospatial queries.

KdTree.java

