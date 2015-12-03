package com.github.sandboxrobot.entites;

import java.io.Serializable;

import com.github.sandboxrobot.Coordinate;
import com.github.sandboxrobot.GuiGraphics;
import com.github.sandboxrobot.Simulation;
import com.github.sandboxrobot.StaticEntity;

public class Wall extends StaticEntity implements Serializable
{
	private static final long serialVersionUID = 2201687667015612389L;
	/**
	 * 
	 */

	public Wall(Simulation simulation, Coordinate position)
	{
		super(simulation, position, GuiGraphics.WALL);
	}

}
