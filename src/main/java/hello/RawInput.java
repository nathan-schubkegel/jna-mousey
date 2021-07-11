package hello;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;
import com.sun.jna.Union;

// Contains the raw input from a device.
// https://docs.microsoft.com/en-us/windows/win32/api/winuser/ns-winuser-rawinput
@FieldOrder({ "header", "mouse" })
public class RawInput extends Structure
{
  // The raw input data header.
  public RawInputHeader header;
  
  // If the data comes from a mouse (as indicated by header.dwType == RIM_TYPEMOUSE(0), this is the raw input data.
  public RawMouse mouse;
  
  // NOTE: I left off the union of other raw data types because I'm not using them --nathschu
}