#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/time.h>

// The Gimple Garbage Collector.

//===============================================================//
// The Java Heap data structure.

/*   
 ----------------------------------------------------
 |                        |                         |
 ----------------------------------------------------
 ^\                      /^
 | \<~~~~~~~ size ~~~~~>/ |
 from                       to
 */
struct JavaHeap {
	int size;         // in bytes, note that this if for semi-heap size
	char *from;       // the "from" space pointer
	char *fromFree;   // the next "free" space in the from space
	char *to;         // the "to" space pointer
	char *toStart;    // "start" address in the "to" space
	char *toNext;     // "next" free space pointer in the to space
};

// The Java heap, which is initialized by the following
// "heap_init" function.
struct JavaHeap heap;
// Lab 4, exercise 10:
// Given the heap size (in bytes), allocate a Java heap
// in the C heap, initialize the relevant fields.
void Tiger_heap_init(int heapSize) {

	// You should write 7 statement here:
	// #1: allocate a chunk of memory of size "heapSize" using "malloc"
	void * chunk = (void *) malloc(heapSize);

	// #2: initialize the "size" field, note that "size" field
	// is for semi-heap, but "heapSize" is for the whole heap.
	heap.size = heapSize / 2;

	// #3: initialize the "from" field (with what value?)
	heap.from = chunk;

	// #4: initialize the "fromFree" field (with what value?)
	heap.fromFree = chunk;

	// #5: initialize the "to" field (with what value?)
	heap.to = (char *) ((int) chunk + heap.size);

	// #6: initizlize the "toStart" field with NULL;
	heap.toStart = NULL;

	// #7: initialize the "toNext" field with NULL;
	heap.toNext = NULL;

	//printf("from:%x\t to:%x\t fromFree:%x\n", heap.from, heap.to, heap.fromFree);

	return;
}

// The "prev" pointer, pointing to the top frame on the GC stack. 
// (see part A of Lab 4)
void * frame_prev = 0;

int hasEnoughSpace(int size) {
	return heap.fromFree + size < heap.from + heap.size;
}

int isInFrom(void * num) {
	return (int) num >= (int) heap.from && (int) num < (int) heap.from + (int) heap.size;
}
int isInTo(void * num) {
	return (int) num >= (int) heap.to && (int) num < (int) heap.to + (int) heap.size;
}

//===============================================================//
// The Gimple Garbage Collector

void Chase(int * p) {
	int isObjOrArray = *((int *) p + 1);
	if (!isObjOrArray)
		return;

	void * vptr = (void*) *p;

	char * gc_map = (char *) *(int *) vptr;

	int len = strlen(gc_map);
	int i;

	for (i = 0; i < len; i++) {
		if (*(gc_map + i) == '1') {
			void * addr = (void *) *(p + 3 + 1 + i);
			if (addr && isInFrom(addr)) {
				unsigned length = *((int *) addr + 2);
				int forwardAddr = (int) *((int *) addr + 3);

				if (!forwardAddr) {

					//printf("struct move from %x to %x , length:%x\n", addr, heap.fromFree, length);

					*(p + 3 + 1 + i) = (int) heap.fromFree;
					memcpy(heap.fromFree, addr, length);

					*((int *) addr + 3) = (int) heap.fromFree;
					heap.fromFree += length;
					Chase(addr);
				}
			}
		}
	}
}

int times;

// Lab 4, exercise 12:
// A copying collector based-on Cheney's algorithm.
static void Tiger_gc() {
// Your code here:

	int orginSize = (int) heap.fromFree - (int) heap.from;

	struct timeval start, end;
	gettimeofday(&start, NULL);

	int * p = (int *) frame_prev;
	int * scan = (int *) heap.to;
	heap.fromFree = heap.to;
	while (p) {
		int * arguments_base_address = (int *) *(p + 1);
		char * arguments_gc_map = (char *) *(p + 2);
		char * locals_gc_map = (char *) *(p + 3);

		if (*arguments_gc_map) {
			//printf("arg:%s\n",arguments_gc_map);
			int len = strlen(arguments_gc_map);
			int i;

			for (i = 0; i < len; i++) {

				if (*(arguments_gc_map + i) == '1') {
					void * addr = (void *) *((int *) *(p + 1) + i);

					if (isInFrom(addr)) {
						int isObjOrArray = *((int *) addr + 1);
						unsigned length = *((int *) addr + 2);
						void * forwarding = (void *) *((int *) addr + 3);
						if (isInTo(forwarding)) {
							*((int *) *(p + 1) + i) = (int) forwarding;
						} else {
							//printf("argument move from %x to %x , length:%x\n", addr, heap.fromFree, length);
							*((int *) *(p + 1) + i) = (int) heap.fromFree;
							memcpy(heap.fromFree, addr, length);
							*((int *) addr + 3) = (int) heap.fromFree;
							heap.fromFree += length;
							Chase(addr);
						}
					}
				}
			}
		}

		if (*locals_gc_map) {

			//printf("local:%s\n",locals_gc_map);
			int len = strlen(locals_gc_map);
			int i;
			for (i = 0; i < len; i++) {
				void * addr = (void *) *(p + 3 + 1 + i);

				if (isInFrom(addr)) {
					int isObjOrArray = *((int *) addr + 1);
					unsigned length = *((int *) addr + 2);
					void * forwarding = (void *) *((int *) addr + 3);

					if (isInTo(forwarding)) {

						*(p + 3 + 1 + i) = (int) forwarding;
					} else {
						//printf("local move from %x to %x , length:%x\n", addr, heap.fromFree, length);
						*(p + 3 + 1 + i) = (int) heap.fromFree;
						memcpy(heap.fromFree, addr, length);
						*((int *) addr + 3) = (int) heap.fromFree;
						heap.fromFree += length;
						Chase(addr);
					}
				}
			}
		}
		p = (int *) *p;
	}

	p = scan;
	while ((int) p < (int) heap.fromFree) {

		void * vptr = (void*) *p;
		int isObjOrArray = *(p + 1);
		unsigned length = *(p + 2);

		if (isObjOrArray) {
			char * gc_map = (char *) *(int *) vptr;
			int len = strlen(gc_map);
			int i;

			for (i = 0; i < len; i++) {
				if (*(gc_map + i) == '1') {
					void * addr = (void *) *(p + 3 + 1 + i);

					if (addr && isInFrom(addr)) {
						int forwardAddr = (int) *((int *) addr + 3);
						if (forwardAddr)
							*(p + 3 + 1 + i) = forwardAddr;
					}
				}
			}
		}

		p = (int *) ((int) p + length);
	}

	heap.to = heap.from;
	heap.from = (char *) scan;

	if (Control_gcLog) {
		int newSize = (int) heap.fromFree - (int) heap.from;
		gettimeofday(&end, NULL);
		int timeuse = 1000000 * (end.tv_sec - start.tv_sec) + end.tv_usec - start.tv_usec;
		printf("%d round of GC: %dus, collected %d bytes\n", ++times, timeuse, orginSize - newSize);
	}
}

//===============================================================//
// Object Model And allocation

// Lab 4: exercise 11:
// "new" a new object, do necessary initializations, and
// return the pointer (reference).
/*    ----------------
 | vptr      ---|----> (points to the virtual method table)
 |--------------|
 | isObjOrArray | (0: for normal objects)
 |--------------|
 | length       | (this field should be empty for normal objects)
 |--------------|
 | forwarding   |
 |--------------|\
p---->| v_0     | \
 |--------------|  s
 | ...          |  i
 |--------------|  z
 | v_{size-1}   | /e
 ----------------/
 */
// Try to allocate an object in the "from" space of the Java
// heap. Read Tiger book chapter 13.3 for details on the
// allocation.
// There are two cases to consider:
//   1. If the "from" space has enough space to hold this object, then
//      allocation succeeds, return the apropriate address (look at
//      the above figure, be careful);
//   2. if there is no enough space left in the "from" space, then
//      you should call the function "Tiger_gc()" to collect garbages.
//      and after the collection, there are still two sub-cases:
//        a: if there is enough space, you can do allocations just as case 1; 
//        b: if there is still no enough space, you can just issue
//           an error message ("OutOfMemory") and exit.
//           (However, a production compiler will try to expand
//           the Java heap.)
void *Tiger_new(void *vtable, int size) {
	void* temp;

	if (hasEnoughSpace(size)) {
		temp = heap.fromFree;
		memset(temp, 0, size);
		heap.fromFree += size;
	} else {
		Tiger_gc();
		if (hasEnoughSpace(size)) {
			temp = heap.fromFree;
			memset(temp, 0, size);
			heap.fromFree += size;
		} else {
			printf("error! OutOfMemory!\n");
			exit(1);
		}
	}

	*((int *) temp) = (int) vtable;
	*((int *) temp + 1) = 1;
	*((int *) temp + 2) = size;
	*((int *) temp + 3) = 0;

	//printf("new:%x\t size:%x\t fromFree:%x\n", temp, size, heap.fromFree);
	return temp;
}

// "new" an array of size "length", do necessary
// initializations. And each array comes with an
// extra "header" storing the array length and other information.
/*    ----------------
 | vptr         | (this field should be empty for an array)
 |--------------|
 | isObjOrArray | (1: for array)
 |--------------|
 | length       |
 |--------------|
 | forwarding   |
 |--------------|\
p---->| e_0          | \      
 |--------------|  s
 | ...          |  i
 |--------------|  z
 | e_{length-1} | /e
 ----------------/
 */
// Try to allocate an array object in the "from" space of the Java
// heap. Read Tiger book chapter 13.3 for details on the
// allocation.
// There are two cases to consider:
//   1. If the "from" space has enough space to hold this array object, then
//      allocation succeeds, return the apropriate address (look at
//      the above figure, be careful);
//   2. if there is no enough space left in the "from" space, then
//      you should call the function "Tiger_gc()" to collect garbages.
//      and after the collection, there are still two sub-cases:
//        a: if there is enough space, you can do allocations just as case 1; 
//        b: if there is still no enough space, you can just issue
//           an error message ("OutOfMemory") and exit.
//           (However, a production compiler will try to expand
//           the Java heap.)
void *Tiger_new_array(int length) {

	void* temp;

	length += 16;
	if (hasEnoughSpace(length)) {
		temp = heap.fromFree;
		memset(temp, 0, length);
		heap.fromFree += length;
	} else {
		Tiger_gc();
		if (hasEnoughSpace(length)) {
			temp = heap.fromFree;
			memset(temp, 0, length);
			heap.fromFree += length;
		} else {
			printf("error! OutOfMemory!\n");
		}
	}

	*((int *) temp) = 0;
	*((int *) temp + 1) = 0;
	*((int *) temp + 2) = length;
	*((int *) temp + 3) = 0;

	//printf("newarray:%x\t size:%x\t fromFree:%x\n", temp, length, heap.fromFree);

	return temp;

}

