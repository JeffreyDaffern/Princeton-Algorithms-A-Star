package a04;

import edu.princeton.cs.algs4.*;

public class Solver
{
	private MinPQ<SearchNode> priorityQueue = new MinPQ<SearchNode>();
	private Stack<Board> solutions = new Stack<Board>();
	private int moves;
	private SearchNode solution;

	/**
	 * finds a solution to the initial board using the A* algorithm.
	 * 
	 * @param initial the starting puzzle board
	 */
	public Solver(Board initial)
	{
		if (initial == null)
		{
			throw new NullPointerException("The board cannot be null");
		}
		if (!initial.isSolvable())
		{
			throw new IllegalArgumentException("This board is not solvable.");
		}
		moves = 0;

		solution = new SearchNode(initial, moves, null);
		priorityQueue.insert(solution);

		solution = solvePuzzle(priorityQueue.delMin());
	}

	public SearchNode solvePuzzle(SearchNode currentSearchNode)
	{
		if (currentSearchNode.board.isGoal())
		{
			return currentSearchNode;
		}

		Queue<Board> neighbors = (Queue<Board>) currentSearchNode.board.neighbors();
		moves++;

		while (!neighbors.isEmpty())
		{
			priorityQueue.insert(new SearchNode(neighbors.dequeue(), moves, currentSearchNode));
		}
		SearchNode newNode = priorityQueue.delMin();
		//System.out.println("new node | moves:" + newNode.moves + " | board:" + newNode.board);
		return solvePuzzle(newNode);
	}

	/**
	 * Gives the minimum number of moves to solve initial board
	 * 
	 * @return
	 */
	public int moves()
	{
		return moves;
	}

	/**
	 * Gives the sequence of boards that leads to the shortest solution.
	 * 
	 * @return a Queue of boards in sequence from start to finish
	 */
	public Iterable<Board> solution()
	{
		SearchNode current = solution;

		while (current != null)
		{
			solutions.push(current.board);
			current = current.previous;
		}

		return solutions;
	}

	private class SearchNode implements Comparable<SearchNode>
	{
		private int moves;
		private SearchNode previous;
		private Board board;

		@Override
		public int compareTo(SearchNode other)
		{
			return this.getPriority().compareTo(other.getPriority());
		}

		public SearchNode(Board board, int moves, SearchNode previous)
		{
			this.board = board;
			this.moves = moves;
			this.previous = previous;
		}

		public Integer getPriority()
		{
			return this.board.manhattan() + moves;
		}
	}

	public static void main(String[] args)
	{
		Board board1 = new Board(new int[][]
				{
						{ 2, 3, 0 },
						{ 1, 5, 6 },
						{ 4, 7, 8 } });

			Solver solver = new Solver(board1);
			StdOut.println("Minimum number of moves = " + solver.moves());
			for (Board board : solver.solution())
				StdOut.println(board);
	}
}
