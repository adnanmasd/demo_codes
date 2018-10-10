package com.madeinw.registration.product.business;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.io.FileDeleteStrategy;

import com.madeinw.configuration.business.DBConfigurationBean;
import com.madeinw.logging.LogUtil;
import com.madeinw.registration.product.business.exceptions.ExternalException;
import com.madeinw.registration.product.business.exceptions.InternalException;

@ApplicationScoped
public class DiskImageSaver implements ImageSaver {

	public static final String IMAGE_UPLOAD_PATH_CONFIG_KEY = "images.disk.uploadPath";

	@Inject
	private DBConfigurationBean dbConf;

	public void saveImage(String subDirectory, String fileIndex,
			InputStream imageStream, long contentLength, String contentType)
			throws InternalException {
		OutputStream outStream = null;
		try {
			LogUtil.logInfo("Saving Image to Disk...");
			LogUtil.logInfo("Making sub directory (" + subDirectory + ")...");
			new File(dbConf.getConfigEntry(IMAGE_UPLOAD_PATH_CONFIG_KEY) + "/"
					+ subDirectory).mkdirs();
			File targetFile = new File(
					dbConf.getConfigEntry(IMAGE_UPLOAD_PATH_CONFIG_KEY) + "/"
							+ subDirectory + "/" + fileIndex + ".png");
			outStream = new FileOutputStream(targetFile);
			LogUtil.logInfo("Writing " + fileIndex + ".png Image to Disk ...");

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = imageStream.read(bytes)) != -1) {
				outStream.write(bytes, 0, read);
			}

			LogUtil.logInfo(fileIndex + ".png saved Successfully...");
		} catch (Exception e) {
			LogUtil.log(fileIndex + ".png not saved...", Level.SEVERE, e);
			throw new InternalException();
		} finally {
			if (outStream != null) {
				try {
					// outputStream.flush();
					outStream.close();
				} catch (IOException e) {
					LogUtil.log(fileIndex
							+ ".png error while closing output Stream...",
							Level.SEVERE, e);
					throw new InternalException();
				}
			}
		}
	}

	public void replaceImage(String subDirectory, String fileIndex,
			InputStream imageStream, long contentLength, String contentType)
			throws InternalException {
		OutputStream outStream = null;
		try {
			LogUtil.logInfo("Saving Image to Disk...");
			File targetFile = new File(
					dbConf.getConfigEntry(IMAGE_UPLOAD_PATH_CONFIG_KEY) + "/"
							+ subDirectory + "/" + fileIndex + ".png");
			outStream = new FileOutputStream(targetFile);
			LogUtil.logInfo("Writing " + fileIndex + ".png Image to Disk ...");
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = imageStream.read(bytes)) != -1) {
				outStream.write(bytes, 0, read);
			}
			LogUtil.logInfo(fileIndex + ".png saved Successfully...");
		} catch (Exception e) {
			LogUtil.log(fileIndex + ".png not saved...", Level.SEVERE, e);
			throw new InternalException();
		} finally {
			if (outStream != null) {
				try {
					// outputStream.flush();
					outStream.close();
				} catch (IOException e) {
					LogUtil.log(fileIndex
							+ ".png error while closing output Stream...",
							Level.SEVERE, e);
					throw new InternalException();
				}
			}
		}
	}

	public boolean performImageOperation(String action, String subDirectory,
			String fileIndex) throws IOException, ExternalException,
			InternalException, InterruptedException {
		try {
			if (renameImage(subDirectory, fileIndex)) {
				if (action.equals("deleted")) {
					if (deleteImage(subDirectory, fileIndex))
						return true;
				} else
					return true;
			}
			return false;
		} catch (Exception e) {
			LogUtil.log("Error Happend while deleting......", Level.SEVERE, e);
			return false;
		}
	}

	private boolean deleteImage(String subDirectory, String fileIndex) {
		Path oldFilePath = Paths.get(dbConf
				.getConfigEntry(IMAGE_UPLOAD_PATH_CONFIG_KEY)
				+ "/"
				+ subDirectory + "/" + fileIndex + ".png");
		try {
			LogUtil.logInfo("Finding " + oldFilePath.toString() + "...");
			if (Files.exists(oldFilePath)) {
				LogUtil.logInfo("Found " + oldFilePath.toString() + "...");
				LogUtil.logInfo("Deleting " + oldFilePath.toString() + "...");
				FileDeleteStrategy.FORCE.deleteQuietly(oldFilePath.toFile());
				LogUtil.logInfo(oldFilePath.toString()
						+ " delete succesfull......");
				return true;
			}
			return false;
		} catch (Exception e) {
			LogUtil.log("Error Happend while deleting......", Level.SEVERE, e);
			return false;
		}
	}

	private boolean renameImage(String subDirectory, String fileIndex)
			throws IOException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH-mm");
		String date = dateFormat.format(new Date());
		Path oldFilePath = Paths.get(dbConf
				.getConfigEntry(IMAGE_UPLOAD_PATH_CONFIG_KEY)
				+ "/"
				+ subDirectory + "/" + fileIndex + ".png");
		Path newFilePath = Paths.get(dbConf
				.getConfigEntry(IMAGE_UPLOAD_PATH_CONFIG_KEY)
				+ "/"
				+ subDirectory + "/deleted_" + fileIndex + "_" + date + ".png");
		try {
			LogUtil.logInfo("Finding " + oldFilePath.toString() + "...");
			if (Files.exists(oldFilePath)) {
				LogUtil.logInfo("Found " + oldFilePath.toString() + "...");
				LogUtil.logInfo("Copying " + oldFilePath.toString() + "...");
				Files.copy(oldFilePath, newFilePath);
				LogUtil.logInfo("Copy successfull ... "
						+ oldFilePath.toString() + "...");
				return true;
			}
			return false;
		} catch (Exception e) {
			LogUtil.log("Error Happend while deleting......", Level.SEVERE, e);
			LogUtil.logInfo("Deleting " + newFilePath.toString() + "...");
			Files.deleteIfExists(newFilePath);
			return false;
		}
	}
}
