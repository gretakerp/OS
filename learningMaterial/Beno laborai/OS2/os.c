#include "glo.h"
#include "my_lib.h"


//Declaring globals:
char MODE, SP, IP, SF=0, SI, PI, C1, C2, C3, C4;
short PTR, TIME;
int memory[PAGES][PAGE_SIZE];
char ch1[MCS+1], ch2[MCS+1], ch3[MCS+1], ch4[MCS+1], *err_str=NULL;

void Shell()
{
	char *command=calloc(COMMAND_MAX_LEN+1,1);
	while(TRUE){
		UsePrinter(">");
		fgets(command,COMMAND_MAX_LEN,stdin);
		normalize_string(command,COMMAND_MAX_LEN);
		
		//exit?
		if(strcmp(command,"exit")==0)
		{
			printf("BenediktasG-OS terminated successfully.\n");
			free(command);
			return;
		}
		
		//run...?
		else if(strncmp(command,"run ",4)==0)
		{
			ExecuteProgram(next_word(command));
			continue;
		}
		
		//help?
		else if(strcmp(command,"help")==0)
		{
			DisplayHelp();
			continue;
		}
		
		//Clear trap flag?
		else if(strcmp(command,"dflag 0")==0)
		{
			CLEAR_TF;
			continue;
		}
		//Set trap flag?
		else if(strcmp(command,"dflag 1")==0)
		{
			SET_TF;
			continue;
		}
		else
		{
			printf("Illegal command. Try again.\n");
			continue;
		}
	}
	
	free(command);
}

/*
 * MAIN (OS Entry Point)
 * Returns 0 if OK
 */
int main(void){
	printf("BenediktasG-OS starts running...\n");
	
	err_str=calloc(200,sizeof(char));
	
	if(!CheckConst()){
		printf("Fatal error. Some constants have incorrect initial values.\n");
		return 1;
	}
	
	if(!CheckHDD()){
		printf("Fatal error. HDD is corrupted.\n");
		return 1;
	}
	
	Shell();
	
	return 0;
}
