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
 * File         : Info.java
 * Author       : Sohaib Ayub
 * Created      : Thr Apr 19 2012
 * Revision     : $Revision: 1.00 $
 * Updated      : $Date: 2012/04/13 17:24:47 $
 */
package mpi;

/**
 * info class
 * @author sohaib
 */
public class Info {

    private String information;
    private Info info;
    /**
     * Default constructor
     */
    public Info() {
          information="nothing";
    }

    /**
     * parameterized constructor
     *
     * @param infomation string information
     */
    public Info(String infomation) {
        this.information = infomation;
    }

    /**
     * Sets the information
     *
     * @param info information
     */
    public void setInfo(Info info) {
        this.info = info;
        this.information = info.getInfoValue();
    }

    /**
     * returns the string information
     *
     * @return string containing information
     */
    public String getInfoValue() {
        return information;
    }

    /**
     * gets the information
     *
     * @return information
     */
    public Info getInfo() {
        return info;
    }
}
// info function from ROMIO implementation
/*
int MPI_Info_create(MPI_Info *info);
int MPI_Info_set(MPI_Info info, char *key, char *value);
int MPI_Info_delete(MPI_Info info, char *key);
int MPI_Info_get(MPI_Info info, char *key, int valuelen, 
                         char *value, int *flag);
int MPI_Info_get_valuelen(MPI_Info info, char *key, int *valuelen, 
                                  int *flag);
int MPI_Info_get_nkeys(MPI_Info info, int *nkeys);
int MPI_Info_get_nthkey(MPI_Info info, int n, char *key);
int MPI_Info_dup(MPI_Info info, MPI_Info *newinfo);
int MPI_Info_free(MPI_Info *info);
*/
