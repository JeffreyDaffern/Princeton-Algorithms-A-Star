package a04;

import java.util.Arrays;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

/**
 * Represents a board for a size mutable puzzle. There is 1 empty spot in the
 * puzzle represented by a 0 and the goal is to order the board from 1 to size *
 * size - 1 with 0 at the end in the bottom-right position.
 * 
 * @author Kevin Dapper and Jeff Daffern
 *
 */
public class Board
{
	private final int[][] blocks;
	private final int size;

	/**
	 * Constructs a board from an N-by-N array of blocks (where blocks[i][j] = block
	 * in row i, column j).
	 * 
	 * @param blocks
	 */
	public Board(int[][] blocks)
	{
		size = blocks.length;
		this.blocks = new int[size][size];
		for (int i = 0; i < size; i++)
		{
			for (int j = 0; j < size; j++)
			{
				this.blocks[i][j] = blocks[i][j];
			}
		}
	}

	/**
	 * Gives the size of the board.
	 * 
	 * @return board size
	 */
	public int size()
	{
		return size;
	}

	/**
	 * Determines how many blocks are out of place. Doesn't include zero.
	 * 
	 * @return
	 */
	public int hamming()
	{
		int count = 0;
		for (int i = 0; i < size; i++)
		{
			for (int j = 0; j < size; j++)
			{
				if (blocks[i][j] == 0)
					continue;
				if (blocks[i][j] != ((size * i) + (j + 1)))
					count++;
			}
		}
		return count;
	}

	/**
	 * Determines the Manhattan distance. Manhattan distance is a calculation of the
	 * number of tiles out of place and amount of movements before it is in in the
	 * right position.
	 * 
	 * @return
	 */
	public int manhattan()
	{
		int distance = 0;

		for (int i = 0; i < size; i++)
		{
			for (int j = 0; j < size; j++)
			{
				if (blocks[i][j] == 0)
					continue;
				int rowGoal = (blocks[i][j] - 1) / size;
				int columnGoal = (blocks[i][j] - rowGoal * size) - 1;
				distance += Math.abs(i - rowGoal) + Math.abs(j - columnGoal);
//				System.out.println("Manhattan: " + (Math.abs(i - rowGoal) + Math.abs(j - columnGoal)) + " : "
//						+ Math.abs(i - rowGoal) + " | " + Math.abs(j - columnGoal) + "   [" + i + "][" + j + "] = "
//						+ blocks[i][j]);
			}
		}
		//
		// number of tiles out of place and amount of movements before it is in in the
		// right position
		return distance;
	}

	/**
	 * Determines whether this is the goal board
	 * 
	 * @return true if this is the goal board, false otherwise
	 */
	public boolean isGoal()
	{
		int counter = 0;

		for (int i = 0; i < size(); i++)
		{
			for (int j = 0; j < size(); j++)
			{
				counter++;
				if (blocks[i][j] != counter && blocks[size() - 1][size() - 1] != 0)
				{
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Checks if a board is solvable by comparing total inversions.
	 * 
	 * @return true if total inversion is even and false otherwise
	 */
	public boolean isSolvable()
	{
		int[] tempBlock = new int[(size() * size()) - 1];
		boolean isEven = size() % 2 == 0;
		int counter = 0;
		int inversions = 0;
		int zeroLocation = 0;

		// Create temp array to easily see if inversions exist
		for (int i = 0; i < blocks.length; i++)
		{
			for (int j = 0; j < blocks.length; j++)
			{
				if (blocks[i][j] != 0)
				{
					tempBlock[counter] = blocks[i][j];
					counter++;
				} else
				{
					zeroLocation = i;
				}
			}
		}

		// Check if inversions exist
		for (int i = 0; i < tempBlock.length; i++)
		{
			for (int j = 0; j < tempBlock.length; j++)
			{
				if (tempBlock[i] > tempBlock[j] && i < j)
					inversions++;
			}
		}

		if (isEven)
		{
			return (zeroLocation + inversions) % 2 != 0;
		}
		return inversions % 2 == 0;
	}

	@Override
	public boolean equals(Object y)
	{
		Board other = (Board) y;
		return Arrays.deepEquals(this.blocks, other.blocks);
	}

	/*
	 * Performs all possible moves according to the position of zero in the array
	 * and creates new boards for comparison.
	 * 
	 * @return
	 */
	public Iterable<Board> neighbors()
	{
		Queue<Board> neighbors = new Queue<>();
		int rowZero = 0, colZero = 0;
		if (size == 1)
		{
			return neighbors;
		}
		for (int i = 0; i < size; i++)
		{
			for (int j = 0; j < size; j++)
			{
				if (blocks[i][j] == 0)
				{
					rowZero = i;
					colZero = j;
				}
			}
		}
		if (rowZero == 0) // The top row
		{
			if (colZero == 0) // Top Left
			{
				neighbors.enqueue(new Board(exchange(rowZero, colZero, "right")));
				neighbors.enqueue(new Board(exchange(rowZero, colZero, "down")));
			} else if (colZero == size - 1) // Top Right
			{
				neighbors.enqueue(new Board(exchange(rowZero, colZero, "left")));
				neighbors.enqueue(new Board(exchange(rowZero, colZero, "down")));
			} else if (colZero > 0 && colZero < size - 1) // Top Row
			{
				neighbors.enqueue(new Board(exchange(rowZero, colZero, "left")));
				neighbors.enqueue(new Board(exchange(rowZero, colZero, "right")));
				neighbors.enqueue(new Board(exchange(rowZero, colZero, "down")));
			}
		} else if (rowZero == size - 1) // Bottom Row
		{
			if (colZero == 0) // Bottom Left
			{
				neighbors.enqueue(new Board(exchange(rowZero, colZero, "up")));
				neighbors.enqueue(new Board(exchange(rowZero, colZero, "right")));
			} else if (colZero == size - 1) // Bottom Right
			{
				neighbors.enqueue(new Board(exchange(rowZero, colZero, "up")));
				neighbors.enqueue(new Board(exchange(rowZero, colZero, "left")));
			} else if (colZero > 0 && colZero < size - 1) // Bottom Row
			{
				neighbors.enqueue(new Board(exchange(rowZero, colZero, "up")));
				neighbors.enqueue(new Board(exchange(rowZero, colZero, "left")));
				neighbors.enqueue(new Board(exchange(rowZero, colZero, "right")));
			}
		} else if (colZero == 0 && rowZero > 0 && rowZero < size - 1) // Left Side
		{
			neighbors.enqueue(new Board(exchange(rowZero, colZero, "up")));
			neighbors.enqueue(new Board(exchange(rowZero, colZero, "down")));
			neighbors.enqueue(new Board(exchange(rowZero, colZero, "right")));
		} else if (colZero == size - 1 && rowZero > 0 && rowZero < size - 1) // Right Side
		{
			neighbors.enqueue(new Board(exchange(rowZero, colZero, "up")));
			neighbors.enqueue(new Board(exchange(rowZero, colZero, "down")));
			neighbors.enqueue(new Board(exchange(rowZero, colZero, "left")));
		} else
		{
			neighbors.enqueue(new Board(exchange(rowZero, colZero, "up")));
			neighbors.enqueue(new Board(exchange(rowZero, colZero, "down")));
			neighbors.enqueue(new Board(exchange(rowZero, colZero, "left")));
			neighbors.enqueue(new Board(exchange(rowZero, colZero, "right")));
		}
		return neighbors;
	}

	/**
	 * Swaps two block positions in a board.
	 * 
	 * @param rowZero
	 * @param colZero
	 * @param position
	 * @return updated updated board
	 */
	private int[][] exchange(int rowZero, int colZero, String position)
	{
		int[][] newBoard = new int[size][size];
		for (int i = 0; i < newBoard.length; i++)
		{
			for (int j = 0; j < newBoard.length; j++)
			{
				newBoard[i][j] = blocks[i][j];
			}
		}

		switch (position)
		{
		case "left":
		{
			newBoard[rowZero][colZero] = newBoard[rowZero][colZero - 1];
			newBoard[rowZero][colZero - 1] = 0;
			break;
		}
		case "right":
		{
			newBoard[rowZero][colZero] = newBoard[rowZero][colZero + 1];
			newBoard[rowZero][colZero + 1] = 0;
			break;
		}
		case "up":
		{
			newBoard[rowZero][colZero] = newBoard[rowZero - 1][colZero];
			newBoard[rowZero - 1][colZero] = 0;
			break;
		}
		case "down":
		{
			newBoard[rowZero][colZero] = newBoard[rowZero + 1][colZero];
			newBoard[rowZero + 1][colZero] = 0;
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + position);
		}
		return newBoard;
	}

	@Override
	public String toString()
	{
		StringBuffer s = new StringBuffer();
		s.append(size() + "\n");
		for (int i = 0; i < blocks.length; i++)
		{
			for (int j = 0; j < blocks.length; j++)
			{
				s.append(String.format("%2d ", blocks[i][j]));
			}
			s.append("\n");
		}
		return s.toString();
	}

	/**
	 * Unit Testing
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{

		// Check toString is working
		Board board = new Board(new int[][]
		{
				{ 1, 2, 3 },
				{ 4, 0, 6 },
				{ 7, 8, 5 } });
		StdOut.println(board.toString());
		// Check if equals is working
		Board board2 = new Board(new int[][]
		{
				{ 1, 2, 3 },
				{ 4, 5, 6 },
				{ 7, 0, 8 } });
		StdOut.println(board.equals(board2));
		System.out.println(board.isSolvable() + " : "  + board2.isSolvable());

		board2 = new Board(new int[][]
		{
				{ 0, 2, 3 },
				{ 4, 0, 6 },
				{ 7, 8, 5 } });
		StdOut.println(board.equals(board2));

		Board board3 = new Board(new int[][]
		{
				{ 0 } });
		System.out.println(board3);
		System.out.println(board3.neighbors());

	}
}