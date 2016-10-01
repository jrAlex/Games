package jrAlex.core;

import com.google.gson.Gson;

public class Main
{
	public static void main(String[] args)
	{
		Gson g = new Gson();
		World world = new World(64);
		world.addObject(new Entity(0, 0, 1, 1, world));
		String obj = g.toJson(world);
		World world2 = g.fromJson(obj, World.class);
		System.out.println(world2.scale);
	}
}