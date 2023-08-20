/*   $Archive:: /Ti/jni/BulkDataOutput.java                                $  
 *      $Date:: 6/15/00 5:07p                                              $  
 *  $Revision:: 4                                                          $  
 *   Description: bulk I/O output interface
 *   Copyright 2000, Dan Bonachea <bonachea@cs.berkeley.edu> 
 *    see license.txt for usage information     
 */

package ti.io;

/** Interface for bulk I/O output
 * @see ti.io.BulkDataOutputStream
 * @see ti.io.BulkRandomAccessFile
 *
 * @author Dan Bonachea
 */

public interface BulkDataOutput extends java.io.DataOutput {
  public void writeArray(boolean[] array) throws java.io.IOException;
  public void writeArray(boolean[] array, int arrayoffset, int count) throws java.io.IOException;

  public void writeArray(char[] array) throws java.io.IOException;
  public void writeArray(char[] array, int arrayoffset, int count) throws java.io.IOException;

  public void writeArray(byte[] array) throws java.io.IOException;
  public void writeArray(byte[] array, int arrayoffset, int count) throws java.io.IOException;

  public void writeArray(short[] array) throws java.io.IOException;
  public void writeArray(short[] array, int arrayoffset, int count) throws java.io.IOException;

  public void writeArray(int[] array) throws java.io.IOException;
  public void writeArray(int[] array, int arrayoffset, int count) throws java.io.IOException;

  public void writeArray(long[] array) throws java.io.IOException;
  public void writeArray(long[] array, int arrayoffset, int count) throws java.io.IOException;

  public void writeArray(float[] array) throws java.io.IOException;
  public void writeArray(float[] array, int arrayoffset, int count) throws java.io.IOException;

  public void writeArray(double[] array) throws java.io.IOException;
  public void writeArray(double[] array, int arrayoffset, int count) throws java.io.IOException;
	}
