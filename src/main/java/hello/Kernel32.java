package hello;

import com.sun.jna.Callback;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import java.util.ArrayList;

public class Kernel32
{
  public static final Kernel32Library library;

  // static initializer
  static
  {
    library = (Kernel32Library)Native.load("kernel32", Kernel32Library.class);
  }
}