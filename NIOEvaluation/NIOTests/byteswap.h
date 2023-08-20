/*   $Archive:: /Ti/jni/byteswap.h                                         $  
 *      $Date:: 6/15/00 4:47p                                              $  
 *  $Revision:: 3                                                          $  
 *   Description: high-performance byte-swapping library                           
 *   Copyright 2000, Dan Bonachea <bonachea@cs.berkeley.edu> 
 *    see license.txt for usage information     
 */

#ifndef _include_byteswap_h_
#define _include_byteswap_h_

#include <jni.h>

/* fast in-place byte-swap
   sizeofelem = data type size (e.g. int = 4)
   numofelem = number of elements in the array */
void arrayByteSwap(jbyte *array, jint sizeofelem, jint numofelem);

/* fast byte-swap and copy operation 
   (can be used in some algorithms to remove one copy)
   sizeofelem = data type size (e.g. int = 4)
   numofelem = number of elements in the array */
void arrayCopyByteSwap(jbyte *toarray, const jbyte *fromarray, jint sizeofelem, jint numofelem);


#endif
