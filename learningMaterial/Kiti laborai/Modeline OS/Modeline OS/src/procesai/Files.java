package procesai;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import kompiuteris.ZurnalizavimoIrenginys;
import kompiuteris.RealiMasina;
import modelineos.FileConstants;
import resursai.FailuKomandosElementas;
import resursai.IsvedimoElementas;
import resursai.UzduotiesElementas;
import resursai.UzduotiesFailoElementas;

/**
 *
 * @author Povilas Panavas (povilas.panavas@gmail.com)
 */
public class Files extends AbstractProcess {

	String command = null;

	@Override
	public void doit() {
		switch (state) {
			case 0:
				RealiMasina.kernel.requestResource(itself.getId(), "FailineKomanda", 1);
				state++;
				break;
			case 1:
				FailuKomandosElementas ele = (FailuKomandosElementas) itself.getResourceElement("FailineKomanda");
				command = ele.getKomanda();
				if (command.startsWith("show")) {
					RealiMasina.hdd.showMemory();
				} else if (command.startsWith("format")) {
					RealiMasina.hdd.format();
				} else if (command.startsWith("read file")) {
					String filename = command.substring(10);
					RealiMasina.kernel.freeResource(new IsvedimoElementas("Isvedimas",
                                                false, RealiMasina.kernel.readFile(filename) + "\n"), -1);
				} else if (command.startsWith("write track")) {
					String info = command.substring(command.indexOf("|") + 1);
					RealiMasina.kernel.writeMemoryTrack(info, 0);
				} else if (command.startsWith("read track")) {
					ZurnalizavimoIrenginys.println(RealiMasina.kernel.readMemoryTrack(0));
				} else if (command.startsWith("delete file")) {
					String filename = command.substring(12);
					RealiMasina.kernel.deleteFile(filename);
				} else if (command.startsWith("ls")) {
					RealiMasina.kernel.freeResource(new IsvedimoElementas("Isvedimas",
                                                false, RealiMasina.kernel.listFiles() + "\n"), -1);
				} else if (command.startsWith("create file")) {
					String filename = command.substring(12, command.indexOf("|"));
					String info = command.substring(command.indexOf("|") + 1);
					RealiMasina.kernel.writeFile(info, filename);
				} else if (command.startsWith("create samples")) {
					RealiMasina.kernel.writeFile(FileConstants.DATASEGMENT +
							"0004" + FileConstants.CODESEGMENT +
							"LR00SR01PUSHPUSHMUL0POP0SR80PD80HALT" + FileConstants.FILEEND, "pavyzdys");
					RealiMasina.kernel.writeFile(FileConstants.DATASEGMENT +
							FileConstants.CODESEGMENT +
							"GD00LR00SR01PUSHPUSHMUL0POP0SR80PD80HALT" + FileConstants.FILEEND, "ivedimas");
                                        RealiMasina.kernel.writeFile(FileConstants.DATASEGMENT +
                                                        FileConstants.CODESEGMENT +
                                                        "GD00GD01LR00PUSHLR01PUSHADD0POP0SR02PD02HALT" + FileConstants.FILEEND, "suma");
                                        RealiMasina.kernel.writeFile(FileConstants.DATASEGMENT + "Labas. Ivesk skaiciunnnnSkaiciaus kvadratas:" +
                                                        FileConstants.CODESEGMENT +
                                                        "PD00PD01PD02PD03PD04PD05GD19LR19PUSHPUSHMUL0POP0SR80PD06PD07PD08PD09PD10PD80PD05HALT"
                                                        + FileConstants.FILEEND, "kvadratas");
				} else if (command.startsWith("load file")) {
					RandomAccessFile f = null;
					try {
						String filename = command.substring(10);
						f = new RandomAccessFile(filename, "r");
						String line = "", text = "";
						while ((line = f.readLine()) != null) {
							text += line;
						}
						RealiMasina.kernel.freeResource(
								new UzduotiesElementas(
								"Uzduotis", true,
								UzduotiesElementas.UzduotiesTipas.UZKRAUTI, filename, text), -1);
					} catch (IOException ex) {
						state = 2;
						break;
					} finally {
						try {
							f.close();
						} catch (Exception ex) {
						}
					}
				} else {
					// Komanda neatpažinta, tad reikia išvesti atitinkamą pranešimą
					state = 2;
					break;
				}
				state = 3;
				break;
			case 2:
				RealiMasina.kernel.freeResource(new IsvedimoElementas("Isvedimas",
						false, "Komanda neatpažinta: \"" + command + "\"\n"), -1);
				state++;
				break;
			case 3:
				itself.takeResourceElement("FailineKomanda");
				state = 0;
				break;
		}
	}
}
