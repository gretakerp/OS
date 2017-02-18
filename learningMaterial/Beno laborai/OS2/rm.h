#include <stdio.h>

//check whether OS can run at all:
int CheckHDD();
int CheckConst();

void DisplayHelp(void);

void ExecuteProgram(char *filename);
void LoadProgram(FILE *fd);

//for channels:
int UsePrinter(char* string);

//
int ExecuteInstruction(void);
int HandleInterrupt(void);
	int HandleSI(void);
	int HandlePI(void);
	int HandleTimer(void);
