package ui;

import static org.lwjgl.opengl.GL11.*;
import render.util.RenderHelper;

public class Button extends Component
{
	String title;

	/**
	 * 0 = unselected 1 = hovered 2 = active
	 */
	int state;

	public Button(int x, int y, int width, int height, String title)
	{
		super(x, y, width, height);
		this.title = title;
		this.state = 0;
	}

	public void setState(int s)
	{
		state = s;
	}

	@Override
	public void draw()
	{
		if (state != 1) glColor4f(34f / 255f, 34f / 255f, 34f / 255f, 0.6f);
		else glColor4f(1, 153f / 255f, 51f / 255f, 0.6f);
		RenderHelper.renderRect(x, y, width, height);
	}
}
