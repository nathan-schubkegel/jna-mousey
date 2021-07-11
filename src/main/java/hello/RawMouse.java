package hello;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;
import com.sun.jna.Union;

// Contains information about the state of the mouse.
// https://docs.microsoft.com/en-us/windows/win32/api/winuser/ns-winuser-rawmouse
@FieldOrder({ "usFlags", "usButtonFlags", "usButtonData", "ulRawButtons", "lLastX", "lLastY", "ulExtraInformation" })
public class RawMouse extends Structure
{
  // The mouse state. This member can be any reasonable combination of the following.
  // MOUSE_MOVE_RELATIVE - 0x00 - Mouse movement data is relative to the last mouse position.
  // MOUSE_MOVE_ABSOLUTE - 0x01 - Mouse movement data is based on absolute position.
  // MOUSE_VIRTUAL_DESKTOP - 0x02 - Mouse coordinates are mapped to the virtual desktop (for a multiple monitor system).
  // MOUSE_ATTRIBUTES_CHANGED - 0x04 - Mouse attributes changed; application needs to query the mouse attributes.
  // MOUSE_MOVE_NOCOALESCE - 0x08 - This mouse movement event was not coalesced. Mouse movement events can be coalescened by default.
  public short usFlags;
  
  // The transition state of the mouse buttons. This member can be one or more of the following values.
  // RI_MOUSE_BUTTON_1_DOWN / RI_MOUSE_LEFT_BUTTON_DOWN - 0x0001 - Left button changed to down.
  // RI_MOUSE_BUTTON_1_UP / RI_MOUSE_LEFT_BUTTON_UP - 0x0002 - Left button changed to up.
  // RI_MOUSE_BUTTON_2_DOWN / RI_MOUSE_RIGHT_BUTTON_DOWN - 0x0004 - Right button changed to down.
  // RI_MOUSE_BUTTON_2_UP / RI_MOUSE_RIGHT_BUTTON_UP - 0x0008 - Right button changed to up.
  // RI_MOUSE_BUTTON_3_DOWN / RI_MOUSE_MIDDLE_BUTTON_DOWN - 0x0010 - Middle button changed to down.
  // RI_MOUSE_BUTTON_3_UP / RI_MOUSE_MIDDLE_BUTTON_UP - 0x0020 - Middle button changed to up.
  // RI_MOUSE_BUTTON_4_DOWN - 0x0040 - XBUTTON1 changed to down.
  // RI_MOUSE_BUTTON_4_UP - 0x0080 - XBUTTON1 changed to up.
  // RI_MOUSE_BUTTON_5_DOWN - 0x0100 - XBUTTON2 changed to down.
  // RI_MOUSE_BUTTON_5_UP - 0x0200 - XBUTTON2 changed to up.
  // RI_MOUSE_WHEEL - 0x0400 - Raw input comes from a mouse wheel. The wheel delta is stored in usButtonData.
  //                        A positive value indicates that the wheel was rotated forward, away from the user; 
  //                        a negative value indicates that the wheel was rotated backward, toward the user.
  // RI_MOUSE_HWHEEL - 0x0800 - Raw input comes from a horizontal mouse wheel. The wheel delta is stored in usButtonData.
  //                        A positive value indicates that the wheel was rotated to the right; 
  //                        a negative value indicates that the wheel was rotated to the left.
  public short usButtonFlags;
  
  // If usButtonFlags has RI_MOUSE_WHEEL or RI_MOUSE_HWHEEL, this member specifies the distance the wheel is rotated.
  public short usButtonData;

  // The raw state of the mouse buttons. The Win32 subsystem does not use this member.
  public int ulRawButtons;
  
  // The motion in the X direction. This is signed relative motion or absolute motion, depending on the value of usFlags.
  public int lLastX;
  
  // The motion in the Y direction. This is signed relative motion or absolute motion, depending on the value of usFlags.
  public int lLastY;
  
  // The device-specific additional information for the event.
  public int ulExtraInformation;
}