package com.example.storage;

import java.nio.file.Path;
import java.nio.file.Paths;


import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class callStorage {

	public String downLoadFromStorage() {
		
		String msg = null;

		String bucketName = "testunit";
		String srcFilename = "UnitTestDemo.zip";
		Path destFilePath = Paths.get(System.getProperty("user.home") + "/Downloads/UnitTestDemo.zip");
		ClassLoader classLoader = getClass().getClassLoader();

		Storage storage;
		try {
			storage = StorageOptions.newBuilder()
					.setCredentials(
							ServiceAccountCredentials.fromStream(classLoader.getResourceAsStream("authenGCS.json")))
					.build().getService();

			BlobId blobId = BlobId.of(bucketName, srcFilename);
			Blob blob = storage.get(blobId);
			blob.downloadTo(destFilePath);
			
			String zipFilePath = destFilePath.toString();
			String destDir = System.getProperty("user.home") + "/Downloads/";
			unZipIt(zipFilePath, destDir);
			
			msg = "success";
			
		} catch (Exception e) {
			msg = e.toString();
		}
		
		return msg;

	}

	public void unZipIt(String zipFilePath, String destDir) {

		try {
			ZipFile zipFile = new ZipFile(zipFilePath);
			zipFile.extractAll(destDir);
		} catch (ZipException e) {
			e.printStackTrace();
		}
	}

}
