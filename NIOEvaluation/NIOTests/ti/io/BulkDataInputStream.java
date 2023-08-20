/*   $Archive:: /Ti/jni/BulkDataInputStream.java                           $  
 *      $Date:: 6/15/00 4:47p                                              $  
 *  $Revision:: 4                                                          $  
 *   Description: bulk I/O sequential input
 *   Copyright 2000, Dan Bonachea <bonachea@cs.berkeley.edu> 
 *    see license.txt for usage information     
 */

package ti.io;

/** bulk synchronous sequential I/O input class
 * Inherits non-bulk (single primitive) methods from superclass
 *
 * @see java.io.DataInputStream
 * @see ti.io.BulkDataInput
 *
 * @author Dan Bonachea
 */

public class BulkDataInputStream extends java.io.DataInputStream implements BulkDataInput  {

  // constructors

  public BulkDataInputStream(java.io.InputStream in) {
    super(in);
    }

  // bulk synchronous read

  public void readArray(boolean[] array, int arrayoffset, int count) throws java.io.IOException {
    readArray0(array, arrayoffset, count);
    }
  public void readArray(boolean[] array) throws java.io.IOException {
    readArray0(array, 0, array.length);
    }
  private native void readArray0(boolean[] array, int arrayoffset, int count);

  public void readArray(char[] array, int arrayoffset, int count) throws java.io.IOException {
    readArray0(array, arrayoffset, count);
    }
  public void readArray(char[] array) throws java.io.IOException {
    readArray0(array, 0, array.length);
    }
  private native void readArray0(char[] array, int arrayoffset, int count);

  public void readArray(byte[] array, int arrayoffset, int count) throws java.io.IOException {
    readArray0(array, arrayoffset, count);
    }
  public void readArray(byte[] array) throws java.io.IOException {
    readArray0(array, 0, array.length);
    }
  private native void readArray0(byte[] array, int arrayoffset, int count);

  public void readArray(short[] array, int arrayoffset, int count) throws java.io.IOException {
    readArray0(array, arrayoffset, count);
    }
  public void readArray(short[] array) throws java.io.IOException {
    readArray0(array, 0, array.length);
    }
  private native void readArray0(short[] array, int arrayoffset, int count);

  public void readArray(int[] array, int arrayoffset, int count) throws java.io.IOException {
    readArray0(array, arrayoffset, count);
    }
  public void readArray(int[] array) throws java.io.IOException {
    readArray0(array, 0, array.length);
    }
  private native void readArray0(int[] array, int arrayoffset, int count);

  public void readArray(long[] array, int arrayoffset, int count) throws java.io.IOException {
    readArray0(array, arrayoffset, count);
    }
  public void readArray(long[] array) throws java.io.IOException {
    readArray0(array, 0, array.length);
    }
  private native void readArray0(long[] array, int arrayoffset, int count);

  public void readArray(float[] array, int arrayoffset, int count) throws java.io.IOException {
    readArray0(array, arrayoffset, count);
    }
  public void readArray(float[] array) throws java.io.IOException {
    readArray0(array, 0, array.length);
    }
  private native void readArray0(float[] array, int arrayoffset, int count);

  public void readArray(double[] array, int arrayoffset, int count) throws java.io.IOException {
    readArray0(array, arrayoffset, count);
    }
  public void readArray(double[] array) throws java.io.IOException {
    readArray0(array, 0, array.length);
    }
  private native void readArray0(double[] array, int arrayoffset, int count);

  private static native void init();

  static {
   System.loadLibrary("ti_io_BulkDataInputStream");
   init();
   }

  };
