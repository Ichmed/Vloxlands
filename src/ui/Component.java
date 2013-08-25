package ui;


public class Component
{
  int x;
  int y;
  int width;
  int height;
  
  String texture;
  
  public Component(int x, int y, int width, int height)
  {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    
    texture = null;
  }
  
  public void draw() {}
}
