/*   $Archive:: /Ti/jni/ti_io_BulkRandomAccessFile.c                       $  
 *      $Date:: 8/26/00 1:57a                                              $  
 *  $Revision:: 7                                                          $  
 *   Description: bulk I/O native methods
 *   Copyright 2000, Dan Bonachea <bonachea@cs.berkeley.edu> 
 *    see license.txt for usage information     
 */

#include <jni.h>

#include "ti_io_BulkRandomAccessFile.h"
#include "bulkiohelp.h"

#define FILE_IS_RAF 1

static jmethodID readMethodID = NULL; /* method ID cache */
static jmethodID writeMethodID = NULL; /* method ID cache */
static jboolean little_endian = JNI_FALSE;

#ifndef NO_RAFDIRECT 
  jboolean userafdirect = JNI_FALSE;
  /* ID cache for RAFDIRECT */
  static jmethodID RAFgetFDmethodID = NULL;
  static jfieldID FDFDfieldID = NULL;
#endif

JNIEXPORT void JNICALL Java_ti_io_BulkRandomAccessFile_init (JNIEnv *env, jclass cls) {
  /* cache method ID to avoid the expensive lookup */
  /* we do this separately from the read/write methods to avoid a subtle inheritance bug */
  readMethodID = (*env)->GetMethodID(env, cls, "readFully", "([BII)V");
  if (readMethodID == NULL) return; /* exception - didn't find method */
  writeMethodID = (*env)->GetMethodID(env, cls, "write", "([BII)V");
  if (writeMethodID == NULL) return; /* exception - didn't find method */

  #ifndef NO_RAFDIRECT 
  do { /* do this carefully and disable RAFDIRECT if anything fails here */
    jclass RAFclass, FDclass;
    RAFclass = (*env)->FindClass(env, "java/io/RandomAccessFile");
    if (RAFclass == NULL) {
      DEBUGMSG("RAFDIRECT failed while getting RAFclass. RAFDIRECT disabled.");
      (*env)->ExceptionClear(env);
      userafdirect = JNI_FALSE;
      break;
      }
    /*RAFFDfieldID = (*env)->GetFieldID(env, RAFclass, "fd", "Ljava/io/FileDescriptor;");*/
    RAFgetFDmethodID = (*env)->GetMethodID(env, RAFclass, "getFD", "()Ljava/io/FileDescriptor;");
    if (RAFgetFDmethodID == NULL) {
      DEBUGMSG("RAFDIRECT failed while getting RAFgetFDmethodID. RAFDIRECT disabled.");
      (*env)->ExceptionClear(env);
      userafdirect = JNI_FALSE;
      break;
      }

    FDclass = (*env)->FindClass(env, "java/io/FileDescriptor");
    if (FDclass == NULL) {
      DEBUGMSG("RAFDIRECT failed while getting FDclass. RAFDIRECT disabled.");
      (*env)->ExceptionClear(env);
      userafdirect = JNI_FALSE;
      break;
      }
    FDFDfieldID = (*env)->GetFieldID(env, FDclass, "fd", "I");
    if (FDFDfieldID == NULL) {
      DEBUGMSG("RAFDIRECT failed while getting FDFDfieldID. RAFDIRECT disabled.");
      (*env)->ExceptionClear(env);
      userafdirect = JNI_FALSE;
      break;
      }
    userafdirect = JNI_TRUE;
    DEBUGMSG("RAFDIRECT is enabled");
    } while (0);
  #endif
  
  little_endian = isLittleEndian();
  if (little_endian) DEBUGMSG("platform is little-endian");
  else DEBUGMSG("platform is big-endian");
  }

/* this handy macro trick concisely covers all the possiblities in the absence of templates */
#define BULKIO_CLASSMANGLE ti_io_BulkRandomAccessFile

/* BulkDataInput methods */

#define BULKIO_PRIMTYPENAME   Boolean
#define BULKIO_PRIMTYPE       jboolean
#define BULKIO_ARGMANGLE      3ZII
#include "bulkioread.c"

#define BULKIO_PRIMTYPENAME   Char
#define BULKIO_PRIMTYPE       jchar
#define BULKIO_ARGMANGLE      3CII
#include "bulkioread.c"

#define PRIMTYPE_IS_JBYTE
#define BULKIO_PRIMTYPENAME   Byte
#define BULKIO_PRIMTYPE       jbyte
#define BULKIO_ARGMANGLE      3BII
#include "bulkioread.c"
#undef PRIMTYPE_IS_JBYTE

#define BULKIO_PRIMTYPENAME   Short
#define BULKIO_PRIMTYPE       jshort
#define BULKIO_ARGMANGLE      3SII
#include "bulkioread.c"

#define BULKIO_PRIMTYPENAME   Int
#define BULKIO_PRIMTYPE       jint
#define BULKIO_ARGMANGLE      3III
#include "bulkioread.c"

#define BULKIO_PRIMTYPENAME   Long
#define BULKIO_PRIMTYPE       jlong
#define BULKIO_ARGMANGLE      3JII
#include "bulkioread.c"

#define BULKIO_PRIMTYPENAME   Float
#define BULKIO_PRIMTYPE       jfloat
#define BULKIO_ARGMANGLE      3FII
#include "bulkioread.c"

#define BULKIO_PRIMTYPENAME   Double
#define BULKIO_PRIMTYPE       jdouble
#define BULKIO_ARGMANGLE      3DII
#include "bulkioread.c"

/* BulkDataOutput methods */

#define BULKIO_PRIMTYPENAME   Boolean
#define BULKIO_PRIMTYPE       jboolean
#define BULKIO_ARGMANGLE      3ZII
#include "bulkiowrite.c"

#define BULKIO_PRIMTYPENAME   Char
#define BULKIO_PRIMTYPE       jchar
#define BULKIO_ARGMANGLE      3CII
#include "bulkiowrite.c"

#define PRIMTYPE_IS_JBYTE
#define BULKIO_PRIMTYPENAME   Byte
#define BULKIO_PRIMTYPE       jbyte
#define BULKIO_ARGMANGLE      3BII
#include "bulkiowrite.c"
#undef PRIMTYPE_IS_JBYTE

#define BULKIO_PRIMTYPENAME   Short
#define BULKIO_PRIMTYPE       jshort
#define BULKIO_ARGMANGLE      3SII
#include "bulkiowrite.c"

#define BULKIO_PRIMTYPENAME   Int
#define BULKIO_PRIMTYPE       jint
#define BULKIO_ARGMANGLE      3III
#include "bulkiowrite.c"

#define BULKIO_PRIMTYPENAME   Long
#define BULKIO_PRIMTYPE       jlong
#define BULKIO_ARGMANGLE      3JII
#include "bulkiowrite.c"

#define BULKIO_PRIMTYPENAME   Float
#define BULKIO_PRIMTYPE       jfloat
#define BULKIO_ARGMANGLE      3FII
#include "bulkiowrite.c"

#define BULKIO_PRIMTYPENAME   Double
#define BULKIO_PRIMTYPE       jdouble
#define BULKIO_ARGMANGLE      3DII
#include "bulkiowrite.c"
