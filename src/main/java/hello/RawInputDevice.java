package hello;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

// Defines information for the raw input devices.
// https://docs.microsoft.com/en-us/windows/win32/api/winuser/ns-winuser-rawinputdevice
@FieldOrder({ "usUsagePage", "usUsage", "dwFlags", "hwndTarget" })
public class RawInputDevice extends Structure
{
  // Top level collection Usage page for the raw input device.
  public short usUsagePage;

  // Top level collection Usage ID for the raw input device.
  public short usUsage;

  // Mode flag that specifies how to interpret the information provided by usUsagePage and usUsage.
  // It can be zero (the default). By default, the operating system sends raw input from devices
  // with the specified top level collection (TLC) to the registered application as long as it has
  // the window focus.
  public int dwFlags;

  // A handle to the target window. If NULL it follows the keyboard focus.
  public Pointer hwndTarget;
}