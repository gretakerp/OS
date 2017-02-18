#include "my_lib.h"

int fsize(FILE *fd)
	{
	int position_before=ftell(fd); //get file pointer
	fseek(fd,0,SEEK_SET); //beginning
	fseek(fd,0,SEEK_END); //the end
	int size=ftell(fd); //get end position
	fseek(fd,position_before,SEEK_SET); //restore previous file pointer
	return size;
	}


void normalize_string(char *text, int symbols){
	int i;
	for(i=0; i<symbols; i++) if(text[i]==10){text[i]=0; return;}
}

char* next_word(char *text){
	//if string already finished, return:
	if(*text==0) return text;
	//skip spaces before word:
	for(;(*text==' ') && (*text !=0);text++){};
	//skip word until a space is met:
	for(;(*text !=' ') && (*text !=0);text++){};
	//skip spaces after word:
	for(;(*text==' ') && (*text !=0);text++){};
	return text;
}
