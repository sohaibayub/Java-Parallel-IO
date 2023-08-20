/*   $Archive:: /Ti/jni/bulkiowrite.c                                      $  
 *      $Date:: 6/15/00 4:47p                                              $  
 *  $Revision:: 11                                                         $  
 *   Description: this is a macrofied function file - not meant to be compiled directly                           
 *   Copyright 2000, Dan Bonachea <bonachea@cs.berkeley.edu> 
 *    see license.txt for usage information     
 */

#include "byteswap.h"

#if !defined(BULKIO_PRIMTYPENAME) || !defined(BULKIO_PRIMTYPE) || !defined(BULKIO_ARGMANGLE) || !defined(BULKIO_CLASSMANGLE) 
  #error this file is not meant to be compiled directly, and requires certain constants be defined before inclusion
#endif

/* helper macros */
#ifndef _BULKIOWRITE_MACROS_H
  #define _BULKIOWRITE_MACROS_H
  #define CONC_HELPER(a,b) a ## b    
  #define CONC(a,b) CONC_HELPER(a,b)
  #define STRINGIFY_HELPER(x) #x
  #define STRINGIFY(x) STRINGIFY_HELPER(x)
#endif

/* auxilliary identifiers */
#define BULKIO_FUNCNAME  CONC(Java_,CONC(BULKIO_CLASSMANGLE,CONC(_writeArray0___,BULKIO_ARGMANGLE)))
#define BULKIO_ARRAYTYPE CONC(BULKIO_PRIMTYPE,Array)
#define BULKIO_GETELEMFUNC CONC(Get,CONC(BULKIO_PRIMTYPENAME,ArrayElements))
#define BULKIO_RELEASEELEMFUNC CONC(Release,CONC(BULKIO_PRIMTYPENAME,ArrayElements))

/* decide which optimizations we're using on this file */
#ifdef RAFDIRECT
#undef RAFDIRECT
#endif
#if !defined(NO_RAFDIRECT) && defined(FILE_IS_RAF)
#define RAFDIRECT 1
#else
#define RAFDIRECT 0
#endif

#ifdef DIRECTPASSTHRU
#undef DIRECTPASSTHRU
#endif
#if !defined(NO_DIRECTPASSTHRU) && defined(PRIMTYPE_IS_JBYTE)
#define DIRECTPASSTHRU 1
#else
#define DIRECTPASSTHRU 0
#endif

JNIEXPORT void JNICALL BULKIO_FUNCNAME
(JNIEnv *env, jobject thisobj, BULKIO_ARRAYTYPE array, jint offset, jint length) {
  DEBUGMSG("Starting " STRINGIFY(BULKIO_FUNCNAME) "()");

  assert1(env != NULL && *env != NULL,"got a null environment");

#if DIRECTPASSTHRU
  {
  /* pass byte arrays through directly */
  DEBUGMSG("Peforming shortcut upcall to write(byte[])");

  /* use cached method ID to avoid the expensive lookup */
  (*env)->CallVoidMethod(env, thisobj, writeMethodID, array, offset, length);
  if ((*env)->ExceptionOccurred(env)) return; /* method threw an exception */
  DEBUGMSG("upcall returned.");
    }
#else
  {
  BULKIO_PRIMTYPE* datasrc;
  jbyte* datadest;
  jsize arraylength;
  jsize bytelength;
  jsize byteoffset;
  jbyteArray bytearray;
  jboolean iscopysrc = JNI_FALSE;
  jboolean iscopydest = JNI_FALSE;
  jboolean entirearray = JNI_FALSE;
  const jboolean byteswap = little_endian && (sizeof(BULKIO_PRIMTYPE) > 1);

  /* null object check & null array check */
  if (thisobj == NULL || array == NULL) {
    jclass nullptrexnclass = (*env)->FindClass(env, "java/lang/NullPointerException");
    if (nullptrexnclass == NULL) return; /* failed to find exn */
    (*env)->ThrowNew(env, nullptrexnclass, "encountered null pointer in writeArray()");
    return; /* throw exception */
    }

  arraylength = (*env)->GetArrayLength(env, array);
  
  /* array bounds check */
  if (offset < 0 || length < 0 || offset+length > arraylength) {
    jclass arrayboundsexnclass = (*env)->FindClass(env, "java/lang/IndexOutOfBoundsException");
    if (arrayboundsexnclass == NULL) return; /* failed to find exn */
    (*env)->ThrowNew(env, arrayboundsexnclass, "array bounds error in writeArray()");
    return; /* throw exception */
    }

  /* determine whether or not we're using a sub-array */
  if (offset == 0 && length == arraylength) entirearray = JNI_TRUE;
  else entirearray = JNI_FALSE;

  if (entirearray) ; /* avoid a silly warning about unused var. remove later once we actually use it */

  if (length == 0) return; /* silly corner case */

  byteoffset = ((int)offset)*sizeof(BULKIO_PRIMTYPE);
  bytelength = ((int)length)*sizeof(BULKIO_PRIMTYPE);
#if RAFDIRECT
if (userafdirect && !byteswap) { /* RAFDIRECT writes save nothing if we must byteswap */
  /* get the file descriptor */
  jobject fdobj;
  jint fd;
  int retval;

  DEBUGMSG("Getting FD for RAFDIRECT");
  /* fdobj = (*env)->GetObjectField(env, thisobj, RAFFDfieldID); */
  fdobj = (*env)->CallObjectMethod(env, thisobj, RAFgetFDmethodID);
  if (fdobj == NULL) {
    jclass internalerrorclass = (*env)->FindClass(env, "java/lang/InternalError");
    if (internalerrorclass == NULL) return; /* failed to find exn */
    (*env)->ThrowNew(env, internalerrorclass, "failed to get FileDescriptor from RandomAccessFile.getFD()");
    return; /* exception thrown */
    }
  fd = (*env)->GetIntField(env, fdobj, FDFDfieldID);

  /* get a pointer to the element destination */
  #ifdef JNI_VERSION_1_2
  if (usecritical) { /* this is the critical region */
    /* get the destination java array elements, possibly copying */
    datasrc = (*env)->GetPrimitiveArrayCritical(env, array, &iscopysrc);
    if (datasrc == NULL) {
      return; /* exception thrown */
      }
    DEBUGMSG1("GetPrimitiveArrayCritical(src) gave us a %s", iscopysrc?"copy":"reference");

    /* never need to byteswap for RAFDIRECT writes */

    /* perform the write */
    retval = jwrite(fd, ((jbyte*)datasrc)+byteoffset, bytelength);

    /* release without writeback */
    (*env)->ReleasePrimitiveArrayCritical(env, array, datasrc, JNI_ABORT);      

    /* throw IO Exns */
    if (!retval) return;
    }
  else 
  #endif /* JNI_VERSION_1_2 */
  { /* don't use critical */
    /* get the destination java array elements, possibly copying */
    datasrc = (*env)->BULKIO_GETELEMFUNC(env, array, &iscopysrc); /* call Get<TYPE>ArrayElements */
    if (datasrc == NULL) {
      return; /* exception thrown */
      }
    DEBUGMSG1(STRINGIFY(BULKIO_GETELEMFUNC)"(src) gave us a %s", iscopysrc?"copy":"reference");

    /* never need to byteswap for RAFDIRECT writes */

    /* perform the write */
    retval = jwrite(fd, ((jbyte*)datasrc)+byteoffset, bytelength);

    /* release without writeback */
    (*env)->BULKIO_RELEASEELEMFUNC(env, array, datasrc, JNI_ABORT);/* call Release<TYPE>ArrayElements */
    
    /* throw IO Exns */
    if (!retval) return;
    }
  } /* userafdirect */
else
#endif
 { /* not RAFDIRECT - must use a temporary array */

  /* create byte array - MUST be outside the critical region*/
  bytearray = (*env)->NewByteArray(env, bytelength);
  if (bytearray == NULL) return; /* exception thrown */
  
  /* ---------------------------------------------------------------------*/
  #ifdef JNI_VERSION_1_2
  if (usecritical) { /* this is the critical region */
    /* get the java array elements, possibly copying */
    datasrc = (*env)->GetPrimitiveArrayCritical(env, array, &iscopysrc);
    if (datasrc == NULL) return; /* exception thrown */
    DEBUGMSG1("GetPrimitiveArrayCritical(src) gave us a %s", iscopysrc?"copy":"reference");

    /* get the byte array elements, possibly copying */
    datadest = (*env)->GetPrimitiveArrayCritical(env, bytearray, &iscopydest);
    if (datadest == NULL) { /* exception thrown */
      (*env)->ReleasePrimitiveArrayCritical(env, array, datasrc, JNI_ABORT);
      return; 
      }
    DEBUGMSG1("GetPrimitiveArrayCritical(dest) gave us a %s", iscopydest?"copy":"reference");

    /* copy to byte array */
    if (iscopydest) { /* we're forced to let JNI do a copy */
      if (byteswap) {
        arrayCopyByteSwap(datadest, ((jbyte*)datasrc)+byteoffset, sizeof(BULKIO_PRIMTYPE), length);
        (*env)->ReleasePrimitiveArrayCritical(env, bytearray, datadest, 0);
        }
      else (*env)->ReleasePrimitiveArrayCritical(env, bytearray, ((jbyte*)datasrc)+byteoffset, 0);
      }
    else { /* we have direct access, so do the copy ourselves using memcpy, which may be faster */
      if (byteswap) arrayCopyByteSwap(datadest, ((jbyte*)datasrc)+byteoffset, sizeof(BULKIO_PRIMTYPE), length);
      else memcpy(datadest, ((jbyte*)datasrc)+byteoffset, bytelength);
      (*env)->ReleasePrimitiveArrayCritical(env, bytearray, datadest, 0); /* won't copy */
      }

    /* release source handle - we changed nothing in this array, so don't copy back */
    (*env)->ReleasePrimitiveArrayCritical(env, array, datasrc, JNI_ABORT);
    }
  /* ---------------------------------------------------------------------*/
  else 
  #endif /* JNI version 1.2 */
  { /* don't use critical */
    /* get the java array elements, possibly copying */
    datasrc = (*env)->BULKIO_GETELEMFUNC(env, array, &iscopysrc); /* call Get<TYPE>ArrayElements */
    if (datasrc == NULL) return; /* exception thrown */
    DEBUGMSG1(STRINGIFY(BULKIO_GETELEMFUNC)"(src) gave us a %s", iscopysrc?"copy":"reference");

    /* get the byte array elements, possibly copying */
    datadest = (*env)->GetByteArrayElements(env, bytearray, &iscopydest);
    if (datadest == NULL) { /* exception thrown */
      (*env)->BULKIO_RELEASEELEMFUNC(env, array, datasrc, JNI_ABORT); /* call Release<TYPE>ArrayElements */
      return; 
      }
    DEBUGMSG1("GetByteArrayElements(dest) gave us a %s", iscopydest?"copy":"reference");

    /* copy to byte array */
    if (iscopydest) { /* we're forced to let JNI do the copy */
      if (byteswap) {
        arrayCopyByteSwap(datadest, ((jbyte*)datasrc)+byteoffset, sizeof(BULKIO_PRIMTYPE), length);
        (*env)->ReleaseByteArrayElements(env, bytearray, datadest, 0);
        }
      else (*env)->ReleaseByteArrayElements(env, bytearray, ((jbyte*)datasrc)+byteoffset, 0);
      }
    else { /* we have direct access, so do the copy ourselves using memcpy, which may be faster */
      if (byteswap) arrayCopyByteSwap(datadest, ((jbyte*)datasrc)+byteoffset, sizeof(BULKIO_PRIMTYPE), length);
      else memcpy(datadest, ((jbyte*)datasrc)+byteoffset, bytelength);
      (*env)->ReleaseByteArrayElements(env, bytearray, datadest, 0); /* won't copy */
      }

    /* release source handle - we changed nothing in this array, so don't copy back */
    (*env)->BULKIO_RELEASEELEMFUNC(env, array, datasrc, JNI_ABORT); /* call Release<TYPE>ArrayElements */
    }
  /* ---------------------------------------------------------------------*/
  /* now use our byte array to perform the upcall - MUST be outside the critical region */
  {
    DEBUGMSG("Peforming upcall to write(byte[])");

    /* use cached method ID to avoid the expensive lookup */
    (*env)->CallVoidMethod(env, thisobj, writeMethodID, bytearray, 0, bytelength);
    if ((*env)->ExceptionOccurred(env)) return; /* method threw an exception */
    DEBUGMSG("upcall returned.");
    }
  } /* userafdirect */
}
#endif /* DIRECTPASSTHRU */

  DEBUGMSG("Exiting " STRINGIFY(BULKIO_FUNCNAME) "()");
  return; 
  }

/* undef our args */
#undef BULKIO_PRIMTYPENAME
#undef BULKIO_PRIMTYPE
#undef BULKIO_ARGMANGLE
/*#undef BULKIO_CLASSMANGLE*/
  
/* undef our temporary macros */
#undef BULKIO_FUNCNAME
#undef BULKIO_ARRAYTYPE
#undef BULKIO_GETELEMFUNC
#undef BULKIO_RELEASEELEMFUNC
