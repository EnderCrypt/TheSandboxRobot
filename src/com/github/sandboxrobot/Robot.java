package com.github.sandboxrobot;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import com.github.sandboxrobot.actions.Move;
import com.github.sandboxrobot.actions.Rotate;
import com.github.sandboxrobot.actions.Stay;
import com.github.sandboxrobot.entites.SandboxRobot;

public class Robot
{
	private final Simulation simulation;

	public final MoveCategory move = new MoveCategory();
	public final EyeCategory eye = new EyeCategory();
	public final StrafeCategory strafe = new StrafeCategory();
	public final RotateCategory rotate = new RotateCategory();
	public final MarkCategory mark = new MarkCategory();

	public Robot()
	{
		simulation = new Simulation();
		// finalize
		simulation.awaitGui();
	}

	public Robot(String scenario)
	{
		simulation = new Simulation();
		File file = new File(SaveModule.saveDirectory + "/" + scenario);
		if ((file.exists()) && (file.isFile()))
		{
			try
			{
				SaveModule.load(simulation, file);
			}
			catch (ClassNotFoundException | IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			System.err.println("The scenario file " + file.getAbsolutePath() + " does not exist");
		}
		// finalize
		simulation.awaitGui();
	}

	// ADVANCED //

	/**
	 * sets the animation speed of the simulation
	 * 
	 * @param simulationSpeed
	 */
	public void setSpeed(double simulationSpeed)
	{
		simulation.setSpeed(simulationSpeed);
	}

	/**
	 * pauses the simulation, mildly usefull for debugging
	 */
	public void pauseSimulation()
	{
		simulation.pause();
		simulation.blockIfPause(); // needs to be tested
	}

	/**
	 * makes the bot show debug messages about what happends
	 */
	public void activateDebug()
	{
		simulation.robotEntity.setDebug(true);
	}

	// PUBLIC COMMANDS //

	public int getVisionDistance()
	{
		return SandboxRobot.MAX_VISION;
	}

	/**
	 * @return the rotation of the robot
	 */
	public Rotation getRotation()
	{
		return simulation.robotEntity.getRotation();
	}

	/**
	 * @return the coordinates of the robot
	 */
	public Coordinate getPosition()
	{
		return simulation.robotEntity.getPosition();
	}

	/**
	 * checks if the bot has available storage in it
	 * 
	 * @return
	 */
	public boolean hasFreeStorage()
	{
		return simulation.robotEntity.hasFreeCarryStorage();
	}

	// PUBLIC ROBOT ACTION COMMANDS //

	/**
	 * attempts to grab the item infront of it
	 * 
	 * @return if grab was successful
	 */
	public boolean grab()
	{
		simulation.blockIfPause();
		return simulation.robotEntity.grab(simulation);
	}

	/**
	 * attempts to place the carried item infront of it
	 * 
	 * @return if place was successful
	 */
	public boolean place()
	{
		simulation.blockIfPause();
		return simulation.robotEntity.place(simulation);
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
	 * This category contains methods for moving forwards and backwards
	 */
	public class MoveCategory
	{
		/**
		 * attempts to move the robot forwards a certain amount of moves
		 * 
		 * @return weather moving forwards was successfull
		 */

		public int forwards(int moves)
		{
			int succeded = 0;
			for (int i = 0; i < moves; i++)
			{
				if (forwards() == false)
				{
					break;
				}
				succeded++;

			}
			return succeded;
		}

		/**
		 * attempts to move the robot forwards
		 * 
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
		 * attempts to move the robot backwards a certain amount of moves
		 * 
		 * @return weather true/false moving was successful
		 */
		public int backwards(int moves)
		{
			int succeded = 0;
			for (int i = 0; i < moves; i++)
			{
				if (backwards() == false)
				{
					break;
				}
				succeded++;

			}
			return succeded;
		}

		/**
		 * attempts to move the robot backwards
		 * 
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
	}

	/**
	 * this category contains methods for seeing and looking at external data
	 */
	public class EyeCategory
	{
		public int look()
		{
			return simulation.robotEntity.checkVision(simulation);
		}

		/**
		 * tells you what object it is seeing
		 * 
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

		/**
		 * tells you if theres something right infront of you
		 * 
		 * @return
		 */
		public boolean detect()
		{
			return (simulation.robotEntity.getFrontEntity(simulation) != null);
		}

		/**
		 * tells you what is right infront of you
		 * 
		 * @return
		 */
		public Class<? extends Entity> detectWhat()
		{
			Entity entity = simulation.robotEntity.getFrontEntity(simulation);
			if (entity == null)
			{
				return null;
			}
			Class<? extends Entity> type = entity.getClass();
			return type;
		}
	}

	/**
	 * category for strafing left and right of the currently aimed direction
	 */
	public class StrafeCategory
	{
		/**
		 * attempts to move the robot towards the left of its current direction a certain amount of moves, whitout rotating it
		 * 
		 * @return weather true/false moving was successful
		 */
		public int left(int moves)
		{
			int succeded = 0;
			for (int i = 0; i < moves; i++)
			{
				if (left() == false)
				{
					break;
				}
				succeded++;

			}
			return succeded;
		}

		/**
		 * attempts to move the robot towards the left of its current direction, whitout rotating it
		 * 
		 * @return weather true/false moving was successful
		 */
		public boolean left()
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

		/**
		 * attempts to move the robot towards the right of its current direction a certain amount of moves, whitout rotating it
		 * 
		 * @return weather true/false moving was successful
		 */
		public int right(int moves)
		{
			int succeded = 0;
			for (int i = 0; i < moves; i++)
			{
				if (right() == false)
				{
					break;
				}
				succeded++;

			}
			return succeded;
		}

		/**
		 * attempts to move the robot towards the right of its current direction, whitout rotating it
		 * 
		 * @return weather true/false moving was successful
		 */
		public boolean right()
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
	}

	/**
	 * this category contains methods for rotating the robot left and right
	 */
	public class RotateCategory
	{
		/**
		 * makes the robot rotate to the left
		 */
		public void left()
		{
			simulation.blockIfPause();
			simulation.animate(new Rotate(-1));
		}

		/**
		 * makes the robot rotate to the right
		 */
		public void right()
		{
			simulation.blockIfPause();
			simulation.animate(new Rotate(1));
		}
	}

	/**
	 * this category contains methods for marking tiles with diffrent colors (mostly for debugging)
	 */
	public class MarkCategory
	{
		private Mark get(Coordinate coordinate)
		{
			coordinate = coordinate.getLocation();
			Mark mark = null;
			synchronized (simulation.marks)
			{
				mark = simulation.marks.get(coordinate);
				if (mark == null)
				{
					mark = new Mark();
					simulation.marks.put(coordinate, mark);
				}
			}
			return mark;
		}

		public void set(int x, int y, Color color)
		{
			set(new Coordinate(x, y), color);
		}

		public void set(Coordinate coordinate, Color color)
		{
			Mark mark = get(coordinate);
			mark.setColor(color);
		}

		public void set(int x, int y, Color color, String text)
		{
			set(new Coordinate(x, y), color, text);
		}

		public void set(Coordinate coordinate, Color color, String text)
		{
			Mark mark = get(coordinate);
			mark.setColor(color);
			mark.setText(text);
		}

		public void remove(int x, int y)
		{
			remove(new Coordinate(x, y));
		}

		public void remove(Coordinate coordinate)
		{
			synchronized (simulation.marks)
			{
				simulation.marks.remove(coordinate);
			}
		}

		public void clear()
		{
			synchronized (simulation.marks)
			{
				simulation.marks.clear();
			}
		}
	}
}
