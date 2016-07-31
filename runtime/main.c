#include <stdio.h>
#include <stdlib.h>
#include <string.h>

extern void Tiger_heap_init(int);

int main(int argc, char **argv) {
	// Lab 4, exercise 13:
	// You should add some command arguments to the generated executable
	// to control the behaviour of your Gimple garbage collector.
	// For instance, you should run:
	//   $ a.out @tiger -heapSize 1 @
	// to set the Java heap size to 1K. Or you can run
	//   $ a.out @tiger -gcLog @
	// to generate the log (which is discussed in this exercise).
	// You can use the offered function in file "control.c"
	// and "command-line.c"
	// Your code here:

	printf("\n%s\n", "If you want to set the size of heap, please use arguments: @tiger -heapSize <n>@, the unit of <n> is KB\nfor example: ./a.out @tiger -heapSize 1 @");
	printf("\n%s\n", "If you want to output the log of GC, please use arguments: @tiger -gcLog @\nfor example: ./a.out @tiger -gcLog @");
	printf("\n%s\n\n", "The related arguments is in runtime/control.c, you can modified it if you want.");

	CommandLine_doarg(argc, argv);

	// initialize the Java heap
	Tiger_heap_init(Control_heapSize);
	printf("The init of heap is completed , the size of heap is:%dbytes\n\n", Control_heapSize);

	// enter Java code...
	Tiger_main();
}
