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
 * File         : View.java
 * Author       : Sohaib Ayub
 * Created      : Tues May 1 2012
 * Revision     : $Revision: 1.00 $
 * Updated      : $Date: 2012/05/01 12:39:47 $
 */
package mpi;

/**
 * View class for storing view information
 *
 * @author sohaib
 */
public class View {

    private Offset disp;
    private Datatype etype,filetype;
    private String datarep;
    
    /**
     * Sets the view
     *
     * @param disp displacement
     * @param etype elementary datatype object (mpi.Datatype)
     * @param filetype file type object (mpi.Datatype)
     * @param datarep data representation
     */
    public void setView(Offset disp, Datatype etype, Datatype filetype, String datarep) {
        this.disp=disp;
        this.etype=etype;
        this.filetype=filetype;
        this.datarep=datarep;
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
        disp=this.disp;
        etype=this.etype;
        filetype= this.filetype;
        datarep=new StringBuffer(this.datarep);
    }
}
