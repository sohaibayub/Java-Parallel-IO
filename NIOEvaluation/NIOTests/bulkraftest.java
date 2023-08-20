/*   $Archive:: /Ti/jni/bulkraftest.java                                   $  
 *      $Date:: 6/15/00 4:47p                                              $  
 *  $Revision:: 4                                                          $  
 *   Description: bulk I/O random access test program
 *   Copyright 2000, Dan Bonachea <bonachea@cs.berkeley.edu> 
 *    see license.txt for usage information     
 */

import ti.io.*;
import java.io.*;
import java.lang.*;

public class bulkraftest {
  public static long starttime;
  public static void MSG(String s) {
    System.out.println((System.currentTimeMillis() - starttime) / 1000.0 + ": " + s);
    }

  public static void main (String [] args) {
    if (args.length != 2) {
      System.out.println("Usage: bulkraftest <infilename> <type>");
      System.exit(1);
      }
    String infilename = args[0];
    File infile = new File(infilename);
    String type = args[1];
    String outfilename = "bulktest.tmp";
    File outfile = new File(outfilename);
    starttime = System.currentTimeMillis();

    try {
      BulkRandomAccessFile brafin = new BulkRandomAccessFile(infile, "r");

      MSG("File size is: " + brafin.length());
      int len = (int)brafin.length(); // Java sucks

      //------------------------------------------------------------------------------------
      if (type.indexOf("byte") >= 0 || type.indexOf("all") >= 0) {
        if (outfile.exists()) outfile.delete();
        BulkRandomAccessFile brafout = new BulkRandomAccessFile(outfile, "rw");
        MSG(" -------------- Beginning byte test.. -------------- ");
        int numelem = len;
        byte[] a = new byte[numelem];
        byte[] b = new byte[numelem];

        MSG("Seeking..");
        brafin.seek(0);
        MSG("Reading..");
        brafin.readArray(a, 0, numelem);
        MSG("Writing..");
        brafout.writeArray(a, 0, numelem);

        MSG("Re-reading..");
        brafout.seek(0);
        brafout.readArray(b, 0, numelem);

        MSG("Verifying..");
        for (int i=0; i < numelem; i++) {
          if (a[i] != b[i]) {
            int first = i-5;
            int last = i+5;
            if (first < 0) first = 0;
            if (last >= numelem) last = numelem;
            for (int x = first; x < last; x++) 
              MSG("  a[" + x + "]= " + a[x] + "  b[" + x + "]= " + b[x]);
            throw new InternalError("Verification mismatch at element " + i);
            }
          }
        MSG("Verified.");
        }
      //------------------------------------------------------------------------------------
      if (type.indexOf("short") >= 0 || type.indexOf("all") >= 0) {
        if (outfile.exists()) outfile.delete();
        BulkRandomAccessFile brafout = new BulkRandomAccessFile(outfile, "rw");
        MSG(" -------------- Beginning short test.. -------------- ");
        int numelem = len/2;
        short[] a = new short[numelem];
        short[] b = new short[numelem];

        MSG("Seeking..");
        brafin.seek(0);
        MSG("Reading..");
        brafin.readArray(a, 0, numelem);
        MSG("Writing..");
        brafout.writeArray(a, 0, numelem);

        MSG("Re-reading..");
        brafout.seek(0);
        brafout.readArray(b, 0, numelem);

        MSG("Verifying..");
        for (int i=0; i < numelem; i++) {
          if (a[i] != b[i]) {
            int first = i-5;
            int last = i+5;
            if (first < 0) first = 0;
            if (last >= numelem) last = numelem;
            for (int x = first; x < last; x++) 
              MSG("  a[" + x + "]= " + a[x] + "  b[" + x + "]= " + b[x]);
            throw new InternalError("Verification mismatch at element " + i);
            }
          }
        MSG("Verified.");
        }
      //------------------------------------------------------------------------------------
      if (type.indexOf("int") >= 0 || type.indexOf("all") >= 0) {
        if (outfile.exists()) outfile.delete();
        BulkRandomAccessFile brafout = new BulkRandomAccessFile(outfile, "rw");
        MSG(" -------------- Beginning int test.. -------------- ");
        int numelem = len/4;
        int[] a = new int[numelem];
        int[] b = new int[numelem];

        MSG("Seeking..");
        brafin.seek(0);
        MSG("Reading..");
        brafin.readArray(a, 0, numelem);
        MSG("Writing..");
        brafout.writeArray(a, 0, numelem);

        MSG("Re-reading..");
        brafout.seek(0);
        brafout.readArray(b, 0, numelem);

        MSG("Verifying..");
        for (int i=0; i < numelem; i++) {
          if (a[i] != b[i]) {
            int first = i-5;
            int last = i+5;
            if (first < 0) first = 0;
            if (last >= numelem) last = numelem;
            for (int x = first; x < last; x++) 
              MSG("  a[" + x + "]= " + a[x] + "  b[" + x + "]= " + b[x]);
            throw new InternalError("Verification mismatch at element " + i);
            }
          }
        MSG("Verified.");
        }
      //------------------------------------------------------------------------------------
      if (type.indexOf("float") >= 0 || type.indexOf("all") >= 0) {
        if (outfile.exists()) outfile.delete();
        BulkRandomAccessFile brafout = new BulkRandomAccessFile(outfile, "rw");
        MSG(" -------------- Beginning float test.. -------------- ");
        int numelem = len/4;
        float[] a = new float[numelem];
        float[] b = new float[numelem];

        MSG("Seeking..");
        brafin.seek(0);
        MSG("Reading..");
        brafin.readArray(a, 0, numelem);
        MSG("Writing..");
        brafout.writeArray(a, 0, numelem);

        MSG("Re-reading..");
        brafout.seek(0);
        brafout.readArray(b, 0, numelem);

        MSG("Verifying..");
        for (int i=0; i < numelem; i++) {
          if (a[i] != b[i] && !Float.isNaN(a[i]) && !Float.isNaN(b[i])) {
            int first = i-5;
            int last = i+5;
            if (first < 0) first = 0;
            if (last >= numelem) last = numelem;
            for (int x = first; x < last; x++) 
              MSG("  a[" + x + "]= " + a[x] + "  b[" + x + "]= " + b[x]);
            throw new InternalError("Verification mismatch at element " + i);
            }
          }
        MSG("Verified.");
        }
      //------------------------------------------------------------------------------------
      if (type.indexOf("long") >= 0 || type.indexOf("all") >= 0) {
        if (outfile.exists()) outfile.delete();
        BulkRandomAccessFile brafout = new BulkRandomAccessFile(outfile, "rw");
        MSG(" -------------- Beginning long test.. -------------- ");
        int numelem = len/8;
        long[] a = new long[numelem];
        long[] b = new long[numelem];

        MSG("Seeking..");
        brafin.seek(0);
        MSG("Reading..");
        brafin.readArray(a, 0, numelem);
        MSG("Writing..");
        brafout.writeArray(a, 0, numelem);

        MSG("Re-reading..");
        brafout.seek(0);
        brafout.readArray(b, 0, numelem);

        MSG("Verifying..");
        for (int i=0; i < numelem; i++) {
          if (a[i] != b[i]) {
            int first = i-5;
            int last = i+5;
            if (first < 0) first = 0;
            if (last >= numelem) last = numelem;
            for (int x = first; x < last; x++) 
              MSG("  a[" + x + "]= " + a[x] + "  b[" + x + "]= " + b[x]);
            throw new InternalError("Verification mismatch at element " + i);
            }
          }
        MSG("Verified.");
        }
      //------------------------------------------------------------------------------------
      if (type.indexOf("consistb") >= 0 || type.indexOf("all") >= 0) {
        if (outfile.exists()) outfile.delete();
        RandomAccessFile vrafin = new RandomAccessFile(infile, "r");
        BulkRandomAccessFile brafout = new BulkRandomAccessFile(outfile, "rw");
        MSG(" -------------- Beginning byte consistency test.. -------------- ");
        int numelem = len;
        byte[] a = new byte[numelem];
        byte[] b = new byte[numelem];

        MSG("Seeking..");
        brafin.seek(0);
        MSG("Reading..");
        brafin.readArray(a, 0, numelem);
        MSG("Reference Read..");
        for (int i=0; i < numelem; i++) {
          b[i] = vrafin.readByte();
          }
        MSG("Verifying..");
        for (int i=0; i < numelem; i++) {
          if (a[i] != b[i]) {
            int first = i-5;
            int last = i+5;
            if (first < 0) first = 0;
            if (last >= numelem) last = numelem;
            for (int x = first; x < last; x++) {
              MSG("  a[" + x + "]= " + a[x] + "  b[" + x + "]= " + b[x]);
              MSG("  a[" + x + "]= 0x" + Integer.toHexString(a[x]) + 
                  "  b[" + x + "]= 0x" + Integer.toHexString(b[x]));
              }
            throw new InternalError("Verification mismatch at element " + i);
            }
          }
        MSG("Verified.");


        MSG("Writing..");
        brafout.writeArray(a, 0, numelem);

        MSG("Reference Read..");
        RandomAccessFile vrafout = new RandomAccessFile(outfile, "r");
        for (int i=0; i < numelem; i++) {
          b[i] = vrafout.readByte();
          }
        MSG("Verifying..");
        for (int i=0; i < numelem; i++) {
          if (a[i] != b[i]) {
            int first = i-5;
            int last = i+5;
            if (first < 0) first = 0;
            if (last >= numelem) last = numelem;
            for (int x = first; x < last; x++) {
              MSG("  a[" + x + "]= " + a[x] + "  b[" + x + "]= " + b[x]);
              MSG("  a[" + x + "]= 0x" + Integer.toHexString(a[x]) + 
                  "  b[" + x + "]= 0x" + Integer.toHexString(b[x]));
              }
            throw new InternalError("Verification mismatch at element " + i);
            }
          }
        MSG("Verified.");
        }
      //------------------------------------------------------------------------------------
      if (type.indexOf("consists") >= 0 || type.indexOf("all") >= 0) {
        if (outfile.exists()) outfile.delete();
        RandomAccessFile vrafin = new RandomAccessFile(infile, "r");
        BulkRandomAccessFile brafout = new BulkRandomAccessFile(outfile, "rw");
        MSG(" -------------- Beginning short consistency test.. -------------- ");
        int numelem = len/2;
        short[] a = new short[numelem];
        short[] b = new short[numelem];

        MSG("Seeking..");
        brafin.seek(0);
        MSG("Reading..");
        brafin.readArray(a, 0, numelem);
        MSG("Reference Read..");
        for (int i=0; i < numelem; i++) {
          b[i] = vrafin.readShort();
          }
        MSG("Verifying..");
        for (int i=0; i < numelem; i++) {
          if (a[i] != b[i]) {
            int first = i-5;
            int last = i+5;
            if (first < 0) first = 0;
            if (last >= numelem) last = numelem;
            for (int x = first; x < last; x++) {
              MSG("  a[" + x + "]= " + a[x] + "  b[" + x + "]= " + b[x]);
              MSG("  a[" + x + "]= 0x" + Integer.toHexString(a[x]) + 
                  "  b[" + x + "]= 0x" + Integer.toHexString(b[x]));
              }
            throw new InternalError("Verification mismatch at element " + i);
            }
          }
        MSG("Verified.");


        MSG("Writing..");
        brafout.writeArray(a, 0, numelem);

        MSG("Reference Read..");
        RandomAccessFile vrafout = new RandomAccessFile(outfile, "r");
        for (int i=0; i < numelem; i++) {
          b[i] = vrafout.readShort();
          }
        MSG("Verifying..");
        for (int i=0; i < numelem; i++) {
          if (a[i] != b[i]) {
            int first = i-5;
            int last = i+5;
            if (first < 0) first = 0;
            if (last >= numelem) last = numelem;
            for (int x = first; x < last; x++) {
              MSG("  a[" + x + "]= " + a[x] + "  b[" + x + "]= " + b[x]);
              MSG("  a[" + x + "]= 0x" + Integer.toHexString(a[x]) + 
                  "  b[" + x + "]= 0x" + Integer.toHexString(b[x]));
              }
            throw new InternalError("Verification mismatch at element " + i);
            }
          }
        MSG("Verified.");
        }
      //------------------------------------------------------------------------------------
      if (type.indexOf("consisti") >= 0 || type.indexOf("all") >= 0) {
        if (outfile.exists()) outfile.delete();
        RandomAccessFile vrafin = new RandomAccessFile(infile, "r");
        BulkRandomAccessFile brafout = new BulkRandomAccessFile(outfile, "rw");
        MSG(" -------------- Beginning int consistency test.. -------------- ");
        int numelem = len/4;
        int[] a = new int[numelem];
        int[] b = new int[numelem];

        MSG("Seeking..");
        brafin.seek(0);
        MSG("Reading..");
        brafin.readArray(a, 0, numelem);
        MSG("Reference Read..");
        for (int i=0; i < numelem; i++) {
          b[i] = vrafin.readInt();
          }
        MSG("Verifying..");
        for (int i=0; i < numelem; i++) {
          if (a[i] != b[i]) {
            int first = i-5;
            int last = i+5;
            if (first < 0) first = 0;
            if (last >= numelem) last = numelem;
            for (int x = first; x < last; x++) {
              MSG("  a[" + x + "]= " + a[x] + "  b[" + x + "]= " + b[x]);
              MSG("  a[" + x + "]= 0x" + Integer.toHexString(a[x]) + 
                  "  b[" + x + "]= 0x" + Integer.toHexString(b[x]));
              }
            throw new InternalError("Verification mismatch at element " + i);
            }
          }
        MSG("Verified.");


        MSG("Writing..");
        brafout.writeArray(a, 0, numelem);

        MSG("Reference Read..");
        RandomAccessFile vrafout = new RandomAccessFile(outfile, "r");
        for (int i=0; i < numelem; i++) {
          b[i] = vrafout.readInt();
          }
        MSG("Verifying..");
        for (int i=0; i < numelem; i++) {
          if (a[i] != b[i]) {
            int first = i-5;
            int last = i+5;
            if (first < 0) first = 0;
            if (last >= numelem) last = numelem;
            for (int x = first; x < last; x++) {
              MSG("  a[" + x + "]= " + a[x] + "  b[" + x + "]= " + b[x]);
              MSG("  a[" + x + "]= 0x" + Integer.toHexString(a[x]) + 
                  "  b[" + x + "]= 0x" + Integer.toHexString(b[x]));
              }
            throw new InternalError("Verification mismatch at element " + i);
            }
          }
        MSG("Verified.");
        }
      //------------------------------------------------------------------------------------
      if (type.indexOf("consistl") >= 0 || type.indexOf("all") >= 0) {
        if (outfile.exists()) outfile.delete();
        RandomAccessFile vrafin = new RandomAccessFile(infile, "r");
        BulkRandomAccessFile brafout = new BulkRandomAccessFile(outfile, "rw");
        MSG(" -------------- Beginning long consistency test.. -------------- ");
        int numelem = len/8;
        long[] a = new long[numelem];
        long[] b = new long[numelem];

        MSG("Seeking..");
        brafin.seek(0);
        MSG("Reading..");
        brafin.readArray(a, 0, numelem);
        MSG("Reference Read..");
        for (int i=0; i < numelem; i++) {
          b[i] = vrafin.readLong();
          }
        MSG("Verifying..");
        for (int i=0; i < numelem; i++) {
          if (a[i] != b[i]) {
            int first = i-5;
            int last = i+5;
            if (first < 0) first = 0;
            if (last >= numelem) last = numelem;
            for (int x = first; x < last; x++) {
              MSG("  a[" + x + "]= " + a[x] + "  b[" + x + "]= " + b[x]);
              MSG("  a[" + x + "]= 0x" + Long.toHexString(a[x]) + 
                  "  b[" + x + "]= 0x" + Long.toHexString(b[x]));
              }
            throw new InternalError("Verification mismatch at element " + i);
            }
          }
        MSG("Verified.");


        MSG("Writing..");
        brafout.writeArray(a, 0, numelem);

        MSG("Reference Read..");
        RandomAccessFile vrafout = new RandomAccessFile(outfile, "r");
        for (int i=0; i < numelem; i++) {
          b[i] = vrafout.readLong();
          }
        MSG("Verifying..");
        for (int i=0; i < numelem; i++) {
          if (a[i] != b[i]) {
            int first = i-5;
            int last = i+5;
            if (first < 0) first = 0;
            if (last >= numelem) last = numelem;
            for (int x = first; x < last; x++) {
              MSG("  a[" + x + "]= " + a[x] + "  b[" + x + "]= " + b[x]);
              MSG("  a[" + x + "]= 0x" + Long.toHexString(a[x]) + 
                  "  b[" + x + "]= 0x" + Long.toHexString(b[x]));
              }
            throw new InternalError("Verification mismatch at element " + i);
            }
          }
        MSG("Verified.");
        }
      //------------------------------------------------------------------------------------
      if (type.indexOf("stress") >= 0 || type.indexOf("all") >= 0) {
        if (outfile.exists()) outfile.delete();
        MSG("Testing Random Access Stress Test..");
        BulkRandomAccessFile brafout = new BulkRandomAccessFile(outfile, "rw");

        int testsz = 16536;
        long [] testarray = new long[testsz];
        brafout.writeArray(testarray); // zero everything out.

        /* create a shuffled list */
        java.util.Random rand = new java.util.Random(5);
        for (int i=0; i < testsz; i++) testarray[i] = i;
        for (int i=testsz-1; i > 0; i--) {
	        int x = Math.abs(rand.nextInt()) % i;
	        long temp = testarray[i];
	        testarray[i] = testarray[x];
	        testarray[x] = temp;
	        }

        long [] probe = new long[1];
        for (int i = 0; i < 2; i++) {
	        for (int j = 0; j < testsz; j++) {
		        brafout.seek(testarray[j]*8);
		        brafout.readArray(probe);
		        if (probe[0] != i) throw new InternalError("Stress Test Failed - read mismatch."+
              " i = " + i + "  j = " + j + "  testarray[j] = " + testarray[j] + "  probe[0] = " + probe[0]); 
		        probe[0] = i+1;
		        brafout.seek(testarray[j]*8);
		        brafout.writeArray(probe);
		        }
	        }
        MSG("Random Access Stress Test Passed.");
        }
      //------------------------------------------------------------------------------------
      if (type.indexOf("misc") >= 0 || type.indexOf("all") >= 0) {
        if (outfile.exists()) outfile.delete();
        BulkRandomAccessFile brafout = new BulkRandomAccessFile(outfile, "rw");
        MSG(" -------------- Beginning miscellaneous tests.. -------------- ");
        int sz = 512;
        int[] a = new int[sz];
        int[] b = new int[sz];
        for (int i = 0; i < sz; i++) {
          a[i] = i;
          b[i] = 0;
          }

        MSG("Seeking..");
        brafout.seek(0);
        MSG("Writing..");
        brafout.writeArray(a);
        MSG("Seeking..");
        brafout.seek(0);
        MSG("Reading..");
        brafout.readArray(b);
        MSG("Verifying..");
        for (int k=0; k < sz; k++) {
          if (b[k] != a[k]) throw new InternalError("Verification mismatch at offset " + k + 
            ", b[" + k + "] = " + b[k] + "  a[" + k + "] = " + a[k]);
          }
        MSG("Verified.");

        MSG("Seeking..");
        brafout.seek(0);
        MSG("Writing..");
        brafout.writeArray(a, 100, 200);
        MSG("Seeking..");
        brafout.seek(100*4);
        MSG("Reading..");
        brafout.readArray(b, 400, 100);

        MSG("Verifying..");
        for (int j=0; j < 100; j++) {
          if (b[j+400] != a[j+200]) 
            throw new InternalError("Verification mismatch at offset " + j + 
            ", b[" + (j+400) + "] = " + b[j+400] + "  a[" + (j+200) + "] = " + a[j+200]);
          }
        MSG("Verified.");
      
        MSG("Seeking..");
        brafin.seek(0);
        MSG("Testing Null Ptr Exn..");
        try {
          byte[] q = null;
          brafin.readArray(q);
          MSG("Null Ptr Exn Test Failed.");
          throw new InternalError("Null Ptr Exn Test Failed."); 
          }
        catch(NullPointerException exn) {
          MSG("Null Ptr Exn Test Passed.");
          }

        MSG("Testing ArrayOutOfBounds Exn 1..");
        try {
          byte[] q = new byte[len];
          brafin.readArray(q, 0, len+1);
          MSG("ArrayOutOfBounds Exn 1 Failed.");
          throw new InternalError("ArrayOutOfBounds Exn 1 Test Failed."); 
          }
        catch(IndexOutOfBoundsException exn) {
          MSG("ArrayOutOfBounds Exn 1 Test Passed.");
          }

        MSG("Testing ArrayOutOfBounds Exn 2..");
        try {
          byte[] q = new byte[len];
          brafin.readArray(q, -1, len);
          MSG("ArrayOutOfBounds Exn 2 Failed.");
          throw new InternalError("ArrayOutOfBounds Exn 2 Test Failed."); 
          }
        catch(IndexOutOfBoundsException exn) {
          MSG("ArrayOutOfBounds Exn 2 Test Passed.");
          }

        MSG("Testing Legacy EOF..");
        BulkRandomAccessFile vraf = new BulkRandomAccessFile(infile,"r");
        try {
          byte x;
          for (int i=0; i < len; i++) x = vraf.readByte();
          }
        catch(EOFException exn) {
          MSG("EOF Test Failed.");
          throw new InternalError("Legacy EOF Test Failed."); 
          }
        try {
          byte x;
          x = vraf.readByte();
          MSG("EOF Test Failed.");
          throw new InternalError("Legacy EOF Test Failed."); 
          }
        catch(EOFException exn) {
          MSG("Legacy EOF Test Passed.");
          }

        MSG("Seeking..");
        brafin.seek(0);
        MSG("Testing EOF..");
        try {
          byte[] q = new byte[len+1];
          brafin.readArray(q);
          MSG("EOF Test Failed.");
          throw new InternalError("EOF Test Failed."); 
          }
        catch(EOFException exn) {
          MSG("EOF Test Passed.");
          }


        {/*
          MSG("Testing array type checking..");
          BulkRandomAccessFile vraf2 = new BulkRandomAccessFile(infile,"r");
          try {
            String [] x = new String[5];
            vraf2.readArray(x);
            MSG("Array type checking Test Failed");
            throw new InternalError("Array type checking Test Failed."); 
            }
          catch(IOException exn) {
            }
          try {
            int [][] x = new int[5][7];
            vraf2.readArray(x);
            MSG("Array type checking Test Failed");
            throw new InternalError("Array type checking Test Failed."); 
            }
          catch(IOException exn) {
            }
          MSG("Array type checking Test Passed.");
          */
          }
        }


      if (outfile.exists()) outfile.delete();
      }
    catch (Throwable exn) {
      MSG("caught " + exn + " : "  + exn.getMessage());
      }
		}

	}
