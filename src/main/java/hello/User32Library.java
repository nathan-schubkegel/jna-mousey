package hello;

import com.sun.jna.Library;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.LastErrorException;

public interface User32Library extends Library
{
  public int MessageBoxW(int something, WString text, WString caption, int flags);
    
  // Registers the devices that supply the raw input data.
  // Returns TRUE if the function succeeds; call GetLastError for failure information.
  // https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-registerrawinputdevices
  public boolean RegisterRawInputDevices(
    RawInputDevice[] pRawInputDevices,
    int uiNumDevices,
    int cbSize) throws LastErrorException;

  // Retrieves a message from the calling thread's message queue, blocking until one is available.
  // If the function retrieves the WM_QUIT message, the return value is zero.
  // If there is an error, the return value is -1.
  // https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-getmessage
  public int GetMessageW(
    Msg lpMsg,
    Pointer hWnd,
    int wMsgFilterMin,
    int wMsgFilterMax);
  
  // Dispatches a message to a window procedure.
  // The return value specifies the value returned by the window procedure. Generally ignored.
  // https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-dispatchmessage
  public Pointer DispatchMessageW(Msg lpMsg);
  
  // Calls the default window procedure to provide default processing for window messages.
  // The return value is the result of the message processing and depends on the message.
  public Pointer DefWindowProcW(
    Pointer hWnd,
    int msg,
    Pointer wParam,
    Pointer lParam);
  
  // Registers a window class for subsequent use in calls to the CreateWindow or CreateWindowEx function.
  // If the function succeeds, the return value is a class atom that uniquely identifies the class being registered.
  // If the function fails, the return value is zero. To get extended error information, call GetLastError.
  // https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-registerclassw
  short RegisterClassW(WndClassW lpWndClass) throws LastErrorException;
  
  // Creates an overlapped, pop-up, or child window.
  // If the function succeeds, the return value is a handle to the new window.
  // If the function fails, the return value is NULL. To get extended error information, call GetLastError.
  // https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-createwindoww
  Pointer CreateWindowExW(
   int dwExStyle,
   WString lpClassName,
   WString lpWindowName,
   int dwStyle,
   int x,
   int y,
   int nWidth,
   int nHeight,
   Pointer hWndParent,
   Pointer hMenu,
   Pointer hInstance,
   Pointer lpParam) throws LastErrorException;

  // Retrieves the raw input from the specified device.
  // If pData is NULL and the function is successful, the return value is 0. If pData is not NULL and the function is successful, the return value is the number of bytes copied into pData.
  // If there is an error, the return value is (UINT)-1.
  // https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-getrawinputdata
  int GetRawInputData(
    Pointer hRawInput,
    int uiCommand,
    RawInput pData,
    RefLong pcbSize,
    int cbSizeHeader);
}

