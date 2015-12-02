package com.github.sandboxrobot.entites;

import com.github.sandboxrobot.Coordinate;
import com.github.sandboxrobot.GuiGraphics;
import com.github.sandboxrobot.Simulation;
import com.github.sandboxrobot.StaticEntity;

public class Crate extends StaticEntity
{

	public Crate(Simulation simulation, Coordinate position)
	{
		super(simulation, position, GuiGraphics.CRATE);
		makeGrabAble();
	}
	
}
