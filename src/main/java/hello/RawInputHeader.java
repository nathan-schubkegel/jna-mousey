package hello;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;
import com.sun.jna.Union;

// Contains the header information that is part of the raw input data.
// https://docs.microsoft.com/en-us/windows/win32/api/winuser/ns-winuser-rawinputheader
@FieldOrder({ "dwType", "dwSize", "hDevice", "wParam" })
public class RawInputHeader extends Structure
{
  // The type of raw input. It can be one of the following values:
  // RIM_TYPEMOUSE 0   Raw input comes from the mouse.
  // RIM_TYPEKEYBOARD 1   Raw input comes from the keyboard.
  // RIM_TYPEHID 2   Raw input comes from some device that is not a keyboard or a mouse.
  public int dwType;
  
  // The size, in bytes, of the entire input packet of data. This includes RAWINPUT plus possible extra input reports in the RAWHID variable length array.
  public int dwSize;
  
  // A handle to the device generating the raw input data.
  public Pointer hDevice;
  
  // The value passed in the wParam parameter of the WM_INPUT message.
  public Pointer wParam;
}