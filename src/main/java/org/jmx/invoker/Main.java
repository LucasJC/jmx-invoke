package org.jmx.invoker;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
/**
 * 
 * @author lcouchot
 *
 */
public class Main {
	public static final String URLS_OPT = "urls";
	public static final String METHOD_OPT = "method";
	public static final String MBCLASS_OPT = "mbclass";
	public static final String PARAMS_OPT = "params";
	public static final String USAGE = "jmx-invoker -" + URLS_OPT + " 'file_name' -" + MBCLASS_OPT + " 'class_name' -" + METHOD_OPT + " 'method_name' [-" + PARAMS_OPT + " param1,param2,...]";
	/**
	 * Main method. Reads a file with a jmxrmi urls list. For each url, invokes the method passed as an argument.
	 * @param args input arguments
	 */
	public static void main(String[] args) {
		
		
		//Options for cli
		Options options = new Options();
		options.addOption(URLS_OPT, true, "URLs list.");
		options.addOption(METHOD_OPT, true, "Method to execute.");
		options.addOption(MBCLASS_OPT, true, "MBean class that exposes method to execute. Example: 'org.example.MyClass'");
		options.addOption(PARAMS_OPT, true, "Params array.");
		
		CommandLineParser parser = new org.apache.commons.cli.BasicParser();
		CommandLine cmd = null;
		//parse args from options
		try {
			cmd = parser.parse(options, args);
			
			if(cmd.hasOption(URLS_OPT) && cmd.hasOption(METHOD_OPT) && cmd.hasOption(MBCLASS_OPT)){
				String urlsFile = cmd.getOptionValue(URLS_OPT);
				String methodName = cmd.getOptionValue(METHOD_OPT);
				String mbClassName = cmd.getOptionValue(MBCLASS_OPT);
				
				String[] methodParams = null;
				
				if(cmd.hasOption(PARAMS_OPT)){
					methodParams = cmd.getOptionValue(PARAMS_OPT).split(",");
				}
				
				BufferedReader br = null;
				
				//try to open urls file
				try {
					br = new BufferedReader(new FileReader(urlsFile));
				} catch (FileNotFoundException e) {
					printError("Error while trying to open file: " + urlsFile,e);
				}
				
				System.out.println("Method: " + methodName + ". Args: " + Arrays.toString(methodParams) + ". MBean: " + mbClassName + "\n");
				
				String line;
				
				try{
					//while there is a url, invoke method
					while ((line = br.readLine()) != null) {
						MBServerUtils.invokeMethod(line,methodName,mbClassName,methodParams);
					}
					
				} catch (IOException e) {
					printError("Error reading file content: " + urlsFile,e);
				}
				
				System.out.println("End of proccess.");
				
				try {
					br.close();
				} catch (IOException e) {
					printError("Error while trying to close file: " + urlsFile,e);
				}
			}else{
				//if no urls list was specified
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp(USAGE, options);
			}
			
		} catch (ParseException e) {
			printError("Error parsing args: " + e.getLocalizedMessage(),e);
		}			
	}
	
	static void printError(String errorMessage, Exception exception){
		System.out.println(errorMessage + "\n" + exception.getCause());
	}
}	
