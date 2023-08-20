/* -*- Mode: C; c-basic-offset:4 ; -*- */
#include "mpi.h"
#include "mpio.h"  /* not necessary with MPICH 1.1.1 or HPMPI 1.4 */
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#define SIZE (65536*4*4*4)

/* Each process writes to separate files and reads them back. 
   The file name is taken as a command-line argument, and the process rank 
   is appended to it. */ 

int main(int argc, char **argv)
{
    int *buf, i, rank, nints, len,nprocs;
	double stim, read_tim, write_tim,new_write_tim,new_read_tim;
	double min_read_tim=10000000.0, min_write_tim=10000000.0, read_bw, write_bw;
    char *filename, *tmp;
    MPI_File fh;
    MPI_Status status;

    MPI_Init(&argc,&argv);
	MPI_Comm_size(MPI_COMM_WORLD, &nprocs);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);

/* process 0 takes the file name as a command-line argument and 
   broadcasts it to other processes */
    if (!rank) {
	i = 1;
	while ((i < argc) && strcmp("-fname", *argv)) {
	    i++;
	    argv++;
	}
	if (i >= argc) {
	    fprintf(stderr, "\n*#  Usage: simple -fname filename\n\n");
	    MPI_Abort(MPI_COMM_WORLD, 1);
	}
	argv++;
	len = strlen(*argv);
	filename = (char *) malloc(len+10);
	strcpy(filename, *argv);
	MPI_Bcast(&len, 1, MPI_INT, 0, MPI_COMM_WORLD);
	MPI_Bcast(filename, len+10, MPI_CHAR, 0, MPI_COMM_WORLD);
    }
    else {
	MPI_Bcast(&len, 1, MPI_INT, 0, MPI_COMM_WORLD);
	filename = (char *) malloc(len+10);
	MPI_Bcast(filename, len+10, MPI_CHAR, 0, MPI_COMM_WORLD);
    }
    

    buf = (int *) malloc(SIZE);
    nints = SIZE/sizeof(int);
    for (i=0; i<nints; i++) buf[i] = rank*100000 + i;

    /* each process opens a separate file called filename.'myrank' */
    tmp = (char *) malloc(len+10);
    strcpy(tmp, filename);
    sprintf(filename, "%s.%d", tmp, rank);

    MPI_File_open(MPI_COMM_SELF, filename, MPI_MODE_CREATE | MPI_MODE_RDWR,
		   MPI_INFO_NULL, &fh);
	stim = MPI_Wtime();
    MPI_File_write(fh, buf, nints, MPI_INT, &status);
	write_tim = MPI_Wtime() - stim;
    MPI_File_close(&fh);

    /* reopen the file and read the data back */

    for (i=0; i<nints; i++) buf[i] = 0;
    MPI_File_open(MPI_COMM_SELF, filename, MPI_MODE_CREATE | MPI_MODE_RDWR, 
                  MPI_INFO_NULL, &fh);
	stim = MPI_Wtime();
    MPI_File_read(fh, buf, nints, MPI_INT, &status);
	read_tim = MPI_Wtime() - stim;
    MPI_File_close(&fh);

	MPI_Allreduce(&write_tim, &new_write_tim, 1, MPI_DOUBLE, MPI_MAX,
		      MPI_COMM_WORLD);
	MPI_Allreduce(&read_tim, &new_read_tim, 1, MPI_DOUBLE, MPI_MAX,
		    MPI_COMM_WORLD);
    if (rank == 0) {
	read_bw = (SIZE*nprocs)/(new_read_tim*1024.0*1024.0);
	write_bw = (SIZE*nprocs)/(new_write_tim*1024.0*1024.0);
	fprintf(stderr, "Write bandwidth = %f Mbytes/sec\n", write_bw);
	fprintf(stderr, "Read bandwidth = %f Mbytes/sec\n", read_bw);
    }
	
    /* check if the data read is correct */
    for (i=0; i<nints; i++) 
	if (buf[i] != (rank*100000 + i)) 
	    fprintf(stderr, "Process %d: error, read %d, should be %d\n", rank, buf[i], rank*100000+i);

    if (!rank) fprintf(stderr, "Done\n");

    free(buf);
    free(filename);
    free(tmp);

    MPI_Finalize();
    return 0; 
}
