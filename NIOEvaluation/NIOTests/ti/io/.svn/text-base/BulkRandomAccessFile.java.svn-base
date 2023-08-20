/*   $Archive:: /Ti/jni/BulkRandomAccessFile.java                          $  
 *      $Date:: 6/15/00 4:47p                                              $  
 *  $Revision:: 4                                                          $  
 *   Description: bulk I/O random access input/output
 *   Copyright 2000, Dan Bonachea <bonachea@cs.berkeley.edu> 
 *    see license.txt for usage information     
 */

package ti.io;

/** bulk synchronous random access I/O class
 * Inherits non-bulk (single primitive) methods from superclass
 *
 * @see java.io.RandomAccessFile
 * @see ti.io.BulkDataInput
 * @see ti.io.BulkDataOutput
 *
 * @author Dan Bonachea
 */

public class BulkRandomAccessFile extends java.io.RandomAccessFile implements BulkDataInput,BulkDataOutput {

  // constructors

  public BulkRandomAccessFile(String name, String mode) throws java.io.IOException {
    super(name, mode);
    }
  public BulkRandomAccessFile(java.io.File file, String mode) throws java.io.IOException {
    super(file, mode);
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
   System.loadLibrary("ti_io_BulkRandomAccessFile");
   init();
   }

  };
