package hello;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import java.util.Scanner;
import java.util.ArrayList;

public class HelloWorld
{
  public static void main(String[] args)
  {
    try
    {
      ArrayList<Mouse> mice = User32.GetMice();
      for (Mouse mouse : mice)
      {
        System.out.println("enumerated mouse " +
          " hDevice=" + Long.toHexString(Pointer.nativeValue(mouse.hDevice)) +
          " friendlyName=" + mouse.friendlyName + 
          " hardwareId=" + mouse.hardwareId);
      }

      // create an invisible window that receives raw mouse event messages and prints them to the console
      WndProc wndproc = new WndProc()
      {
        public Pointer callback(Pointer hwnd, int uMsg, Pointer wParam, Pointer lParam)
        {
          MouseEvent mouse = User32.TryGetRawMouseEventData(uMsg, lParam);
          if (mouse != null)
          {
            System.out.println("WM_INPUT MOUSE" +
              " hDevice=" + Long.toHexString(Pointer.nativeValue(mouse.hDevice)) +
              " usFlags=" + Integer.toHexString(mouse.data.usFlags) + 
              " usButtonFlags=" + Integer.toHexString(mouse.data.usButtonFlags) + 
              " usButtonData=" + Integer.toHexString(mouse.data.usButtonData) + 
              " ulRawButtons=" + Integer.toHexString(mouse.data.ulRawButtons) + 
              " lLastX=" + Integer.toString(mouse.data.lLastX, 10) +
              " lLastY=" + Integer.toString(mouse.data.lLastY, 10) + 
              " ulExtraInformation=" + Integer.toHexString(mouse.data.ulExtraInformation));
          }
          return User32.library.DefWindowProcW(hwnd, uMsg, wParam, lParam);
        }
      };
      Pointer hwnd = User32.CreateMessageHandlingWindow(wndproc);
      User32.RegisterToReceiveRawMouseEvents(hwnd);
      
      // create a window to demonstrate cursor clipping
      WndProc wndproc2 = new WndProc()
      {
        public Pointer callback(Pointer hwnd, int uMsg, Pointer wParam, Pointer lParam)
        {
          // TODO: is there anything else to do here?
          System.out.println("example hwnd=" + Long.toHexString(Pointer.nativeValue(hwnd)) + " uMsg=" + Integer.toHexString(uMsg) + " wParam" + Long.toHexString(Pointer.nativeValue(wParam)) + " lParam" + Long.toHexString(Pointer.nativeValue(lParam)));
          return User32.library.DefWindowProcW(hwnd, uMsg, wParam, lParam);
        }
      };
      Pointer hwnd2 = User32.CreateExampleWindow(wndproc2);
      
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