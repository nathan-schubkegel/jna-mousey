package hello;

import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

// The RECT structure defines a rectangle by the coordinates of its upper-left and lower-right corners.
// https://docs.microsoft.com/en-us/windows/win32/api/windef/ns-windef-rect
@FieldOrder({ "left", "top", "right", "bottom" })
public class Rect extends Structure
{
  public int left;
  public int top;
  public int right;
  public int bottom;
  
  public Rect() { }
  public Rect(int left, int top, int right, int bottom)
  {
    this.left = left;
    this.top = top;
    this.right = right;
    this.bottom = bottom;
  }
}