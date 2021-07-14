package hello;

import com.sun.jna.Pointer;

// Event information from a single mouse currently plugged into the PC.
public class MouseEvent
{
  // A handle to the device.
  public Pointer hDevice;

  // The raw mouse event data
  public RawMouse data;
}