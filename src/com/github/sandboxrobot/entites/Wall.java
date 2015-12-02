package com.github.sandboxrobot.entites;

import com.github.sandboxrobot.Coordinate;
import com.github.sandboxrobot.GuiGraphics;
import com.github.sandboxrobot.StaticEntity;

public class Wall extends StaticEntity
{

	public Wall(Coordinate position)
	{
		super(position, GuiGraphics.WALL);
	}

}
