package com.jreader.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileAssistent {
	// Ҫ�������ļ���
	private String strSrcPath;
	// Ҫcopy�ļ�������Ŀ���ļ���
	private String strDestPath;
	// Ҫcopy����ɾ�����ļ��ĺ�׺
	private String strFilePrefix;
	// ����
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
	 * @category �����ļ����еĹؼ���
	 * @param strDest
	 * @param strFilePrefix
	 * @param isEnd
	 *            true:��strFilePrefix��β���ļ�����false���ļ����ַ����а���strFilePrefx���ļ�
	 * @return
	 * @throws Exception
	 */
	public boolean isMatch(String strDest, String strFilePrefix, boolean isEnd)
			throws Exception {

		String strPrefix = "";
		if (null == strFilePrefix)
			throw new Exception("Ҫ���˵��ļ��ؼ��ֲ���Ϊ�գ�");
		if (null == strDest)
			throw new Exception("�ļ�������Ϊ�գ�");
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
	 * @category ���õݹ鷽�����ݹ���ҡ������ļ�
	 * @param isEnd
	 *            true:��strFilePrefix��β���ļ�����false���ļ����ַ����а���strFilePrefx���ļ�
	 * @throws Exception
	 */
	public void copyFiles(boolean isEnd) throws Exception {
		copyFiles(strSrcPath, strDestPath, strFilePrefix, isEnd);
	}

	/**
	 * @category ���أ����õݹ鷽�����ݹ���ҡ������ļ�
	 * @param isEnd
	 *            true:��strFilePrefix��β���ļ�����false���ļ����ַ����а���strFilePrefx���ļ�
	 * @param isOpenDestFoulder
	 *            true:�������Ŀ���ļ��У�false����֮
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
	 * @category ���ҡ������ļ����ݹ�
	 * @param strSrcPath
	 * @param strDestPath
	 * @param isEnd
	 *            true:��strFilePrefix��β���ļ�����false���ļ����ַ����а���strFilePrefx���ļ�
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
					System.out.println("׼��������" + files[i].getName());
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
					System.out.println(files[i].getName() + "  ����������ϣ�");
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
	 * @category ���ҡ�ɾ���ļ����ݹ�
	 * @param strSrcPath
	 * @param isEnd
	 *            true:��strFilePrefix��β���ļ�����false���ļ����ַ����а���strFilePrefx���ļ�
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
					System.out.println("׼��ɾ���ļ���" + strName);
					files[i].delete();
					System.out.println(strName + "  ɾ����ϣ�");
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
