package com.github.sandboxrobot.entites;

import com.github.sandboxrobot.Coordinate;
import com.github.sandboxrobot.GuiGraphics;
import com.github.sandboxrobot.Simulation;
import com.github.sandboxrobot.StaticEntity;

public class Wall extends StaticEntity
{

	public Wall(Simulation simulation, Coordinate position)
	{
		super(simulation, position, GuiGraphics.WALL);
	}

}
