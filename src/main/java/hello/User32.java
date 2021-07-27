package hello;

import com.sun.jna.Callback;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import java.util.ArrayList;

public class User32
{
  public static final User32Library library;

  // static initializer
  static
  {
    library = (User32Library)Native.load("user32", User32Library.class);
  }

  public static void RegisterToReceiveRawMouseEvents(Pointer hwndTarget) throws Exception
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
    // TODO: kinda lame that this will only ever succeed once. Oh well. It's a learning app.
    WndClassW wc = new WndClassW();
    wc.lpfnWndProc = wndproc;
    wc.lpszClassName = new WString("RawInputListener");
    short rc = library.RegisterClassW(wc);
    if (rc == 0) throw new Exception("blah, registerClass failed");

    // create the window
    Pointer hwnd = library.CreateWindowExW(
      0,
      new WString("RawInputListener"),
      null,
      WindowStyle.WS_POPUP,
      0, 0, 0, 0,
      null, null, null, null);
    if (hwnd == null || Pointer.nativeValue(hwnd) == 0) throw new Exception("blah, CreateWindowExW failed");
    return hwnd;
  }
  
  public static Pointer CreateExampleWindow(WndProc wndproc) throws Exception
  {
    // Register the window class.
    // TODO: kinda lame that this will only ever succeed once. Oh well. It's a learning app.
    WndClassW wc = new WndClassW();
    wc.lpfnWndProc = wndproc;
    wc.lpszClassName = new WString("ExampleWindow");
    short rc = library.RegisterClassW(wc);
    if (rc == 0) throw new Exception("blah, registerClass failed");
    
    // create the window
    Pointer hwnd = library.CreateWindowExW(
      0,
      new WString("ExampleWindow"),
      new WString("Example"),
      WindowStyle.WS_OVERLAPPED | WindowStyle.WS_VISIBLE | WindowStyle.WS_CAPTION,
      100, 100, 200, 300,
      null, null, null, null);
    if (hwnd == null || Pointer.nativeValue(hwnd) == 0) throw new Exception("blah, CreateWindowExW failed");
    return hwnd;
  }

  public static MouseEvent TryGetRawMouseEventData(int uMsg, Pointer lParam)
  {
    final int WM_INPUT = 0xFF;
    switch (uMsg)
    {
      case WM_INPUT:
      {
        int RID_INPUT = 0x10000003;
        int RIM_TYPEMOUSE = 0;
        RawInput data = new RawInput();
        RefInt dataSize = new RefInt(data.size());
        int copiedSize = User32.library.GetRawInputData(lParam, RID_INPUT, data, dataSize, new RawInputHeader().size());
        if (copiedSize != data.size())
        {
          //System.out.println("ignored a raw input event because copiedSize == " + Integer.toString(copiedSize, 10));
        }
        else if (data.header.dwType == RIM_TYPEMOUSE)
        {
          MouseEvent result = new MouseEvent();
          result.hDevice = data.header.hDevice;
          result.data = data.mouse;
          return result;
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
  
  public static ArrayList<Mouse> GetMice() throws Exception
  {
    ArrayList<Mouse> results = new ArrayList<Mouse>();
    
    RefInt numDevices = new RefInt();
    if (-1 == library.GetRawInputDeviceList(null, numDevices, new RawInputDeviceList().size()))
    {
      throw new Exception("GetRawInputDeviceList returned failure code when asked for number of devices");
    }

    // times 2 + 10 to avoid needing to handle ERROR_INSUFFICIENT_BUFFER scenarios
    RawInputDeviceList[] devices = (RawInputDeviceList[])(new RawInputDeviceList()).toArray(numDevices.value * 2 + 10);
    numDevices.value = devices.length;
    int numDevices2 = library.GetRawInputDeviceList(devices, numDevices, new RawInputDeviceList().size());
    if (-1 == numDevices2)
    {
      throw new Exception("GetRawInputDeviceList returned failure code when asked for all devices");
    }
    Memory buffer = new Memory(1000);
    for (int i = 0; i < numDevices2; i++)
    {
      int RIM_TYPEMOUSE = 0;
      int RIDI_DEVICENAME = 0x20000007;
      if (devices[i].dwType == RIM_TYPEMOUSE)
      {
        RefInt nameLen = new RefInt(0);
        int result = library.GetRawInputDeviceInfoW(devices[i].hDevice, RIDI_DEVICENAME, null, nameLen);
        if (result != 0)
        {
          System.out.println("GetRawInputDeviceInfoW failed to retrieve DEVICENAME length for some mouse device");
          continue;
        }
        if (nameLen.value <= 0)
        {
          System.out.println("GetRawInputDeviceInfoW retrieved non-positive DEVICENAME length for some mouse device");
          continue;
        }
        if (buffer.size() < nameLen.value * 2) buffer = new Memory(nameLen.value * 2);
        buffer.clear();
        result = library.GetRawInputDeviceInfoW(devices[i].hDevice, RIDI_DEVICENAME, buffer, nameLen);
        if (result <= 0)
        {
          // NOTE: this happens when connected to the PC over remote desktop (at least on my Win7 PC).
          // I googled it. Others have seen it. Apparently you just can't enumerate remote desktop mice. Sad day. oh well.
          System.out.println("GetRawInputDeviceInfoW returned " + Integer.toString(result, 10) + " with previously determined nameLen=" + Integer.toString(nameLen.value, 10) + " for some mouse device");
          continue;
        }

        // TODO: I don't like JNA's getWideString() API here. It reads to a null terminator, but I know exactly how many characters should be read!
        String hardwareId = buffer.getWideString(0);
        //System.out.println("nameCharsLen=" + Integer.toString(result, 10) + " nameLen=" + Integer.toString(nameLen.value, 10) + " name=" + hardwareId);
        
        // TODO: ask for friendly name
        
        Mouse mouse = new Mouse();
        mouse.hDevice = devices[i].hDevice;
        mouse.hardwareId = hardwareId;
        //mouse.friendlyName = "bozo the monkey";
        results.add(mouse);
      }
    }
    // TODO: I'd like to dispose 'buffer' immediately... but I'm not seeing a way to do that in the javadocs...
    return results;
  }
  
  public static ArrayList<Pointer> GetVisibleTopLevelWindows()
  {
    // what's my process id?
    int myPid = Kernel32.library.GetCurrentProcessId();
    
    // scan all top-level windows
    ArrayList<Pointer> foundWindows = new ArrayList<Pointer>();
    RefInt processId = new RefInt();
    EnumWindowsProc proc = new EnumWindowsProc()
    {
      public boolean callback(Pointer hwnd, Pointer lParam)
      {
        // only want visible windows
        if (library.IsWindowVisible(hwnd))
        {
          // only want windows in this process
          processId.value = 0;
          library.GetWindowThreadProcessId(hwnd, processId);
          if (processId.value == myPid)
          {
            foundWindows.add(hwnd);
          }
        }
        return true;
      }
    };
    library.EnumWindows(proc, null);
    return foundWindows;
  }
}