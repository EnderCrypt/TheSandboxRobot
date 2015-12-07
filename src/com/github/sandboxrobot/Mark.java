package com.github.sandboxrobot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class Mark
{
	private Color color;
	private String text = null;

	private final int MARK_SIZE = 8;

	public Mark setColor(Color color)
	{
		this.color = color;
		return this;
	}

	public Mark setText(String text)
	{
		this.text = text;
		return this;
	}

	protected void draw(Graphics2D g2d, int x, int y)
	{
		g2d.setColor(Color.BLACK);
		g2d.drawOval(x - MARK_SIZE, y - MARK_SIZE, (MARK_SIZE * 2), (MARK_SIZE * 2));
		g2d.setColor(color);
		g2d.fillOval(x - MARK_SIZE, y - MARK_SIZE, (MARK_SIZE * 2), (MARK_SIZE * 2));
	}

	protected void drawText(Graphics2D g2d, int x, int y)
	{
		if (text != null)
		{
			x += 10;
			FontMetrics fm = g2d.getFontMetrics();
			Dimension textSize = new Dimension(fm.stringWidth(text), fm.getHeight());
			Dimension borderSize = new Dimension(textSize.width + 8, textSize.height + 3);
			g2d.setColor(new Color(255, 255, 150));
			g2d.fillRect(x, y, borderSize.width, borderSize.height);
			g2d.setColor(Color.BLACK);
			g2d.drawRect(x, y, borderSize.width, borderSize.height);
			g2d.drawString(text, x + 4, y + borderSize.height - 4);
		}
	}

}
