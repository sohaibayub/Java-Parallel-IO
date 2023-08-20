import mpi.*;
import java.util.Arrays;
import java.io.IOException;

/**
 * file test class
 * @author yasir
 */
public class Misc_test{

    /**
     *
     * The main function
     *
     * @param args arguments
     * @throws IOException  
     */
    public static void main(String[] args) throws IOException {
        MPI.Init(args);
        int SIZE = 1024;
        String filename = null;
        int myRank = MPI.COMM_WORLD.Rank();
            /*
             * Process 0
             */
        System.out.println("...............Test started.................");

        if(Arrays.asList(args).contains("-fname")) {
            filename = args[Arrays.asList(args).indexOf("-fname") + 1];
            System.out.println("File Name = " + filename);
                  }
        else{
            System.out.println("mpjrun.sh ..... Atomicity_test.java -fname filename ");
            System.exit(0);
              }

        if(myRank == 0){
            int p[] = new int[SIZE];
            for (int i = 0; i < SIZE; i++) {
                p[i] = 5;
            }
            
            File file1 = MPI.COMM_WORLD.fileOpen(filename, MPI.MODE.RDWR | MPI.MODE.CREATE, MPI.INFO_NULL);
            file1.setView(new Offset(0), MPI.INT, MPI.INT, "native", MPI.INFO_NULL);
            file1.getPosition();
            file1.getByteOffset(new Offset(0));
            Status status = file1.write(p, 0, SIZE, MPI.INT);
            file1.sync();
            MPI.COMM_WORLD.Barrier();

        } else {
            /*
             * Process 1
             */
            int q[] = new int[SIZE];
            File file2 = MPI.COMM_WORLD.fileOpen(filename, MPI.MODE.RDWR | MPI.MODE.CREATE, MPI.INFO_NULL);
            file2.setView(new Offset(0), MPI.INT, MPI.INT, "native", MPI.INFO_NULL);
            file2.seek(new Offset(0), 0);
            MPI.COMM_WORLD.Barrier();
            file2.sync();
            Status status = file2.read(q, 0, SIZE, MPI.INT);
        }
        MPI.Finalize();
    }
}
  
