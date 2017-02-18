/*
   * Create new HDD according to HDD params set in hdd_param.h
*/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define TRUE 1
#define FALSE 0

#define SECTORS_IN_HDD 16
#define SECTOR_SIZE 64


//Every function returns TRUE on success, if something fails: FALSE
//Prints message if some information is incorrect.

int SetPartitionsTable(int hdd_data[SECTORS_IN_HDD][SECTOR_SIZE])
{
	hdd_data[0][0] = SECTOR_SIZE;
	hdd_data[0][1] = SECTORS_IN_HDD;
	//Defining partition sizes:
	hdd_data[0][2] = 3; //how many partitions (including #0)
	
	hdd_data[0][3] = 1; //partition table size in sectors
	memcpy(&hdd_data[0][4],"MBR",4);
	
	hdd_data[0][5] = 2; //swap size in sectos
	memcpy(&hdd_data[0][6],"SWP",4);
	
	hdd_data[0][7] = 13; //left HDD space - for data (size in sectors)
	memcpy(&hdd_data[0][8],"DATA",4);
	
	return TRUE;
}

int CheckPartitionsTable(int hdd_data[SECTORS_IN_HDD][SECTOR_SIZE])
{
	//Is sector size correct?
	if(hdd_data[0][0] != SECTOR_SIZE){
		printf("Failure. Incorrect sector size (0).\n");
		return FALSE;
	}
	
	//Is sectors in hdd number correct?
	if(hdd_data[0][1] != SECTORS_IN_HDD)
	{
		printf("Failure. Incorrect number of sectors in HDD (1).\n");
		return FALSE;
	}
	
	//Is sector size positive?
	if(SECTOR_SIZE<=0){
		printf("SECTOR_SIZE must be positive!\n");
		return FALSE;
	}
	
	//Is sectors in hdd number positive?
	if(SECTORS_IN_HDD<=0)
	{
		printf("SECTORS_IN_HDD number must be positive!\n");
		return FALSE;
	}
	
	//Is "ammount of sectors" used equal to "sectors_in_hdd"?
	int partitions = hdd_data[0][2], i, sect_used=0;
	
	for(i=0; i<partitions; i++)
		sect_used+=*(*hdd_data+3+2*i); //sizes in 3,5,7,9.. etc.
	
	if(sect_used != SECTORS_IN_HDD){
		printf("Failure. Incorrect number of sectors used.\n");
		printf("Sectors in the disk: %d\n",SECTORS_IN_HDD);
		printf("Sectors used: %d\n",sect_used);
		return FALSE;
	}
	
	//Is MBR first partition?
	if(memcmp("MBR",&hdd_data[0][4],4) !=0){
		printf("Failure. The #0 partition must be MBR.\n");
		return FALSE;
	}
	//...and no more MBR defined?
	for(i=1; i<partitions; i++){
		if(memcmp("MBR",&hdd_data[0][4+2*i],4)==0){
			printf("Failure. Only partition #0 can be named MBR.\n");
			printf("One more partition named MBR found. #%d\n",i);
			return FALSE;
		}
	}
	
	//Is there SWaP defined?
	int swp_part_nr=0;
	for(i=1; i<partitions; i++){
		if(memcmp("SWP",&hdd_data[0][4+2*i],4)==0){
			swp_part_nr=i;
			break;
		}
	}
	if(!swp_part_nr){
		printf("Failure. No SWaP partition defined.\n");
		return FALSE;
	}
	//...and no more SWaP defined?
	for(i=swp_part_nr+1; i<partitions; i++){
		if(memcmp("SWP",&hdd_data[0][4+2*i],4)==0)
		{
			printf("Failure. Another SWaP partition found!\n");
			printf("First one #%d\n",swp_part_nr);
			printf("And one more: #%d\n",i);
			return FALSE;
		}
	}
	
	return TRUE;
}

int BuildHddImage(int hdd_data[SECTORS_IN_HDD][SECTOR_SIZE])
{
	FILE *fd;
	
	if(!(fd=fopen("HDD.bin","w+")))
	{
		printf("Failure. Could not open HDD.bin file for writing.\n");
		return FALSE;
	}
	
	if(fwrite(hdd_data,SECTOR_SIZE*SECTORS_IN_HDD*sizeof(int),1,fd) !=1)
	{
		printf("Failed while writing to HDD.\n");
		fclose(fd);
		return FALSE;
	}
	
	fclose(fd);
	return TRUE;
}

//Exits with 0 if OK
int main(void)
{
	int hdd_data[SECTORS_IN_HDD][SECTOR_SIZE];
	SetPartitionsTable(hdd_data);
	if(CheckPartitionsTable(hdd_data)){
		if(BuildHddImage(hdd_data))
		{
			printf("HDD image built successfully.\n");
			printf("It has:\n");
			printf("Sectors: %d\n",SECTORS_IN_HDD);
			printf("Size of each sector: %d\n",SECTOR_SIZE);
			
			return 0;
		}
		else
		{
			printf("Building HDD image failed.\n");
		}
	}
	else /*Partitions table built bad*/
	{
		return 1;
	}
	return 0;
}
