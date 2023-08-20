
/**
 *
 * @author Ammar
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import ti.io.*;

public class SerialMicroTestsFinal {

    static int ITERATIONS = 50;
    static int WARM_UP = 20;

    public static void main(String args[]) {
        // Declare the number of threads
        boolean DEBUG = false;

        if (args.length < 1) {
        System.out.println("java SerialMicroTests buf_size");
        return;
        }

        int nints = /*25000;*/Integer.parseInt(args[0]);
        
		System.out.println("----------------------------------------------------");
		System.out.println("Buffer_Size = " + nints);
		System.out.println("Iterations = " + ITERATIONS);
		System.out.println("Warm_Up = " + WARM_UP );
		System.out.println("----------------------------------------------------");
		
        int[] iarr = new int[nints];
        for (int i = 0; i < nints; i++) {
            iarr[i] = i;
        }

        // Serial Tests 

//        SerialReadWrite_IntBuffer(iarr, nints, DEBUG);

//        SerialReadWrite_Mapped_IntBuffer(iarr, nints, DEBUG);

        SerialReadWrite_Streams_Extensions(iarr, nints, DEBUG);
          
        SerialReadWrite_BufferedStreams(iarr, nints, DEBUG);

//        SerialReadWrite_Streams(iarr, nints, DEBUG);

        System.out.println("-----------------Program Finished-------------------");
    }

    static void SerialReadWrite_IntBuffer(int[] iarr, int nints, boolean DEBUG) {
        try {
            /**
             * *************** Serial Writing ( IntBuffer ) ******************
             */
            FileChannel fc = FileChannel.open(Paths.get("ibuf_serial_test.txt"),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.READ);

            long start[] = new long[ITERATIONS];
            long end[] = new long[ITERATIONS];
            long sum = 0;
            long sum_read = 0;
            ByteBuffer bbuf = ByteBuffer.allocate(nints * 4);
            IntBuffer ibuf = bbuf.asIntBuffer();

            for (int i = 0; i < WARM_UP; i++) {
                ibuf.put(iarr);
                ibuf.flip();
                fc.position(0);
                start[i] = System.nanoTime();
                fc.write(bbuf);
                fc.force(true);
                end[i] = System.nanoTime() - start[i];
                bbuf.clear();
                System.out.println(" Iteration " + i + " Time " + end[i]);				
            }

            for (int i = WARM_UP; i < ITERATIONS; i++) {
                ibuf.put(iarr);
                ibuf.flip();
                fc.position(0);
                start[i] = System.nanoTime();
                fc.write(bbuf);
                fc.force(true);
                end[i] = System.nanoTime() - start[i];
                sum += end[i];
                bbuf.clear();
				System.out.println(" Iteration " + i + " Time " + end[i]);
                //System.out.println("Serial\tWrite\tIntBuffer\t\t" + end[i] / 1000 + " for Iteration: " + i);
            }
            fc.close();
            long total_time = sum / (ITERATIONS - WARM_UP);
            System.out.println("Serial\tWrite\tIntBuffer\t\t" + total_time / 1000);

            /**
             * ************ Serial Reading ( IntBuffer ) *********************
             */
            long start_read[] = new long[ITERATIONS];
            long end_read[] = new long[ITERATIONS];

            ByteBuffer bbuf2 = ByteBuffer.allocate(nints * 4);
            IntBuffer ibuf2 = bbuf2.asIntBuffer();

            //long part_total = 0;
            //start = 0;
            fc = FileChannel.open(Paths.get("ibuf_serial_test.txt"),
                        StandardOpenOption.READ);

            for (int i = 0; i < WARM_UP; i++) {
                fc.position(0);
                int bytesRead = 0;
                start_read[i] = System.nanoTime();
                do {
                    bytesRead = bytesRead + fc.read(bbuf2);
                    //System.out.println("BytesRead = " + bytesRead + " nints*4 = " + (nints * 4));
                } while (bytesRead < (nints * 4));

                end_read[i] = System.nanoTime() - start_read[i];

                ibuf2.get(iarr);
                ibuf2.clear();
                bbuf2.clear();
				System.out.println(" Iteration " + i + " Time " + end_read[i]);
            }
            ibuf2.clear();
            bbuf2.clear();
            for (int i = WARM_UP; i < ITERATIONS; i++) {
                fc.position(0);
                int bytesRead = 0;
                start_read[i] = System.nanoTime();
                do {
                    bytesRead = bytesRead + fc.read(bbuf2);
                    //System.out.println("BytesRead = " + bytesRead + " nints*4 = " + (nints * 4));
                } while (bytesRead < (nints * 4));
                end_read[i] = System.nanoTime() - start_read[i];
                sum_read += end_read[i];
                ibuf2.get(iarr);
                ibuf2.clear();
                bbuf2.clear();
				System.out.println(" Iteration " + i + " Time " + end_read[i]);
            }
            fc.close();
            long total_readtime = sum_read / (ITERATIONS - WARM_UP);

            System.out.println("Serial\tRead\tIntBuffer\t\t" + total_readtime / 1000);

            if (DEBUG) {
                for (int i = 0; i < iarr.length; i++) {
                    System.out.print(iarr[i] + " ");
                }
                System.out.println();
            }

        } catch (IOException e) {
            System.out.println("SerialRead_IntBuffer - Exception : " + e);
        }

    }

    static void SerialReadWrite_Mapped_IntBuffer(int[] iarr, int nints, boolean DEBUG) {
        try {
            /**
             * *************** Serial Writing ( IntBuffer ) ******************
             */
            FileChannel fc;

            long start[] = new long[ITERATIONS];
            long end[] = new long[ITERATIONS];
            long sum_read = 0;
            long sum_write = 0;

            MappedByteBuffer bbuf;
            IntBuffer ibuf;
            fc = FileChannel.open(Paths.get("mapped_ibuf_serial_test.txt"),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.READ);

            for (int i = 0; i < WARM_UP; i++) {

                start[i] = System.nanoTime();
                bbuf = fc.map(FileChannel.MapMode.READ_WRITE, 0, nints * 4);
                ibuf = bbuf.asIntBuffer();
                ibuf.put(iarr);
                bbuf.force();
                fc.force(true);
                end[i] = System.nanoTime() - start[i];
                ibuf.clear();
				System.out.println(" Iteration " + i + " Time " + end[i]);

            }
            for (int i = WARM_UP; i < ITERATIONS; i++) {

                start[i] = System.nanoTime();
                bbuf = fc.map(FileChannel.MapMode.READ_WRITE, 0, nints * 4);
                ibuf = bbuf.asIntBuffer();
                ibuf.put(iarr);
                bbuf.force();
                fc.force(true);
                end[i] = System.nanoTime() - start[i];
                sum_write += end[i];
                ibuf.clear();
				System.out.println(" Iteration " + i + " Time " + end[i]);

            }
            fc.close();
            long total_time = sum_write / (ITERATIONS - WARM_UP);
            System.out.println("Serial\tWrite\tMapped_IntBuffer\t" + total_time / 1000);

            /**
             * ************ Serial Reading ( IntBuffer ) *********************
             */
            long start_read[] = new long[ITERATIONS];
            long end_read[] = new long[ITERATIONS];


            int iarr2[] = new int[nints];


            MappedByteBuffer bbuf2;
            IntBuffer ibuf2;
                            fc = FileChannel.open(Paths.get("mapped_ibuf_serial_test.txt"),
                        StandardOpenOption.READ);

            for (int i = 0; i < WARM_UP; i++) {
                start_read[i] = System.nanoTime();
                bbuf2 = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                ibuf2 = bbuf2.asIntBuffer();
                ibuf2.get(iarr2);
                end_read[i] = System.nanoTime() - start_read[i];
                ibuf2.clear();
				System.out.println(" Iteration " + i + " Time " + end_read[i]);
            }

            for (int i = WARM_UP; i < ITERATIONS; i++) {
                start_read[i] = System.nanoTime();
                bbuf2 = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                ibuf2 = bbuf2.asIntBuffer();
                ibuf2.get(iarr2);
                end_read[i] = System.nanoTime() - start_read[i];
                sum_read += end_read[i];
                ibuf2.clear();
				System.out.println(" Iteration " + i + " Time " + end_read[i]);
            }
            fc.close();
            long total_readtime = sum_read / (ITERATIONS - WARM_UP);
            System.out.println("Serial\tRead\tMapped_IntBuffer\t" + total_readtime / 1000);

            if (DEBUG) {
                for (int i = 0; i < iarr2.length; i++) {
                    System.out.print(iarr2[i] + " ");
                }
                System.out.println();
            }

        } catch (IOException e) {
            System.err.println("SerialRead-MappedIntBuffer - Exception : " + e);
        }

    }

    static void SerialReadWrite_Streams(int[] iarr, int nints, boolean DEBUG) {
        try {
            /**
             * *************** Serial Writing ( Streams ) ******************
             */
            File file = new File("streams_serial_test.txt");
            if (!file.exists()) {
                //System.out.println("File does not exist so creating a new file");
                try {
                    file.createNewFile();
                } catch (IOException ex) {
                    System.out.println("Cannot Create New File - Function - SerialReadWrite_Streams");
                }
            }


            RandomAccessFile raf;
            long start[] = new long[ITERATIONS];
            long end[] = new long[ITERATIONS];
            long sum_read = 0;
            long sum_write = 0;

            raf = new RandomAccessFile(file, "rw");

            for (int j = 0; j < WARM_UP; j++) {
                raf.seek(0);
                start[j] = System.nanoTime();
                for (int i = 0; i < iarr.length; i++) {
                    raf.writeInt(iarr[i]);
                    //System.out.println("Writing " + iarr[i] );
                }
                end[j] = System.nanoTime() - start[j];
				System.out.println(" Iteration " + j + " Time " + end[j]);
                //System.out.println("Serial\tWrite\tStreams\t\t" + end[j] / 1000 + " for Iteration: " + j);
            }
            for (int j = WARM_UP; j < ITERATIONS; j++) {
                raf.seek(0);
                start[j] = System.nanoTime();
                for (int i = 0; i < iarr.length; i++) {
                    raf.writeInt(iarr[i]);
                }
                end[j] = System.nanoTime() - start[j];
                sum_write += end[j];
				System.out.println(" Iteration " + j + " Time " + end[j]);
                //System.out.println("Serial\tWrite\tStreams\t\t" + end[j] / 1000 + " for Iteration: " + j);
            }
            raf.close();
            long total_time = sum_write / (ITERATIONS - WARM_UP);

            System.out.println("Serial\tWrite\tStreams\t\t\t" + total_time / 1000);

            /**
             * *************** Serial Reading ( Streams ) ******************
             */
            long start_read[] = new long[ITERATIONS];
            long end_read[] = new long[ITERATIONS];
            raf = new RandomAccessFile(file, "r");

            for (int j = 0; j < WARM_UP; j++) {
                raf.seek(0);
                start_read[j] = System.nanoTime();
                for (int i = 0; i < iarr.length; i++) {
                    iarr[i] = raf.readInt();
                }
                end_read[j] = System.nanoTime() - start_read[j];
				System.out.println(" Iteration " + j + " Time " + end_read[j]);
            }
            for (int j = WARM_UP; j < ITERATIONS; j++) {
                raf.seek(0);
                start_read[j] = System.nanoTime();
                for (int i = 0; i < iarr.length; i++) {
                    iarr[i] = raf.readInt();
                }
                end_read[j] = System.nanoTime() - start_read[j];
                sum_read += end_read[j];
				System.out.println(" Iteration " + j + " Time " + end_read[j]);

            }
            raf.close();
            long total_readtime = sum_read / (ITERATIONS - WARM_UP);

            System.out.println("Serial\tRead\tStreams\t\t\t" + total_readtime / 1000);
        } catch (IOException e) {
            System.out.println("SerialRead_Streams - Exception : " + e);
        }
    }

    static void SerialReadWrite_Streams_Extensions(int[] iarr, int nints, boolean DEBUG) {
        try {
            File file = new File("streams_extensions_serial_test.txt");
            if (!file.exists()) {
                //System.out.println("File does not exist so creating a new file");
                try {
                    file.createNewFile();
                } catch (IOException ex) {
                    System.out.println("Cannot Create File - StreamsExtensions_Serial");
                }
            }

            BulkRandomAccessFile braf;

            long start[] = new long[ITERATIONS];
            long end[] = new long[ITERATIONS];
            long sum_read = 0;
            long sum_write = 0;
            braf = new BulkRandomAccessFile(file, "rws");

            for (int i = 0; i < WARM_UP; i++) {
                braf.seek(0);
                start[i] = System.nanoTime();
                braf.writeArray(iarr);
                end[i] = System.nanoTime() - start[i];
				System.out.println(" Iteration " + i + " Time " + end[i]);

            }
            for (int i = WARM_UP; i < ITERATIONS; i++) {
                braf.seek(0);
                start[i] = System.nanoTime();
                braf.writeArray(iarr);
                end[i] = System.nanoTime() - start[i];
                sum_write += end[i];
				System.out.println(" Iteration " + i + " Time " + end[i]);
            }
		
            braf.close();
            long total_time = sum_write / (ITERATIONS - WARM_UP);

            System.out.println("Serial\tWrite\tBulk Streams\t\t" + total_time / 1000);

            long start_read[] = new long[ITERATIONS];
            long end_read[] = new long[ITERATIONS];

            braf = new BulkRandomAccessFile(file, "r");

            for (int i = 0; i < WARM_UP; i++) {
                braf.seek(0);
                start_read[i] = System.nanoTime();
                braf.readArray(iarr);
                end_read[i] = System.nanoTime() - start_read[i];
				System.out.println(" Iteration " + i + " Time " + end_read[i]);

            }
            for (int i = WARM_UP; i < ITERATIONS; i++) {
                braf.seek(0);
                start_read[i] = System.nanoTime();
                braf.readArray(iarr);
                end_read[i] = System.nanoTime() - start_read[i];
                sum_read += end_read[i];
				System.out.println(" Iteration " + i + " Time " + end_read[i]);
            }
            braf.close();
            long total_readtime = sum_read / (ITERATIONS - WARM_UP);

            System.out.println("Serial\tRead\tBulk Streams\t\t" + total_readtime / 1000);

        } catch (IOException e) {
            System.out.println("StreamsExtensions - Exception : " + e);
        }
    }

    static void SerialReadWrite_BufferedStreams(int[] iarr, int nints, boolean DEBUG) {
        try {
            /**
             * *************** Serial Writing ( Streams ) ******************
             */
            File file = new File("buf_streams_serial_test.txt");
            if (!file.exists()) {
                //System.out.println("File does not exist so creating a new file");
                try {
                    file.createNewFile();
                } catch (IOException ex) {
                    System.out.println("Cannot Create New File - Function - SerialReadWrite_BufferedStreams");
                }
            }


            long start[] = new long[ITERATIONS];
            long end[] = new long[ITERATIONS];
            long sum_read = 0;
            long sum_write = 0;

            RandomAccessFile raf = new RandomAccessFile(file, "rws");
            FileDescriptor fd = raf.getFD();
            FileOutputStream fos = new FileOutputStream(fd);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            DataOutputStream dos = new DataOutputStream(bos);

            for (int j = 0; j < WARM_UP; j++) {
                raf.seek(0);
                start[j] = System.nanoTime();
                for (int i = 0; i < iarr.length; i++) {
                    dos.writeInt(iarr[i]);
                }
                dos.flush();
                bos.flush();
                fos.flush();
                end[j] = System.nanoTime() - start[j];
				System.out.println(" Iteration " + j + " Time " + end[j]);
            }
            for (int j = WARM_UP; j < ITERATIONS; j++) {
                raf.seek(0);
                start[j] = System.nanoTime();
                for (int i = 0; i < iarr.length; i++) {
                    dos.writeInt(iarr[i]);
                }
                dos.flush();
                bos.flush();
                fos.flush();
                end[j] = System.nanoTime() - start[j];
                sum_write += end[j];
				System.out.println(" Iteration " + j + " Time " + end[j]);
            }
            dos.close();
            raf.close();
            long total_time = sum_write / (ITERATIONS - WARM_UP);

            System.out.println("Serial\tWrite\tBuffered Streams\t" + total_time / 1000);

            /**
             * *************** Serial Reading ( Streams ) ******************
             */
            long start_read[] = new long[ITERATIONS];
            long end_read[] = new long[ITERATIONS];

            raf = new RandomAccessFile(file, "r");
            FileInputStream fis = new FileInputStream(raf.getFD());
            BufferedInputStream bis = new BufferedInputStream(fis);
            DataInputStream dis = new DataInputStream(bis);

            for (int j = 0; j < WARM_UP; j++) {
                raf.seek(0);
                start_read[j] = System.nanoTime();
                for (int i = 0; i < iarr.length; i++) {
                    iarr[i] = dis.readInt();
                }
                end_read[j] = System.nanoTime() - start_read[j];
				System.out.println(" Iteration " + j + " Time " + end_read[j]);
            }
            for (int j = WARM_UP; j < ITERATIONS; j++) {
                raf.seek(0);
                start_read[j] = System.nanoTime();
                for (int i = 0; i < iarr.length; i++) {
                    iarr[i] = dis.readInt();
                }
                end_read[j] = System.nanoTime() - start_read[j];
				System.out.println(" Iteration " + j + " Time " + end_read[j]);
                sum_read += end_read[j];
                
            }
            dis.close();
            raf.close();
            long total_readtime = sum_read / (ITERATIONS - WARM_UP);
            System.out.println("Serial\tRead\tBuffered Streams\t" + total_readtime / 1000);
        } catch (IOException e) {
            System.out.println("SerialRead_BufferedStreams - Exception : " + e);
        }
    }
}
