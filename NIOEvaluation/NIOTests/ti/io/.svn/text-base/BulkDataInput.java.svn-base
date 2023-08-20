/*   $Archive:: /Ti/jni/BulkDataInput.java                                 $  
 *      $Date:: 6/15/00 5:07p                                              $  
 *  $Revision:: 4                                                          $  
 *   Description: bulk I/O input interface
 *   Copyright 2000, Dan Bonachea <bonachea@cs.berkeley.edu> 
 *    see license.txt for usage information     
 */

package ti.io;

/** Interface for bulk I/O input
 * @see ti.io.BulkDataInputStream
 * @see ti.io.BulkRandomAccessFile
 *
 * @author Dan Bonachea
 */

public interface BulkDataInput extends java.io.DataInput {
  public void readArray(boolean[] array) throws java.io.IOException;
  public void readArray(boolean[] array, int arrayoffset, int count) throws java.io.IOException;

  public void readArray(char[] array) throws java.io.IOException;
  public void readArray(char[] array, int arrayoffset, int count) throws java.io.IOException;

  public void readArray(byte[] array) throws java.io.IOException;
  public void readArray(byte[] array, int arrayoffset, int count) throws java.io.IOException;

  public void readArray(short[] array) throws java.io.IOException;
  public void readArray(short[] array, int arrayoffset, int count) throws java.io.IOException;

  public void readArray(int[] array) throws java.io.IOException;
  public void readArray(int[] array, int arrayoffset, int count) throws java.io.IOException;

  public void readArray(long[] array) throws java.io.IOException;
  public void readArray(long[] array, int arrayoffset, int count) throws java.io.IOException;

  public void readArray(float[] array) throws java.io.IOException;
  public void readArray(float[] array, int arrayoffset, int count) throws java.io.IOException;

  public void readArray(double[] array) throws java.io.IOException;
  public void readArray(double[] array, int arrayoffset, int count) throws java.io.IOException;
	}
