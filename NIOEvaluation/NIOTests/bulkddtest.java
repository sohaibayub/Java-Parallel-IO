/*   $Archive:: /Ti/jni/bulkddtest.java                                    $  
 *      $Date:: 6/15/00 4:47p                                              $  
 *  $Revision:: 5                                                          $  
 *   Description: bulk I/O sequential test program
 *   Copyright 2000, Dan Bonachea <bonachea@cs.berkeley.edu> 
 *    see license.txt for usage information     
 */

import ti.io.*;
import java.io.*;
import java.lang.*;

public class bulkddtest {
  public static long starttime;
  public static void MSG(String s) {
    System.out.println((System.currentTimeMillis() - starttime) / 1000.0 + ": " + s);
    }

  public static BulkDataInputStream getBDIS(File infile) throws IOException {
    return new BulkDataInputStream(new BufferedInputStream(new FileInputStream(infile)));
    }

  public static DataInputStream getDIS(File infile) throws IOException {
    return new DataInputStream(new BufferedInputStream(new FileInputStream(infile)));
    }

  public static BulkDataOutputStream getBDOS(File outfile) throws IOException {
    return new BulkDataOutputStream(new BufferedOutputStream(new FileOutputStream(outfile)));
    }

  public static void main (String [] args) {
    if (args.length != 2) {
      System.out.println("Usage: bulkddtest <infilename> <type>");
      System.exit(1);
      }
    String infilename = args[0];
    String type = args[1];
    String outfilename = "bulktest.tmp";
    File infile = new File(infilename);
    File outfile = new File(outfilename);
    starttime = System.currentTimeMillis();

    MSG("File size is: " + infile.length());
    int len = (int)infile.length(); 

    try {

      //------------------------------------------------------------------------------------
      if (type.indexOf("byte") >= 0 || type.indexOf("all") >= 0) {
        BulkDataInputStream bdis = getBDIS(infile);
        if (outfile.exists()) outfile.delete();
        BulkDataOutputStream bdos = getBDOS(outfile);
        MSG(" -------------- Beginning byte test.. -------------- ");
        int numelem = len;
        byte[] a = new byte[numelem];
        byte[] b = new byte[numelem];

        MSG("Reading..");
        bdis.readArray(a, 0, numelem);
        MSG("Writing..");
        bdos.writeArray(a, 0, numelem);

        MSG("Re-reading..");
        BulkDataInputStream bdisRR = getBDIS(outfile);
        bdisRR.readArray(b, 0, numelem);

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
        BulkDataInputStream bdis = getBDIS(infile);
        if (outfile.exists()) outfile.delete();
        BulkDataOutputStream bdos = getBDOS(outfile);
        MSG(" -------------- Beginning short test.. -------------- ");
        int numelem = len/2;
        short[] a = new short[numelem];
        short[] b = new short[numelem];

        MSG("Reading..");
        bdis.readArray(a, 0, numelem);
        MSG("Writing..");
        bdos.writeArray(a, 0, numelem);

        MSG("Re-reading..");
        BulkDataInputStream bdisRR = getBDIS(outfile);
        bdisRR.readArray(b, 0, numelem);

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
        BulkDataInputStream bdis = getBDIS(infile);
        if (outfile.exists()) outfile.delete();
        BulkDataOutputStream bdos = getBDOS(outfile);
        MSG(" -------------- Beginning int test.. -------------- ");
        int numelem = len/4;
        int[] a = new int[numelem];
        int[] b = new int[numelem];

        MSG("Reading..");
        bdis.readArray(a, 0, numelem);
        MSG("Writing..");
        bdos.writeArray(a, 0, numelem);

        MSG("Re-reading..");
        BulkDataInputStream bdisRR = getBDIS(outfile);
        bdisRR.readArray(b, 0, numelem);

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
        BulkDataInputStream bdis = getBDIS(infile);
        if (outfile.exists()) outfile.delete();
        BulkDataOutputStream bdos = getBDOS(outfile);
        MSG(" -------------- Beginning float test.. -------------- ");
        int numelem = len/4;
        float[] a = new float[numelem];
        float[] b = new float[numelem];

        MSG("Reading..");
        bdis.readArray(a, 0, numelem);
        MSG("Writing..");
        bdos.writeArray(a, 0, numelem);

        MSG("Re-reading..");
        BulkDataInputStream bdisRR = getBDIS(outfile);
        bdisRR.readArray(b, 0, numelem);

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
        BulkDataInputStream bdis = getBDIS(infile);
        if (outfile.exists()) outfile.delete();
        BulkDataOutputStream bdos = getBDOS(outfile);
        MSG(" -------------- Beginning long test.. -------------- ");
        int numelem = len/8;
        long[] a = new long[numelem];
        long[] b = new long[numelem];

        MSG("Reading..");
        bdis.readArray(a, 0, numelem);
        MSG("Writing..");
        bdos.writeArray(a, 0, numelem);

        MSG("Re-reading..");
        BulkDataInputStream bdisRR = getBDIS(outfile);
        bdisRR.readArray(b, 0, numelem);

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
        BulkDataInputStream bdis = getBDIS(infile);
        DataInputStream vdis = getDIS(infile);
        BulkDataOutputStream bdos = getBDOS(outfile);
        MSG(" -------------- Beginning byte consistency test.. -------------- ");
        int numelem = len;
        byte[] a = new byte[numelem];
        byte[] b = new byte[numelem];

        MSG("Reading..");
        bdis.readArray(a, 0, numelem);
        MSG("Reference Read..");
        for (int i=0; i < numelem; i++) {
          b[i] = vdis.readByte();
          }
        MSG("Verifying..");
        for (int i=0; i < numelem; i++) {
          if (a[i] != b[i]) {
            int first = i-5;
            int last = i+5;
            if (first < 0) first = 0;
            if (last >= numelem) last = numelem;
            for (int x = first; x < last; x++) {
              // MSG("  a[" + x + "]= " + a[x] + "  b[" + x + "]= " + b[x]);
              String as = " " + Integer.toHexString(a[x]), 
                     bs = " " + Integer.toHexString(b[x]);
              MSG("  a[" + x + "]= 0x" + as.substring(as.length()-2) + 
                  "  b[" + x + "]= 0x" + bs.substring(bs.length()-2));
              }
            throw new InternalError("Verification mismatch at element " + i);
            }
          }
        MSG("Verified.");


        MSG("Writing..");
        bdos.writeArray(a, 0, numelem);

        MSG("Reference Read..");
        DataInputStream vdis2 = getDIS(outfile);
        for (int i=0; i < numelem; i++) {
          b[i] = vdis2.readByte();
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
        BulkDataInputStream bdis = getBDIS(infile);
        DataInputStream vdis = getDIS(infile);
        BulkDataOutputStream bdos = getBDOS(outfile);
        MSG(" -------------- Beginning short consistency test.. -------------- ");
        int numelem = len/2;
        short[] a = new short[numelem];
        short[] b = new short[numelem];

        MSG("Reading..");
        bdis.readArray(a, 0, numelem);
        MSG("Reference Read..");
        for (int i=0; i < numelem; i++) {
          b[i] = vdis.readShort();
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
        bdos.writeArray(a, 0, numelem);

        MSG("Reference Read..");
        DataInputStream vdis2 = getDIS(outfile);
        for (int i=0; i < numelem; i++) {
          b[i] = vdis2.readShort();
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
        BulkDataInputStream bdis = getBDIS(infile);
        DataInputStream vdis = getDIS(infile);
        BulkDataOutputStream bdos = getBDOS(outfile);
        MSG(" -------------- Beginning int consistency test.. -------------- ");
        int numelem = len/4;
        int[] a = new int[numelem];
        int[] b = new int[numelem];

        MSG("Reading..");
        bdis.readArray(a, 0, numelem);
        MSG("Reference Read..");
        for (int i=0; i < numelem; i++) {
          b[i] = vdis.readInt();
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
        bdos.writeArray(a, 0, numelem);

        MSG("Reference Read..");
        DataInputStream vdis2 = getDIS(outfile);
        for (int i=0; i < numelem; i++) {
          b[i] = vdis2.readInt();
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
        BulkDataInputStream bdis = getBDIS(infile);
        DataInputStream vdis = getDIS(infile);
        BulkDataOutputStream bdos = getBDOS(outfile);
        MSG(" -------------- Beginning long consistency test.. -------------- ");
        int numelem = len/8;
        long[] a = new long[numelem];
        long[] b = new long[numelem];

        MSG("Reading..");
        bdis.readArray(a, 0, numelem);
        MSG("Reference Read..");
        for (int i=0; i < numelem; i++) {
          b[i] = vdis.readLong();
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
        bdos.writeArray(a, 0, numelem);

        MSG("Reference Read..");
        DataInputStream vdis2 = getDIS(outfile);
        for (int i=0; i < numelem; i++) {
          b[i] = vdis2.readLong();
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
      if (type.indexOf("misc") >= 0 || type.indexOf("all") >= 0) {
        int sz = 512;
        int[] a = new int[sz];
        int[] b = new int[sz];
        {
          MSG(" -------------- Beginning miscellaneous tests.. -------------- ");
          if (outfile.exists()) outfile.delete();
          BulkDataOutputStream bdos = getBDOS(outfile);
          for (int i = 0; i < sz; i++) {
            a[i] = i;
            b[i] = 0;
            }

          MSG("Writing..");
          bdos.writeArray(a);
          bdos.close();

          BulkDataInputStream bdis = getBDIS(outfile);
          MSG("Reading..");
          bdis.readArray(b);
          MSG("Verifying..");
          for (int k=0; k < sz; k++) {
            if (b[k] != a[k]) throw new InternalError("Verification mismatch at offset " + k + 
              ", b[" + k + "] = " + b[k] + "  a[" + k + "] = " + a[k]);
            }
          MSG("Verified.");
          }
        {

          BulkDataOutputStream bdos = getBDOS(outfile);
          MSG("Writing..");
          bdos.writeArray(a, 100, 200);
          bdos.close();

          BulkDataInputStream bdis = getBDIS(outfile);
          MSG("Seeking..");
          bdis.skipBytes(100*4);
          MSG("Reading..");
          bdis.readArray(b, 400, 100);

          MSG("Verifying..");
          for (int j=0; j < 100; j++) {
            if (b[j+400] != a[j+200]) 
              throw new InternalError("Verification mismatch at offset " + j + 
              ", b[" + (j+400) + "] = " + b[j+400] + "  a[" + (j+200) + "] = " + a[j+200]);
            }
          MSG("Verified.");
          }
        {
          MSG("Testing EOF..");
          BulkDataInputStream bdis = getBDIS(infile);
          try {
            byte[] q = new byte[len+1];
            bdis.readArray(q);
            MSG("EOF Test Failed.");
            throw new InternalError("EOF Test Failed."); 
            }
          catch(EOFException exn) {
            MSG("EOF Test Passed.");
            }

          MSG("Testing Legacy EOF..");
          DataInputStream vdis = new DataInputStream(new BufferedInputStream(new FileInputStream(infile)));
          try {
            byte x;
            for (int i=0; i < len; i++) x = vdis.readByte();
            }
          catch(EOFException exn) {
            MSG("EOF Test Failed.");
            throw new InternalError("Legacy EOF Test Failed."); 
            }
          try {
            byte x;
            x = vdis.readByte();
            MSG("EOF Test Failed.");
            throw new InternalError("Legacy EOF Test Failed."); 
            }
          catch(EOFException exn) {
            MSG("Legacy EOF Test Passed.");
            }

          }
        }

      if (outfile.exists()) outfile.delete();

      }
    catch (Throwable exn) {
      MSG("caught " + exn + " : "  + exn.getMessage());
      }
		}

	}
