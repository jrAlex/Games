package jrAlex.core;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class World
{
	protected WorldObject[][]	objects;
	protected Point[]			endpoints;

	public World(WorldObject[][] objects, Point[] endpoints)
	{
		this.objects = objects;
		this.endpoints = endpoints;
	}

	public void addObject(WorldObject wo, int col, int row)
	{
		objects[row][col] = wo;
	}

	public WorldObject[][] getObjects()
	{
		return objects;
	}

	public int getHeight()
	{
		return objects.length;
	}

	public int getWidth()
	{
		return objects[0].length;
	}

	public Point getPlayerCoords()
	{
		for (int row = 0; row < getHeight(); row++)
			for (int col = 0; col < getWidth(); col++)
				if (objects[row][col] != null && objects[row][col].type == ObjectType.PLAYER)
					return new Point(col, row);
		return null;
	}

	public void movePlayer()
	{
		Point playerCoords = getPlayerCoords();
		moveObject(playerCoords.x, playerCoords.y,
				playerCoords.x + objects[playerCoords.y][playerCoords.x].dir.colShift,
				playerCoords.y + objects[playerCoords.y][playerCoords.x].dir.rowShift);
	}

	public void changePlayerDir(Direction newDir)
	{
		Point playerCoords = getPlayerCoords();
		objects[playerCoords.y][playerCoords.x].changeDir(newDir);
	}

	public boolean moveObject(int oldCol, int oldRow, int newCol, int newRow)
	{
		if (oldCol < 0 || oldCol >= getWidth() || oldRow < 0 || oldRow >= getHeight() || newCol < 0
				|| newCol >= getWidth() || newRow < 0 || newRow >= getHeight())
			return false;
		else if (objects[oldRow][oldCol] == null)
			return true;
		else if (objects[newRow][newCol] == null)
		{
			objects[newRow][newCol] = objects[oldRow][oldCol];
			objects[oldRow][oldCol] = null;
			return true;
		}
		else
		{
			objects[newRow][newCol].dir = objects[oldRow][oldCol].dir;
			if (objects[oldRow][oldCol].isMoveable() && objects[newRow][newCol].isMoveable()
					&& moveObject(newCol, newRow, newCol + objects[oldRow][oldCol].dir.colShift,
							newRow + objects[oldRow][oldCol].dir.rowShift))
			{
				objects[newRow][newCol] = objects[oldRow][oldCol];
				objects[oldRow][oldCol] = null;
				return true;
			}
			return false;
		}
	}

	public static World load(String fileName)
	{
		try
		{
			Scanner scanner = new Scanner(new File("./res/levels/" + fileName + ".lvl"));

			String[] props = scanner.nextLine().split(" ");
			int width = Integer.parseInt(props[0]);
			int height = Integer.parseInt(props[1]);

			WorldObject[][] objects = new WorldObject[height][width];
			for (int row = 0; row < height; row++)
			{
				String[] objectStrings = scanner.nextLine().split(", ");
				for (int col = 0; col < width; col++)
					if (Integer.parseInt(objectStrings[col]) - 1 != -1)
						objects[row][col] = new WorldObject(
								ObjectType.values()[Integer.parseInt(objectStrings[col]) - 1]);
			}

			String[] pointStrings = scanner.nextLine().split(", ");
			Point[] points = new Point[pointStrings.length];
			for (int i = 0; i < points.length; i++)
			{
				String[] pointProps = pointStrings[i].split(" ");
				points[i] = new Point(Integer.parseInt(pointProps[0]), Integer.parseInt(pointProps[1]));
			}

			scanner.close();
			return new World(objects, points);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public boolean checkWin()
	{
		for (Point point : this.endpoints)
			if (objects[point.y][point.x] == null || !(objects[point.y][point.x].type == ObjectType.CRATE))
				return false;
		return true;
	}

	@Override
	public String toString()
	{
		String desc = "";
		for (int row = 0; row < getHeight(); row++)
		{
			desc += "\n";
			for (int col = 0; col < getWidth(); col++)
				desc += ((objects[row][col] == null) ? "null" : objects[row][col].type.name()) + ", ";
		}
		return desc;
	}
}
