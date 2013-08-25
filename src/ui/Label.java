package ui;

public class Label extends Component
{
	String title;

	public Label(int x, int y, int width, int height, String title)
	{
		super(x, y, width, height);
		this.title = title;
	}

	@Override
	public void mouseEvent(int posX, int posY, byte b, boolean c)
	{
	}
}
