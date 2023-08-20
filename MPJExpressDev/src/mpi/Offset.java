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
 * File         : Offset.java
 * Author       : Sohaib Ayub
 * Created      : Thr Apr 19 2012
 * Revision     : $Revision: 1.00 $
 * Updated      : $Date: 2012/04/13 17:24:47 $
 */
package mpi;

/**
 * offset class
 * @author sohaib
 */
public class Offset {

    private long offset;

    /**
     * Default constructor
     */
    public Offset() {
        offset = 0;
    }

    /**
     * parameterized constructor
     *
     * @param offset long offset for file
     */
    public Offset(long offset) {
        this.offset = offset;
    }

    /**
     * Sets the offset object
     *
     * @param off offset object
     */
    public void setOffset(Offset off) {
        this.offset = off.getOffsetValue();
    }

    /**
     * return the offset of offset object
     *
     * @return long offset
     */
    public long getOffsetValue() {
        return this.offset;
    }

    /**
     * returns offset object
     *
     * @return offset object
     */
    public Offset getOffset() {
        return this;
    }
}