#include "glo.h"
#include "my_lib.h"
#include "rm.h"

int CheckHDD()
{
	FILE *hdd;
	if(!(hdd=fopen("HDD.bin","rb"))){return FALSE;}
	
	int size=fsize(hdd);
	if(size<8){fclose(hdd); return FALSE;}
	
	int a[2]; //array for reading sector size and sectors
	fread(a,2,4,hdd);
	if(a[0]*a[1]*4 !=size){fclose(hdd); return FALSE;}
	if(a[0] <= 0){fclose(hdd); return FALSE;}
	if(a[1] <= 0){fclose(hdd); return FALSE;}
	
	fclose(hdd);
	return TRUE;
}
//---------------------------------------
int CheckConst()
{
	if(MAX_CHANNEL_SIZE <= 0){return FALSE;}
	if(PAGES <= 1){return FALSE;}
	if(PAGE_SIZE <= 1){return FALSE;}
	if(DEFAULT_TIMER <= 0){return FALSE;}
	return TRUE;
}
//---------------------------------------
void DisplayHelp(void){
	UsePrinter("dflag X - set value of debug flag to X (X=0 means off, X=1 means on)\n");
	UsePrinter("exit - terminate operating system\n");
	UsePrinter("help - show this help screen\n");
	UsePrinter("run myfile - run the given file named myfile as a program\n");
	UsePrinter("----------------------------\n");
	UsePrinter("* - When debug flag is set every step and registers are printed to file report.txt\n");
}
//---------------------------------------
void ExecuteProgram(char *filename)
{
	FILE *fd=NULL;
	fd=fopen(filename,"r");
	if(fd==NULL){printf("Failure. Could not open program.\n"); return;}
	LoadProgram(fd);
	fclose(fd);
}
//---------------------------------------
void LoadProgram(FILE *fd)
{
	
}
//---------------------------------------
int UsePrinter(char* string)
{
	if(C2 !=0){return FALSE;} //channel busy
	char *tmp=string;
	int i;
	C2=1; //take channel
	while(*tmp)
	{
		//read block to buffer of screen
		for(i=1; (i<=MAX_CHANNEL_SIZE) && (*tmp); i++, tmp++)
		{
			ch2[0]=i;
			ch2[i]=*tmp;
		}
		//print buffer contents to screen
		for(i=1; i<=ch2[0]; i++){printf("%c",ch2[i]);}
	}
	C2=0; //release channel
	return TRUE;
}
//---------------------------------------
int ExecuteInstruction(void)
{
	return TRUE;
}
//---------------------------------------
//HDD...Mutex?
