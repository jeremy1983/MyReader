package com.jreader.common;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import com.jreader.myreader.R;

public class FileHelper {
	
	private static BufferedInputStream in;
	public static String ReadFileContent(File file)
	{
        BufferedReader reader;
        String text = "";
        try {
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream in = new BufferedInputStream(fis);
                in.mark(4);
                byte[] first3bytes = new byte[3];
                int len = Integer.parseInt(String.valueOf(file.length()));
                byte[] bytes = new byte[len];
                in.read(first3bytes);//找到文档的前三个字节并自动判断文档类型。
                in.reset();                
                
			if (first3bytes[0] == (byte) 0xEF && first3bytes[1] == (byte) 0xBB
					&& first3bytes[2] == (byte) 0xBF) {// utf-8

				 reader = new BufferedReader(new InputStreamReader(in, "utf-8"));

			} else if (first3bytes[0] == (byte) 0xFF
					&& first3bytes[1] == (byte) 0xFE) {

				 reader = new BufferedReader(new InputStreamReader(in,
				 "unicode"));
			} else if (first3bytes[0] == (byte) 0xFE
					&& first3bytes[1] == (byte) 0xFF) {
				 reader = new BufferedReader(new InputStreamReader(in,"utf-16be"));
			} else if (first3bytes[0] == (byte) 0xFF
					&& first3bytes[1] == (byte) 0xFF) {
				text = new String(bytes, 0, bytes.length, "utf-16le");

				 reader = new BufferedReader(new InputStreamReader(in,"utf-16le"));
			} else {
				 reader = new BufferedReader(new InputStreamReader(in,
				 "GBK"));
			}
			
            String str = reader.readLine();
            while (str != null) {
                    text = text + str + "\n";
                    str = reader.readLine();

            }
            reader.close();

        } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        }
        return text;		
	}
	public static String ReadFileContent2(File file)
	{
        String code = "";
        String text = "";
        try {
                FileInputStream fis = new FileInputStream(file);
                in = new BufferedInputStream(fis);
                in.mark(4);
                byte[] first3bytes = new byte[6]; 
                int len = Integer.parseInt(String.valueOf(file.length()));
                byte[] bytes = new byte[len];
                in.read(first3bytes);//找到文档的前三个字节并自动判断文档类型。
                in.reset();
                
			if (first3bytes[0] == (byte) 0xEF && first3bytes[1] == (byte) 0xBB
					&& first3bytes[2] == (byte) 0xBF) {// utf-8
				
				code="utf-8";

			} else if (first3bytes[0] == (byte) 0xFF
					&& first3bytes[1] == (byte) 0xFE) {

				code="unicode";
			} else if (first3bytes[0] == (byte) 0xFE
					&& first3bytes[1] == (byte) 0xFF) {

				code="utf-16be";
			} else if (first3bytes[0] == (byte) 0xFF
					&& first3bytes[1] == (byte) 0xFF) {
				
				code="utf-16le";
			} else {
				
				code="GBK";
			}

			in.read(bytes);
			text = new String(bytes, 0, bytes.length, code);

        } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        }
        return text;		
	}

	public static String LoadFileSize(File file) {
		Long size = file.length();
		DecimalFormat formater = new DecimalFormat();
		formater.setMaximumFractionDigits(2);
		if (size / 1000f <= 1)
			return String.format("%s b", formater.format(size));

		if (size / 1000f / 1000f <= 1)
			return String.format("%s k", formater.format(size / 1000f));

		return String.format("%s m", formater.format(size  / 1000f / 1000f));
	}
	
	public static int LoadIcon(String path)
	{
		path = path.toLowerCase();
		if(path.endsWith(".txt"))
			return R.drawable.text;
		else if(path.endsWith(".pdf"))
			return R.drawable.pdf;
		else if(path.endsWith(".zip"))
			return R.drawable.zip;
		else if(path.endsWith(".mp3"))
			return R.drawable.music;		
		else if(path.endsWith(".doc") || path.endsWith(".docx"))
			return R.drawable.doc;
		else if(path.endsWith(".xls") || path.endsWith(".xlsx"))
			return R.drawable.xlsx;
		else if(path.endsWith(".avi") || path.endsWith(".rmvb")
				|| path.endsWith(".mkv")|| path.endsWith(".wmv"))
			return R.drawable.video;
		else if(path.endsWith("/") || path.endsWith("<--"))
			return R.drawable.folder;
		else
			return R.drawable.folder;
			
	}
}
