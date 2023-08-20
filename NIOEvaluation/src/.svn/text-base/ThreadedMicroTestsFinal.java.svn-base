
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
//import ti.io.*;

/**
 *
 * @author Ammar
 */
public class ThreadedMicroTestsFinal {

    static int ITERATIONS = 25;
    static int WARM_UP = 10;

    public static void main(String args[]) {

        if (args.length < 2) {
            System.out.println("java ThreadedMicroTestsFinal num_threads buf_size");
            return;
        }

        //int num_threads = 1; 
        int num_threads = Integer.parseInt(args[0]);
        //int nints = 240000;
        int nints = Integer.parseInt(args[1]);

        System.out.println("--------------------Test Started--------------------");
        System.out.println("Buffer_Size = " + nints);
        System.out.println("Iterations = " + ITERATIONS);
        System.out.println("Warm_Up = " + WARM_UP);
        System.out.println("----------------------------------------------------");

        try {
            boolean DEBUG = false;

            int[] iarr = new int[nints];
            for (int i = 0; i < nints; i++) {
                iarr[i] = i;
            }

            // Parallel Tests - Using Threads
            long sum = 0;
            sum = ParallelWrite_IntBuffer(iarr, nints, num_threads, DEBUG);
            System.out.println("Write\tIntBuffer\t\t" + sum / 1000);

            sum = ParallelRead_IntBuffer(iarr, nints, num_threads, DEBUG);
            System.out.println("Read\tIntBuffer\t\t" + sum / 1000);

            sum = ParallelWrite_MappedIntBuffer(iarr, nints, num_threads, DEBUG);
            System.out.println("Write\tMapped_IntBuffer\t" + sum / 1000);

            sum = ParallelRead_MappedIntBuffer(iarr, nints, num_threads, DEBUG);
            System.out.println("Read\tMapped_IntBuffer\t" + sum / 1000);

            sum = ParallelWrite_StreamsExtensions(iarr, nints, num_threads, DEBUG);
            System.out.println("Write\tStream_Extensions\t" + sum / 1000);

            sum = ParallelRead_StreamsExtensions(iarr, nints, num_threads, DEBUG);
            System.out.println("Read\tStream_Extensions\t" + sum / 1000);

//            sum = ParallelWrite_Streams(iarr, nints, num_threads, DEBUG);
//            System.out.println("Write\tStreams\t\t\t" + sum / 1000 );

//            sum = ParallelRead_Streams(iarr, nints, num_threads, DEBUG);
///            System.out.println("Read\tStreams\t\t\t" + sum / 1000 );


            System.out.println("-----------------Program Finished-------------------");
        } catch (IOException | InterruptedException ioex) {
            System.err.println(ioex);
        }
    }  //main method

    static long ParallelWrite_IntBuffer(int[] iarr, int nints, int num_threads, boolean DEBUG) throws InterruptedException, IOException {

        IntBufferWrite ibuf_write_threads_array[] = new IntBufferWrite[num_threads];
        for (int i = 0; i < ibuf_write_threads_array.length; i++) {
            ibuf_write_threads_array[i] = new IntBufferWrite();
            //threads_array[i].setParameters( position, int_array, no_of_ints);
            ibuf_write_threads_array[i].setParameters((nints / num_threads * i), iarr, nints / num_threads, DEBUG, WARM_UP, ITERATIONS);
            ibuf_write_threads_array[i].start();
        }

        for (int i = 0; i < ibuf_write_threads_array.length; i++) {
            ibuf_write_threads_array[i].join();
        }

        //System.out.print("IntBuffer\tWrite\t[");
        long sum_time = 0;
        long t = 0;
        for (int i = 0; i < ibuf_write_threads_array.length; i++) {
            t = ibuf_write_threads_array[i].getTime();
            //System.out.print(t + "\t");
            sum_time += t;
        }
        //System.out.println("(" + sum_time / num_threads + ")]");
        return (sum_time / num_threads);
    }

    static long ParallelRead_IntBuffer(int[] iarr, int nints, int num_threads, boolean DEBUG) throws InterruptedException, IOException {

        IntBufferRead ibuf_read_threads_array[] = new IntBufferRead[num_threads];
        for (int i = 0; i < ibuf_read_threads_array.length; i++) {
            ibuf_read_threads_array[i] = new IntBufferRead();
            //threads_array[i].setParameters( position, int_array, no_of_ints);
            ibuf_read_threads_array[i].setParameters((nints / num_threads * i), iarr, nints / num_threads, DEBUG, WARM_UP, ITERATIONS);
            ibuf_read_threads_array[i].start();
        }



        for (int i = 0; i < ibuf_read_threads_array.length; i++) {
            ibuf_read_threads_array[i].join();
        }

        long sum_time = 0;
        long t = 0;
        //System.out.print("IntBuffer\tRead\t[");
        for (int i = 0; i < ibuf_read_threads_array.length; i++) {
            t = ibuf_read_threads_array[i].getTime();
            //System.out.print(t + "\t");
            sum_time += t;
        }
        //System.out.println("(" + sum_time / num_threads + ")]");
        return (sum_time / num_threads);
    }

    static long ParallelWrite_MappedIntBuffer(int[] iarr, int nints, int num_threads, boolean DEBUG) throws InterruptedException, IOException {

        MappedIntBufferWrite mibuf_write_threads_array[] = new MappedIntBufferWrite[num_threads];
        for (int i = 0; i < mibuf_write_threads_array.length; i++) {
            mibuf_write_threads_array[i] = new MappedIntBufferWrite();
            //threads_array[i].setParameters( position, int_array, no_of_ints);
            mibuf_write_threads_array[i].setParameters((nints / num_threads * i), iarr, nints / num_threads, DEBUG, WARM_UP, ITERATIONS);
            mibuf_write_threads_array[i].start();
        }

        for (int i = 0; i < mibuf_write_threads_array.length; i++) {
            mibuf_write_threads_array[i].join();
        }

        //System.out.print("MappedIntBuffer\tWrite\t[");
        long sum_time = 0;
        long t = 0;
        for (int i = 0; i < mibuf_write_threads_array.length; i++) {
            t = mibuf_write_threads_array[i].getTime();
            //System.out.print(t + "\t");
            sum_time += t;
        }
        //System.out.println("(" + sum_time / num_threads + ")]");
        return (sum_time / num_threads);
    }

    static long ParallelRead_MappedIntBuffer(int[] iarr, int nints, int num_threads, boolean DEBUG) throws InterruptedException, IOException {

        //System.out.println();
        //System.out.println("2)MappedIntBuffer Read");
        //System.out.println();
        // Create threads = num_threads
        MappedIntBufferRead mibuf_read_threads_array[] = new MappedIntBufferRead[num_threads];
        for (int i = 0; i < mibuf_read_threads_array.length; i++) {
            mibuf_read_threads_array[i] = new MappedIntBufferRead();
            //threads_array[i].setParameters( position, int_array, no_of_ints);
            mibuf_read_threads_array[i].setParameters((nints / num_threads * i), iarr, nints / num_threads, DEBUG, WARM_UP, ITERATIONS);
            mibuf_read_threads_array[i].start();
        }

        for (int i = 0; i < mibuf_read_threads_array.length; i++) {
            mibuf_read_threads_array[i].join();
        }
        long sum_time = 0;
        long t = 0;
        //System.out.print("MappedIntBuffer\tRead\t[");
        for (int i = 0; i < mibuf_read_threads_array.length; i++) {
            t = mibuf_read_threads_array[i].getTime();
            //System.out.print(t + "\t");
            sum_time += t;
        }
        //System.out.println("(" + sum_time / num_threads + ")]");
        return (sum_time / num_threads);
    }

    static long ParallelWrite_Streams(int[] iarr, int nints, int num_threads, boolean DEBUG) throws InterruptedException, IOException {

        StreamsWrite streams_write_threads_array[] = new StreamsWrite[num_threads];
        for (int i = 0; i < streams_write_threads_array.length; i++) {
            streams_write_threads_array[i] = new StreamsWrite();
            //threads_array[i].setParameters( position, int_array, no_of_ints);
            streams_write_threads_array[i].setParameters((nints / num_threads * i), iarr, nints / num_threads, DEBUG, WARM_UP, ITERATIONS);
            streams_write_threads_array[i].start();
        }

        for (int i = 0; i < streams_write_threads_array.length; i++) {
            streams_write_threads_array[i].join();
        }

        //System.out.print("Streams     \tWrite\t[");
        long sum_time = 0;
        long t = 0;
        for (int i = 0; i < streams_write_threads_array.length; i++) {
            t = streams_write_threads_array[i].getTime();
            //System.out.print(t + "\t");
            sum_time += t;
        }
        //System.out.println("(" + sum_time / num_threads + ")]");
        return (sum_time / num_threads);
    }

    static long ParallelRead_Streams(int[] iarr, int nints, int num_threads, boolean DEBUG) throws InterruptedException, IOException {

        StreamsRead streams_read_threads_array[] = new StreamsRead[num_threads];
        for (int i = 0; i < streams_read_threads_array.length; i++) {
            streams_read_threads_array[i] = new StreamsRead();
            //threads_array[i].setParameters( position, int_array, no_of_ints);
            streams_read_threads_array[i].setParameters((nints / num_threads * i), iarr, nints / num_threads, DEBUG, WARM_UP, ITERATIONS);
            streams_read_threads_array[i].start();
        }

        for (int i = 0; i < streams_read_threads_array.length; i++) {
            streams_read_threads_array[i].join();
        }

        long sum_time = 0;
        long t = 0;
        //System.out.print("Streams     \tRead\t[");
        for (int i = 0; i < streams_read_threads_array.length; i++) {
            t = streams_read_threads_array[i].getTime();
            //System.out.print(t + "\t");
            sum_time += t;
        }
        //System.out.println("(" + sum_time / num_threads + ")]");
        return (sum_time / num_threads);
    }

    static long ParallelWrite_StreamsExtensions(int[] iarr, int nints, int num_threads, boolean DEBUG) throws InterruptedException, IOException {

        StreamsExtensionsWrite streams_write_threads_array[] = new StreamsExtensionsWrite[num_threads];
        for (int i = 0; i < streams_write_threads_array.length; i++) {
            streams_write_threads_array[i] = new StreamsExtensionsWrite();
            //threads_array[i].setParameters( position, int_array, no_of_ints);
            streams_write_threads_array[i].setParameters((nints / num_threads * i), iarr, nints / num_threads, DEBUG, WARM_UP, ITERATIONS);
            streams_write_threads_array[i].start();
        }

        for (int i = 0; i < streams_write_threads_array.length; i++) {
            streams_write_threads_array[i].join();
        }

        //System.out.print("Stream_Exts\tWrite\t[");
        long sum_time = 0;
        long t = 0;
        for (int i = 0; i < streams_write_threads_array.length; i++) {
            t = streams_write_threads_array[i].getTime();
            //System.out.print(t + "\t");
            sum_time += t;
        }
        //System.out.println("(" + sum_time / num_threads + ")]");
        return (sum_time / num_threads);
    }

    static long ParallelRead_StreamsExtensions(int[] iarr, int nints, int num_threads, boolean DEBUG) throws InterruptedException, IOException {

        StreamsExtensionsRead streams_read_threads_array[] = new StreamsExtensionsRead[num_threads];
        for (int i = 0; i < streams_read_threads_array.length; i++) {
            streams_read_threads_array[i] = new StreamsExtensionsRead();
            //threads_array[i].setParameters( position, int_array, no_of_ints);
            streams_read_threads_array[i].setParameters((nints / num_threads * i), iarr, nints / num_threads, DEBUG, WARM_UP, ITERATIONS);
            streams_read_threads_array[i].start();
        }

        for (int i = 0; i < streams_read_threads_array.length; i++) {
            streams_read_threads_array[i].join();
        }
        long sum_time = 0;
        long t = 0;
        //System.out.print("Stream_Exts\tRead\t[");
        for (int i = 0; i < streams_read_threads_array.length; i++) {
            t = streams_read_threads_array[i].getTime();
            //System.out.print(t + "\t");
            sum_time += t;
        }
        //System.out.println("(" + sum_time / num_threads + ")]");
        return (sum_time / num_threads);
    }
}

class IntBufferWrite extends Thread {

    FileChannel fc;
    int position;
    int myNumInts;
    int iarr[];
    long time;
    int WARM_UP;
    int ITERATIONS;
    boolean DEBUG;

    void setParameters(int pos, int[] ints, int nints, boolean debug, int warmup, int iterations) throws IOException {
        try {
            myNumInts = nints;
            iarr = ints;
            fc = FileChannel.open(Paths.get("/home/08bitadnan.tmp/ibuf_test.txt"),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.READ);
            position = pos;
            WARM_UP = warmup;
            ITERATIONS = iterations;
            DEBUG = debug;

        } catch (FileNotFoundException ex) {
            System.out.println("setParameters: " + ex);
        }
    }

	
    public void run() {
        try {
            long start[] = new long[ITERATIONS];
            long end[] = new long[ITERATIONS];
            long sum = 0;

            ByteBuffer bbuf = ByteBuffer.allocate(myNumInts * 4);
            IntBuffer ibuf = bbuf.asIntBuffer();

            int index = position;
            for (int i = 0; i < WARM_UP; i++) {
                index = position;
                ibuf.put(iarr, index, myNumInts);
                ibuf.flip();
                fc.position(position * 4);   // * 4byte = 1int
                start[i] = System.nanoTime();
                fc.write(bbuf);
                fc.force(true);
                end[i] = System.nanoTime() - start[i];
                bbuf.clear();
            }
            for (int i = WARM_UP; i < ITERATIONS; i++) {
                index = position;
                ibuf.put(iarr, index, myNumInts);
                ibuf.flip();
                fc.position(position * 4);   // * 4byte = 1int
                start[i] = System.nanoTime();
                fc.write(bbuf);
                fc.force(true);
                end[i] = System.nanoTime() - start[i];
                sum += end[i];
                bbuf.clear();
            }
            fc.close();
            time = sum / (ITERATIONS - WARM_UP);

        } catch (IOException ex) {
        }
    }

    public long getTime() {
        return time;
    }
}

class IntBufferRead extends Thread {

    FileChannel fc;
    int position;
    int myNumInts;
    int iarr[];
    boolean DEBUG;
    long time;
    int WARM_UP;
    int ITERATIONS;

    void setParameters(int pos, int[] ints, int nints, boolean debug, int warmup, int iterations) throws IOException {
        try {
            DEBUG = debug;
            myNumInts = nints;
            iarr = ints;
            fc = FileChannel.open(Paths.get("/home/08bitadnan.tmp/ibuf_test.txt"),
                    StandardOpenOption.READ);
            position = pos;
            WARM_UP = warmup;
            ITERATIONS = iterations;
        } catch (FileNotFoundException ex) {
            System.out.println("setParameters: " + ex);
        }
    }

    public void run() {
        try {
            long start[] = new long[ITERATIONS];
            long end[] = new long[ITERATIONS];
            long sum = 0;

            ByteBuffer bbuf = ByteBuffer.allocateDirect(myNumInts * 4);
            IntBuffer ibuf = bbuf.asIntBuffer();

            int index = position;
            for (int i = 0; i < WARM_UP; i++) {
                index = position;
                fc.position(position * 4);   // * 4byte = 1int
                bbuf.clear();
                start[i] = System.nanoTime();
                int bytesRead = 0;
                do {
                    bytesRead = bytesRead + fc.read(bbuf);
                } while (bytesRead < (myNumInts * 4));
                end[i] = System.nanoTime() - start[i];
				//IntBuffer ibuf = bbuf.asIntBuffer(); ? on uper side
                bbuf.rewind();
                ibuf.get(iarr, index, myNumInts);
                ibuf.clear();
                bbuf.clear();
            }
            for (int i = WARM_UP; i < ITERATIONS; i++) {
                index = position;
                fc.position(position * 4);   // * 4byte = 1int
                bbuf.clear();
                start[i] = System.nanoTime();
                int bytesRead = 0;
                do {
                    bytesRead = bytesRead + fc.read(bbuf);
                } while (bytesRead < (myNumInts * 4));
                end[i] = System.nanoTime() - start[i];
                sum += end[i];
                bbuf.rewind();
                ibuf.get(iarr, index, myNumInts);
                ibuf.clear();
                bbuf.clear();

            }
            fc.close();
            time = sum / (ITERATIONS - WARM_UP);

            if (DEBUG == true) {
                //System.out.println(this.getName());
                for (int i = 0; i < iarr.length; i++) {
                    System.out.print(iarr[i]);
                }

            }

            //System.out.println(Thread.currentThread().getName() + " time = " + time );

        } catch (IOException ex) {
        }
    }

    public long getTime() {
        return time;
    }
}

class MappedIntBufferWrite extends Thread {

    FileChannel fc;
    int position;
    int myNumInts;
    int iarr[];
    long time;
    int WARM_UP;
    int ITERATIONS;
    boolean DEBUG;

    void setParameters(int pos, int[] ints, int nints, boolean debug, int warmup, int iterations) throws IOException {
        try {
            myNumInts = nints;
            iarr = ints;

            fc = FileChannel.open(Paths.get("/home/08bitadnan.tmp/mapped_ibuf_test.txt"),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.READ);
            position = pos;
            DEBUG = debug;
            WARM_UP = warmup;
            ITERATIONS = iterations;
        } catch (FileNotFoundException ex) {
            System.out.println("setParameters: " + ex);
        }
    }

    public void run() {
        try {

            long start[] = new long[ITERATIONS];
            long end[] = new long[ITERATIONS];
            long sum = 0;

            //fc.position(position * 4);   // * 4byte = 1int
            int index = position;

            MappedByteBuffer bbuf;
            IntBuffer ibuf;

            for (int i = 0; i < WARM_UP; i++) {
                index = position;
                start[i] = System.nanoTime();
                bbuf = fc.map(FileChannel.MapMode.READ_WRITE, (index * 4), (myNumInts * 4));
                ibuf = bbuf.asIntBuffer();
                ibuf.put(iarr, index, myNumInts);
                bbuf.force();
                //fc.write(bbuf);
                end[i] = System.nanoTime() - start[i];
                ibuf.clear();
                bbuf.clear();
            }
            for (int i = WARM_UP; i < ITERATIONS; i++) {
                index = position;
                start[i] = System.nanoTime();
                bbuf = fc.map(FileChannel.MapMode.READ_WRITE, (index * 4), (myNumInts * 4));
                ibuf = bbuf.asIntBuffer();
                ibuf.put(iarr, index, myNumInts);
                bbuf.force();
                //fc.write(bbuf);
                end[i] = System.nanoTime() - start[i];
                sum += end[i];
                ibuf.clear();
                bbuf.clear();
            }
            fc.close();
			
            time = sum / (ITERATIONS - WARM_UP);
        } catch (IOException ex) {
            //System.out.println("Exception on thread " + this.getId() + " = " + ex);
        }

    }

    public long getTime() {
        return time;
    }
}

class MappedIntBufferRead extends Thread {

    FileChannel fc;
    int position;
    int myNumInts;
    int iarr[];
    long time;
    int WARM_UP;
    int ITERATIONS;
    boolean DEBUG;

    void setParameters(int pos, int[] ints, int nints, boolean debug, int warmup, int iterations) throws IOException {
        try {
            myNumInts = nints;
            iarr = ints;

            fc = FileChannel.open(Paths.get("/home/08bitadnan.tmp/mapped_ibuf_test.txt"),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.READ);
            position = pos;
            WARM_UP = warmup;
            ITERATIONS = iterations;
            DEBUG = debug;
        } catch (FileNotFoundException ex) {
            System.out.println("setParameters: " + ex);
        }
    }

    public void run() {

        try {
            long start[] = new long[ITERATIONS];
            long end[] = new long[ITERATIONS];
            long sum = 0;

            //fc.position(position * 4);   // * 4byte = 1int
            int index = position;
            MappedByteBuffer bbuf;
            IntBuffer ibuf;
            for (int i = 0; i < WARM_UP; i++) {
                index = position;
                start[i] = System.nanoTime();
                bbuf = fc.map(FileChannel.MapMode.READ_ONLY, (index * 4), (myNumInts * 4));
                ibuf = bbuf.asIntBuffer();
                ibuf.get(iarr, index, myNumInts);
                //fc.write(bbuf);
                end[i] = System.nanoTime() - start[i];
                ibuf.clear();
                bbuf.clear();
            }
            for (int i = WARM_UP; i < ITERATIONS; i++) {
                index = position;
                start[i] = System.nanoTime();
                bbuf = fc.map(FileChannel.MapMode.READ_ONLY, (index * 4), (myNumInts * 4));
                ibuf = bbuf.asIntBuffer();
                ibuf.get(iarr, index, myNumInts);
                //fc.write(bbuf);
                end[i] = System.nanoTime() - start[i];
                sum += end[i];
                ibuf.clear();
                bbuf.clear();
            }
            //System.out.println( " thread = " + Thread.currentThread().getId() + " ; time = " + time );
            fc.close();
            time = sum / (ITERATIONS - WARM_UP);
        } catch (IOException ex) {
            //System.out.println("Exception on thread " + this.getId() + " = " + ex);
        }

    }

    public long getTime() {
        return time;
    }
}

class StreamsWrite extends Thread {

    RandomAccessFile raf;
    int position;
    int myNumInts;
    int iarr[];
    long time;
    int WARM_UP;
    int ITERATIONS;
    boolean DEBUG;

    void setParameters(int pos, int[] ints, int nints, boolean debug, int warmup, int iterations) {
        try {
            File file = new File("/home/08bitadnan.tmp/streams_test.txt");
            if (!file.exists()) {
                //System.out.println("File does not exist so creating a new file");
                try {
                    file.createNewFile();
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }
            myNumInts = nints;
            iarr = ints;
            raf = new RandomAccessFile(file, "rw");
            position = pos;
            WARM_UP = warmup;
            ITERATIONS = iterations;
            DEBUG = debug;
        } catch (FileNotFoundException ex) {
            System.out.println("setParameters: " + ex);
        }
    }

    public void run() {

        try {
            long start[] = new long[ITERATIONS];
            long end[] = new long[ITERATIONS];
            long sum = 0;

            int index = position;
            for (int i = 0; i < WARM_UP; i++) {
                index = position;
                raf.seek(position * 4);   // * 4byte = 1int
                start[i] = System.nanoTime();
                for (int j = 0; j < myNumInts; j++) {
                    raf.writeInt(iarr[index]);
                    index++;
                }
                end[i] = System.nanoTime() - start[i];
            }

            for (int i = WARM_UP; i < ITERATIONS; i++) {
                index = position;
                raf.seek(position * 4);   // * 4byte = 1int
                start[i] = System.nanoTime();
                for (int j = 0; j < myNumInts; j++) {
                    raf.writeInt(iarr[index]);
                    index++;
                }
                end[i] = System.nanoTime() - start[i];
                sum += end[i];
            }
            raf.close();
            //System.out.println(Thread.currentThread().getId() + " time = " + time);
            time = sum / (ITERATIONS - WARM_UP);
        } catch (IOException ex) {
        }

    }

    public long getTime() {
        return time;
    }
}

class StreamsRead extends Thread {

    RandomAccessFile raf;
    int position;
    int myNumInts;
    int iarr[];
    boolean DEBUG;
    long time;
    int WARM_UP;
    int ITERATIONS;

    void setParameters(int pos, int[] ints, int nints, boolean debug, int warmup, int iterations) {
        try {
            File file = new File("/home/08bitadnan.tmp/streams_test.txt");
            if (!file.exists()) {
                //System.out.println("File does not exist so creating a new file");
                try {
                    file.createNewFile();
                } catch (IOException ex) {
                    System.out.println(ex);

                }
            }
            DEBUG = debug;
            myNumInts = nints;
            iarr = ints;
            raf = new RandomAccessFile(file, "rw");
            position = pos;
            WARM_UP = warmup;
            ITERATIONS = iterations;
        } catch (FileNotFoundException ex) {
            System.out.println("setParameters: " + ex);
        }
    }

    public void run() {
        try {
            long start[] = new long[ITERATIONS];
            long end[] = new long[ITERATIONS];
            long sum = 0;

            int index = position;
            for (int i = 0; i < WARM_UP; i++) {
                index = position;
                raf.seek(position * 4);   // * 4byte = 1int
                start[i] = System.nanoTime();
                for (int j = 0; j < myNumInts; j++) {
                    iarr[index] = raf.readInt();//(iarr[index]);
                    index++;
                }
                end[i] = System.nanoTime() - start[i];
            }

            for (int i = WARM_UP; i < ITERATIONS; i++) {
                index = position;
                raf.seek(position * 4);   // * 4byte = 1int
                start[i] = System.nanoTime();
                for (int j = 0; j < myNumInts; j++) {
                    iarr[index] = raf.readInt();//(iarr[index]);
                    index++;
                }
                end[i] = System.nanoTime() - start[i];
                sum += end[i];
            }
            if (DEBUG == true) {
                //int p = position;
                //System.out.print("iarr = ");
                for (int i = 0; i < iarr.length; i++) {
                    System.out.print(iarr[i]);
                    //p++;
                }
                System.out.println();
            }
            raf.close();
            //System.out.println(Thread.currentThread().getName() + " time = " + (end - start));
            time = sum / (ITERATIONS - WARM_UP);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public long getTime() {
        return time;
    }
}

class StreamsExtensionsWrite extends Thread {

    BulkRandomAccessFile braf;
    int position;
    int myNumInts;
    int iarr[];
    long time;
    int WARM_UP;
    int ITERATIONS;
    boolean DEBUG;

    void setParameters(int pos, int[] ints, int nints, boolean debug, int warmup, int iterations) throws IOException {
        try {
            File file = new File("/home/08bitadnan.tmp/streams_extensions_test.txt");
            if (!file.exists()) {
                //System.out.println("File does not exist so creating a new file");
                try {
                    file.createNewFile();
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }
            myNumInts = nints;
            iarr = ints;
            braf = new BulkRandomAccessFile(file, "rws");
            position = pos;
            DEBUG = debug;
            WARM_UP = warmup;
            ITERATIONS = iterations;
        } catch (FileNotFoundException ex) {
            System.out.println("setParameters: " + ex);
        }
    }

    public void run() {
        try {
            long start[] = new long[ITERATIONS];
            long end[] = new long[ITERATIONS];
            long sum = 0;

            int index = position;
            for (int i = 0; i < WARM_UP; i++) {
                index = position;
                braf.seek(position * 4);   // * 4byte = 1int
                start[i] = System.nanoTime();
                braf.writeArray(iarr, index, myNumInts);
                end[i] = System.nanoTime() - start[i];
            }
            for (int i = WARM_UP; i < ITERATIONS; i++) {
                index = position;
                braf.seek(position * 4);   // * 4byte = 1int
                start[i] = System.nanoTime();
                braf.writeArray(iarr, index, myNumInts);
                end[i] = System.nanoTime() - start[i];
                sum += end[i];
            }
            //System.out.println(Thread.currentThread().getId() + " time = " + time );
            braf.close();
            time = sum / (ITERATIONS - WARM_UP);
        } catch (IOException ex) {
        }

    }

    public long getTime() {
        return time;
    }
}

class StreamsExtensionsRead extends Thread {

    BulkRandomAccessFile braf;
    int position;
    int myNumInts;
    int iarr[];
    boolean DEBUG;
    long time;
    int WARM_UP;
    int ITERATIONS;

    void setParameters(int pos, int[] ints, int nints, boolean debug, int warmup, int iterations) throws IOException {
        try {
            File file = new File("/home/08bitadnan.tmp/streams_extensions_test.txt");
            if (!file.exists()) {
                //System.out.println("File does not exist so creating a new file");
                try {
                    file.createNewFile();
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }
            myNumInts = nints;
            iarr = ints;
            braf = new BulkRandomAccessFile(file, "r");
            DEBUG = debug;
            position = pos;
            WARM_UP = warmup;
            ITERATIONS = iterations;
        } catch (FileNotFoundException ex) {
            System.out.println("setParameters: " + ex);
        }
    }

    public void run() {

        try {
            long start[] = new long[ITERATIONS];
            long end[] = new long[ITERATIONS];
            long sum = 0;

            int index = position;
            for (int i = 0; i < WARM_UP; i++) {
                index = position;
                braf.seek(position * 4);   // * 4byte = 1int
                start[i] = System.nanoTime();
                braf.readArray(iarr, index, myNumInts);
                end[i] = System.nanoTime() - start[i];
            }
            for (int i = WARM_UP; i < ITERATIONS; i++) {
                index = position;
                braf.seek(position * 4);   // * 4byte = 1int
                start[i] = System.nanoTime();
                braf.readArray(iarr, index, myNumInts);
                end[i] = System.nanoTime() - start[i];
                sum += end[i];
            }
            if (DEBUG == true) {
                //int p = position;
                //System.out.print("iarr = ");
                for (int i = 0; i < iarr.length; i++) {
                    System.out.print(iarr[i]);
                    //p++;
                }

            }
            braf.close();
            time = sum / (ITERATIONS - WARM_UP);
            //System.out.println(Thread.currentThread().getId() + " time = " + time );
        } catch (IOException ex) {
        }
    }

    public long getTime() {
        return time;
    }
}
