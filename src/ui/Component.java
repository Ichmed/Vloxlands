package ui;

public abstract class Component
{
	int x;
	int y;
	int width;
	int height;

	String texture;

	boolean visible;

	public Component(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		visible = true;
		texture = null;
	}

	public void draw()
	{
	}
	
	public abstract void mouseEvent(int posX, int posY, byte b);
}
