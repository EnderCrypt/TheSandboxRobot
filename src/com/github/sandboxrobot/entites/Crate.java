package com.github.sandboxrobot.entites;

import com.github.sandboxrobot.Coordinate;
import com.github.sandboxrobot.GuiGraphics;
import com.github.sandboxrobot.StaticEntity;

public class Crate extends StaticEntity
{

	public Crate(Coordinate position)
	{
		super(position, GuiGraphics.CRATE);
		makeGrabAble();
	}
	
}
