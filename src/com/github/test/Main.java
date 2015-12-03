package com.github.test;

import com.github.sandboxrobot.Robot;
import com.github.sandboxrobot.entites.Crate;

public class Main
{

	public static void main(String[] args) throws InterruptedException
	{
		Robot robot = new Robot("storage 1.scen");
		
		robot.pauseSimulation();
		
		robot.rotateRight();
		robot.rotateRight();
		while (true)
		{
			if ((robot.look() == 0) && (robot.lookWhat() == Crate.class))
			{
				robot.grab();
				robot.rotateRight();
				int track1 = 0;
				while (robot.detect())
				{
					robot.strafeRight();
					track1++;
				}
				int track2 = 0;
				while (true)
				{
					int look = robot.look();
					if ((look == -1) || (look > 1))
					{
						robot.forwards();
						track2++;
						continue;
					}
					break;
				}
				robot.place();
				for (int i=0;i<track2;i++)
				{
					robot.backwards();
				}
				robot.rotateLeft();
				for (int i=0;i<track1;i++)
				{
					robot.forwards();
				}
			}
		}
	}

}
