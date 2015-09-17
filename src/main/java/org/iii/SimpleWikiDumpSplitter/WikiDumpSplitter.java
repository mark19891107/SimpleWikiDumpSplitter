package org.iii.SimpleWikiDumpSplitter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Lu Fang
 * 
 * We can download wiki dump at http://dumps.wikimedia.org/enwiki/latest/
 */
public class WikiDumpSplitter {
	public static String baseDir;
	public static String fileBase = "split";
	public static int currentId = 0;
	
	public static boolean split(String originWikiDumpXml, String baseDir){
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(originWikiDumpXml));
			String splitFileName = baseDir + "/" + fileBase + "-" + currentId+ ".xml";
			BufferedWriter bw = new BufferedWriter(new FileWriter(splitFileName));
			StringBuffer sb = new StringBuffer();
			
			boolean pageStart = false;
			
			while (true){
				String line = br.readLine();
				if (line == null){
					break;
				}
				line = line.trim();
				if (line.equals("<page>")){
					if (pageStart){
						//Parsing error
						System.err.println("Something is wrong with <page>!");
					}
					sb.append(line);
					sb.append("\n");
					pageStart = true;
				}else if (line.equals("</page>")){
					if (!pageStart){
						//Parsing error
						System.err.println("Something is wrong with </page>!");
					}else{
						sb.append(line);
						sb.append("\n");
						bw.write(sb.toString());
						bw.close();
						
						splitFileName = baseDir + "/" + fileBase + "-" + currentId + ".xml";
						
						bw = new BufferedWriter(new FileWriter(splitFileName));
						bw.write(sb.toString());
						
						System.out.println(splitFileName);
						
						++ currentId;
						
						sb.delete(0, sb.length());
					}
					pageStart = false;
				}else{
					if (pageStart){
						sb.append(line);
						sb.append("\n");
					}
				}
				
			}
			
			br.close();
			bw.close();	//If there is no </page>, we ignore the rest in sb
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
}