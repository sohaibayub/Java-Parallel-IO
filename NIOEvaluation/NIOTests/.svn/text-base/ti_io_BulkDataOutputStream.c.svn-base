/*   $Archive:: /Ti/jni/ti_io_BulkDataOutputStream.c                       $  
 *      $Date:: 6/15/00 4:47p                                              $  
 *  $Revision:: 8                                                          $  
 *   Description: bulk I/O native methods
 *   Copyright 2000, Dan Bonachea <bonachea@cs.berkeley.edu> 
 *    see license.txt for usage information     
 */

#include <jni.h>

#include "ti_io_BulkDataOutputStream.h"
#include "bulkiohelp.h"

static jmethodID writeMethodID = NULL; /* method ID cache */
static jboolean little_endian = JNI_FALSE;

JNIEXPORT void JNICALL Java_ti_io_BulkDataOutputStream_init (JNIEnv *env, jclass cls) {
  /* cache method ID to avoid the expensive lookup */
  /* we do this separately from the read/write methods to avoid a subtle inheritance bug */
  writeMethodID = (*env)->GetMethodID(env, cls, "write", "([BII)V");
  if (writeMethodID == NULL) return; /* exception - didn't find method */

  little_endian = isLittleEndian();
  if (little_endian) DEBUGMSG("platform is little-endian");
  else DEBUGMSG("platform is big-endian");
  }

/* this handy macro trick concisely covers all the possiblities in the absence of templates */
#define BULKIO_CLASSMANGLE ti_io_BulkDataOutputStream

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

