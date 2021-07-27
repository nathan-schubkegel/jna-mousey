package hello;

import com.sun.jna.Pointer;
import com.sun.jna.Callback;

// An application-defined callback function used with the EnumWindows or EnumDesktopWindows function.
// https://docs.microsoft.com/en-us/previous-versions/windows/desktop/legacy/ms633498(v=vs.85)
public interface EnumWindowsProc extends Callback
{
  boolean callback(
    Pointer hwnd,
    Pointer lParam);
}