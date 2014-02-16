package com.jreader.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileAssistent {
	// 要遍历的文件夹
	private String strSrcPath;
	// 要copy文件所到的目的文件夹
	private String strDestPath;
	// 要copy或者删除的文件的后缀
	private String strFilePrefix;
	// 计数
	private int ICOUNT;

	public String getStrSrcPath() {
		return strSrcPath;
	}

	public void setStrSrcPath(String strSrcPath) {
		this.strSrcPath = strSrcPath;
	}

	public String getStrDestPath() {
		return strDestPath;
	}

	public void setStrDestPath(String strDestPath) {
		this.strDestPath = strDestPath;
	}

	public String getStrFilePrefix() {
		return strFilePrefix;
	}

	public void setStrFilePrefix(String strFilePrefix) {
		this.strFilePrefix = strFilePrefix;
	}

	public int getICOUNT() {
		return ICOUNT;
	}

	public void setICOUNT(int iCOUNT) {
		ICOUNT = iCOUNT;
	}

	public boolean isMatch(String strDest, boolean isEnd) throws Exception {
		return isMatch(strDest, strFilePrefix, isEnd);
	}

	/**
	 * @category 过滤文件名中的关键字
	 * @param strDest
	 * @param strFilePrefix
	 * @param isEnd
	 *            true:以strFilePrefix结尾的文件名，false：文件名字符串中包含strFilePrefx的文件
	 * @return
	 * @throws Exception
	 */
	public boolean isMatch(String strDest, String strFilePrefix, boolean isEnd)
			throws Exception {

		String strPrefix = "";
		if (null == strFilePrefix)
			throw new Exception("要过滤的文件关键字不能为空！");
		if (null == strDest)
			throw new Exception("文件名不能为空！");
		if (strFilePrefix.trim().length() == 0)
			return true;
		String[] str = strFilePrefix.split(",");
		if (isEnd) {
			for (int i = 0; i < str.length; i++) {
				if (str[i].trim().length() == 0)
					continue;
				if (strPrefix.equals(""))
					strPrefix = "(.*" + str[i] + "$)";
				else
					strPrefix += "|(.*" + str[i] + "$)";
			}
		} else {
			for (int i = 0; i < str.length; i++) {
				if (str[i].trim().length() == 0)
					continue;
				if (strPrefix.equals(""))
					strPrefix = "(.*" + str[i] + ".*)";
				else
					strPrefix += "|(.*" + str[i] + ".*)";
			}

		}
		Pattern p = Pattern.compile(strPrefix);
		Matcher m = p.matcher(strDest);
		return m.find();
	}

	/**
	 * @category 调用递归方法，递归查找、拷贝文件
	 * @param isEnd
	 *            true:以strFilePrefix结尾的文件名，false：文件名字符串中包含strFilePrefx的文件
	 * @throws Exception
	 */
	public void copyFiles(boolean isEnd) throws Exception {
		copyFiles(strSrcPath, strDestPath, strFilePrefix, isEnd);
	}

	/**
	 * @category 重载，调用递归方法，递归查找、拷贝文件
	 * @param isEnd
	 *            true:以strFilePrefix结尾的文件名，false：文件名字符串中包含strFilePrefx的文件
	 * @param isOpenDestFoulder
	 *            true:结束后打开目的文件夹，false：反之
	 * @throws Exception
	 */
	public void copyFiles(boolean isOpenDestFoulder, boolean isEnd)
			throws Exception {
		copyFiles(strSrcPath, strDestPath, strFilePrefix, isEnd);
		if (isOpenDestFoulder) {
			String strDosComm = "cmd /c  start " + strDestPath;
			Runtime.getRuntime().exec(strDosComm);
		}
	}

	/**
	 * @category 查找、拷贝文件，递归
	 * @param strSrcPath
	 * @param strDestPath
	 * @param isEnd
	 *            true:以strFilePrefix结尾的文件名，false：文件名字符串中包含strFilePrefx的文件
	 * @param strFilePrefx
	 * @throws Exception
	 */

	public void copyFiles(String strSrcPath, String strDestPath,
			String strFilePrefx, boolean isEnd) throws Exception {

		File file = new File(strSrcPath);

		if (!file.isDirectory())
			return;
		File files[] = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory())
				copyFiles(files[i].getAbsolutePath(), strDestPath,
						strFilePrefx, isEnd);
			else {
				// if(files[i].getAbsolutePath().endsWith(strFilePrefx))
				if (isMatch(files[i].getName(), isEnd)) {
					System.out.println("准备拷贝：" + files[i].getName());
					File fDest = new File(strDestPath, files[i].getName());
					if (!fDest.getParentFile().exists())
						fDest.getParentFile().mkdirs();
					int blok = 2048;
					byte[] bt = new byte[blok];
					InputStream in = new FileInputStream(files[i]);
					OutputStream out = new FileOutputStream(fDest);
					int count = 0;
					while ((count = in.read(bt)) > 0)
						out.write(bt, 0, count);
					out.flush();
					out.close();
					in.close();
					System.out.println(files[i].getName() + "  拷贝拷贝完毕！");
					ICOUNT++;
				}
			}

		}
	}

	public void delFiles(boolean isOpenFoulder, boolean isFileEnd)
			throws Exception {
		delFiles(strSrcPath, strFilePrefix, isFileEnd);
		if (isOpenFoulder) {
			String strDosComm = "cmd /c  start " + strDestPath;
			Runtime.getRuntime().exec(strDosComm);
		}
	}

	/**
	 * @category 查找、删除文件，递归
	 * @param strSrcPath
	 * @param isEnd
	 *            true:以strFilePrefix结尾的文件名，false：文件名字符串中包含strFilePrefx的文件
	 * @param strFilePrefx
	 * @throws Exception
	 */

	public void delFiles(String strSrcPath, String strFilePrefx,
			boolean isFileEnd) throws Exception {

		File file = new File(strSrcPath);

		if (!file.isDirectory())
			return;
		File files[] = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory())
				delFiles(files[i].getAbsolutePath(), strFilePrefx, isFileEnd);
			else {
				if (isMatch(files[i].getName(), isFileEnd)) {
					String strName = files[i].getName();
					System.out.println("准备删除文件：" + strName);
					files[i].delete();
					System.out.println(strName + "  删除完毕！");
					ICOUNT++;
				}
			}

		}
	}
	public static String getExtension(File f) {
        return (f != null) ? getExtension(f.getName()) : "";
    }
    public static String getExtension(String filename) {
        return getExtension(filename, "");
    }        
    public static String getExtension(String filename, String defExt) {
        if ((filename != null) && (filename.length() > 0)) {
            int i = filename.lastIndexOf('.');
            if ((i >-1) && (i < (filename.length() - 1))) {
                return filename.substring(i + 1);
            }
        }
        return defExt;
    }
    public static String trimExtension(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int i = filename.lastIndexOf('.');
            if ((i >-1) && (i < (filename.length()))) {
                return filename.substring(0, i);
            }
        }
        return filename;
    }
}
