package ru.will0376.xBlocker.server.utils.cfg;

import ru.justagod.mineplugin.GradleSide;
import ru.justagod.mineplugin.GradleSideOnly;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

@GradleSideOnly(GradleSide.SERVER)
public class ConfigCraftUtils {
	private static BufferedReader br;

	public static String readFromFileToString(String par1FileName) {
		String filePath = "./config/xBlocker/" + par1FileName + ".txt";
		StringBuilder jsonfull = new StringBuilder();
		BufferedReader bufReader = null;
		if ((new File(filePath)).exists()) {
			try {
				bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), Charset.forName("UTF-8")));
				jsonfull = new StringBuilder();

				String e;
				while ((e = bufReader.readLine()) != null) {
					jsonfull.append(e);
				}

				bufReader.close();
			} catch (FileNotFoundException var6) {
				var6.printStackTrace();
			} catch (IOException var7) {
				var7.printStackTrace();
			}
		} else {
			try {
				(new File(filePath)).createNewFile();
			} catch (IOException var5) {
				var5.printStackTrace();
			}
		}

		return jsonfull.toString();
	}

	public static void addToFile(String par1FileName, String str) {
		String filePath = "./config/xBlocker/" + par1FileName + ".txt";
		if ((new File(filePath)).exists()) {
			PrintStream e = null;

			try {
				e = new PrintStream(new BufferedOutputStream(new FileOutputStream(filePath, true)));
				String allStr = "";

				String e1;
				for (br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8)); (e1 = br.readLine()) != null; allStr = e1) {
				}

				if (allStr.contains(":") && !allStr.contains("@") || allStr.contains(":") && allStr.contains("@")) {
					e.print("@" + str);
				} else {
					e.print(str);
				}
			} catch (IOException var11) {
				var11.printStackTrace();
			} finally {
				if (e != null) {
					e.close();
				}

			}
		} else {
			try {
				(new File(filePath)).createNewFile();
			} catch (IOException var10) {
				var10.printStackTrace();
			}
		}

	}

	public static void deleteFromFile(String par1FileName, String strDelete) {
		String filePath = "./config/xBlocker/" + par1FileName + ".txt";
		if ((new File(filePath)).exists()) {
			try {
				String str = "null";
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8));

				String e;
				while ((e = br.readLine()) != null) {
					if (e != null) {
						str = e;
					}
				}

				String[] text;
				if (str.contains("@" + strDelete)) {
					text = (str).replace("@" + strDelete, "").split("@");
					writeToFile(par1FileName, str.replace("@" + strDelete, ""));
				} else if (str.contains(strDelete + "@")) {
					text = (str).replace(strDelete + "@", "").split("@");
					writeToFile(par1FileName, str.replace(strDelete + "@", ""));
				} else if (str.contains(strDelete) && !str.contains(strDelete + "@") && !str.contains("@" + strDelete)) {
					text = (str).replace(strDelete, "").split("@");
					writeToFile(par1FileName, str.replace(strDelete, ""));
				} else if (str.contains(strDelete) && !str.contains(strDelete + "@")) {
					text = (str).replace(strDelete + "@", "").split("@");
					writeToFile(par1FileName, str.replace(strDelete, ""));
				}

				br.close();
			} catch (IOException var8) {
				Logger.getLogger(ConfigCraftUtils.class.getName()).log(Level.SEVERE, null, var8);
			}
		} else {
			try {
				(new File(filePath)).createNewFile();
			} catch (IOException var7) {
				var7.printStackTrace();
			}
		}

	}

	public static void writeToFile(String par1FileName, String par2String) {
		File configFolder = new File("./config/xBlocker/");
		configFolder.mkdir();

		try {
			FileWriter e = new FileWriter("./config/xBlocker/" + par1FileName + ".txt", false);
			e.write(par2String);
			e.close();
		} catch (IOException var4) {
			var4.printStackTrace();
		}

	}
}
