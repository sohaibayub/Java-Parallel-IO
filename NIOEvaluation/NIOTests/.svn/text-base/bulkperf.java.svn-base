/*   $Archive:: /Ti/jni/bulkperf.java                                      $  
 *      $Date:: 6/15/00 4:47p                                              $  
 *  $Revision:: 5                                                          $  
 *   Description: bulk I/O performance test program
 *   Copyright 2000, Dan Bonachea <bonachea@cs.berkeley.edu> 
 *    see license.txt for usage information     
 */

import ti.io.*;
import java.io.*;
import java.lang.*;

public class bulkperf {
  public static long starttime;
  public static long temptime;
  public static long readtime=0;
  public static long writetime=0;

  public static void MSG(String s) {
    System.out.println((System.currentTimeMillis() - starttime) / 1000.0 + ": " + s);
    }
  //------------------------------------------------------------------------------------
  public static void main (String [] args) {
    if (args.length != 3) {
      System.out.println("Usage: bulkperf <infilename> <outfilename> <chunksizeinbytes>");
      System.exit(1);
      }

    File infile = new File(args[0]);
    File outfile = new File(args[1]);
		int filesz = (int)infile.length();
    System.out.println("filesz= " + filesz);

		int chunksize=1024;
    try { chunksize = Integer.parseInt(args[2]); } catch (Throwable exn) { System.out.println("bad chunksize"); System.exit(1);};
    System.out.println("chunksize= " + chunksize);

    int numchunks = (new Long(filesz/chunksize)).intValue(); // Java sucks
    if (numchunks * chunksize != filesz) {
      System.out.println("chunksize not an even divisor of filesz.");
      throw new InternalError("goodbye");
      }
    if (((int)(chunksize / 8)) * 8 != chunksize) {
      System.out.println("chunksize not a double-word size");
      throw new InternalError("goodbye");
      }

		starttime = System.currentTimeMillis();
    try {
      {
        //------------------------------------------------------------------------------------
        // byte
        byte[] buf = new byte[chunksize];

        {
          MSG("--------- BulkRandomAccessFile byte test -----------");
          BulkRandomAccessFile inf = new BulkRandomAccessFile(infile, "r");
          BulkRandomAccessFile outf = new BulkRandomAccessFile(outfile, "rw");

		      temptime = System.currentTimeMillis();
          for (int i = 0; i < numchunks; i++) {
            inf.readArray(buf);
            outf.writeArray(buf);
            }
          temptime = System.currentTimeMillis() - temptime;
          MSG("Time elapsed: " + temptime/1000.0);
          }

        {
          MSG("--------- RandomAccessFile byte test -----------");
          RandomAccessFile inf = new RandomAccessFile(infile, "r");
          RandomAccessFile outf = new RandomAccessFile(outfile, "rw");

		      temptime = System.currentTimeMillis();
          for (int i = 0; i < numchunks; i++) {
            for (int j = 0; j < chunksize; j++) {
              buf[j] = inf.readByte();
              }
            for (int j = 0; j < chunksize; j++) {
              outf.writeByte(buf[j]);
              }
            }
          temptime = System.currentTimeMillis() - temptime;
          MSG("Time elapsed: " + temptime/1000.0);
          }
        {
          MSG("--------- BulkDataInputStream/BulkDataOutputStream byte test -----------");
          BulkDataInputStream inf = 
            new BulkDataInputStream(new BufferedInputStream(new FileInputStream(infile)));
          BulkDataOutputStream outf = 
            new BulkDataOutputStream(new BufferedOutputStream(new FileOutputStream(outfile)));

		      temptime = System.currentTimeMillis();
          for (int i = 0; i < numchunks; i++) {
            inf.readArray(buf);
            outf.writeArray(buf);
            }
          temptime = System.currentTimeMillis() - temptime;
          MSG("Time elapsed: " + temptime/1000.0);
          }

        {
          MSG("--------- DataInputStream/DataOutputStream byte test -----------");
          DataInputStream inf = 
            new DataInputStream(new BufferedInputStream(new FileInputStream(infile)));
          DataOutputStream outf = 
            new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outfile)));

		      temptime = System.currentTimeMillis();
          for (int i = 0; i < numchunks; i++) {
            for (int j = 0; j < chunksize; j++) {
              buf[j] = inf.readByte();
              }
            for (int j = 0; j < chunksize; j++) {
              outf.writeByte(buf[j]);
              }
            }
          temptime = System.currentTimeMillis() - temptime;
          MSG("Time elapsed: " + temptime/1000.0);
          }
        } // bytes
      {
        //------------------------------------------------------------------------------------
        // longs
        int numelems = chunksize/8;
        long[] buf = new long[numelems];

        {
          MSG("--------- BulkRandomAccessFile long test -----------");
          BulkRandomAccessFile inf = new BulkRandomAccessFile(infile, "r");
          BulkRandomAccessFile outf = new BulkRandomAccessFile(outfile, "rw");

		      temptime = System.currentTimeMillis();
          for (int i = 0; i < numchunks; i++) {
            inf.readArray(buf);
            outf.writeArray(buf);
            }
          temptime = System.currentTimeMillis() - temptime;
          MSG("Time elapsed: " + temptime/1000.0);
          }

        {
          MSG("--------- RandomAccessFile long test -----------");
          RandomAccessFile inf = new RandomAccessFile(infile, "r");
          RandomAccessFile outf = new RandomAccessFile(outfile, "rw");

		      temptime = System.currentTimeMillis();
          for (int i = 0; i < numchunks; i++) {
            for (int j = 0; j < numelems; j++) {
              buf[j] = inf.readLong();
              }
            for (int j = 0; j < numelems; j++) {
              outf.writeLong(buf[j]);
              }
            }
          temptime = System.currentTimeMillis() - temptime;
          MSG("Time elapsed: " + temptime/1000.0);
          }
        {
          MSG("--------- BulkDataInputStream/BulkDataOutputStream long test -----------");
          BulkDataInputStream inf = 
            new BulkDataInputStream(new BufferedInputStream(new FileInputStream(infile)));
          BulkDataOutputStream outf = 
            new BulkDataOutputStream(new BufferedOutputStream(new FileOutputStream(outfile)));

		      temptime = System.currentTimeMillis();
          for (int i = 0; i < numchunks; i++) {
            inf.readArray(buf);
            outf.writeArray(buf);
            }
          temptime = System.currentTimeMillis() - temptime;
          MSG("Time elapsed: " + temptime/1000.0);
          }

        {
          MSG("--------- DataInputStream/DataOutputStream long test -----------");
          DataInputStream inf = 
            new DataInputStream(new BufferedInputStream(new FileInputStream(infile)));
          DataOutputStream outf = 
            new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outfile)));

		      temptime = System.currentTimeMillis();
          for (int i = 0; i < numchunks; i++) {
            for (int j = 0; j < numelems; j++) {
              buf[j] = inf.readLong();
              }
            for (int j = 0; j < numelems; j++) {
              outf.writeLong(buf[j]);
              }
            }
          temptime = System.currentTimeMillis() - temptime;
          MSG("Time elapsed: " + temptime/1000.0);
          }
        } // longs

      MSG("Tests Complete.");
      }
    catch (Throwable exn) {
      MSG("caught " + exn + " : "  + exn.getMessage());
      }
    }
  }
