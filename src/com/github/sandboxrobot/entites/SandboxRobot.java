package com.github.sandboxrobot.entites;

import java.util.ArrayList;
import java.util.List;

import com.github.sandboxrobot.Coordinate;
import com.github.sandboxrobot.DynamicEntity;
import com.github.sandboxrobot.Entity;
import com.github.sandboxrobot.GuiGraphics;
import com.github.sandboxrobot.Rotation;
import com.github.sandboxrobot.Simulation;

public class SandboxRobot extends DynamicEntity
{
	private static final int MAX_VISION = 10;
	private static final int MAX_CARRY = 10;
	
	private boolean debug_messages = false;
	
	private List<Entity> carried = new ArrayList<>();
	
	public SandboxRobot(Coordinate position)
	{
		super(position, Rotation.NORTH, GuiGraphics.ROBOT);
	}
	
	public void setDebug(boolean debug_messages)
	{
		this.debug_messages = debug_messages;
	}
	
	private void debug(String message)
	{
		if (debug_messages)
		{
			System.err.println(message);
		}
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
	
	public Entity getFrontEntity(Simulation simulation)
	{
		Coordinate coordinate = getFrontCoordinates(simulation);
		return simulation.getEntity(coordinate);
	}
	
	public boolean hasFreeCarryStorage()
	{
		return (carried.size() < MAX_CARRY);
	}
	
	public boolean grab(Simulation simulation)
	{
		Coordinate position = getFrontCoordinates(simulation);
		Entity grab = simulation.getEntity(position);
		if (grab == null)
		{
			debug("Alert: tried to grab nothing");
			return false;
		}
		if (hasFreeCarryStorage() == false)
		{
			debug("Warning: storage capacity was reached");
			return false;
		}
		if (grab.isGrabAble() == false)
		{
			debug("Alert: tried to grab unGrabAble object");
			return false;
		}
		simulation.removeEntity(position);
		carried.add(grab);
		return true;
	}
	
	public boolean place(Simulation simulation)
	{
		if (carried.size() == 0)
		{
			debug("Alert: tried to place nothing");
			return false;
		}
		Coordinate position = getFrontCoordinates(simulation);
		if (simulation.isFreeTile(position) == false)
		{
			debug("Alert: tried to place on occupied space");
			return false;
		}
		Entity entity = carried.remove(carried.size()-1);
		entity.placeAt(simulation, position);
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
