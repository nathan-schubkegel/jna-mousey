package hello;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import java.util.Scanner;

public class HelloWorld
{
  public static void main(String[] args)
  {
    try
    {
      RefInt numDevices = new RefInt();
      if (-1 == User32.library.GetRawInputDeviceList(null, numDevices, new RawInputDeviceList().size()))
      {
        throw new Exception("GetRawInputDeviceList returned failure code when asked for number of devices");
      }
      
      // // times 2 + 10 to avoid needing to handle ERROR_INSUFFICIENT_BUFFER scenarios
      RawInputDeviceList[] devices = (RawInputDeviceList[])(new RawInputDeviceList()).toArray(numDevices.value * 2 + 10);
      numDevices.value = devices.length;
      int numDevices2 = User32.library.GetRawInputDeviceList(devices, numDevices, new RawInputDeviceList().size());
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
          buffer.clear();
          RefInt nameLen = new RefInt(499);
          int nameCharsLen = User32.library.GetRawInputDeviceInfoW(devices[i].hDevice, RIDI_DEVICENAME, buffer, nameLen);
          if (nameCharsLen < 0)
          {
            System.out.println("GetRawInputDeviceInfoW failed for some mouse device");
          }
          else
          {
            String mouseName = buffer.getWideString(0); // TODO: how to only slurp 'nameCharsLen' characters, instead of reading to null terminator?
            System.out.println("nameCharsLen=" + Integer.toString(nameCharsLen, 10) + " nameLen=" + Integer.toString(nameLen.value, 10) + " name=" + mouseName);
          }
        }
      }
      
      WndProc wndproc = new WndProc()
      {
        public Pointer callback(Pointer hwnd, int uMsg, Pointer wParam, Pointer lParam)
        {
          RawMouse mouse = User32.TryGetRawMouseEventData(uMsg, lParam);
          if (mouse != null)
          {
            System.out.println("WM_INPUT MOUSE" +
              " usFlags=" + Integer.toHexString(mouse.usFlags) + 
              " usButtonFlags=" + Integer.toHexString(mouse.usButtonFlags) + 
              " usButtonData=" + Integer.toHexString(mouse.usButtonData) + 
              " ulRawButtons=" + Integer.toHexString(mouse.ulRawButtons) + 
              " lLastX=" + Integer.toString(mouse.lLastX, 10) +
              " lLastY=" + Integer.toString(mouse.lLastY, 10) + 
              " ulExtraInformation=" + Integer.toHexString(mouse.ulExtraInformation));
          }
          return User32.library.DefWindowProcW(hwnd, uMsg, wParam, lParam);
        }
      };
      Pointer hwnd = User32.CreateMessageHandlingWindow(wndproc);
      User32.RegisterKeyboardAndMouse(hwnd);
      Msg msg = new Msg();
      while (User32.library.GetMessageW(msg, null, 0, 0) != 0)
      {
        //System.out.println("main hwnd=" + Long.toHexString(Pointer.nativeValue(msg.hWnd)) + " uMsg=" + Integer.toHexString(msg.message) + " wParam" + Long.toHexString(Pointer.nativeValue(msg.wParam)) + " lParam" + Long.toHexString(Pointer.nativeValue(msg.lParam)));
        User32.library.DispatchMessageW(msg);
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
}