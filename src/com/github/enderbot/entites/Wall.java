package com.github.enderbot.entites;

import com.github.enderbot.Coordinate;
import com.github.enderbot.GuiGraphics;
import com.github.enderbot.StaticEntity;

public class Wall extends StaticEntity
{

	public Wall(Coordinate position)
	{
		super(position, GuiGraphics.WALL);
	}

}
