package com.github.sandboxrobot;

public interface Action
{
	public void init(Simulation robot, DynamicEntity entity, int updates);
	
	public void update(DynamicEntity entity, double progress);
	
	public void finish(DynamicEntity entity);
	
}
