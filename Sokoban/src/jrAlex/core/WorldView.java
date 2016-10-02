package jrAlex.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class WorldView extends View
{
	private static final long	serialVersionUID	= 1L;

	protected int				scale, i, widthInBlocks, heightInBlocks;
	protected World				world;

	public WorldView(int scale)
	{
		this.i = 2;
		this.widthInBlocks = 9;
		this.heightInBlocks = 9;
		loadNext();
		this.scale = scale;
		setBackground(Color.white);
		setSize(widthInBlocks * scale, heightInBlocks * scale);
		addKeyListener(new KeyboardControl());
		setFocusable(true);
		grabFocus();
	}

	public WorldView(World world, int scale)
	{
		this(scale);
		this.world = world;
	}

	@Override
	public void update(int delta)
	{
		if (world.checkWin())
			loadNext();
	}

	public void loadNext()
	{
		world = World.load("test" + i++);
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Point playerCoords = world.getPlayerCoords();
		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(widthInBlocks / 2 * scale, heightInBlocks / 2 * scale);
		g2d.translate(-playerCoords.x * scale, -playerCoords.y * scale);
		for (int row = 0; row < world.getHeight(); row++)
			for (int col = 0; col < world.getWidth(); col++)
			{
				g2d.drawImage(Images.getImage("floor"), col * scale, row * scale, scale, scale, null);
				if (world.getObjects()[row][col] != null)
					world.getObjects()[row][col].render(g2d, col, row, scale);
			}
		for (Point point : world.endpoints)
			g.drawImage(Images.getImage("endpoint"), point.x * scale, point.y * scale, scale, scale, null);
	}

	public void setWorld(World world)
	{
		this.world = world;
	}

	class KeyboardControl implements KeyListener
	{

		@Override
		public void keyPressed(KeyEvent e)
		{
			if (e.getKeyCode() == KeyEvent.VK_W)
				world.changePlayerDir(Direction.NORTH);
			else if (e.getKeyCode() == KeyEvent.VK_S)
				world.changePlayerDir(Direction.SOUTH);
			else if (e.getKeyCode() == KeyEvent.VK_A)
				world.changePlayerDir(Direction.WEST);
			else if (e.getKeyCode() == KeyEvent.VK_D)
				world.changePlayerDir(Direction.EAST);

			if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_A
					|| e.getKeyCode() == KeyEvent.VK_D)
				world.movePlayer();
		}

		@Override
		public void keyReleased(KeyEvent e)
		{

		}

		@Override
		public void keyTyped(KeyEvent e)
		{

		}
	}
}
