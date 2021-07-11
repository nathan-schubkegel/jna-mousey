package hello;

import com.sun.jna.Native;
import com.sun.jna.WString;

public class HelloWorld
{
  public static void main(String[] args)
  {
    Greeter greeter = new Greeter();
    System.out.println(greeter.sayHello());
    
    User32 lib = (User32) Native.loadLibrary("user32", User32.class);
    System.out.println("Presenting Message Box ...");
    lib.MessageBoxW(0, new WString("MessageBox success!!!"), new WString("Attention"), 0);
  }
}