package Processes;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import Emulator.GUI;
import Emulator.Kernel;
import Resources.Pertraukimas;
import Resources.UzduotisIsorinejeAtmintyje;
import Resources.ValidiUzduotisIsorinejeAtmintyje;

public class JobGoverner extends Process {

	static String externalName = "JobGoverner";

	public JobGoverner(int id) {
		super(id, externalName);
		state = "ready";
	}

	public void executeProc() {

		while (state == "running") {
			switch (savedState) {
			case 0: { // Atminties laukimas
				GUI.OS.requestResource(ID, "Atmintis");
				state = "blocked";
				savedState++;
				break;
			}
			case 1: { // Treciojo kanalo laukimas
				System.out.println("WAITINGCHANNEL");
				GUI.OS.requestResource(ID, "TreciasisKanalas");
				state = "blocked";
				savedState++;
				break;
			}
			case 2: {

				try {

					FileInputStream fstream = new FileInputStream(
							"HARDDRIVE.txt");
					DataInputStream in = new DataInputStream(fstream);
					BufferedReader br = new BufferedReader(
							new InputStreamReader(in));
					String strLine;

					while ((strLine = br.readLine()) != null) {
						String[] line = strLine.split(":");
						System.out.println(line[0]);
						if (line[0].equals(UzduotisIsorinejeAtmintyje.place)) {
							System.out.println("Yes it is");
							String[] masina = new String[Integer
									.parseInt(line[1]) * 10 + 1];
							for (int i = 0; i < 10 * Integer.parseInt(line[1]); i++) {
								strLine = br.readLine();
								String[] line2 = strLine.split(":");
								if (line2.length > 1) {
									masina[i] = line2[1];
								} else {
									masina[i] = " ";
								}
							}
							int newID = Kernel.getNewProcID(Kernel.processes);
							masina[masina.length - 1] = Integer.toString(newID);

							Kernel.VM.add(masina);
							break;

						}

					}

					in.close();

				} catch (Exception e) { // Catch exception if any
					System.err.println("Error: " + e.getMessage());
				}

				System.out.println(Kernel.VM.size());
				GUI.OS.createProcess("Atmintis");
				GUI.OS.releaseResource("TreciasisKanalas");
				GUI.OS.createProcess("VirtualiMasina", ID);
				GUI.OS.requestResource(ID, "Pertraukimas");
				state = "blocked";
				savedState++;
				break;
			}
			case 3: { // Interrupt apdorojimas
				
				switch (Pertraukimas.interruptType) {
				case "OUT":
					GUI.printToConsole(Pertraukimas.whichVM + " VM imitacija");
					break;
				case "HALT":
					GUI.OS.destroyProcess(GUI.OS.getProc(Pertraukimas.ID));
					GUI.OS.releaseResource("ValidiUzduotisIsorinejeAtmintyje");
					ValidiUzduotisIsorinejeAtmintyje.vykdymoLaikas = 0;
					ValidiUzduotisIsorinejeAtmintyje.jobID = ID;
					break;
				}
				
				
				GUI.OS.requestResource(ID, "Pertraukimas");
				state = "blocked";
				break;
			}

			}
		}
	}
}
