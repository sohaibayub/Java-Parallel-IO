/*The MIT License

 Copyright (c) 2005 - 2008
   1. Distributed Systems Group, University of Portsmouth (2005)
   2. Community Grids Laboratory, Indiana University (2005)
   3. Aamir Shafi (2005 - 2008)
   4. Bryan Carpenter (2005 - 2008)
   5. Mark Baker (2005 - 2008)

Permission is hereby granted, free of charge, to any person obtaining a
copy of this software and associated documentation files (the "Software"),
to deal in the Software without restriction, including without limitation
the rights to use, copy, modify, merge, publish, distribute, sublicense,
and/or sell copies of the Software,m and to permit persons to whom the
Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included
in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.
 */

/*
 * File         : FileTest.java
 * Author       : Sohaib Ayub
 * Created      : Thr Apr 19 2012
 * Revision     : $Revision: 1.00 $
 * Updated      : $Date: 2012/04/13 17:24:47 $
 */

import mpi.*;
import java.io.IOException;

/**
 * file test class
 * @author sohaib
 */
public class FileTest {

    /**
     *
     * The main function
     * 
     * @param args arguments
     * @throws IOException  
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        MPI.Init(args);
        int myRank = MPI.COMM_WORLD.Rank();

        int b[] = new int[100];
        if (myRank == 0) {
            /*
             * Process 0
             */

            int a[] = new int[100];
            for (int i = 0; i < 100; i++) {
                a[i] = i;
		System.out.print(a[i]+"\t");
            }
            Offset offset = new Offset(0);
            File file1 = MPI.COMM_WORLD.fileOpen("workfile",  MPI.MODE.RDWR | MPI.MODE.CREATE, MPI.INFO_NULL);
            file1.setView(offset, MPI.INT, MPI.INT, "native", MPI.INFO_NULL);
            Status status = file1.write(a, 0, 100, MPI.INT);
            file1.sync();
            MPI.COMM_WORLD.Barrier();

        } else {
            /*
             * Process 1
             */
            Offset offset = new Offset(0);
            File file2 = MPI.COMM_WORLD.fileOpen("workfile", MPI.MODE.RDWR | MPI.MODE.CREATE, MPI.INFO_NULL);
            file2.setView(offset, MPI.INT, MPI.INT, "native", MPI.INFO_NULL);
            MPI.COMM_WORLD.Barrier();
            file2.sync();
            Status status = file2.read(b, 0, 100, MPI.INT);
            for (int i = 0; i < 100; i++) {
     		System.out.print(b[i]+"\t");
            }
            
	}
        MPI.Finalize();
    }
}
