package io.github.luohong.excel;

import io.github.luohong.entity.ExcelInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

import com.google.gson.Gson;

public class ZipUtil {

	private static Gson gson = new Gson();
	private static Type type = new com.google.gson.reflect.TypeToken<List<ExcelInfo>>(){}.getType();

	/**
	 * 文件压缩zip文件
	 * @param srcPathName	目标文件
	 * @param zipFilename	zip压缩或的文件名
	 */
	public static void compress(String srcPathName,String zipFilename){
		File srcdir = new File(srcPathName);
		if(!srcdir.exists()){
			return;
		}

		File zipFile = new File(zipFilename);
		Project pro = new Project();
		Zip zip = new Zip();
		zip.setProject(pro);
		zip.setDestFile(zipFile);
		FileSet fileSet = new FileSet();
		fileSet.setProject(pro);
		fileSet.setDir(srcdir);
		zip.addFileset(fileSet);
		zip.execute();
	}

	/**
	 * 读取json文件找到对应节点的excelinfo对象
	 * @param filepath
	 * @param excelid
	 * @return
	 */
	public static ExcelInfo getExcelJson(String filepath,String excelid) {
		
		File file = new File(filepath);
		BufferedReader reader = null;
		StringBuffer laststr = new StringBuffer("");

		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
			String tempString = null;
			int line = 1;
			while((tempString = reader.readLine()) != null){
				laststr.append(tempString);
				line++;
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		List<ExcelInfo> list = gson.fromJson(laststr.toString(), type);
		if(list != null && list.size() > 0){
			for(ExcelInfo e : list){
				if(excelid.equals(e.getExcelid())){
					return e;
				}
			}
		}
		
		return null;
	}
	
	
	
	public static void main(String[] args) {
		/*ExcelInfo excelinfo = ZipUtil.getExcelJson("C:\\temp.json","day_01");
		System.out.println(excelinfo.getName());
		System.out.println(excelinfo.getType());
		System.out.println(excelinfo.getExcelid());*/
	}
}
