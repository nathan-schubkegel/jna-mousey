package hello;

import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.LastErrorException;

public interface Kernel32Library extends Library
{
  // Gets the pid of the running process
  // https://docs.microsoft.com/en-us/windows/win32/api/processthreadsapi/nf-processthreadsapi-getcurrentprocessid
  int GetCurrentProcessId();
}