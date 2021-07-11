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

    RawInputDevice[] Rid = (RawInputDevice[])(new RawInputDevice()).toArray(1);

    Rid[0].usUsagePage = 0x01;          // HID_USAGE_PAGE_GENERIC
    Rid[0].usUsage = 0x02;              // HID_USAGE_GENERIC_MOUSE
    Rid[0].dwFlags = RIDEV_NOLEGACY | RIDEV_INPUTSINK;
    Rid[0].hwndTarget = hwndTarget;

    // commented out because I'm not interested in keyboard events right now --nathschu
    //Rid[1].usUsagePage = 0x01;          // HID_USAGE_PAGE_GENERIC
    //Rid[1].usUsage = 0x06;              // HID_USAGE_GENERIC_KEYBOARD
    //Rid[1].dwFlags = RIDEV_NOLEGACY | RIDEV_INPUTSINK;
    //Rid[1].hwndTarget = hwndTarget;
    
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
  
  public static RawMouse TryGetRawMouseEventData(int uMsg, Pointer lParam)
  {
    final int WM_INPUT = 0xFF;
    switch (uMsg)
    {
      case WM_INPUT:
      {
        int RID_INPUT = 0x10000003;
        int RIM_TYPEMOUSE = 0;
        RawInput data = new RawInput();
        RefLong dataSize = new RefLong(data.size());
        int copiedSize = User32.library.GetRawInputData(lParam, RID_INPUT, data, dataSize, new RawInputHeader().size());
        if (copiedSize != data.size())
        {
          //System.out.println("ignored a raw input event because copiedSize == " + Integer.toString(copiedSize, 10));
        }
        else if (data.header.dwType == RIM_TYPEMOUSE)
        {
          return data.mouse;
        }
        else
        {
          //System.out.println("ignored a raw input event because it wasn't RIM_TYPEMOUSE");
        }
        break;
      }
      default:
      {
        //System.out.println("wndproc hwnd=" + Long.toHexString(Pointer.nativeValue(hwnd)) + " uMsg=" + Integer.toHexString(uMsg) + " wParam" + Long.toHexString(Pointer.nativeValue(wParam)) + " lParam" + Long.toHexString(Pointer.nativeValue(lParam)));
      }
    }
    return null;
  }
}