  import mpi.*;
  import java.util.Arrays;
  import java.io.IOException;
  
  class Perf{

      public static void main(String args[]) throws IOException {

          
          int SIZE = 1024*1024*256;
          int[] buf = new int[SIZE];
          int i, j, nprocs, mynod, ntimes = 5;
          double stim;
          double[] read_tim = new double[1];
          double[] write_tim = new double[1];
          double[] new_read_tim = new double[1];
          double[] new_write_tim = new double[1]; 
          new_write_tim[0] = 0.0;
          read_tim[0] = 0.0;
          write_tim[0] = 0.0; 
          double[] min_read_tim = new double[1];
          min_read_tim[0] = 10000000.0;
          double[] min_write_tim = new double [1];
          min_write_tim[0] = 10000000.0; 
          double read_bw, write_bw;
	  String  filename = null;	
          MPI.Init(args);
          mynod = MPI.COMM_WORLD.Rank();
          nprocs = MPI.COMM_WORLD.Size();

         // if(mynod == 0) {
                    
          System.out.println("...............Test started.................");

          if(Arrays.asList(args).contains("-fname")) {
              filename = args[Arrays.asList(args).indexOf("-fname") + 1];
              System.out.println("File Name = " + filename);
              }
          else{
              System.out.println("mpjrun.sh ..... Perf.c -fname filename ");
              System.exit(0);
              } 

	  for(i = 0; i < SIZE; i++) {
              buf[i] = i;
          }    
          for(j=0; j<ntimes ; j++){
              
              //stem.out.println(mynod +" "+MPI.COMM_WORLD.Rank());
              MPI.COMM_WORLD.Barrier(); 
              File file = MPI.COMM_WORLD.fileOpen(filename, MPI.MODE.RDWR | MPI.MODE.CREATE, MPI.INFO_NULL);
              file.seek(new Offset((SIZE/nprocs) * mynod), 0);
              MPI.COMM_WORLD.Barrier();
          
              stim = System.nanoTime();
          //System.out.println(SIZE/nprocs * mynod +" is the offset\n "+ SIZE/nprocs + "is no of integers written \n "+ buf.length*4);
          //System.out.println(file.getPosition().getOffsetlength()); 
             
              file.write(buf, SIZE/nprocs * mynod, SIZE/nprocs, MPI.INT);
              
              write_tim[0] = System.nanoTime()-stim;
          
              MPI.COMM_WORLD.fileClose(file);
         
              MPI.COMM_WORLD.Barrier();
          
              file = MPI.COMM_WORLD.fileOpen(filename, MPI.MODE.RDWR | MPI.MODE.CREATE, MPI.INFO_NULL);
              file.seek(new Offset((SIZE/nprocs) * mynod), 0);
              MPI.COMM_WORLD.Barrier();
          
              stim = System.nanoTime();      
              file.read(buf, SIZE/nprocs * mynod, SIZE/nprocs, MPI.INT);
          //System.out.println( SIZE/nprocs + "is no of integers read" );
          //MPI.COMM_WORLD.fileClose(file);        
              read_tim[0] = System.nanoTime()-stim;
              MPI.COMM_WORLD.fileClose(file);
              try{
                  MPI.COMM_WORLD.Allreduce(write_tim, 0, new_write_tim, 0, 1, MPI.DOUBLE, MPI.MAX);
                  MPI.COMM_WORLD.Allreduce(read_tim, 0, new_read_tim, 0, 1, MPI.DOUBLE, MPI.MAX);
                  min_read_tim[0] = (new_read_tim[0] < min_read_tim[0]) ? new_read_tim[0] : min_read_tim[0];
                  min_write_tim[0] = (new_write_tim[0] < min_write_tim[0]) ? new_write_tim[0] : min_write_tim[0];
          
              }catch(Exception e){
                  System.out.println(e);
              }
              }
              if(mynod == 0){
                  read_bw = (SIZE)/(min_read_tim[0]*1024.0*1024.0);
                  write_bw = (SIZE)/(min_write_tim[0]*1024.0*1024.0);
                  System.out.println("Write bandwidth without file sync ="+ write_bw*1000000000.0+ " Mbytes/sec\n");
                  System.out.println("Read bandwidth without prior file sync =" + read_bw*1000000000.0 + " Mbytes/sec\n");

              }   
              
              min_write_tim[0] = 10000000.0;
              min_read_tim[0] = 10000000.0;

          
              for(j=0; j<ntimes; j++){

                  File file2 = MPI.COMM_WORLD.fileOpen(filename, MPI.MODE.RDWR | MPI.MODE.CREATE, MPI.INFO_NULL);
                  file2.seek(new Offset((SIZE/nprocs) * mynod), 0);
                  MPI.COMM_WORLD.Barrier();
                  stim = System.nanoTime();
                  file2.write(buf, SIZE/nprocs * mynod, SIZE/nprocs, MPI.INT);
                  file2.sync();
                  write_tim[0] = System.nanoTime()-stim;
          

                  MPI.COMM_WORLD.fileClose(file2);

                  MPI.COMM_WORLD.Barrier();
                  file2 = MPI.COMM_WORLD.fileOpen(filename, MPI.MODE.RDWR | MPI.MODE.CREATE, MPI.INFO_NULL);
                  file2.seek(new Offset((SIZE/nprocs) * mynod), 0);
                  MPI.COMM_WORLD.Barrier();

                  stim = System.nanoTime();
                  file2.read(buf, SIZE/nprocs * mynod, SIZE/nprocs, MPI.INT);
              //file2.sync();
                  read_tim[0] = System.nanoTime()-stim;
                  MPI.COMM_WORLD.fileClose(file2);
                  try{
                      MPI.COMM_WORLD.Allreduce(write_tim, 0, new_write_tim, 0, 1, MPI.DOUBLE, MPI.MAX);
                      MPI.COMM_WORLD.Allreduce(read_tim, 0, new_read_tim, 0, 1, MPI.DOUBLE, MPI.MAX);
                      min_read_tim[0] = (new_read_tim[0] < min_read_tim[0]) ? new_read_tim[0] : min_read_tim[0];
                      min_write_tim[0] = (new_write_tim[0] < min_write_tim[0]) ? new_write_tim[0] : min_write_tim[0];
   
          
                  }catch(Exception e){
                
                      System.out.println(e);
                  }
                  }
                  if(mynod == 0){
            //if (flag) System.out.println("MPI_File_sync returns error.\n");
        //else {
                      read_bw = (SIZE)/(min_read_tim[0]*1024.0*1024.0);
                      write_bw = (SIZE)/(min_write_tim[0]*1024.0*1024.0);
                      System.out.println( "Write bandwidth including file sync =" + write_bw*1000000000.0 +" Mbytes/sec\n");
                      System.out.println("Read bandwidth after file sync ="+ read_bw*1000000000.0 +" Mbytes/sec\n");
                  }  

              MPI.Finalize();   
   }
}
