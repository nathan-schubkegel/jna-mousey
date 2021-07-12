package hello;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;
import com.sun.jna.Union;

// A struct intended for use with APIs that take a pointer and write a 4-byte value there.
@FieldOrder({ "value" })
public class RefInt extends Structure
{
  public int value;
  public RefInt() { value = 0; }
  public RefInt(int value) { this.value = value; }
}