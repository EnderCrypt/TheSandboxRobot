package com.github.sandboxrobot.entites;

import java.io.Serializable;

import com.github.sandboxrobot.Coordinate;
import com.github.sandboxrobot.GuiGraphics;
import com.github.sandboxrobot.Simulation;
import com.github.sandboxrobot.StaticEntity;

public class Crate extends StaticEntity implements Serializable
{
	private static final long serialVersionUID = 2741599410246035142L;
	/**
	 * 
	 */

	public Crate(Simulation simulation, Coordinate position)
	{
		super(simulation, position, GuiGraphics.CRATE);
		makeGrabAble();
	}
	
}
