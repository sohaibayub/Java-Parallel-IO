/*   $Archive:: /Ti/jni/BulkDataOutputStream.java                          $  
 *      $Date:: 6/15/00 4:47p                                              $  
 *  $Revision:: 5                                                          $  
 *   Description: bulk I/O sequential output
 *   Copyright 2000, Dan Bonachea <bonachea@cs.berkeley.edu> 
 *    see license.txt for usage information     
 */

package ti.io;

/** bulk synchronous sequential I/O output class
 * Inherits non-bulk (single primitive) methods from superclass
 *
 * @see java.io.DataOutputStream
 * @see ti.io.BulkDataOutput
 *
 * @author Dan Bonachea
 */

public class BulkDataOutputStream extends java.io.DataOutputStream implements BulkDataOutput {

  // constructors

  public BulkDataOutputStream(java.io.OutputStream out) {
    super(out);
    }

  // bulk synchronous write
  public void writeArray(boolean[] array, int arrayoffset, int count) throws java.io.IOException {
    writeArray0(array, arrayoffset, count);
    }
  public void writeArray(boolean[] array) throws java.io.IOException {
    writeArray0(array, 0, array.length);
    }
  private native void writeArray0(boolean[] array, int arrayoffset, int count);

  public void writeArray(char[] array, int arrayoffset, int count) throws java.io.IOException {
    writeArray0(array, arrayoffset, count);
    }
  public void writeArray(char[] array) throws java.io.IOException {
    writeArray0(array, 0, array.length);
    }
  private native void writeArray0(char[] array, int arrayoffset, int count);

  public void writeArray(byte[] array, int arrayoffset, int count) throws java.io.IOException {
    writeArray0(array, arrayoffset, count);
    }
  public void writeArray(byte[] array) throws java.io.IOException {
    writeArray0(array, 0, array.length);
    }
  private native void writeArray0(byte[] array, int arrayoffset, int count);

  public void writeArray(short[] array, int arrayoffset, int count) throws java.io.IOException {
    writeArray0(array, arrayoffset, count);
    }
  public void writeArray(short[] array) throws java.io.IOException {
    writeArray0(array, 0, array.length);
    }
  private native void writeArray0(short[] array, int arrayoffset, int count);

  public void writeArray(int[] array, int arrayoffset, int count) throws java.io.IOException {
    writeArray0(array, arrayoffset, count);
    }
  public void writeArray(int[] array) throws java.io.IOException {
    writeArray0(array, 0, array.length);
    }
  private native void writeArray0(int[] array, int arrayoffset, int count);

  public void writeArray(long[] array, int arrayoffset, int count) throws java.io.IOException {
    writeArray0(array, arrayoffset, count);
    }
  public void writeArray(long[] array) throws java.io.IOException {
    writeArray0(array, 0, array.length);
    }
  private native void writeArray0(long[] array, int arrayoffset, int count);

  public void writeArray(float[] array, int arrayoffset, int count) throws java.io.IOException {
    writeArray0(array, arrayoffset, count);
    }
  public void writeArray(float[] array) throws java.io.IOException {
    writeArray0(array, 0, array.length);
    }
  private native void writeArray0(float[] array, int arrayoffset, int count);

  public void writeArray(double[] array, int arrayoffset, int count) throws java.io.IOException {
    writeArray0(array, arrayoffset, count);
    }
  public void writeArray(double[] array) throws java.io.IOException {
    writeArray0(array, 0, array.length);
    }
  private native void writeArray0(double[] array, int arrayoffset, int count);

  private static native void init();

  static {
   System.loadLibrary("ti_io_BulkDataOutputStream");
   init();
   }

  };
