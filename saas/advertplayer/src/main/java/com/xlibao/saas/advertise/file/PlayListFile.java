package com.xlibao.saas.advertise.file;

import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author user
 *
 */
public class PlayListFile {

	private static final String PLAY_LIST_CONFIG =  "play.lst";//视频播放列表文件
	private static  String playListPath;

	public PlayListFile(){
		playListPath = this.getClass().getResource("/").getPath()+PLAY_LIST_CONFIG;
	}


	/**
	 *
	 * @param conent
	 */
	public void println(String conent) {
		FileWriter fw = null;
		try {
		//
		File f=new File(this.playListPath);
		fw = new FileWriter(f, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintWriter pw = new PrintWriter(fw);
		pw.println(conent);
		pw.flush();
		try {
			fw.flush();
			pw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 *向文件写入内容
	 * @param file
	 * @param conent
	 */
	public static void println(String file, String conent) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
			new FileOutputStream(file, true)));
			out.write(conent+"\r\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
	}
	/**
	 *清除播放列表
	 */
	public  void clearInfoForFile() {
        File file =new File(this.playListPath);
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter =new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	/**
	 * 功能：Java读取txt文件的内容
	 * 步骤：1：先获得文件句柄
	 * 2：获得文件句柄当做是输入一个字节码流，需要对这个输入流进行读取
	 * 3：读取到输入流后，需要读取生成字节流
	 * 备注：需要考虑的是异常情况
	 */
	public  ArrayList<String> readTxtFile(){
		String lineTxt = "";
		ArrayList<String> arrayList = new ArrayList<String>();
		try {
			String encoding="GBK";
			File file =new File(this.playListPath);
			if(file.isFile() && file.exists()){ //判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file),encoding);//考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);

				while((lineTxt = bufferedReader.readLine()) != null){
					arrayList.add(lineTxt.trim());
				}
				read.close();
			}else{
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		return arrayList;
	}
}
