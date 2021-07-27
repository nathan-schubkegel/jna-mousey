package hello;

import com.sun.jna.Library;
import com.sun.jna.Memory;
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
    // The command flag. This parameter can be one of the following values.
    // RID_HEADER - 0x10000005 - Get the header information from the RAWINPUT structure.
    // RID_INPUT - 0x10000003 - Get the raw data from the RAWINPUT structure.
    int uiCommand,
    // A pointer to the data that comes from the RAWINPUT structure. This depends on the value of uiCommand. 
    // If pData is NULL, the required size of the buffer is returned in *pcbSize.
    RawInput pData,
    // If pData is NULL, this value becomes populated with the required space.
    // If pData != NULL, this value indicates how much space is available in pData.
    RefInt pcbSize,
    // The size, in bytes, of the RAWINPUTHEADER structure.
    int cbSizeHeader);

  // Enumerates the raw input devices attached to the system.
  // If the function is successful, the return value is the number of devices stored in the buffer pointed to by pRawInputDeviceList.
  // On any other error, the function returns (UINT) -1 and GetLastError returns the error indication.
  // https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-getrawinputdevicelist
  int GetRawInputDeviceList(
    // An array of RAWINPUTDEVICELIST structures for the devices attached to the system. 
    // If NULL, the number of devices are returned in *puiNumDevices.
    RawInputDeviceList[] pRawInputDeviceList,
    // If pRawInputDeviceList is NULL, the function populates this variable with the number of devices attached to the system;
    // otherwise, this variable specifies the number of RAWINPUTDEVICELIST structures that can be contained in the buffer to which pRawInputDeviceList points. 
    // If this value is less than the number of devices attached to the system, the function returns the actual number of devices in this variable and fails with ERROR_INSUFFICIENT_BUFFER.
    RefInt puiNumDevices,
    // The size of a RAWINPUTDEVICELIST structure, in bytes.
    int cbSize);

  // Retrieves information about the raw input device.
  // If successful, this function returns a non-negative number indicating the number of bytes copied to pData.
  // If pData is not large enough for the data, the function returns -1. 
  // If pData is NULL, the function returns a value of zero. 
  // In both of these cases, pcbSize is set to the minimum size required for the pData buffer.
  // Call GetLastError to identify any other errors.
  // https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-getrawinputdeviceinfow
  int GetRawInputDeviceInfoW(
    // A handle to the raw input device. This comes from the hDevice member of RAWINPUTHEADER or from GetRawInputDeviceList.
    Pointer hDevice,
    // Specifies what data will be returned in pData. This parameter can be one of the following values.
    // RIDI_PREPARSEDDATA - 0x20000005 - pData is a PHIDP_PREPARSED_DATA pointer to a buffer for a top-level collection's preparsed data.
    // RIDI_DEVICENAME - 0x20000007 - pData points to a string that contains the device interface name.
    // If this device is opened with Shared Access Mode then you can call CreateFile with this name to open a HID collection and use returned handle for calling ReadFile to read input reports and WriteFile to send output reports.
    // For more information, see Opening HID Collections and Handling HID Reports.
    // For this uiCommand only, the value in pcbSize is the character count (not the byte count).
    // RIDI_DEVICEINFO - 0x2000000b - pData points to an RID_DEVICE_INFO structure. 
    int uiCommand,
    // A pointer to a buffer that receives the information specified by uiCommand.
    // If uiCommand is RIDI_DEVICEINFO, set the cbSize member of RID_DEVICE_INFO to sizeof(RID_DEVICE_INFO) before calling GetRawInputDeviceInfo.
    Memory pData,
    // The size, in bytes, of the data in pData.
    RefInt pcbSize) throws LastErrorException;

  // Confines the cursor to a rectangular area on the screen. If a subsequent cursor position (set by the SetCursorPos function or the mouse)
  // lies outside the rectangle, the system automatically adjusts the position to keep the cursor inside the rectangular area.
  // If the function succeeds, the return value is nonzero. Else the return value is zero. To get extended error information, call GetLastError.
  // https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-clipcursor
  public boolean ClipCursor(
    // A pointer to the structure that contains the screen coordinates of the upper-left and lower-right corners of the confining rectangle. If this parameter is NULL, the cursor is free to move anywhere on the screen.
    Rect lpRect) throws LastErrorException;

  // Retrieves information about the specified window. The function also retrieves the value at a specified offset into the extra window memory.
  // https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-getwindowlongptrw
  public Pointer GetWindowLongPtrW(
    // A handle to the window and, indirectly, the class to which the window belongs.
    Pointer hWnd,
    // The zero-based offset to the value to be set. Valid values are in the range zero through the number of bytes of extra window memory, minus the size of a LONG_PTR.
    // To set any other value, specify one of the following values.
    // GWL_EXSTYLE -20 Retrieves the extended window styles.
    // GWLP_HINSTANCE -6 Retrieves a handle to the application instance.
    // GWLP_HWNDPARENT -8 Retrieves a handle to the parent window, if there is one.
    // GWLP_ID -12 Retrieves the identifier of the window.
    // GWL_STYLE -16 Gets the window style.
    // GWLP_USERDATA -21 Gets the user data associated with the window. This data is intended for use by the application that created the window. Its value is initially zero.
    // GWLP_WNDPROC -4 Retrieves the pointer to the window procedure, or a handle representing the pointer to the window procedure. You must use the CallWindowProc function to call the window procedure.
    int nIndex) throws LastErrorException;

  // Changes an attribute of the specified window. The function also sets a value at the specified offset in the extra window memory.
  // If the function succeeds, the return value is the previous value of the specified offset.
  // If the function fails, the return value is zero. To get extended error information, call GetLastError.
  // If the previous value is zero and the function succeeds, the return value is zero, but the function does not clear the last error information.
  // https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-setwindowlongptrw
  public Pointer SetWindowLongPtrW(
    // A handle to the window and, indirectly, the class to which the window belongs.
    Pointer hWnd,
    // The zero-based offset to the value to be set. Valid values are in the range zero through the number of bytes of extra window memory, minus the size of a LONG_PTR.
    // To set any other value, specify one of the following values.
    // GWL_EXSTYLE -20 Sets a new extended window style.
    // GWLP_HINSTANCE -6 Sets a new application instance handle.
    // GWLP_ID -12 Sets a new identifier of the child window. The window cannot be a top-level window.
    // GWL_STYLE -16 Sets a new window style.
    // GWLP_USERDATA -21 Sets the user data associated with the window. This data is intended for use by the application that created the window. Its value is initially zero.
    // GWLP_WNDPROC -4 Sets a new address for the window procedure.
    int nIndex,
    // The replacement value.
    WndProc dwNewLong) throws LastErrorException;

  // Passes message information to the specified window procedure.
  // This method is typically used with SetWindowLongPtr to support chained window message handling procedures for a single window.
  // The return value specifies the result of the message processing and depends on the message sent.
  public Pointer CallWindowProcW(
    // The previous window procedure, as previously fetched by GetWindowLongPtr with GWLP_WNDPROC
    Pointer lpPrevWndFunc,
    // A handle to the window procedure to receive the message.
    Pointer hWnd,
    int Msg,
    Pointer wParam,
    Pointer lParam);

  // Enumerates all top-level windows on the screen by passing the handle to each window, in turn, to
  // an application-defined callback function. EnumWindows continues until the last top-level window is
  // enumerated or the callback function returns FALSE.
  // https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-enumwindows
  public boolean EnumWindows(
    // A pointer to an application-defined callback function.
    EnumWindowsProc lpEnumFunc,
    // An application-defined value to be passed to the callback function.
    Pointer lParam) throws LastErrorException;
  
  // Returns true if the window is visible. 'nuff said.
  // https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-iswindowvisible
  boolean IsWindowVisible(Pointer hwnd);
  
  // Retrieves the identifier of the thread that created the specified window and
  // optionally also the identifier of the process that created the window.
  // The return value is the identifier of the thread that created the window.
  // https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-getwindowthreadprocessid
  int GetWindowThreadProcessId(Pointer hWnd, RefInt lpdwProcessId);
}