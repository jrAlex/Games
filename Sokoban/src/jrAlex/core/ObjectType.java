package jrAlex.core;

public enum ObjectType
{
	WALL(false), CRATE(true), PLAYER(true, true);

	public boolean moveable, dirImages;

	private ObjectType(boolean moveable)
	{
		this.moveable = moveable;
		this.dirImages = false;
	}
	
	private ObjectType(boolean moveable, boolean dirImages)
	{
		this(moveable);
		this.dirImages = dirImages;
	}
}
