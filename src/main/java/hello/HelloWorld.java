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