/*   $Archive:: /Ti/jni/bulkiohelp.h                                       $  
 *      $Date:: 6/15/00 4:47p                                              $  
 *  $Revision:: 4                                                          $  
 *   Description: Helper functions for bulk I/O                               
 *   Copyright 2000, Dan Bonachea <bonachea@cs.berkeley.edu> 
 *    see license.txt for usage information     
 */

#include <sys/param.h> /* LITTLE_ENDIAN or BIG_ENDIAN */

#include "jni.h"

#ifndef _BULKIOHELP_H
#define _BULKIOHELP_H

extern JavaVM* vm;
extern jint jniversion;
extern jboolean usecritical;

JNIEnv* getJNIEnv();  /* returns the currently active environment */

jboolean isLittleEndian();

int jread(int fd, jbyte *dataptr, int datalen);
int jwrite(int fd, jbyte *dataptr, int datalen);
void throwIOError(char *classname, char *message);

JNIEXPORT jint JNI_OnLoad(JavaVM*, void* );
JNIEXPORT void JNI_OnUnload(JavaVM*, void*);

#define DEBUGSTREAM stderr
#ifdef NDEBUG
  #define DEBUGMSG(s)
  #define DEBUGMSG1(s,a1)
  #define DEBUGMSG2(s,a1,a2)
  #define DEBUGMSG3(s,a1,a2,a3)
  #define assert(p)
  #define assert1(p,msg)
#else
  #define _DEBUGMSG(a) do { fprintf a; fprintf(DEBUGSTREAM, "\n"); fflush(DEBUGSTREAM); } while(0)
  #define DEBUGMSG(s) _DEBUGMSG((DEBUGSTREAM, s))
  #define DEBUGMSG1(s,a1) _DEBUGMSG((DEBUGSTREAM, s, a1))
  #define DEBUGMSG2(s,a1,a2) _DEBUGMSG((DEBUGSTREAM, s, a1, a2))
  #define DEBUGMSG3(s,a1,a2,a3) _DEBUGMSG((DEBUGSTREAM, s, a1, a2, a3))
  #define assert(p) if (!(p)) { _fail( #p, __FILE__, __LINE__, NULL); }
  #define assert1(p,msg) if (!(p)) { _fail( #p, __FILE__, __LINE__, msg); }
  void _fail(char* pred, char* file, int line, char* extramsg);
#endif

#ifndef JNI_VERSION_1_1
#define JNI_VERSION_1_1 0x00010001
#endif

#endif /* _BULKIOHELP_H */
