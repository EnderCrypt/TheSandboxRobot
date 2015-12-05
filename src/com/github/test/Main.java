package com.github.test;

import com.github.sandboxrobot.Robot;
import com.github.sandboxrobot.entites.Crate;

public class Main
{

	public static void main(String[] args)
	{
		Robot robot = new Robot("storage 1.scen");

		robot.rotate.right();
		robot.rotate.right();
		;
		while (true)
		{
			if ((robot.eye.look() == 0) && (robot.eye.lookWhat() == Crate.class))
			{
				robot.grab();
				robot.rotate.right();
				int track1 = 0;
				while (robot.eye.detect())
				{
					robot.strafe.right();
					track1++;
				}
				int track2 = 0;
				while (true)
				{
					int look = robot.eye.look();
					if ((look == -1) || (look > 1))
					{
						robot.move.forwards();
						track2++;
						continue;
					}
					break;
				}
				robot.place();
				for (int i = 0; i < track2; i++)
				{
					robot.move.backwards();
				}
				robot.rotate.left();
				for (int i = 0; i < track1; i++)
				{
					robot.move.forwards();
				}
			}
		}
	}

}
