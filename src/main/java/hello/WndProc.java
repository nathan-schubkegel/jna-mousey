package hello;

import com.sun.jna.Pointer;
import com.sun.jna.Callback;

// An application-defined function that processes messages sent to a window.
// https://docs.microsoft.com/en-us/windows/win32/api/winuser/nc-winuser-wndproc
public interface WndProc extends Callback
{
  Pointer callback(
    Pointer hwnd,
    int uMsg,
    Pointer wParam,
    Pointer lParam);
}