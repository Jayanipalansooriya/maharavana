package com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;
public class FileInput_reader {

	/**
	 * @param args
	 */
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		readFile();
//
//		}


        public static void readFile( String  inputText)
{
  //  File file = new File("/Users/nirashakatugampala/Desktop/Test.txt");
		BufferedReader reader = null;
		Vector<String> vc=new Vector<String>();

		//try {
			//reader = new BufferedReader(new FileReader(file));
			String text = null;
		   // String strLine;
		    //Read File Line By Line
		    //while ((strLine = reader.readLine()) != null)   {
			StringTokenizer tok = new StringTokenizer(inputText,".,:\" ?<>+=-_)(*&^%$#@!~`{}[]|\\;'/[0-9]",false);
			// repeat until all lines is read
				while (tok.hasMoreTokens()) {
				text = tok.nextToken();
				 // add vector elements
				vc.add(text);
			//}
		//}
			// get elements of Vector
//	        for(int i=0;i<vc.size();i++)
//	        {
//	            System.out.println("Vector Element "+i+" :"+vc.get(i));
//	        }
//		}
//		catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//		catch (IOException e) {
//			e.printStackTrace();
//		}
//		finally {
//			try {
//				if (reader != null) {
//					reader.close();
//				}
//			}
//			catch (IOException e) {
//				e.printStackTrace();
//			}
		}
}


	}




