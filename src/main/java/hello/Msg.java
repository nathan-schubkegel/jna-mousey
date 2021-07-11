package hello;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

// Contains message information from a thread's message queue.
// https://docs.microsoft.com/en-us/windows/win32/api/winuser/ns-winuser-msg
@FieldOrder({ "hWnd", "message", "wParam", "lParam", "time", "pt_x", "pt_y", "lPrivate" })
public class Msg extends Structure
{
  // A handle to the window whose window procedure receives the message.
  // This member is NULL when the message is a thread message.
  public Pointer hWnd;
  
  // The message identifier.
  public int message;
  
  // Additional information about the message.
  public Pointer wParam;
  
  // Additional information about the message.
  public Pointer lParam;
  
  // The time at which the message was posted.
  public int time;
  
  // The X portion of the cursor position, in screen coordinates, when the message was posted.
  public int pt_x;
  
  // The Y portion of the cursor position, in screen coordinates, when the message was posted.
  public int pt_y;
  
  // who knows
  public int lPrivate;
}