/*The MIT License
 * 
 * Copyright (c) 2005 - 2008
 * 1. Distributed Systems Group, University of Portsmouth (2005)
 * 2. Community Grids Laboratory, Indiana University (2005)
 * 3. Aamir Shafi (2005 - 2008)
 * 4. Bryan Carpenter (2005 - 2008)
 * 5. Mark Baker (2005 - 2008)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

/*
 * File         : File.java
 * Author       : Ammar Ahmad Awan
 * Created      : Fri Apr 13 2012
 * Revision     : $Revision: 1.00 $
 * Updated      : $Date: 2012/04/13 17:24:47 $
 */
package mpi;

import java.io.IOException;
import java.nio.*;
import java.nio.channels.*;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * File class for IO operations
 *
 * @author sohaib
 * @version 1.0
 * @since Fri Apr 13 2012
 */
public class File {
    
    private FileChannel fc;
    private String fileSystem;
    private String fileName;
    private boolean atomicity = false;
    private java.io.File file;
    private Intracomm intracomm;
    private static final Logger LOG = Logger.getLogger(File.class.getName());
    private Set<StandardOpenOption> hashset;

    /**
     * Default constructor for testing purpose only
     */
    public File() {
    }

    /**
     * constructor for file opening
     *
     * @param comm communicator object
     * @param fileName name of the file
     * @param amode access mode for file
     * @param info info object
     */
    public File(Intracomm comm, String fileName, int amode, Info info) {
        this.intracomm = comm;
        this.fileName = fileName;
        this.file = new java.io.File(fileName);
        MPI.MODE.setAmode(amode);
        MPI.INFO = info;
        this.fileSystem = "";
        this.hashset = new HashSet<>(10); //Set hashset <-> HashSet hashset

        try {

            // Testing the conditions specified in MPI-2 standard

            // if amode & MPI.MODE.XXXXX is !=0, it means that
            // amode has XXXXX MODE set
            // for example if(amode & MPI.MODE.RDONLY)!=0) true; //amode also contains READONLY access
            if (((((amode & MPI.MODE.RDONLY) != 0) ? 1 : 0)
                    + (((amode & MPI.MODE.RDWR) != 0) ? 1 : 0)
                    + (((amode & MPI.MODE.WRONLY) != 0) ? 1 : 0)) != 1) {
                // the result of above expression is equal to zero then none of the mode is specified
                // and if greater then one then two modes are specified at the same time 
                System.out.println("Intracomm.fileOpen: Exactly one of MPI.MODE.RDONLY, "
                        + "MPI.MODE.WRONLY, or MPI.MODE.RDWR must be specified");
            }
            
            if (((amode & MPI.MODE.RDONLY) != 0)
                    && (((amode & MPI.MODE.CREATE) != 0) || ((amode & MPI.MODE.EXCL) != 0))) {
                System.out.println("Intracomm.fileOpen: It is erroneous to specify "
                        + "MPI.MODE.CREATE or MPI.MODE.EXCL with MPI.MODE.RDONLY");
            }
            
            if ((amode & MPI.MODE.RDWR) != 0 && (amode & MPI.MODE.SEQUENTIAL) != 0) {
                System.out.println("Intracomm.fileOpen: It is erroneous to specify "
                        + "MPI.MODE.SEQUENTIAL with MPI.MODE.RDWR");
            }
            
            if (!MPI.Initialized()) {
                System.out.println("Error: MPI.Init() must be called before using MPJ-IO");
            }
            
            if ((("PIOFS".equals(fileSystem)) || ("PVFS".equals(fileSystem)))
                    && (amode & MPI.MODE.SEQUENTIAL) != 0) {
                // Kept for future use when we add FileSystem implementations
                System.out.println("Intracomm.fileOpen: MPI.MODE.SEQUENTIAL not supported "
                        + "on PIOFS and PVFS");
            }
            
            if ((amode & MPI.MODE.CREATE) != 0 && (amode & MPI.MODE.EXCL) != 0) {
                // from ROMIO
            /*
                 * the open should fail if the file exists. Only process 0
                 * should check this. Otherwise, if all processes try to check
                 * and the file does not exist, one process will create the file
                 * and others who reach later will return error.
                 */
                if (MPI.COMM_WORLD.Rank() == 0) {
                    //ammar: check if file exists
                    if (file.exists()) {
                        fc.close(); // from ROMIO if file exist then close it
                    }
                }
                amode ^= MPI.MODE.EXCL;  /*
                 * turn off MPI.MODE.EXCL
                 */
            }
            if ((amode & MPI.MODE.RDONLY) != 0) {
                hashset.add(StandardOpenOption.READ);
            }
            if ((amode & MPI.MODE.WRONLY) != 0) {
                hashset.add(StandardOpenOption.WRITE);
            }
            if ((amode & MPI.MODE.RDWR) != 0) {
                hashset.add(StandardOpenOption.READ);
                hashset.add(StandardOpenOption.WRITE);
            }
            if ((amode & MPI.MODE.DELETE_ON_CLOSE) != 0) { //delete file on close
                hashset.add(StandardOpenOption.DELETE_ON_CLOSE); //Deletes the file when the stream is closed. This option is useful for temporary files.
            }
            if ((amode & MPI.MODE.APPEND) != 0) {  //set initial position of all file pointers to end of file
                hashset.add(StandardOpenOption.APPEND); //Appends the new data to the end of the file. This option is used with the WRITE or CREATE options.
            }
            if ((amode & MPI.MODE.CREATE) != 0) { //create the file if it does not exist
                hashset.add(StandardOpenOption.CREATE); //creates a new file if it does not or Opens the file if it exists.
            }
            if ((amode & MPI.MODE.EXCL) != 0) { //error if creating file that already exists
                hashset.add(StandardOpenOption.CREATE_NEW); //Creates a new file and throws an exception if the file already exists.
            }
            if ((amode & MPI.MODE.SEQUENTIAL) != 0) { //file will only be accessed sequentially
                //FIXME: what should be here?
                //hashset.add(StandardOpenOption.CREATE);
            }

            //below are the Standarded option which are not being used
            //FC.TRUNCATE_EXISTING â€“ Truncates the file to zero bytes. This option is used with the WRITE option.
            //FC.SPARSE â€“ Hints that a newly created file will be sparse. This advanced option is honored on some file systems, such as NTFS, where large files with data "gaps" can be stored in a more efficient manner where those empty gaps do not consume disk space.
            //FC.SYNC â€“ Keeps the file (both content and metadata) synchronized with the underlying storage device.
            //FC.DSYNC â€“ Keeps the file content synchronized with the underlying storage device.       

            //fc = FileChannel.open(Paths.get(fileName), hashset);
            fc = FileChannel.open(file.toPath(), hashset);
            //RandomAccessFile aFile = new RandomAccessFile(fileName, "rw");
            //fc = aFile.getChannel();
            if ((amode & MPI.MODE.UNIQUE_OPEN) != 0) { //file will not be concurrently opened elsewhere
                fc.tryLock();
            }
            
        } catch (IllegalArgumentException ex) {
            System.out.println("Illegal Argument");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (UnsupportedOperationException ex) {
            System.out.println("Path Do not Support the options");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (IOException ex) {
            System.out.println("IO Exception");
            System.out.println("Stack Trace is " + ex.toString());
        }
    }

    /**
     * File close method this method is called from IntraComm
     */
    public void fileClose(Intracomm comm) {
        try {
            fc.close();
        } catch (ClosedChannelException ex) {
            System.out.println("Already Closed Channel");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (IOException ex) {
            System.out.println("IO Exception");
            System.out.println("Stack Trace is " + ex.toString());
        }
    }

    /**
     * deletes the file
     *
     * @param fileName the name of the file
     * @param info the info object
     */
    public void delete(String fileName, Info info) {
        boolean isdeleted = file.delete();
        if (isdeleted) {
            System.out.println("File Deleted Secussfully");
        }
    }

    /**
     * Preallocates the size
     *
     * @param size size to pre allocate file
     */
    public void preallocate(Offset size) {
        MPI.OFFSET.setOffset(size);
    }

    /**
     * sets the size of the file
     *
     * @param size size to truncate or expand file
     */
    public void setSize(Offset size) {
        try {
            fc.truncate(size.getOffsetValue());
        } catch (NonWritableChannelException ex) {
            System.out.println("Exception: Non Writeable Channel ");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (ClosedChannelException ex) {
            System.out.println("Exception: Channel is closed");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (IllegalArgumentException ex) {
            System.out.println("Exception: Illegal Arguments");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (IOException ex) {
            System.out.println("IO Exception");
            System.out.println("Stack Trace is " + ex.toString());
        }
    }

    /**
     * returns the size
     *
     * @return offset
     */
    public Offset getSize() {
        return MPI.OFFSET;
    }

    /**
     * sets the info object
     *
     * @param info the info object
     */
    public void setInfo(Info info) {
        MPI.INFO.setInfo(info);
    }

    /**
     * returns the info object
     *
     * @return the info object
     */
    public Info getInfo() {
        return MPI.INFO.getInfo();
    }

    /**
     * returns the group
     *
     * @return the group that opened the file (mpj.Group)
     */
    public Group getGroup() {
        return MPI.COMM_WORLD.Group();
    }

    /**
     * returns the mode
     *
     * @return the file access mode of this file
     */
    public int getAmode() {
        return MPI.MODE.getAmode();
    }

    /**
     * Sets the atomicity
     *
     * @param flag the flag to set/unset the atomicity
     */
    public void setAtomicity(boolean flag) {
        atomicity = flag;
    }

    /**
     * get the atomicity
     *
     * @return true if atomic mode, false if non atomic mode
     */
    public boolean getAtomicity() {
        return atomicity;
    }

    /**
     * file sync function
     */
    public void sync() {
        try {
            fc.force(true);
        } catch (ClosedChannelException ex) {
            System.out.println("Exception: Channel is closed");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (IOException ex) {
            System.out.println("IO Exception");
            System.out.println("Stack Trace is " + ex.toString());
        }
    }

    /**
     * seeks the file to specified offset
     *
     * @param offset file offset
     * @param whence update mode
     */
    public void seek(Offset offset, int whence) {
        try {
            if (fc.isOpen()) {
                fc.position(offset.getOffsetValue());
            }
        } catch (ClosedChannelException ex) {
            System.out.println("Exception: Channel is closed");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (IllegalArgumentException ex) {
            System.out.println("Exception: Illegal Arguments");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (IOException ex) {
            System.out.println("IO Exception");
            System.out.println("Stack Trace is " + ex.toString());
        }
    }

    /**
     * get the offset position of the file
     *
     * @return the file offset
     */
    public Offset getPosition() {
        return MPI.OFFSET.getOffset();
    }

    /**
     * returns the absolute byte file offset
     *
     * @param offset the view-relative offset
     * @return the absolute byte file offset
     */
    public Offset getByteOffset(Offset offset) {
        return MPI.OFFSET.getOffset();
    }

    /**
     * Sets the view
     *
     * @param disp displacement
     * @param etype elementary datatype object (mpi.Datatype)
     * @param filetype file type object (mpi.Datatype)
     * @param datarep data representation
     * @param info the info object
     */
    public void setView(Offset disp, Datatype etype, Datatype filetype, String datarep, Info info) {
        MPI.VIEW.setView(disp, etype, filetype, datarep);
        try {
            fc.position(disp.getOffsetValue());
        } catch (ClosedChannelException ex) {
            System.out.println("Exception: Channel is closed");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (IllegalArgumentException ex) {
            System.out.println("Exception: Illegal Arguments");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (IOException ex) {
            System.out.println("IO Exception");
            System.out.println("Stack Trace is " + ex.toString());
        }
    }

    /**
     * Get the view information
     *
     * @param disp displacement
     * @param etype elementary datatype object (mpj.Datatype)
     * @param filetype file type object (mpj.Datatype)
     * @param datarep data representation
     */
    public void getView(Offset disp, Datatype etype, Datatype filetype, StringBuffer datarep) {
        MPI.VIEW.getView(disp, etype, filetype, datarep);
    }

    /**
     * Read the file
     *
     * @param offset Offset for reading the file
     * @param buf buffer object
     * @param bufOffset the buffer offset
     * @param count number of elements in buffer
     * @param datatype datatype of each buffer element
     * @return returns the status object
     * @throws IOException
     */
    public Status readAt(Offset offset, Object buf, int bufOffset, int count, Datatype datatype) throws IOException {
        
        ByteBuffer bbuf = ByteBuffer.allocate(count * 4);
        Status status = new Status(MPI.ANY_SOURCE, MPI.ANY_TAG, count, count);
        
        try {
            if (atomicity) {
                sync();
                MPI.COMM_WORLD.Barrier();
                sync();
            }
            fc.position(offset.getOffsetValue());
            status.count = fc.read(bbuf);
            bbuf.flip();
            
            if (datatype == MPI.INT) {
                IntBuffer ibuf = bbuf.asIntBuffer();
                int[] arr = (int[]) buf;
                ibuf.get(arr, bufOffset, count);
                buf = arr;
            } else if (datatype == MPI.CHAR) {
                CharBuffer ibuf = bbuf.asCharBuffer();
                char[] arr = (char[]) buf;
                ibuf.get(arr, bufOffset, count);
                buf = arr;
            } else if (datatype == MPI.DOUBLE) {
                DoubleBuffer ibuf = bbuf.asDoubleBuffer();
                double[] arr = (double[]) buf;
                ibuf.get(arr, bufOffset, count);
                buf = arr;
            } else if (datatype == MPI.LONG) {
                LongBuffer ibuf = bbuf.asLongBuffer();
                long[] arr = (long[]) buf;
                ibuf.get(arr, bufOffset, count);
                buf = arr;
            } else if (datatype == MPI.FLOAT) {
                FloatBuffer ibuf = bbuf.asFloatBuffer();
                float[] arr = (float[]) buf;
                ibuf.get(arr, bufOffset, count);
                buf = arr;
            } else if (datatype == MPI.SHORT) {
                ShortBuffer ibuf = bbuf.asShortBuffer();
                short[] arr = (short[]) buf;
                ibuf.get(arr, bufOffset, count);
                buf = arr;
            } else if (datatype == MPI.BYTE) {
                // no need to do anything
                ByteBuffer ibuf = (ByteBuffer) buf;
                byte[] arr = (byte[]) buf;
                ibuf.get(arr, bufOffset, count);
                buf = arr;
            } else {
                // do something
            }
            // what is the use of count here ??
            //sohaib : we had allocated the buffer of count elements, so here why we have to use count

            // what will the read method do, i.e where shall the read keep the buffer and what form
            // Sohaib : buffer is an object (Object buf) and passed by refernece, we will use it afterwards for reading
            //buf = buff;
            // then will use that buf which was passed
            // there can be another option that we do not made the object BufeertoWrite and use the same buf
        } catch (ClosedByInterruptException ex) {
            System.out.println("Exception: Channel closed by intterupt");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (AsynchronousCloseException ex) {
            System.out.println("Exception: Channel Closed Asynchronusly");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (ClosedChannelException ex) {
            System.out.println("Exception: Channel is closed");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (IllegalArgumentException ex) {
            System.out.println("Exception: Illegal Arguments");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (NonReadableChannelException ex) {
            System.out.println("Exception: Non Readable Channel ");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (IOException ex) {
            System.out.println("IO Exception");
            System.out.println("Stack Trace is " + ex.toString());
        }
        
        
        return status;
    }

    /**
     * Write data on file
     *
     * @param offset offset for writing a file
     * @param buf buffer object
     * @param bufOffset the buffer offset
     * @param count number of elements in buffer
     * @param datatype datatype of each buffer element
     * @return returns the status object
     */
    public Status writeAt(Offset offset, Object buf, int bufOffset, int count, Datatype datatype) {
        
        ByteBuffer bbuf = ByteBuffer.allocate(count * 4);
        Status status = new Status(MPI.ANY_SOURCE, MPI.ANY_TAG, count, count);
        //why there are MPI.Float and MPI.Float2       
        try {
            if (datatype == MPI.INT) {
                IntBuffer ibuf = bbuf.asIntBuffer();
                int[] arr = (int[]) buf;
                ibuf.put(arr, bufOffset, count);
            } else if (datatype == MPI.CHAR) {
                CharBuffer ibuf = bbuf.asCharBuffer();
                char[] arr = (char[]) buf;
                ibuf.put(arr, bufOffset, count);
            } else if (datatype == MPI.DOUBLE) {
                DoubleBuffer ibuf = bbuf.asDoubleBuffer();
                double[] arr = (double[]) buf;
                ibuf.put(arr, bufOffset, count);
            } else if (datatype == MPI.LONG) {
                LongBuffer ibuf = bbuf.asLongBuffer();
                long[] arr = (long[]) buf;
                ibuf.put(arr, bufOffset, count);
            } else if (datatype == MPI.FLOAT) {
                FloatBuffer ibuf = bbuf.asFloatBuffer();
                float[] arr = (float[]) buf;
                ibuf.put(arr, bufOffset, count);
            } else if (datatype == MPI.SHORT) {
                ShortBuffer ibuf = bbuf.asShortBuffer();
                short[] arr = (short[]) buf;
                ibuf.put(arr, bufOffset, count);
            } else if (datatype == MPI.BYTE) {
                // no need to do anything
            } else {
                // do something
            }
            
            fc.position(offset.getOffsetValue());
            FileLock lock = fc.lock(offset.getOffsetValue(), count, false);
            status.count = fc.write(bbuf);
            sync();
            lock.release();
            if (atomicity) {
                sync();
                MPI.COMM_WORLD.Barrier();
                sync();
            }
        } catch (ClosedByInterruptException ex) {
            System.out.println("Exception: Channel closed by intterupt");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (AsynchronousCloseException ex) {
            System.out.println("Exception: Channel Closed Asynchronusly");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (ClosedChannelException ex) {
            System.out.println("Exception: Channel is closed");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (IllegalArgumentException ex) {
            System.out.println("Exception: Illegal Arguments");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (NonWritableChannelException ex) {
            System.out.println("Exception: Non Writeable Channel ");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (IOException ex) {
            System.out.println("IO Exception");
            System.out.println("Stack Trace is " + ex.toString());
        }
        
        
        
        return status;
    }

    /**
     * Read the file
     *
     * @param buf buffer object
     * @param bufOffset the buffer offset
     * @param count number of elements in buffer
     * @param datatype datatype of each buffer element
     * @return returns the status object
     */
    public Status read(Object buf, int bufOffset, int count, Datatype datatype) {
        
        Status status = new Status(MPI.ANY_SOURCE, MPI.ANY_TAG, count, count);
        ByteBuffer bbuf = ByteBuffer.allocate(count * 4);
        try {
            if (atomicity) {
                sync();
                MPI.COMM_WORLD.Barrier();
                sync();
            }
            //get position from get offset function and apply here to reading from file channel
            fc.position(MPI.OFFSET.getOffsetValue()); //FIXME:CHECK this
            status.count = fc.read(bbuf);
            bbuf.flip();
            
            if (datatype == MPI.INT) {
                IntBuffer ibuf = bbuf.asIntBuffer();
                int[] arr = (int[]) buf;
                ibuf.get(arr, bufOffset, count);
                buf = arr;
            } else if (datatype == MPI.CHAR) {
                CharBuffer ibuf = bbuf.asCharBuffer();
                char[] arr = (char[]) buf;
                ibuf.get(arr, bufOffset, count);
                buf = arr;
            } else if (datatype == MPI.DOUBLE) {
                DoubleBuffer ibuf = bbuf.asDoubleBuffer();
                double[] arr = (double[]) buf;
                ibuf.get(arr, bufOffset, count);
                buf = arr;
            } else if (datatype == MPI.LONG) {
                LongBuffer ibuf = bbuf.asLongBuffer();
                long[] arr = (long[]) buf;
                ibuf.get(arr, bufOffset, count);
                buf = arr;
            } else if (datatype == MPI.FLOAT) {
                FloatBuffer ibuf = bbuf.asFloatBuffer();
                float[] arr = (float[]) buf;
                ibuf.get(arr, bufOffset, count);
                buf = arr;
            } else if (datatype == MPI.SHORT) {
                ShortBuffer ibuf = bbuf.asShortBuffer();
                short[] arr = (short[]) buf;
                ibuf.get(arr, bufOffset, count);
                buf = arr;
            } else if (datatype == MPI.BYTE) {
                // no need to do anything
                ByteBuffer ibuf = (ByteBuffer) buf;
                byte[] arr = (byte[]) buf;
                ibuf.get(arr, bufOffset, count);
                buf = arr;
            } else {
                // do something
            }
        } catch (ClosedByInterruptException ex) {
            System.out.println("Exception: Channel closed by intterupt");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (AsynchronousCloseException ex) {
            System.out.println("Exception: Channel Closed Asynchronusly");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (ClosedChannelException ex) {
            System.out.println("Exception: Channel is closed");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (IllegalArgumentException ex) {
            System.out.println("Exception: Illegal Arguments");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (NonReadableChannelException ex) {
            System.out.println("Exception: Non Readable Channel ");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (IOException ex) {
            System.out.println("IO Exception");
            System.out.println("Stack Trace is " + ex.toString());
        }
        return status;
        // what is the use of count here ??
        //sohaib : we had allocated the buffer of count elements, so here why we have to use count

        // what will the read method do, i.e where shall the read keep the buffer and what form
        // Sohaib : buffer is an object (Object buf) and passed by refernece, we will use it afterwards for reading
        //buf = buff;
        // then will use that buf which was passed
        // there can be another option that we do not made the object BufeertoWrite and use the same buf

    }

    /**
     * Write data on file
     *
     * @param buf buffer object
     * @param bufOffset the buffer offset
     * @param count number of elements in buffer
     * @param datatype datatype of each buffer element
     * @return returns the status object
     */
    public Status write(Object buf, int bufOffset, int count, Datatype datatype) {
        
        Status status = new Status(MPI.ANY_SOURCE, MPI.ANY_TAG, count, count);
        ByteBuffer bbuf = ByteBuffer.allocate(count * 4);
        
        if (datatype == MPI.INT) {
            IntBuffer ibuf = bbuf.asIntBuffer();
            int[] arr = (int[]) buf;
            ibuf.put(arr, bufOffset, count);
        } else if (datatype == MPI.CHAR) {
            CharBuffer ibuf = bbuf.asCharBuffer();
            char[] arr = (char[]) buf;
            ibuf.put(arr, bufOffset, count);
        } else if (datatype == MPI.DOUBLE) {
            DoubleBuffer ibuf = bbuf.asDoubleBuffer();
            double[] arr = (double[]) buf;
            ibuf.put(arr, bufOffset, count);
        } else if (datatype == MPI.LONG) {
            LongBuffer ibuf = bbuf.asLongBuffer();
            long[] arr = (long[]) buf;
            ibuf.put(arr, bufOffset, count);
        } else if (datatype == MPI.FLOAT) {
            FloatBuffer ibuf = bbuf.asFloatBuffer();
            float[] arr = (float[]) buf;
            ibuf.put(arr, bufOffset, count);
        } else if (datatype == MPI.SHORT) {
            ShortBuffer ibuf = bbuf.asShortBuffer();
            short[] arr = (short[]) buf;
            ibuf.put(arr, bufOffset, count); //ShortBuffer.wrap(arr); // arr, offset, length
        } else if (datatype == MPI.BYTE) {
            // no need to do anything
        } else {
            // do something
        }
        try {
            if (atomicity) {
                sync();
                MPI.COMM_WORLD.Barrier();
                sync();
            }
            fc.position(fc.position());
            //System.out.println("Locked from " + fc.position() + " to " + (fc.position()+count) );
            FileLock lock = fc.lock(fc.position(), count, false);
            status.count = fc.write(bbuf);
            sync();
            lock.release();
            
        } catch (ClosedByInterruptException ex) {
            System.out.println("Exception: Channel closed by intterupt");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (AsynchronousCloseException ex) {
            System.out.println("Exception: Channel Closed Asynchronusly");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (ClosedChannelException ex) {
            System.out.println("Exception: Channel is closed");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (IllegalArgumentException ex) {
            System.out.println("Exception: Illegal Arguments");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (NonWritableChannelException ex) {
            System.out.println("Exception: Non Writeable Channel ");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (IOException ex) {
            System.out.println("IO Exception");
            System.out.println("Stack Trace is " + ex.toString());
        }
        
        return status;
    }

    /**
     * Read the file in non blocking mode Java binding for the MPI operation
     * MPI_FILE_IREAD
     *
     * @param buf buffer object
     * @param bufOffset the buffer offset
     * @param count number of elements in buffer
     * @param datatype datatype of each buffer element
     * @return returns the status object
     */
    public Status iread(Object buf, int bufOffset, int count, Datatype datatype) {
        
        System.out.println("iread");
        ReadThread readThread = new ReadThread();
        readThread.SetParameters(this.fc, buf, bufOffset, count, datatype, this.file, this.hashset);
        if (atomicity) {
            sync();
            MPI.COMM_WORLD.Barrier();
            sync();
        }
        readThread.start();
        if (readThread.isDone) {
            System.out.println("Iread Complete");
        }
        
        return readThread.status;
    }

    /**
     * Write data on file in nonblocking mode Java binding for the MPI operation
     * MPI_FILE_IWRITE
     *
     * @param buf buffer object
     * @param bufOffset the buffer offset
     * @param count number of elements in buffer
     * @param datatype datatype of each buffer element
     * @return returns the status object
     */
    public Status iwrite(Object buf, int bufOffset, int count, Datatype datatype) {
        
        System.out.println("Iwrite");
        WriteThread writeThread = new WriteThread();
        writeThread.SetParameters(this.fc, buf, bufOffset, count, datatype, this.file, this.hashset);
        writeThread.start();
        if (atomicity) {
            sync();
            MPI.COMM_WORLD.Barrier();
            sync();
        }
        if (writeThread.isDone) {
            System.out.println("Iwrite Complete");
        }
        
        System.out.println("Write Thead status" + writeThread.status.count);
        return writeThread.status;
    }

    /**
     * Read the file collectively
     *
     * @param buf buffer object
     * @param bufOffset the buffer offset
     * @param count number of elements in buffer
     * @param datatype datatype of each buffer element
     * @return returns the status object
     */
    public Status readAll(Object buf, int bufOffset, int count, Datatype datatype) {
        MPI.COMM_WORLD.Barrier();
        Status status = new Status(MPI.ANY_SOURCE, MPI.ANY_TAG, count, count);
        ByteBuffer bbuf = ByteBuffer.allocate(count * 4);
        try {
            if (atomicity) {
                sync();
                MPI.COMM_WORLD.Barrier();
                sync();
            }
            //get position from get offset function and apply here to reading from file channel
            fc.position(MPI.OFFSET.getOffsetValue());
            status.count = fc.read(bbuf);
            bbuf.flip();
            
            
            if (datatype == MPI.INT) {
                IntBuffer ibuf = bbuf.asIntBuffer();
                int[] arr = (int[]) buf;
                ibuf.get(arr, bufOffset, count);
                buf = arr;
            } else if (datatype == MPI.CHAR) {
                CharBuffer ibuf = bbuf.asCharBuffer();
                char[] arr = (char[]) buf;
                ibuf.get(arr, bufOffset, count);
                buf = arr;
            } else if (datatype == MPI.DOUBLE) {
                DoubleBuffer ibuf = bbuf.asDoubleBuffer();
                double[] arr = (double[]) buf;
                ibuf.get(arr, bufOffset, count);
                buf = arr;
            } else if (datatype == MPI.LONG) {
                LongBuffer ibuf = bbuf.asLongBuffer();
                long[] arr = (long[]) buf;
                ibuf.get(arr, bufOffset, count);
                buf = arr;
            } else if (datatype == MPI.FLOAT) {
                FloatBuffer ibuf = bbuf.asFloatBuffer();
                float[] arr = (float[]) buf;
                ibuf.get(arr, bufOffset, count);
                buf = arr;
            } else if (datatype == MPI.SHORT) {
                ShortBuffer ibuf = bbuf.asShortBuffer();
                short[] arr = (short[]) buf;
                ibuf.get(arr, bufOffset, count);
                buf = arr;
            } else if (datatype == MPI.BYTE) {
                // no need to do anything
                ByteBuffer ibuf = (ByteBuffer) buf;
                byte[] arr = (byte[]) buf;
                ibuf.get(arr, bufOffset, count);
                buf = arr;
            } else {
                // do something
            }
        } catch (ClosedByInterruptException ex) {
            System.out.println("Exception: Channel closed by intterupt");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (AsynchronousCloseException ex) {
            System.out.println("Exception: Channel Closed Asynchronusly");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (ClosedChannelException ex) {
            System.out.println("Exception: Channel is closed");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (IllegalArgumentException ex) {
            System.out.println("Exception: Illegal Arguments");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (NonReadableChannelException ex) {
            System.out.println("Exception: Non Readable Channel ");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (IOException ex) {
            System.out.println("IO Exception");
            System.out.println("Stack Trace is " + ex.toString());
        }
        return status;
    }

    /**
     * Write data on file collectively Java binding for the MPI operation
     * MPI_FILE_WRITE_ALL
     *
     * @param buf buffer object
     * @param bufOffset the buffer offset
     * @param count number of elements in buffer
     * @param datatype datatype of each buffer element
     * @return returns the status object
     */
    public Status writeAll(Object buf, int bufOffset, int count, Datatype datatype) {
        MPI.COMM_WORLD.Barrier();
        Status status = new Status(MPI.ANY_SOURCE, MPI.ANY_TAG, count, count);
        ByteBuffer bbuf = ByteBuffer.allocate(count * 4);
        
        if (datatype == MPI.INT) {
            IntBuffer ibuf = bbuf.asIntBuffer();
            int[] arr = (int[]) buf;
            ibuf.put(arr, bufOffset, count);
        } else if (datatype == MPI.CHAR) {
            CharBuffer ibuf = bbuf.asCharBuffer();
            char[] arr = (char[]) buf;
            ibuf.put(arr, bufOffset, count);
        } else if (datatype == MPI.DOUBLE) {
            DoubleBuffer ibuf = bbuf.asDoubleBuffer();
            double[] arr = (double[]) buf;
            ibuf.put(arr, bufOffset, count);
        } else if (datatype == MPI.LONG) {
            LongBuffer ibuf = bbuf.asLongBuffer();
            long[] arr = (long[]) buf;
            ibuf.put(arr, bufOffset, count);
        } else if (datatype == MPI.FLOAT) {
            FloatBuffer ibuf = bbuf.asFloatBuffer();
            float[] arr = (float[]) buf;
            ibuf.put(arr, bufOffset, count);
        } else if (datatype == MPI.SHORT) {
            ShortBuffer ibuf = bbuf.asShortBuffer();
            short[] arr = (short[]) buf;
            ibuf.put(arr, bufOffset, count); //ShortBuffer.wrap(arr); // arr, offset, length
        } else if (datatype == MPI.BYTE) {
            // no need to do anything
        } else {
            // do something
        }
        //why there are MPI.Float and MPI.Float2       
        try {
            fc.position(fc.position());
            //System.out.println("Locked from " + fc.position() + " to " + (fc.position()+count) );
            FileLock lock = fc.lock(fc.position(), count, false);
            status.count = fc.write(bbuf);
            sync();
            lock.release();
            if (atomicity) {
                sync();
                MPI.COMM_WORLD.Barrier();
                sync();
            }
            
        } catch (ClosedByInterruptException ex) {
            System.out.println("Exception: Channel closed by intterupt");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (AsynchronousCloseException ex) {
            System.out.println("Exception: Channel Closed Asynchronusly");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (ClosedChannelException ex) {
            System.out.println("Exception: Channel is closed");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (IllegalArgumentException ex) {
            System.out.println("Exception: Illegal Arguments");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (NonWritableChannelException ex) {
            System.out.println("Exception: Non Writeable Channel ");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (IOException ex) {
            System.out.println("IO Exception");
            System.out.println("Stack Trace is " + ex.toString());
        }
        
        
        
        return status;
    }
}

class WriteThread extends Thread {
    
    FileChannel fc;
    Object buf;
    int bufOffset;
    int count;
    Datatype datatype;
    public Status status;
    public boolean isDone;
    java.io.File file;
    Set<StandardOpenOption> hashset;
    
    public void SetParameters(FileChannel fileChannel, Object buf, int bufOffset, int count, Datatype datatype, java.io.File file, Set<StandardOpenOption> hashset) {
        this.fc = fileChannel;
        this.buf = buf;
        this.bufOffset = bufOffset;
        this.count = count;
        this.datatype = datatype;
        this.status = new Status();
        this.isDone = false;
        this.file = file;
        this.hashset = hashset;
    }
    
    @Override
    public void run() {
        status = new Status(MPI.ANY_SOURCE, MPI.ANY_TAG, count, count);
        System.out.println("Status" + status.count);
        if (this.fc.isOpen()) {
            System.out.println("FIle Channel open in Run");
        } else {
            try {
                fc = FileChannel.open(file.toPath(), hashset);
                System.out.println("File Channel OPen in Thread");
            } catch (IOException ex) {
                Logger.getLogger(WriteThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        ByteBuffer bbuf = ByteBuffer.allocate(count * 4);
        
        if (datatype == MPI.INT) {
            IntBuffer ibuf = bbuf.asIntBuffer();
            int[] arr = (int[]) buf;
            ibuf.put(arr, bufOffset, count);
            ibuf.flip();
        } else if (datatype == MPI.CHAR) {
            CharBuffer ibuf = bbuf.asCharBuffer();
            char[] arr = (char[]) buf;
            ibuf.put(arr, bufOffset, count);
            ibuf.flip();
        } else if (datatype == MPI.DOUBLE) {
            DoubleBuffer ibuf = bbuf.asDoubleBuffer();
            double[] arr = (double[]) buf;
            ibuf.put(arr, bufOffset, count);
            ibuf.flip();
        } else if (datatype == MPI.LONG) {
            LongBuffer ibuf = bbuf.asLongBuffer();
            long[] arr = (long[]) buf;
            ibuf.put(arr, bufOffset, count);
            ibuf.flip();
        } else if (datatype == MPI.FLOAT) {
            FloatBuffer ibuf = bbuf.asFloatBuffer();
            float[] arr = (float[]) buf;
            ibuf.put(arr, bufOffset, count);
            ibuf.flip();
        } else if (datatype == MPI.SHORT) {
            ShortBuffer ibuf = bbuf.asShortBuffer();
            short[] arr = (short[]) buf;
            ibuf.put(arr, bufOffset, count); //ShortBuffer.wrap(arr); // arr, offset, length
            ibuf.flip();
        } else if (datatype == MPI.BYTE) {
            // no need to do anything
        } else {
            // do something 
        }
        try {
            fc.position(fc.position());
            //System.out.println("Locked from " + fc.position() + " to " + (fc.position()+count) );
            FileLock lock = fc.lock(fc.position(), count, false);
            status.count = fc.write(bbuf);
            fc.force(true);
            lock.release();
            
        } catch (ClosedByInterruptException ex) {
            System.out.println("Exception: Channel closed by intterupt");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (AsynchronousCloseException ex) {
            System.out.println("Exception: Channel Closed Asynchronusly");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (ClosedChannelException ex) {
            System.out.println("Exception: Channel is closed");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (IllegalArgumentException ex) {
            System.out.println("Exception: Illegal Arguments");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (NonWritableChannelException ex) {
            System.out.println("Exception: Non Writeable Channel ");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (IOException ex) {
            System.out.println("IO Exception");
            System.out.println("Stack Trace is " + ex.toString());
        }
        System.out.println("Done");
        System.out.println("Status end" + status.count);
        isDone = true;
    }
}

class ReadThread extends Thread {
    
    FileChannel fc;
    Object buf;
    int bufOffset;
    int count;
    Datatype datatype;
    public Status status;
    public boolean isDone;
    java.io.File file;
    Set<StandardOpenOption> hashset;
    
    public void SetParameters(FileChannel fileChannel, Object buf, int bufOffset, int count, Datatype datatype, java.io.File file, Set<StandardOpenOption> hashset) {
        this.fc = fileChannel;
        this.buf = buf;
        this.bufOffset = bufOffset;
        this.count = count;
        this.datatype = datatype;
        this.status = new Status();
        this.isDone = false;
        this.file = file;
        this.hashset = hashset;
    }
    
    @Override
    public void run() {
        status = new Status(MPI.ANY_SOURCE, MPI.ANY_TAG, count, count);
        if (this.fc.isOpen()) {
            System.out.println("File Channel open in Run");
        } else {
            try {
                fc = FileChannel.open(file.toPath(), hashset);
                System.out.println("File Channel open in Thread");
            } catch (IOException ex) {
                Logger.getLogger(WriteThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        ByteBuffer bbuf = ByteBuffer.allocate(count * 4);
        try {
            //get position from get offset function and apply here to reading from file channel
            status.count = fc.read(bbuf);
            bbuf.flip();
            
            if (datatype == MPI.INT) {
                IntBuffer ibuf = bbuf.asIntBuffer();
                int[] arr = (int[]) buf;
                ibuf.get(arr, bufOffset, count);
                buf = arr;
            } else if (datatype == MPI.CHAR) {
                CharBuffer ibuf = bbuf.asCharBuffer();
                char[] arr = (char[]) buf;
                ibuf.get(arr, bufOffset, count);
                buf = arr;
            } else if (datatype == MPI.DOUBLE) {
                DoubleBuffer ibuf = bbuf.asDoubleBuffer();
                double[] arr = (double[]) buf;
                ibuf.get(arr, bufOffset, count);
                buf = arr;
            } else if (datatype == MPI.LONG) {
                LongBuffer ibuf = bbuf.asLongBuffer();
                long[] arr = (long[]) buf;
                ibuf.get(arr, bufOffset, count);
                buf = arr;
            } else if (datatype == MPI.FLOAT) {
                FloatBuffer ibuf = bbuf.asFloatBuffer();
                float[] arr = (float[]) buf;
                ibuf.get(arr, bufOffset, count);
                buf = arr;
            } else if (datatype == MPI.SHORT) {
                ShortBuffer ibuf = bbuf.asShortBuffer();
                short[] arr = (short[]) buf;
                ibuf.get(arr, bufOffset, count);
                buf = arr;
            } else if (datatype == MPI.BYTE) {
                // no need to do anything
                ByteBuffer ibuf = (ByteBuffer) buf;
                byte[] arr = (byte[]) buf;
                ibuf.get(arr, bufOffset, count);
                buf = arr;
            } else {
                // do something
            }
        } catch (ClosedByInterruptException ex) {
            System.out.println("Exception: Channel closed by intterupt");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (AsynchronousCloseException ex) {
            System.out.println("Exception: Channel Closed Asynchronusly");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (ClosedChannelException ex) {
            System.out.println("Exception: Channel is closed");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (IllegalArgumentException ex) {
            System.out.println("Exception: Illegal Arguments");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (NonReadableChannelException ex) {
            System.out.println("Exception: Non Readable Channel ");
            System.out.println("Stack Trace is " + ex.toString());
        } catch (IOException ex) {
            System.out.println("IO Exception");
            System.out.println("Stack Trace is " + ex.toString());
        }
        System.out.println("Done");
        isDone = true;
    }
}
