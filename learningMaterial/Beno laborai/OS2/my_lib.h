#ifndef _MY_LIB
	#define _MY_LIB 1
	#define TRUE 1
	#define FALSE 0
	#include <stdio.h>
	
	int fsize(FILE *fd);
	void normalize_string(char *text, int symbols);
	char* next_word(char *text);

#endif
