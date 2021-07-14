package hello;

import com.sun.jna.Pointer;

// Information about a single mouse currently plugged into the PC.
public class Mouse
{
  // A handle to the device.
  public Pointer hDevice;

  // The user-friendly name of the mouse.
  public String friendlyName;
  
  // The mouse's hardware id - aka the device interface name.
  public String hardwareId;
}