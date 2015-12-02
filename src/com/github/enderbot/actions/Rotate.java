package com.github.enderbot.actions;

import com.github.enderbot.Action;
import com.github.enderbot.DynamicEntity;
import com.github.enderbot.Simulation;

public class Rotate implements Action
{
	private int dir;
	private double startingRotation;
	
	public Rotate(int dir)
	{
		if ((dir == 1) || (dir == -1))
			this.dir = dir;
		else
			throw new RuntimeException("invalid rotation");
	}
	
	@Override
	public void init(Simulation robot, DynamicEntity entity, int updates)
	{
		startingRotation = Math.toDegrees(entity.getRotation().getRotation());
	}

	@Override
	public void update(DynamicEntity entity, double progress)
	{
		entity.setAnimationRotation(Math.toRadians(startingRotation+((dir*90)*progress)));
	}

	@Override
	public void finish(DynamicEntity entity)
	{
		if (dir == -1)
		{
			entity.setRotation(entity.getRotation().rotateLeft());
		}
		if (dir == 1)
		{
			entity.setRotation(entity.getRotation().rotateRight());
		}
	}

}
