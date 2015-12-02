package com.github.sandboxrobot;

import java.awt.Point;

public interface Clickable
{
	void clicked(Simulation robot, GuiButton source, Point point);
}
