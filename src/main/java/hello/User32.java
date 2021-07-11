package hello;

import com.sun.jna.Callback;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.WString;

public class User32
{
  public static final User32Library library;

  // static initializer
  static
  {
    library = (User32Library)Native.load("user32", User32Library.class);
  }

  public static void RegisterKeyboardAndMouse(Pointer hwndTarget) throws Exception
  {
    // If set, this prevents any devices specified by usUsagePage or usUsage
    // from generating legacy messages. This is only for the mouse and keyboard.
    int RIDEV_NOLEGACY = 0x00000030;

    // If set, this enables the caller to receive WM_INPUT_DEVICE_CHANGE notifications
    // for device arrival and device removal.
    //int RIDEV_DEVNOTIFY = 0x00002000;

    // If set, this enables the caller to receive the input even when the caller is not
    // in the foreground. Note that hwndTarget must be specified.
    int RIDEV_INPUTSINK = 0x00000100;

    RawInputDevice[] Rid = (RawInputDevice[])(new RawInputDevice()).toArray(2);

    Rid[0].usUsagePage = 0x01;          // HID_USAGE_PAGE_GENERIC
    Rid[0].usUsage = 0x02;              // HID_USAGE_GENERIC_MOUSE
    Rid[0].dwFlags = RIDEV_NOLEGACY | RIDEV_INPUTSINK;
    Rid[0].hwndTarget = hwndTarget;

    Rid[1].usUsagePage = 0x01;          // HID_USAGE_PAGE_GENERIC
    Rid[1].usUsage = 0x06;              // HID_USAGE_GENERIC_KEYBOARD
    Rid[1].dwFlags = RIDEV_NOLEGACY | RIDEV_INPUTSINK;
    Rid[1].hwndTarget = hwndTarget;
    
    if (!library.RegisterRawInputDevices(Rid, Rid.length, Rid[0].size()))
    {
       throw new Exception("RegisterRawInputDevices failed");
    }
  }
  
  public static Pointer CreateMessageHandlingWindow(WndProc wndproc) throws Exception
  {
    // Register the window class.
    WndClassW wc = new WndClassW();
    wc.lpfnWndProc = wndproc;
    wc.lpszClassName = new WString("RawInputListener");
    short rc = library.RegisterClassW(wc);
    if (rc == 0) throw new Exception("blah, registerClass failed");
    
    // create the window
    int WS_POPUP = (int)0x80000000;
    Pointer hwnd = library.CreateWindowExW(
      0,
      new WString("RawInputListener"),
      null,
      WS_POPUP,
      0, 0, 0, 0,
      null, null, null, null);
    if (hwnd == null || Pointer.nativeValue(hwnd) == 0) throw new Exception("blah, CreateWindowExW failed");
    return hwnd;
  }
}