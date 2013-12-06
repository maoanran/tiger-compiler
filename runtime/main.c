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

	printf("\n%s\n", "如需要设置栈大小，请使用参数@tiger -heapSize <n>@，<n>的单位为KB\n例： ./a.out @tiger -heapSize 1 @");
	printf("\n%s\n", "如需要输出调试信息，请使用参数@tiger -gcLog @\n例： ./a.out @tiger -gcLog @");
	printf("\n%s\n\n", "相关参数保存在runtime/control.c文件中，可修改文件手动配置");

	CommandLine_doarg(argc, argv);

	// initialize the Java heap
	Tiger_heap_init(Control_heapSize);
	printf("完成初始化堆，堆大小为：%dbytes\n\n", Control_heapSize);

	// enter Java code...
	Tiger_main();
}
