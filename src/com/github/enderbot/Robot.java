package com.github.enderbot;

import com.github.enderbot.actions.Move;
import com.github.enderbot.actions.Rotate;
import com.github.enderbot.actions.Stay;

public class Robot
{
	private Simulation simulation;
	public Robot()
	{
		this(1.0);
	}
	
	public Robot(double simulationSpeed)
	{
		simulation = new Simulation((int) (250/simulationSpeed));
	}
	
	// DEBUG //
	
	/**
	 * this makes the whole simulation pause
	 */
	public void playSimulation()
	{
		simulation.play();
	}
	
	/**
	 * pauses the simulation, mildly usefull for debugging
	 */
	public void pauseSimulation()
	{
		simulation.pause();
	}
	
	// PUBLIC COMMANDS //
	
	/**
	 * @return the rotation of the robot
	 */
	public Rotation getRotation()
	{
		return simulation.robotEntity.getRotation();
	}
	
	/**
	 * allows the robot to check if theres anything infront of it
	 * @return number of free tiles infront of the robot untill it see's something
	 * -1 means it see's nothing
	 * 0 means theres something right infront of it
	 * 1 and above means theres something that far infront of it
	 */
	public int look()
	{
		return simulation.robotEntity.checkVision(simulation);
	}
	
	/**
	 * tells you what object it is seeing
	 * @return the class of the object it is seeing
	 */
	public Class<? extends Entity> lookWhat()
	{
		Entity entity = simulation.robotEntity.checkVisionWhat(simulation);
		if (entity == null)
		{
			return null;
		}
		Class<? extends Entity> type = entity.getClass();
		return type;
	}

	// PUBLIC ROBOT ACTION COMMANDS //
	
	/**
	 * attempts to grab the item infront of it
	 * @return
	 */
	public boolean grab()
	{
		simulation.blockIfPause();
		return simulation.robotEntity.grab(simulation);
	}
	
	/**
	 * attempts to place the carried item infront of it
	 * @return
	 */
	public boolean place()
	{
		simulation.blockIfPause();
		return simulation.robotEntity.put(simulation);
	}
	
	/**
	 * makes the robot do nothing for 1 turn
	 */
	public void stay()
	{
		simulation.blockIfPause();
		simulation.animate(new Stay());
	}
	
	/**
	 * attempts to move the robot forwards
	 * @return weather moving forwards was successfull
	 */
	public boolean forwards()
	{
		simulation.blockIfPause();
		try
		{
			simulation.animate(new Move(0));
		}
		catch (RobotActionFailed e)
		{
			return false;
		}
		return true;
	}
	
	/**
	 * attempts to move the robot backwards
	 * @return weather true/false moving was successful
	 */
	public boolean backwards()
	{
		simulation.blockIfPause();
		try
		{
			simulation.animate(new Move(-2));
		}
		catch (RobotActionFailed e)
		{
			return false;
		}
		return true;
	}
	
	/**
	 * makes the robot rotate to the left
	 */
	public void rotateLeft()
	{
		simulation.blockIfPause();
		simulation.animate(new Rotate(-1));
	}
	
	/**
	 * attempts to move the robot towards the left of its current direction, whitout rotating it
	 * @return weather true/false moving was successful
	 */
	public boolean strafeLeft()
	{
		simulation.blockIfPause();
		try
		{
			simulation.animate(new Move(1));
		}
		catch (RobotActionFailed e)
		{
			return false;
		}
		return true;
	}
	
	/**
	 * makes the robot rotate to the right
	 */
	public void rotateRight()
	{
		simulation.blockIfPause();
		simulation.animate(new Rotate(1));
	}
	
	/**
	 * attempts to move the robot towards the right of its current direction, whitout rotating it
	 * @return weather true/false moving was successful
	 */
	public boolean strafeRight()
	{
		simulation.blockIfPause();
		try
		{
			simulation.animate(new Move(-1));
		}
		catch (RobotActionFailed e)
		{
			return false;
		}
		return true;
	}
	
}
