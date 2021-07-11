package hello;

import com.sun.jna.Library;
import com.sun.jna.WString;

public interface User32 extends Library
{
    public int MessageBoxW(int something, WString text, WString caption, int flags);
}

