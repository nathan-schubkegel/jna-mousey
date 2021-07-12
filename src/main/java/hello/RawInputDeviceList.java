package hello;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

// Contains information about a raw input device.
// https://docs.microsoft.com/en-us/windows/win32/api/winuser/ns-winuser-rawinputdevicelist
@FieldOrder({ "hDevice", "dwType" })
public class RawInputDeviceList extends Structure
{
  // A handle to the raw input device.
  public Pointer hDevice;
  
  // The type of device. This can be one of the following values.
  // RIM_TYPEHID - 2 - The device is an HID that is not a keyboard and not a mouse.
  // RIM_TYPEKEYBOARD - 1 - The device is a keyboard.
  // RIM_TYPEMOUSE - 0 - The device is a mouse. 
  public int dwType;
}