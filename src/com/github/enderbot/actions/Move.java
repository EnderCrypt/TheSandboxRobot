package com.github.enderbot.actions;

import java.awt.Point;

import com.github.enderbot.Action;
import com.github.enderbot.Coordinate;
import com.github.enderbot.DynamicEntity;
import com.github.enderbot.RobotActionFailed;
import com.github.enderbot.Simulation;

public class Move implements Action
{
	private Point movement;
	private Point originalPosition;
	private int dirAdd;
	
	public Move(int dirAdd)
	{
		this.dirAdd = dirAdd;
	}
	
	@Override
	public void init(Simulation simulation, DynamicEntity entity, int updates)
	{
		movement = entity.getRotation().rotate(dirAdd).getMovement();
		originalPosition = entity.getPosition();
		Coordinate newPosition = new Coordinate(originalPosition.x+movement.x, originalPosition.y+movement.y);
		if (simulation.isFreeTile(newPosition) == false)
		{
			throw new RobotActionFailed();
		}
		entity.setPosition(simulation, newPosition);
		entity.setAnimationOffset(-movement.x, -movement.y);
	}

	@Override
	public void update(DynamicEntity entity, double progress)
	{
		double x = movement.x*(1-progress);
		double y = movement.y*(1-progress);
		entity.setAnimationOffset(-x, -y);
	}

	@Override
	public void finish(DynamicEntity entity)
	{
		entity.setAnimationOffset(0, 0);
	}
}
