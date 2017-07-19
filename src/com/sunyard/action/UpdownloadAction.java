package com.sunyard.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sunyard.base.model.Consts;
import com.sunyard.util.SysParamUtil;

@Controller
@Scope("prototype")
public abstract class UpdownloadAction extends BaseAction {

	// 文件上传、下载路径
	public String FilePath = SysParamUtil
			.getSysParamValueStr(Consts.UPLOAD_PATH);

	// 文件路径
	private String paths = ""; // 多文件下载多个路径

	public String exportTitles = ""; // 获取选中的列名称

	public File[] upload; // 得到上传的文件
	public String[] uploadContentType; // 得到文件的类型
	public String[] uploadFileName; // 得到文件的名称

	public String jumpToUploadIndex() throws Exception {
		return "main";
	}

	public String toExcel() throws Exception {
		return "query";
	}

	/**
	 * 文件上传
	 * 
	 * @param response
	 * @param upload
	 *            上传文件 upload 得到上传文件,uploadContentType 得到文件类型 uploadFileName
	 *            得到上传文件名 fileNameType 文件名方式 1--随机生成 0--按原文件名
	 */
	public Map<String, String> doUploadFile(int fileNameType) throws Exception {
		if (upload == null) {
			throw new NullPointerException();
		}
		Map<String, String> path_name = new HashMap<String, String>();
		// String realpath = "c:" + File.separator + "user";
		String realpath = FilePath;
		File file = new File(realpath);
		if (!file.exists())
			file.mkdirs();
		for (int i = 0; i < upload.length; i++) {
			File uploadImage = upload[i];
			String suffix = uploadPostFix(uploadFileName[i]);
			String old_fileName = uploadFileName[i].toString();// 获取文件名
			int start = old_fileName.lastIndexOf("\\");// 2,索引到最后一个反斜杠
			String fileName = old_fileName.substring(start + 1);// 3,截取(+1是去掉反斜杠)
			// 生成文件名
			start = fileName.lastIndexOf("."); // 索引到最后一个点
			if (fileNameType == 1) {
				fileName = UUID.randomUUID().toString()
						+ fileName.substring(start);
			} else {
				fileName = fileName.substring(0, start) // 不含扩展名的文件
						+ UUID.randomUUID().toString() // 随机数
						+ fileName.substring(start); // 扩展名

			}
			uploadFileName[i] = fileName;
			FileUtils.copyFile(uploadImage, new File(file, uploadFileName[i]));
			path_name.put(realpath, fileName);
			path_name.put("realpath", realpath + fileName);
			path_name.put("filename", fileName);
		}
		return path_name;
	}

	public String uploadPostFix(String str) {
		String[] s = str.split("\\.");
		int a = s.length;
		String result = s[a - 1];
		return result;
	}

	/**
	 * 文件批量下载准备，此方法需要提供List<String> fileNameList的文件名。
	 * 
	 * @param response
	 * @param str
	 *            paths List<String> fileNameList
	 */
	public void doDownloadFile(List<String> fileNameList) throws Exception {
		List<String> paramList = new ArrayList<String>();
		String[] paramArr = paths.split(",");
		for (int i = 0; i < paramArr.length; i++) {
			String item = paramArr[i].toString();
			paramList.add(item);
		}
		logger.info("准备下载的文件：" + paramList);
		// 生成的ZIP文件名为Files.zip
		String tmpFileName = "Files.zip";
		byte[] buffer = new byte[1024];
		String strZipPath = FilePath + tmpFileName;

		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
				strZipPath));
		// 需要同时下载的N个文件
		for (int i = 0; i < paramList.size(); i++) {
			File tmp_file = new File(paramList.get(i));

			if (!tmp_file.exists())
				throw new FileNotFoundException();

			FileInputStream fis = new FileInputStream(tmp_file);
			out.putNextEntry(new ZipEntry(fileNameList.get(i)
					+ downloadSufFix(tmp_file.getName())));
			// out.putNextEntry(new ZipEntry(tmp_file.getName()));
			// 设置压缩文件内的字符编码，不然会变成乱码
			out.setEncoding("GBK");
			int len;
			// 读入需要下载的文件的内容，打包到zip文件
			while ((len = fis.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			out.closeEntry();
			fis.close();
		}
		out.close();
		this.downFile(tmpFileName);
	}

	/**
	 * 文件下载方法
	 * 
	 * @param response
	 * @param str
	 */
	private void downFile(String str) throws Exception {
		String path = FilePath + str;
		File file = new File(path);
		if (file.exists()) {
			InputStream ins = new FileInputStream(path);
			BufferedInputStream bins = new BufferedInputStream(ins);// 放到缓冲流里面
			OutputStream outs = response.getOutputStream();// 获取文件输出IO流
			BufferedOutputStream bouts = new BufferedOutputStream(outs);
			response.setContentType("application/x-download");// 设置response内容的类型
			response.setHeader("Content-disposition", "attachment;filename=\""
					+ URLEncoder.encode(str, "UTF-8") + "\"");// 设置头部信息
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			// 开始向网络传输文件流
			while ((bytesRead = bins.read(buffer, 0, 8192)) != -1) {
				bouts.write(buffer, 0, bytesRead);
			}
			bouts.flush();// 这里一定要调用flush()方法
			ins.close();
			bins.close();
			outs.close();
			bouts.close();
		}
	}

	/**
	 * 获取下载时文件后缀名
	 * 
	 * @param response
	 * @param fileName
	 */
	private String downloadSufFix(String fileName) {
		int index = fileName.lastIndexOf(".");
		int length = fileName.length();
		String suffix = fileName.substring(index, length);
		return suffix;
	}

	public String getPaths() {
		return paths;
	}

	public void setPaths(String paths) {
		this.paths = paths;
	}

	public File[] getUpload() {
		return upload;
	}

	public void setUpload(File[] upload) {
		this.upload = upload;
	}

	public String[] getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String[] uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public String[] getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String[] uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String getExportTitles() {
		return exportTitles;
	}

	public void setExportTitles(String exportTitles) {
		this.exportTitles = exportTitles;
	}

}
