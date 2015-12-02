package com.github.enderbot.entites;

import com.github.enderbot.Coordinate;
import com.github.enderbot.GuiGraphics;
import com.github.enderbot.StaticEntity;

public class Crate extends StaticEntity
{

	public Crate(Coordinate position)
	{
		super(position, GuiGraphics.CRATE);
		makeGrabAble();
	}
	
}
