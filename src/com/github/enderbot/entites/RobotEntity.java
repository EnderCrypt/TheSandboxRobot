package com.github.enderbot.entites;

import java.util.ArrayList;
import java.util.List;

import com.github.enderbot.Coordinate;
import com.github.enderbot.DynamicEntity;
import com.github.enderbot.Entity;
import com.github.enderbot.GuiGraphics;
import com.github.enderbot.Rotation;
import com.github.enderbot.Simulation;

public class RobotEntity extends DynamicEntity
{
	private static final int MAX_VISION = 10;
	
	private List<Entity> carried = new ArrayList<>();
	
	public RobotEntity(Coordinate position)
	{
		super(position, Rotation.NORTH, GuiGraphics.ROBOT);
	}
	
	private Coordinate getFrontCoordinates(Simulation simulation)
	{
		// find
		Coordinate position = getPosition();
		Rotation rotation = getRotation();
		Coordinate direction = rotation.getMovement();
		position.translate(direction.x, direction.y);
		return position;
	}
	
	public boolean grab(Simulation simulation)
	{
		Coordinate position = getFrontCoordinates(simulation);
		Entity grab = simulation.getEntity(position);
		if (grab == null)
		{
			return false;
		}
		if (grab.isGrabAble() == false)
		{
			return false;
		}
		simulation.removeEntity(position);
		carried.add(grab);
		return true;
	}
	
	public boolean put(Simulation simulation)
	{
		if (carried.size() == 0)
		{
			return false;
		}
		Coordinate position = getFrontCoordinates(simulation);
		if (simulation.isFreeTile(position) == false)
		{
			return false;
		}
		Entity entity = carried.remove(carried.size()-1);
		entity.setPosition(simulation, position); // adds it
		return true;
	}
	
	public int checkVision(Simulation simulation)
	{
		Coordinate movement = getRotation().getMovement();
		Coordinate checkPos = getPosition();
		int i;
		for (i=0;i<MAX_VISION;i++)
		{
			checkPos.translate(movement.x, movement.y);
			if (simulation.isFreeTile(checkPos) == false)
			{
				break;
			}
		}
		if (i == MAX_VISION)
			return -1;
		else
			return i;
	}
	
	public Entity checkVisionWhat(Simulation simulation)
	{
		int dist = checkVision(simulation);
		if (dist == -1)
		{
			return null;
		}
		Coordinate position = getPosition();
		Coordinate movement = getRotation().getMovement();
		dist++; // needed
		Coordinate target = new Coordinate(position.x+(movement.x*dist), position.y+(movement.y*dist));
		return simulation.getEntity(target);
	}

}
