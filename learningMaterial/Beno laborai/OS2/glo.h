/*
	* Global Headers
	* Global constants, macros
	* Global variables (registers, pointers to memory and channels)
*/
#ifndef _GLO
#define _GLO 1

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "my_lib.h"
#include "rm.h"
#include "vm.h"

#define MAX_CHANNEL_SIZE 16
#define MCS MAX_CHANNEL_SIZE
#define PAGE_SIZE 256
#define PAGES 16
#define DEFAULT_TIMER 10
#define COMMAND_MAX_LEN 200

#define TIME_DOWN(X) (TIME>X) ? TIME = TIME-X : TIME=0
#define TIME_DEFAULT() TIME=DEFAULT_TIMER; 

#define SET_TF SF=SF|4
#define SET_ZF SF=SF|2
#define SET_CF SF=SF|1

#define CLEAR_TF SF=SF&(256-4)
#define CLEAR_ZF SF=SF&(256-2)
#define CLEAR_CF SF=SF&(256-1)

extern char MODE, SP, IP, SF, SI, PI, C1, C2, C3, C4;
extern short PTR, TIME;
extern int memory[PAGES][PAGE_SIZE];
extern char ch1[MCS+1], ch2[MCS+1], ch3[MCS+1], ch4[MCS+1], *err_str;

#endif
