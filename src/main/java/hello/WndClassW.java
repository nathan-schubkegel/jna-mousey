package hello;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;
import com.sun.jna.WString;

// Contains the window class attributes that are registered by the RegisterClass function.
// https://docs.microsoft.com/en-us/windows/win32/api/winuser/ns-winuser-wndclassw
@FieldOrder({ "style", "lpfnWndProc", "cbClsExtra", "cbWndExtra", "hInstance", "hIcon", "hCursor", "hbrBackground", "lpszMenuName", "lpszClassName" })
public class WndClassW extends Structure
{
  // The class style(s).
  public int style;

  // A pointer to the window procedure.
  public WndProc lpfnWndProc;

  // The number of extra bytes to allocate following the window-class structure.
  // The system initializes the bytes to zero.
  public int cbClsExtra;

  // The number of extra bytes to allocate following the window instance.
  // The system initializes the bytes to zero.
  public int cbWndExtra;

  // A handle to the instance that contains the window procedure for the class.
  public Pointer hInstance;

  // A handle to the class icon. This member must be a handle to an icon resource.
  // If this member is NULL, the system provides a default icon.
  public Pointer hIcon;
  
  // A handle to the class cursor. This member must be a handle to a cursor resource.
  // If this member is NULL, an application must explicitly set the cursor shape whenever
  // the mouse moves into the application's window.
  public Pointer hCursor;
  
  // A handle to the class background brush.
  public Pointer hbrBackground;

  // The resource name of the class menu, as the name appears in the resource file.
  public Pointer lpszMenuName;
  
  // A pointer to a null-terminated string specifying the window class name.
  public WString lpszClassName;
}