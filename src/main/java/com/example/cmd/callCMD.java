package com.example.cmd;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Timestamp;

public class callCMD {
	
	public String executeTestCommand() {

		StringBuffer output = new StringBuffer();
		
		StringBuffer cmd = new StringBuffer();

		cmd.append("cd "+System.getProperty("user.home") + "/Downloads/UnitTestDemo;");
		cmd.append("/usr/local/Cellar/maven/3.5.4/bin/mvn -Dtest=DemoServiceTest#testSuccess test --quiet;");


		Process p;
		try {
			ProcessBuilder pb1 = new ProcessBuilder("bash", "-c", cmd.toString());
			pb1.redirectErrorStream(true);
			p = pb1.start();
			p.waitFor();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}


		} catch (Exception e) {
			e.printStackTrace();
		}

		return output.toString();

	}

}
