/*   $Archive:: /Ti/jni/byteswap.c                                         $  
 *      $Date:: 6/15/00 4:47p                                              $  
 *  $Revision:: 4                                                          $  
 *   Description: high-performance byte-swapping library                           
 *   Copyright 2000, Dan Bonachea <bonachea@cs.berkeley.edu> 
 *    see license.txt for usage information     
 */

#include "byteswap.h"

#include <assert.h>

/* define some types not defined by java */
#ifdef MISSING_INTTYPES_H
  /* real type header is missing, so make an educated guess */
  typedef unsigned long juint;
  typedef unsigned long long julong; 
#else
  #include <inttypes.h>
  typedef uint32_t juint;
  typedef uint64_t julong;
#endif



/* OK, I know this file is really ugly to read, but byte swapping could potentially
   be a significant bottleneck in the bulk I/O system for little-endian machines.
   So this module is written with a very careful eye towards memory access pattern
   and to give the C optimizer as much information as possible to help make this fast.
   Hopefully it will never need to be modified... :-)
   i486 has a fast BSWAP instruction that we can hopefully use through the --swabn() macros,
   but it's unclear whether GNU always provides this - we use it when it does.
   A further enhancement would detect the intel architecture and 
   use assembly directly to get the instruction we want.
   */
/*------------------------------------------------------------------------------------*/
/* try to use the BSWAP instruction on x86, if possible */
#ifdef HAVE_ASM_BYTEORDER_H
  #include <asm/byteorder.h>

  /* This portions is based on linux/byteorder/swab.h by Francois-Rene Rideau,
     with various bugs fixed */
  #ifdef __arch__swab16
    #define swab16 __arch__swab16
  #endif
  #ifdef __arch__swab32
    #define swab32 __arch__swab32
  #endif
  #ifdef __arch__swab64
    #define swab64 __arch__swab64
  #else
    #ifdef __arch__swab32 /* use the 32-bit BSWAP for 64-bit */
      __inline__ __const__ __u64 __myfswab64(__u64 x) {
	      __u32 h = x >> 32;
        __u32 l = x & ((1ULL<<32)-1);
        return (((__u64)__arch__swab32(l)) << 32) | ((__u64)(__arch__swab32(h)));
        }
      #define swab64 __myfswab64
    #endif
  #endif
#endif

/*  Make sure we have the primitives */
#ifndef swab16 
  #define swab16(a) ( (((jchar)(a)) << 8) |                            \
                        (((jchar)(a)) >> 8)     )
#endif
#ifndef swab32
  #define swab32(a) ((juint)(                                          \
                         (((juint)(a)) << 24) |                          \
                        ((((juint)(a)) & (juint)0x0000ff00UL) << 8) |    \
                        ((((juint)(a)) & (juint)0x00ff0000UL) >> 8) |    \
                         (((juint)(a)) >> 24)     ))
#endif
#ifndef swab64
  #define swab64(a) ((julong)(                                                    \
                         (((julong)(a)) << 56) |                                    \
                        ((((julong)(a)) & (julong)0x000000000000ff00ULL) << 40) |   \
                        ((((julong)(a)) & (julong)0x0000000000ff0000ULL) << 24) |   \
                        ((((julong)(a)) & (julong)0x00000000ff000000ULL) <<  8) |   \
                        ((((julong)(a)) & (julong)0x000000ff00000000ULL) >>  8) |   \
                        ((((julong)(a)) & (julong)0x0000ff0000000000ULL) >> 24) |   \
                        ((((julong)(a)) & (julong)0x00ff000000000000ULL) >> 40) |   \
                         (((julong)(a)) >> 56)     ))
#endif
/*------------------------------------------------------------------------------------*/
/* Tuning Parameters */

/* This parameter controls whether we do loop unrolling and 
   make all the explicit memory operations 64-bits wide 
   (may help performance on arrays of 16-bit and 32-bit values) */
#define USE_ARRAY_BSWAP_64MEMOPS 0

/* use the swab() function for the 16-bit conversion */
#define USE_SWAB_FOR_16BIT 1

/* This is for performance debugging */
#define DEBUG_SWAP_PERF 0

/*------------------------------------------------------------------------------------*/
#if USE_SWAB_FOR_16BIT
  #include <unistd.h>
#endif

#if DEBUG_SWAP_PERF
  #include <time.h>
  #include <stdio.h>
#endif

/*------------------------------------------------------------------------------------*/
/*  in-place local array byte swap */
void arrayByteSwap(jbyte * array, jint sizeofelem, jint numofelem) {
  #if DEBUG_SWAP_PERF
    clock_t starttime = clock();
  #endif
  switch (sizeofelem) { 
    case 2: { /*  16-bit (jchar = 16 bits) */
      assert(((juint)array) % 2 == 0);
      #if USE_SWAB_FOR_16BIT
        swab(array, array, numofelem*sizeofelem);
      #else
        #if USE_ARRAY_BSWAP_64MEMOPS
          /*  try to reduce memory accesses by making all explicit memory ops 64-bits wide */
      
          { /*  prolog */
            while (((juint)array) % 8 != 0) {
              *((jchar *)array) = swab16(*((jchar *)array));
              array += 2;
              numofelem--;
              }
            }
          { /*  main loop body */
            register julong a; 
            register julong b; 
            register julong * p = (julong *)array; 
            register julong * pend = p + (numofelem / 4);
            while (p < pend) {  
                a = *p;
                b = swab16((jchar)a);
                a >>= 16;
                b |= (((julong)swab16((jchar)a)) << 16);
                a >>= 16;
                b |= (((julong)swab16((jchar)a)) << 32);
                a >>= 16;
                b |= (((julong)swab16((jchar)a)) << 48);
                *(p++) = b;
                }
            { /*  epilog */
              jchar * pepilog = (jchar *)pend;
              int i; 
              for (i = 0; i < numofelem % 4; i++) *(pepilog++) = swab16((*pepilog));
              }
            }
        #else
          {
            register jchar a; 
            register jchar * p = (jchar *)array; 
            register jchar * pend = p + numofelem; 
            while (p < pend) {  
              a = *p;
              *(p++) = swab16(a);  
              }
            }
        #endif
      #endif
      break;
      }
    case 4: { /*  32-bit */
      assert(((juint)array) % 4 == 0);
      #if USE_ARRAY_BSWAP_64MEMOPS
        /*  try to reduce memory accesses by making all explicit memory ops 64-bits wide */
      
        { /*  prolog */
          if (((juint)array) % 8 != 0) {
            *((juint *)array) = swab32(*((juint *)array));
            array += 4;
            numofelem--;
            }
          }
        { /*  main loop body */
          register julong a; 
          register julong b; 
          register julong * p = (julong *)array; 
          register julong * pend = p + ( numofelem / 2); 
          while (p < pend) { 
              a = *p;
              b = swab32((juint)a);
              a >>= 32;
              b |= (((julong)swab32((juint)a)) << 32);
              *(p++) = b;
              }
          if (numofelem % 2)  { /*  epilog */
            *((juint *)pend) = swab32(*((juint *)pend));
            }
          }
      #else
        {
          register juint a; 
          register juint * p = (juint *)array; 
          register juint * pend = p + numofelem; 
          while (p < pend) { 
            a = *p;
            *(p++) = swab32(a); 
            }
          }
      #endif
      break;
      }
    case 8: { /*  64-bit */
      register julong a; 
      register julong * p = (julong *)array; 
      register julong * pend = p + numofelem; 
      assert(((juint)array) % 8 == 0);
      while (p < pend) {
        a = *p;
        *(p++) = swab64(a); 
        }
      break;
      }
    default:
      /* fprintf(stderr, "bad element size in arrayByteSwap\n"); */
      assert(0); /*  bad element size */
    }
  #if DEBUG_SWAP_PERF
    fprintf(stderr, "arrayByteSwap time: %8.5f sec, (%lu elements, %lu bytes/element)\n", 
      ((float)clock()-starttime)/CLOCKS_PER_SEC, numofelem, sizeofelem);
  #endif
  }
/*------------------------------------------------------------------------------------*/
/*  local array-to-array copy and byte swap */
void arrayCopyByteSwap(jbyte * toarray, const jbyte * fromarray, jint sizeofelem, jint numofelem) {
  #if DEBUG_SWAP_PERF
    clock_t starttime = clock();
  #endif
  switch (sizeofelem) { 
    case 2: { /*  16-bit (jchar = 16 bits) */
      assert(((juint)toarray) % 2 == 0);
      assert(((juint)fromarray) % 2 == 0);
      #if USE_SWAB_FOR_16BIT
        swab(fromarray, toarray, numofelem*sizeofelem);
      #else
        #if USE_ARRAY_BSWAP_64MEMOPS
          /*  try to reduce memory accesses by making all explicit memory ops 64-bits wide */
          if (((juint)toarray) % 8 == ((juint)fromarray) % 8) { /*  this should always be true, but just in case */
            /*  prolog */
            while (((juint)fromarray) % 8 != 0) {
              *((jchar *)toarray) = swab16(*((const jchar *)fromarray));
              toarray += 2;
              fromarray += 2;
              numofelem--;
              }

            { /*  main loop body */
              register julong a; 
              register julong b; 
              register const julong * p = (julong *)fromarray; 
              register const julong * pend = p + ( numofelem / 4); 
              register julong * top = (julong *)toarray; 
              while (p < pend) {  
                  a = *(p++);
                  b = swab16((jchar)a);
                  a >>= 16;
                  b |= (((julong)swab16((jchar)a)) << 16);
                  a >>= 16;
                  b |= (((julong)swab16((jchar)a)) << 32);
                  a >>= 16;
                  b |= (((julong)swab16((jchar)a)) << 48);
                  *(top++) = b;
                  }
              { /*  epilog */
                const jchar * pepilog = (jchar *)pend;
                jchar * ptoepilog = (jchar *)top;
                int i; 
                for (i = 0; i < numofelem % 4; i++) *(ptoepilog++) = swab16((*pepilog));
                }
              }
            }
          else
        #endif
          {
            register jchar a; 
            register const jchar * p = (jchar *)fromarray; 
            register const jchar * pend = p + numofelem; 
            register jchar * top = (jchar *)toarray; 
            while (p < pend) {  
              a = *(p++);
              *(top++) = swab16(a);  
              }
          }
      #endif
      break;
      }
    case 4: { /*  32-bit */
      assert(((juint)toarray) % 4 == 0);
      assert(((juint)fromarray) % 4 == 0);
      #if USE_ARRAY_BSWAP_64MEMOPS
        /*  try to reduce memory accesses by making all explicit memory ops 64-bits wide */
        if (((juint)toarray) % 8 == ((juint)fromarray) % 8) { /*  this should always be true, but just in case */
          /*  prolog */
          if (((juint)fromarray) % 8 != 0) {
            *((juint *)toarray) = swab32(*((const juint *)fromarray));
            toarray += 4;
            fromarray += 4;
            numofelem--;
            }

          { /*  main loop body */
            register julong a; 
            register julong b; 
            register const julong * p = (julong *)fromarray; 
            register const julong * pend = p + ( numofelem / 2); 
            register julong * top = (julong *)toarray; 
            while (p < pend) { 
                a = *(p++);
                b = swab32((juint)a);
                a >>= 32;
                b |= (((julong)swab32((juint)a)) << 32);
                *(top++) = b;
                }
            if (numofelem % 2)  { /*  epilog */
              *((juint *)top) = swab32(*((const juint *)pend));
              }
            } 
          }
        else
      #endif
        {
          register juint a; 
          register const juint * p = (juint *)fromarray; 
          register const juint * pend = p + numofelem; 
          register juint * top = (juint *)toarray; 
          while (p < pend) { 
            a = *(p++);
            *(top++) = swab32(a); 
            }
          }
      break;
      }
    case 8: { /*  64-bit */
      register julong a; 
      register const julong * p = (julong *)fromarray; 
      register const julong * pend = p + numofelem; 
      register julong * top = (julong *)toarray; 
      assert(((juint)toarray) % 8 == 0);
      assert(((juint)fromarray) % 8 == 0);
      while (p < pend) {
        a = *(p++);
        *(top++) = swab64(a); 
        }
      break;
      }
    default:
      /* fprintf(stderr, "bad element size in arrayCopyByteSwap\n"); */
      assert(0); /*  bad element size */
    }

  #if DEBUG_SWAP_PERF
    fprintf(stderr, "arrayCopyByteSwap time: %8.5f sec, (%lu elements, %lu bytes/element)\n", 
      ((float)clock()-starttime)/CLOCKS_PER_SEC, numofelem, sizeofelem);
  #endif
  }
/*------------------------------------------------------------------------------------*/





