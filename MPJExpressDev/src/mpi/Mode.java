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
 and/or sell copies of the Software, and to permit persons to whom the
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
 * File         : Mode.java
 * Author       : Ammar Ahmad Awan
 * Created      : Thr Apr 19 2012
 * Revision     : $Revision: 1.00 $
 * Updated      : $Date: 2012/04/13 17:24:47 $
 */
package mpi;

/**
 * Mode class
 *
 * @author sohaib
 */
public class Mode {
    
    private int amode;
    // The value of modes has been taken from ROMIO ( include/mpio.h.in )
    /**
     * MPI_MODE_RDONLY - read only
     */
    public final int RDONLY = 2;
    /**
     * MPI_MODE_RDWR - reading and writing
     */
    public final int RDWR = 8;
    /**
     * MPI_MODE_WRONLY - write only
     */
    public final int WRONLY = 4;
    /**
     * MPI_MODE_CREATE - create the file if it does not exist
     */
    public final int CREATE = 1;
    /**
     * MPI_MODE_EXCL - error if creating file that already exists
     */
    public final int EXCL = 64;
    /**
     * MPI_MODE_DELETE_ON_CLOSE - delete file on close
     */
    public final int DELETE_ON_CLOSE = 16;
    /**
     * MPI_MODE_UNIQUE_OPEN - file will not be concurrently opened elsewhere
     */
    public final int UNIQUE_OPEN = 32;
    /**
     * MPI_MODE_SEQUENTIAL - file will only be accessed sequentially
     */
    public final int SEQUENTIAL = 256;
    /**
     * MPI_MODE_APPEND - set initial position of all file pointers to end of
     * file
     */
    public final int APPEND = 128;

    /**
     * sets the mode of for the file
     *
     * @param amode integer amode
     */
    public void setAmode(int amode) {
        this.amode = amode;
    }

    /**
     * returns the amode
     *
     * @return returns the amode
     */
    public int getAmode() {
        return amode;
    }
}
