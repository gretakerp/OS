package Processes;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import Emulator.GUI;
import Resources.UzduotisIsorinejeAtmintyje;

public class Validator extends Process {

	static String externalName = "Validator";
	
	public Validator(int id) {
		super(id, externalName);
		state = "ready";
	}
	
	
	@Override
	public void executeProc() {
		
		while (state == "running") {
	        switch (savedState) {
		        case 0: { // praso uzduotis išorinėje atmintyje resurso
		            GUI.OS.requestResource(ID, "UzduotisIsorinejeAtmintyje");
		            state = "blocked";
		            savedState++;
		            break;
		        }
		        case 1: { // praso uzduotis trečiojo kanalo resurso
		            GUI.OS.requestResource(ID, "TreciasisKanalas");
		            state = "blocked";
		            savedState++;
		            break;
		        }
		        case 2: { // validavimas
		        	
		        	try {

						FileInputStream fstream = new FileInputStream(
								"HARDDRIVE.txt");
						DataInputStream in = new DataInputStream(fstream);
						BufferedReader br = new BufferedReader(
								new InputStreamReader(in));
						String strLine;

						while ((strLine = br.readLine()) != null) {
							String[] line = strLine.split(":");
							if (line[0].equals(UzduotisIsorinejeAtmintyje.place)) {
								if ((Integer.parseInt(line[1]) > 0) && (Integer.parseInt(line[1]) < 10)) {
									for (int i = 0; i < 10 * Integer.parseInt(line[1]); i++) {
										strLine = br.readLine();
										String[] line2 = strLine.split(":");
										if (line2.length > 1) {
											if (line2[1].equals("HALT")) {
												GUI.OS.createResource("ValidiUzduotisIsorinejeAtmintyje", ID);
												break;
											}
										}
									}
									break;
									
								} else
								{
									break;
								}

							}
								
						}
						
						in.close();


					} catch (Exception e) { // Catch exception if any
						System.err.println("Error: " + e.getMessage());
					}
		        	
		        	
		        	
		        	
		        	GUI.OS.releaseResource("TreciasisKanalas");
		            savedState = 0;
		            break;
		        }
	        }
		}

	}

	}
