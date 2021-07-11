package hello;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.WString;

public class HelloWorld
{
  public static void main(String[] args)
  {
    try
    {
      WndProc wndproc = new WndProc()
      {
        public Pointer callback(Pointer hwnd, int uMsg, Pointer wParam, Pointer lParam)
        {
          System.out.println("wndproc hwnd=" + Long.toHexString(Pointer.nativeValue(hwnd)) + " uMsg=" + Integer.toHexString(uMsg) + " wParam" + Long.toHexString(Pointer.nativeValue(wParam)) + " lParam" + Long.toHexString(Pointer.nativeValue(lParam)));
          return User32.library.DefWindowProcW(hwnd, uMsg, wParam, lParam); 
        }
      };
      Pointer hwnd = User32.CreateMessageHandlingWindow(wndproc);
      User32.RegisterKeyboardAndMouse(hwnd);
      Msg msg = new Msg();
      while (User32.library.GetMessageW(msg, null, 0, 0) != 0)
      {
        System.out.println("main hwnd=" + Long.toHexString(Pointer.nativeValue(msg.hWnd)) + " uMsg=" + Integer.toHexString(msg.message) + " wParam" + Long.toHexString(Pointer.nativeValue(msg.wParam)) + " lParam" + Long.toHexString(Pointer.nativeValue(msg.lParam)));
        User32.library.DispatchMessageW(msg);
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
}