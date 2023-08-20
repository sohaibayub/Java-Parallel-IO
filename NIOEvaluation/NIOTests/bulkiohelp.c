/*   $Archive:: /Ti/jni/bulkiohelp.c                                       $  
 *      $Date:: 7/16/00 12:10a                                             $  
 *  $Revision:: 8                                                          $  
 *   Description: Helper functions for bulk I/O                               
 *   Copyright 2000, Dan Bonachea <bonachea@cs.berkeley.edu> 
 *    see license.txt for usage information     
 */

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>

#include "bulkiohelp.h"

JavaVM* vm = NULL;
jint jniversion = JNI_VERSION_1_1;
jboolean usecritical = JNI_FALSE;

JNIEnv* getJNIEnv() { /* returns the currently active environment (1.2 only) */
  JNIEnv* penv;
  #ifdef JNI_VERSION_1_2
    jint result = (*vm)->GetEnv(vm, (void**)&penv, JNI_VERSION_1_2);
  #else
    jint result = (*vm)->AttachCurrentThread(vm, &penv, NULL);
  #endif
  assert1(result == JNI_OK,"failed to get active environment");
  return penv;
  }


JNIEXPORT jint JNI_OnLoad(JavaVM* activevm, void* reserved) {
  /* only gets called in 1.2 and higher */
  vm = activevm;
  DEBUGMSG("native lib loaded.");

  jniversion = (*getJNIEnv())->GetVersion(getJNIEnv());
  DEBUGMSG2("JNI Version: %i.%i",jniversion>>16,jniversion&0xFFFF);
  if (jniversion > JNI_VERSION_1_1) usecritical = JNI_TRUE; 
  #ifdef NO_CRITICAL
  usecritical = JNI_FALSE;
  #endif

  return jniversion;
  }

JNIEXPORT void JNI_OnUnload(JavaVM* activevm, void* reserved) {
  /* only gets called in 1.2 and higher */
  DEBUGMSG("native lib unloaded.");
  return;
  }

void _fail(char* pred, char* file, int line, char* extramsg) {
  char msg[255]; 
  sprintf(msg, "Failed native assertion '%s' at %s(%i)%s%s\nBailing out...\n", 
          pred, file, line, extramsg?": ":"", extramsg?extramsg:""); 
  fprintf(stderr, msg); 
  fflush(stderr); 
  (*getJNIEnv())->FatalError(getJNIEnv(), msg); 
  }

/* return true if this is a little-endian machine, false if is big-endian */

jboolean isLittleEndian() {
  union tempunion {
    int i;                  /* machine word */
    jbyte b[sizeof(int)];    /* b[0] overlaid with first byte of i */
    } x; 
  x.i = 0x0F;    /* set lsb, zero all others */
  return x.b[0] != 0;
  }

/* perform a read on a file descriptor, 
   return 0 if exn thrown or non-zero for success */

int jread(int fd, jbyte *dataptr, int datalen) {
	while (datalen) {
		int retval = read(fd, dataptr, datalen);
    if (retval == 0) { /* EOF */
      throwIOError("java/io/EOFException","");
      return 0;
      }
    else if (retval < 0 && errno != EINTR) {
      throwIOError("java/io/IOException", "");
      return 0;
      }
    else { /* could be a partial result */
      datalen -= retval;
      dataptr += retval;
      }
	  }
  return 1; /* success */
	}

int jwrite(int fd, jbyte *dataptr, int datalen) {
	while (datalen) {
		int retval = write(fd, dataptr, datalen);
    if (retval < 0 && errno != EINTR) {
      throwIOError("java/io/IOException", "");
      return 0;
      }
    else { /* could be a partial result */
      datalen -= retval;
      dataptr += retval;
      }
	  }
  return 1; /* success */
  }

/* throws an I/O Error after an OS read/write call fails */

void throwIOError(char *classname, char *message) {
  if (classname == NULL) classname = "java/io/IOException";
  if (message == NULL) message = "I/O Error";
  {
    JNIEnv *env = getJNIEnv();
    jclass exnclass = (*env)->FindClass(env, classname);
    if (exnclass == NULL) { /* failed to find exn */
      classname = "java/io/IOException"; /* try again */
      exnclass = (*env)->FindClass(env, classname);
      if (exnclass == NULL) return; /* something is badly wrong - give up */
      }
    (*env)->ThrowNew(env, exnclass, message);
    }
  }
