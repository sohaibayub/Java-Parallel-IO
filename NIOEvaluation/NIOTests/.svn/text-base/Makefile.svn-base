#  $Archive:: /Ti/jni/Makefile                                           $
#     $Date:: 7/02/00 12:25p                                             $
# $Revision:: 22                                                         $
# Description: Makefile for Bulk I/O extensions
# Copyright 2000, Dan Bonachea - see license.txt for usage information

# instructions:
#  follow the 3 steps below to configure the package for your system,
#  then run a "gmake" (or "make") from the command line to compile the 
#  library class files, native libraries, and test programs.
#
#  If anything fails, your best bet is to start with the platform-specific
#  makefile, which may need some tuning for your specific system. 
#  The sticking point is usually getting the C compiler to spit out a 
#  dynamically-linkable ("shared") library in the format that your JVM expects.
#  You might consult your JVM docs for info on how to compile native methods for your system.

# *** Step 1: 
#  uncomment the line specifying a configuration closest to yours

include Makefile.x86.Linux
#include Makefile.SPARC.Solaris
#include Makefile.IBMSP2.AIX
#include Makefile.SGI.IRIX

# *** Step 2: 
# You need to define JAVA_HOME as the root directory where Java is installed
# this directory should have subdirectories called "include" and "bin"
# below are some examples for systems I use

# Redhat Linux
JAVA_HOME=/usr/java/latest
# Quad SP-2 1.1
#JAVA_HOME = /software/aix-4/com/packages/jdk-112
# Denali Origin 2000 1.2
#JAVA_HOME = /usr/java
# Solaris Berkeley SWW 1.2
#JAVA_HOME= /usr/sww/lang/java
# york.cs 1.1.6
#JAVA_HOME=/usr/java1.1
# Berkeley Millenium 1.1
#JAVA_HOME=/usr/local/src/jdk117_v3
# Berkeley Millenium 1.2
#JAVA_HOME=/usr/local/src/jdk1.2

# *** Step 3: 
#  uncomment one of the following lines to get an optimized or debug configuration

DEFS = -DNDEBUG $(PLATFORM_OPT_DEFS)
#DEFS = -DDEBUG $(PLATFORM_DEBUG_DEFS)

#---------------------------------------------------------------------------
# You shouldn't need to modify anything below here
#---------------------------------------------------------------------------

JAVA =  $(JAVA_PREFIX)  $(JAVA_HOME)/bin/java  $(JAVA_SUFFIX)  
JAVAC = $(JAVAC_PREFIX) $(JAVA_HOME)/bin/javac $(JAVAC_SUFFIX) 
JAVAH = $(JAVAH_PREFIX) $(JAVA_HOME)/bin/javah $(JAVAH_SUFFIX) 

JDKINCLUDE = $(JAVA_HOME)/include
JDKPLATFORMINCLUDE = $(JDKINCLUDE)/$(JDKPLATFORMINCLUDESUBDIR)

# JNI settings 
JAVADEFS= -ms100m -mx100m 
#JAVADEFS= -verbose:jni -Xcheck:jni -ms100m -mx100m 

LIBS=

.PHONY: all clean dist javadoc

COMMON_DEPS=bulkiohelp.c bulkiohelp.h byteswap.c byteswap.h Makefile

all: libti_io_BulkDataInputStream.so  \
     libti_io_BulkDataOutputStream.so \
		 libti_io_BulkRandomAccessFile.so \
     bulkddtest.class bulkraftest.class bulkperf.class
	@echo you can now run these test programs with: 
	@echo $(JAVA) ${JAVADEFS} -Djava.library.path=. bulkddtest [infilename] [type]
	@echo $(JAVA) ${JAVADEFS} -Djava.library.path=. bulkraftest [infilename] [type]
	@echo $(JAVA) ${JAVADEFS} -Djava.library.path=. bulkperf [infilename] [outfilename] [chunksizeinbytes]
	@echo "(you may first need to do a "setenv LD_LIBRARY_PATH ." if you're using Java 1.1)"

# these are for my usage as a distributor 
dist: javadoc
	rm -f dist.tar.gz
	tar -cvhf dist.tar dist && gzip dist.tar

javadoc:
	rm -Rf dist/doc/html/*
	mkdir -p dist/doc/html
	javadoc -author -d dist/doc/html -sourcepath . ti.io

# test programs

bulkddtest.class: bulkddtest.java
	$(JAVAC) bulkddtest.java

bulkraftest.class: bulkraftest.java
	$(JAVAC) bulkraftest.java

bulkperf.class: bulkperf.java
	$(JAVAC) bulkperf.java

# native dynamically-linked libraries

libti_io_BulkDataInputStream.so: ti_io_BulkDataInputStream.c ti_io_BulkDataInputStream.h bulkioread.c ${COMMON_DEPS}
	${CC} ${LIBOPTS} ${DEFS} -I${JDKINCLUDE} -I${JDKPLATFORMINCLUDE} ti_io_BulkDataInputStream.c bulkiohelp.c byteswap.c -o libti_io_BulkDataInputStream.so ${LIBS}
	${EXTRALDCMD}

libti_io_BulkDataOutputStream.so: ti_io_BulkDataOutputStream.c ti_io_BulkDataOutputStream.h bulkiowrite.c ${COMMON_DEPS}
	${CC} ${LIBOPTS} ${DEFS} -I${JDKINCLUDE} -I${JDKPLATFORMINCLUDE} ti_io_BulkDataOutputStream.c bulkiohelp.c byteswap.c -o libti_io_BulkDataOutputStream.so ${LIBS}
	${EXTRALDCMD}

libti_io_BulkRandomAccessFile.so: ti_io_BulkRandomAccessFile.c ti_io_BulkRandomAccessFile.h bulkioread.c bulkiowrite.c ${COMMON_DEPS}
	${CC} ${LIBOPTS} ${DEFS} -I${JDKINCLUDE} -I${JDKPLATFORMINCLUDE} ti_io_BulkRandomAccessFile.c bulkiohelp.c byteswap.c -o libti_io_BulkRandomAccessFile.so ${LIBS}
	${EXTRALDCMD}

# native code auto-gen headers

ti_io_BulkDataInputStream.h: ti/io/BulkDataInputStream.class
	$(JAVAH) -jni ti.io.BulkDataInputStream
	touch $@

ti_io_BulkDataOutputStream.h: ti/io/BulkDataOutputStream.class
	$(JAVAH) -jni ti.io.BulkDataOutputStream
	touch $@

ti_io_BulkRandomAccessFile.h: ti/io/BulkRandomAccessFile.class
	$(JAVAH) -jni ti.io.BulkRandomAccessFile
	touch $@

# library java class files

ti/io/BulkDataInputStream.class: ti/io/BulkDataInputStream.java ti/io/BulkDataInput.java
	$(JAVAC) ti/io/BulkDataInputStream.java

ti/io/BulkDataOutputStream.class: ti/io/BulkDataOutputStream.java ti/io/BulkDataOutput.java
	$(JAVAC) ti/io/BulkDataOutputStream.java

ti/io/BulkRandomAccessFile.class: ti/io/BulkRandomAccessFile.java  ti/io/BulkDataInput.java ti/io/BulkDataOutput.java
	$(JAVAC) ti/io/BulkRandomAccessFile.java

clean: 
	rm -f *.so *.class *.o ti/io/*.class so_locations core

