
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
import java.util.Arrays;
import mpi.*;
import ti.io.BulkRandomAccessFile;

/**
 *
 * @author Ammar
 */
public class MPJMicroTestsFinal {

    static int rank;
    static int ITERATIONS = 50;
    static int WARM_UP = 20;

    public static void main(String[] args) {
        MPI.Init(args);
        boolean DEBUG = false;
        int num_threads = MPI.COMM_WORLD.Size();
        rank = MPI.COMM_WORLD.Rank();
		int size= MPI.COMM_WORLD.Size();
        int nints = 25000000;

		
		boolean found=false;
		if (Arrays.asList(args).contains("-size")) {
			nints = Integer.parseInt(args[Arrays.asList(args).indexOf("-size") + 1]);
			System.out.println("Rank "+ rank +" Buffer Size = " + nints);	
			found=true;
		}
			
        if (rank==0) {
			if(found) {
				System.out.println("--------------------Test Started--------------------");
				System.out.println("Buffer Size = " + nints);		
				System.out.println("Total Processes = " + size);
				System.out.println("Iterations = " + ITERATIONS);
				System.out.println("Warm_Up = " + WARM_UP);
				System.out.println("----------------------------------------------------");
			} else {
				System.out.println("--------------------Test Started--------------------");
				System.out.println("USAGE: mpjrun.sh ..... MPJMicroTets -size buf_size");
				System.out.println("----------------------------------------------------");
				System.exit(0);
			}
		}

/*            boolean found = false;
            int s_pos;
            for (s_pos = 0; s_pos < args.length; s_pos++) {
                if (args[s_pos].equals("-size")) {
                    found = true;
                    break;
                }
            }
            if (found) {
                nints = Integer.parseInt(args[s_pos + 1]);
                System.out.println("Buffer Size is " + nints + " Bytes ");
            } else {
                System.out.println("mpjrun.sh ..... MPJMicroTets -size buf_size");
                System.exit(0);
            }
*/

        int[] iarr = new int[nints];
        for (int i = 0; i < nints; i++) {
            iarr[i] = i;
        }
        try {
            for (int i = 0; i < 1; i++) {
                //Parallel Tests - Using MPJ Express 
                ParallelReadWrite_IntBuffer(iarr, nints, num_threads, DEBUG);
                ParallelReadWrite_MappedIntBuffer(iarr, nints, num_threads, DEBUG);
                ParallelReadWrite_Streams_Extensions(iarr, nints, num_threads, DEBUG);
                //ParallelReadWrite_Streams(iarr, nints, num_threads, DEBUG);

                MPI.COMM_WORLD.Barrier();
            }
            if (rank == 0) {
                System.out.println("-----------------Program Finished-------------------");
            }
        } catch (IOException | InterruptedException ioex) {
            System.err.println(ioex);
        }
        MPI.Finalize();
    }

    static void ParallelReadWrite_IntBuffer(int[] iarr, int nints, int num_threads, boolean DEBUG) throws InterruptedException, IOException {

        //System.out.println("Testing IntBuffer Write");
        //System.out.println();

        // Create threads = num_threads
        MPJIntBufferWrite ibuf_write = new MPJIntBufferWrite();
        ibuf_write.setParameters((nints / num_threads * rank), iarr, nints / num_threads, WARM_UP, ITERATIONS);
        ibuf_write.run();

        //System.out.println();
        //System.out.println("Testing IntBuffer Read");
        //System.out.println();

        MPJIntBufferRead ibuf_read = new MPJIntBufferRead();
        ibuf_read.setParameters((nints / num_threads * rank), iarr, nints / num_threads, DEBUG, WARM_UP, ITERATIONS);
        ibuf_read.run();

        //System.out.println();
    }

    static void ParallelReadWrite_MappedIntBuffer(int[] iarr, int nints, int num_threads, boolean DEBUG) throws InterruptedException, IOException {
        //System.out.println("Testing MappedIntBuffer Write");
        //System.out.println();
        // Create threads = num_threads

        MPJMappedIntBufferWrite mibuf_write = new MPJMappedIntBufferWrite();
        mibuf_write.setParameters((nints / num_threads * rank), iarr, nints / num_threads, WARM_UP, ITERATIONS);
        mibuf_write.run();

        //System.out.println();
        //System.out.println("Testing MappedIntBuffer Read");
        //System.out.println();
        // Create threads = num_threads
        MPJMappedIntBufferRead mibuf_read = new MPJMappedIntBufferRead();
        mibuf_read.setParameters((nints / num_threads * rank), iarr, nints / num_threads, DEBUG, WARM_UP, ITERATIONS);
        mibuf_read.run();

    }

    static void ParallelReadWrite_Streams(int[] iarr, int nints, int num_threads, boolean DEBUG) throws InterruptedException, IOException {

        //System.out.println();
        //System.out.println("Testing Streams Write");
        //System.out.println();

        MPJStreamsWrite streams_write = new MPJStreamsWrite();
        streams_write.setParameters((nints / num_threads * rank), iarr, nints / num_threads, WARM_UP, ITERATIONS);
        streams_write.run();

        //System.out.println();
        //System.out.println("Testing Streams Read");
        //System.out.println();

        MPJStreamsRead streams_read = new MPJStreamsRead();
        streams_read.setParameters((nints / num_threads * rank), iarr, nints / num_threads, DEBUG, WARM_UP, ITERATIONS);
        streams_read.run();

    }

    static void ParallelReadWrite_Streams_Extensions(int[] iarr, int nints, int num_threads, boolean DEBUG) throws InterruptedException, IOException {

        //System.out.println();
        //System.out.println("Testing Streams Write");
        //System.out.println();

        MPJStreamsWriteExtensions streams_write = new MPJStreamsWriteExtensions();
        streams_write.setParameters((nints / num_threads * rank), iarr, nints / num_threads, WARM_UP, ITERATIONS);
        streams_write.run();

        //System.out.println();
        //System.out.println("Testing Streams Read");
        //System.out.println();

        MPJStreamsReadExtensions streams_read = new MPJStreamsReadExtensions();
        streams_read.setParameters((nints / num_threads * rank), iarr, nints / num_threads, DEBUG, WARM_UP, ITERATIONS);
        streams_read.run();

    }
}

class MPJIntBufferWrite {

    FileChannel fc;
    int position;
    int myNumInts;
    int iarr[];
    long[] time;
    int WARM_UP;
    int ITERATIONS;

    void setParameters(int pos, int[] ints, int nints, int warmup, int iterations) throws IOException {
        try {
            myNumInts = nints;
            iarr = ints;
            fc = FileChannel.open(Paths.get("ibuf_mpj_test.txt"),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.READ);
            position = pos;
            time = new long[1];
            WARM_UP = warmup;
            ITERATIONS = iterations;
        } catch (FileNotFoundException ex) {
            System.out.println("setParameters: " + ex);
        }
    }

    public void run() {
        try {
            long[] localtime = new long[1];
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
                System.out.println("Rank "+MPI.COMM_WORLD.Rank() + " Iteration " + i + " Time " + end[i]);
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
                System.out.println("Rank "+MPI.COMM_WORLD.Rank() + " Iteration " + i + " Time " + end[i]);
            }
            fc.close();

            localtime[0] = sum / ((ITERATIONS - WARM_UP) * 1000); //time in mirco seocnds
            //System.out.println("IntBufferWrite - Rank:" + MPI.COMM_WORLD.Rank() + " time = " + localtime[0] + "ms");

            MPI.COMM_WORLD.Allreduce(localtime, 0, time, 0, 1, MPI.LONG, MPI.SUM);

            if (MPI.COMM_WORLD.Rank() == 0) {
                System.out.println("Write\tIntBuffer\t\t" + time[0] / MPI.COMM_WORLD.Size());
            }

        } catch (IOException ex) {
        }
    }
}

class MPJIntBufferRead {

    FileChannel fc;
    int position;
    int myNumInts;
    int iarr[];
    boolean DEBUG;
    long[] time;
    int WARM_UP;
    int ITERATIONS;

    void setParameters(int pos, int[] ints, int nints, boolean debug, int warmup, int iterations) throws IOException {
        try {
            DEBUG = debug;
            myNumInts = nints;
            iarr = ints;
            fc = FileChannel.open(Paths.get("ibuf_mpj_test.txt"),
                    StandardOpenOption.READ);
            position = pos;
            time = new long[1];
            WARM_UP = warmup;
            ITERATIONS = iterations;
        } catch (FileNotFoundException ex) {
            System.out.println("setParameters: " + ex);
        }
    }

    public void run() {
        try {
            long[] localtime = new long[1];
            long start[] = new long[ITERATIONS];
            long end[] = new long[ITERATIONS];
            long sum = 0;

            ByteBuffer bbuf = ByteBuffer.allocate(myNumInts * 4);
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
                bbuf.rewind();
                ibuf.get(iarr, index, myNumInts);
                ibuf.clear();
                bbuf.clear();
                System.out.println("Rank "+MPI.COMM_WORLD.Rank() + " Iteration " + i + " Time " + end[i]);
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
                System.out.println("Rank "+MPI.COMM_WORLD.Rank() + " Iteration " + i + " Time " + end[i]);
            }
            fc.close();

            if (DEBUG == true) {
                for (int i = 0; i < iarr.length; i++) {
                    System.out.print(iarr[i]);
                }
                System.out.println();
            }

            localtime[0] = sum / ((ITERATIONS - WARM_UP) * 1000);
            //System.out.println("IntBufferRead - Rank:" + MPI.COMM_WORLD.Rank() + " time = " + localtime[0] + "ms");

            MPI.COMM_WORLD.Allreduce(localtime, 0, time, 0, 1, MPI.LONG, MPI.SUM);
            if (MPI.COMM_WORLD.Rank() == 0) {
                System.out.println("Read\tIntBuffer\t\t" + time[0] / MPI.COMM_WORLD.Size());
            }
        } catch (IOException ex) {
        }
    }
}

class MPJMappedIntBufferWrite {

    FileChannel fc;
    int position;
    int myNumInts;
    int iarr[];
    long[] time;
    int WARM_UP;
    int ITERATIONS;

    void setParameters(int pos, int[] ints, int nints, int warmup, int iterations) throws IOException {
        try {
            myNumInts = nints;
            iarr = ints;

            fc = FileChannel.open(Paths.get("mapped_ibuf_mpj_test.txt"),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.READ);
            position = pos;
            time = new long[1];
            WARM_UP = warmup;
            ITERATIONS = iterations;
        } catch (FileNotFoundException ex) {
            System.out.println("setParameters: " + ex);
        }
    }

    public void run() {
        try {
            long[] localtime = new long[1];
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
                System.out.println("Rank "+MPI.COMM_WORLD.Rank() + " Iteration " + i + " Time " + end[i]);
            }
            for (int i = WARM_UP; i < ITERATIONS; i++) {
                index = position;
                start[i] = System.nanoTime();
                bbuf = fc.map(FileChannel.MapMode.READ_WRITE, (index * 4), (myNumInts * 4));
                ibuf = bbuf.asIntBuffer();
                ibuf.put(iarr, index, myNumInts);
                bbuf.force();
                end[i] = System.nanoTime() - start[i];
                sum += end[i];
                ibuf.clear();
                bbuf.clear();
                System.out.println("Rank "+MPI.COMM_WORLD.Rank() + " Iteration " + i + " Time " + end[i]);
            }
            fc.close();

            localtime[0] = sum / ((ITERATIONS - WARM_UP) * 1000);
            //System.out.println("MappedIntBufferWrite - Rank:" + MPI.COMM_WORLD.Rank() + " time = " + localtime[0] + "ms");

            MPI.COMM_WORLD.Allreduce(localtime, 0, time, 0, 1, MPI.LONG, MPI.SUM);
            if (MPI.COMM_WORLD.Rank() == 0) {
                System.out.println("Write\tMapped_IntBuffer\t" + time[0] / MPI.COMM_WORLD.Size());
            }
        } catch (IOException ex) {
            //System.out.println("Exception on thread " + this.getId() + " = " + ex);
        }
    }
}

class MPJMappedIntBufferRead {

    FileChannel fc;
    int position;
    int myNumInts;
    int iarr[];
    long[] time;
    boolean DEBUG;
    int WARM_UP;
    int ITERATIONS;

    void setParameters(int pos, int[] ints, int nints, boolean debug, int warmup, int iterations) throws IOException {
        try {
            myNumInts = nints;
            iarr = ints;

            fc = FileChannel.open(Paths.get("mapped_ibuf_mpj_test.txt"),
                    StandardOpenOption.READ);
            position = pos;
            DEBUG = debug;
            time = new long[1];
            WARM_UP = warmup;
            ITERATIONS = iterations;
        } catch (FileNotFoundException ex) {
            System.out.println("setParameters: " + ex);
        }
    }

    public void run() {
        try {
            long[] localtime = new long[1];
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
                System.out.println("Rank "+MPI.COMM_WORLD.Rank() + " Iteration " + i + " Time " + end[i]);
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
                System.out.println("Rank "+MPI.COMM_WORLD.Rank() + " Iteration " + i + " Time " + end[i]);
            }
            fc.close();

            localtime[0] = sum / ((ITERATIONS - WARM_UP) * 1000);
            //System.out.println("MappedIntBufferRead - Rank:" + MPI.COMM_WORLD.Rank() + " time = " + localtime[0] + "ms");

            MPI.COMM_WORLD.Allreduce(localtime, 0, time, 0, 1, MPI.LONG, MPI.SUM);
            if (MPI.COMM_WORLD.Rank() == 0) {
                System.out.println("Read\tMapped_IntBuffer\t" + time[0] / MPI.COMM_WORLD.Size());
            }
        } catch (IOException ex) {
            //System.out.println("Exception on thread " + this.getId() + " = " + ex);
        }
    }
}

class MPJStreamsWrite {

    RandomAccessFile raf;
    int position;
    int myNumInts;
    int iarr[];
    long[] time;
    int WARM_UP;
    int ITERATIONS;

    void setParameters(int pos, int[] ints, int nints, int warmup, int iterations) throws IOException {
        try {
            File file = new File("streams_mpj_test.txt");
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
            time = new long[1];
            WARM_UP = warmup;
            ITERATIONS = iterations;
        } catch (FileNotFoundException ex) {
            System.out.println("setParameters: " + ex);
        }
    }

    public void run() {
        try {
            long[] localtime = new long[1];
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
                System.out.println("Rank "+MPI.COMM_WORLD.Rank() + " Iteration " + i + " Time " + end[i]);
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
                System.out.println("Rank "+MPI.COMM_WORLD.Rank() + " Iteration " + i + " Time " + end[i]);				
            }
            raf.close();

            localtime[0] = sum / ((ITERATIONS - WARM_UP) * 1000);
            //System.out.println("StreamsWrite - Rank:" + MPI.COMM_WORLD.Rank() + " time = " + localtime[0] + "ms");

            MPI.COMM_WORLD.Allreduce(localtime, 0, time, 0, 1, MPI.LONG, MPI.SUM);
            if (MPI.COMM_WORLD.Rank() == 0) {
                System.out.println("Write\tStreams\t\t\t" + time[0] / MPI.COMM_WORLD.Size());
            }

        } catch (IOException ex) {
        }
    }
}

class MPJStreamsRead {

    RandomAccessFile raf;
    int position;
    int myNumInts;
    int iarr[];
    boolean DEBUG;
    long[] time;
    int WARM_UP;
    int ITERATIONS;

    void setParameters(int pos, int[] ints, int nints, boolean debug, int warmup, int iterations) {
        try {
            File file = new File("streams_mpj_test.txt");
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
            time = new long[1];
            WARM_UP = warmup;
            ITERATIONS = iterations;
        } catch (FileNotFoundException ex) {
            System.out.println("setParameters: " + ex);
        }
    }

    public void run() {
        try {
            long[] localtime = new long[1];
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
                System.out.println("Rank "+MPI.COMM_WORLD.Rank() + " Iteration " + i + " Time " + end[i]);
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
                System.out.println("Rank "+MPI.COMM_WORLD.Rank() + " Iteration " + i + " Time " + end[i]);
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

            localtime[0] = sum / ((ITERATIONS - WARM_UP) * 1000);
            //System.out.println("StreamsRead - Rank:" + MPI.COMM_WORLD.Rank() + " time = " + localtime[0] + "ms");

            MPI.COMM_WORLD.Allreduce(localtime, 0, time, 0, 1, MPI.LONG, MPI.SUM);
            if (MPI.COMM_WORLD.Rank() == 0) {
                System.out.println("Read\tStreams\t\t\t" + time[0] / MPI.COMM_WORLD.Size());
            }
        } catch (IOException ex) {
        }
    }
}

class MPJStreamsWriteExtensions {

    BulkRandomAccessFile braf;
    int position;
    int myNumInts;
    int iarr[];
    long[] time;
    int WARM_UP;
    int ITERATIONS;

    void setParameters(int pos, int[] ints, int nints, int warmup, int iterations) throws IOException {
        try {
            File file = new File("streams_extensions_mpj_test.txt");
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
            time = new long[1];
            WARM_UP = warmup;
            ITERATIONS = iterations;
        } catch (FileNotFoundException ex) {
            System.out.println("setParameters: " + ex);
        }
    }

    public void run() {
        try {
            long[] localtime = new long[1];
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
                System.out.println("Rank "+MPI.COMM_WORLD.Rank() + " Iteration " + i + " Time " + end[i]);
            }
            for (int i = WARM_UP; i < ITERATIONS; i++) {
                index = position;
                braf.seek(position * 4);   // * 4byte = 1int
                start[i] = System.nanoTime();
                braf.writeArray(iarr, index, myNumInts);
                end[i] = System.nanoTime() - start[i];
                sum += end[i];
                System.out.println("Rank "+MPI.COMM_WORLD.Rank() + " Iteration " + i + " Time " + end[i]);
            }
            braf.close();

            localtime[0] = sum / ((ITERATIONS - WARM_UP) * 1000);
            //System.out.println("StreamsWriteExtensions - Rank:" + MPI.COMM_WORLD.Rank() + " time = " + localtime[0] + "ms");

            MPI.COMM_WORLD.Allreduce(localtime, 0, time, 0, 1, MPI.LONG, MPI.SUM);
            if (MPI.COMM_WORLD.Rank() == 0) {
                System.out.println("Write\tStream_Extensions\t" + time[0] / MPI.COMM_WORLD.Size());
            }
        } catch (IOException ex) {
        }
    }
}

class MPJStreamsReadExtensions {

    BulkRandomAccessFile braf;
    int position;
    int myNumInts;
    int iarr[];
    boolean DEBUG;
    long[] time;
    int WARM_UP;
    int ITERATIONS;

    void setParameters(int pos, int[] ints, int nints, boolean debug, int warmup, int iterations) throws IOException {
        try {
            File file = new File("streams_extensions_mpj_test.txt");
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
            braf = new BulkRandomAccessFile(file, "r");
            position = pos;
            time = new long[1];
            WARM_UP = warmup;
            ITERATIONS = iterations;
        } catch (FileNotFoundException ex) {
            System.out.println("setParameters: " + ex);
        }
    }

    public void run() {
        try {
            long[] localtime = new long[1];
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
                System.out.println("Rank "+MPI.COMM_WORLD.Rank() + " Iteration " + i + " Time " + end[i]);
            }
            for (int i = WARM_UP; i < ITERATIONS; i++) {
                index = position;
                braf.seek(position * 4);   // * 4byte = 1int
                start[i] = System.nanoTime();
                braf.readArray(iarr, index, myNumInts);
                end[i] = System.nanoTime() - start[i];
                sum += end[i];
                System.out.println("Rank "+MPI.COMM_WORLD.Rank() + " Iteration " + i + " Time " + end[i]);
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
            braf.close();
            localtime[0] = sum / ((ITERATIONS - WARM_UP) * 1000);
            //System.out.println("StreamsReadExtensions - Rank:" + MPI.COMM_WORLD.Rank() + " time = " + localtime[0] + "ms");

            MPI.COMM_WORLD.Allreduce(localtime, 0, time, 0, 1, MPI.LONG, MPI.SUM);
            if (MPI.COMM_WORLD.Rank() == 0) {
                System.out.println("Read\tStream_Extensions\t" + time[0] / MPI.COMM_WORLD.Size());
            }
        } catch (IOException ex) {
        }
    }
}