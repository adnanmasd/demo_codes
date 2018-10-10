package com.madeinw.registration.product.business;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.madeinw.configuration.business.DBConfigurationBean;
import com.madeinw.logging.LogUtil;
import com.madeinw.registration.product.business.exceptions.ExternalException;
import com.madeinw.registration.product.business.exceptions.InternalException;

@ApplicationScoped
public class S3AWSImageSaver implements ImageSaver {

	private static final String AWS_ACCESSKEYID_KEY = "aws.AccessKeyID";
	private static final String AWS_SECRETACCESSKEY_KEY = "aws.SecretAccessKey";
	public static final String S3_FOLDERNAME_KEY = "images.s3.folderName";
	public static final String S3_BUCKETNAME_KEY = "images.s3.bucketName";

	@Inject
	private DBConfigurationBean dbConf;

	private AmazonS3 s3Client;

	@PostConstruct
	public void init() {
		LogUtil.logInfo("Creating S3 Credentials ...");
		AWSCredentials credentials = new BasicAWSCredentials(
				dbConf.getConfigEntry(AWS_ACCESSKEYID_KEY),
				dbConf.getConfigEntry(AWS_SECRETACCESSKEY_KEY));
		LogUtil.logInfo("Creating S3 Client ...");
		s3Client = new AmazonS3Client(credentials);
	}

	public void saveImage(String subDirectory, String fileIndex,
			InputStream imageStream, long contentLength, String contentType)
			throws IOException, ExternalException, InternalException {
		try {
			LogUtil.logInfo("trying trigger putObject Request to S3 client ...");
			s3Client.putObject(createPutRequest(subDirectory, fileIndex,
					imageStream, createMetadata(contentLength, contentType)));
			LogUtil.logInfo("putObject Request sucessfull ...");
		} catch (AmazonServiceException ase) {
			LogUtil.log(
					"Caught an AmazonServiceException, which means your request made it "
							+ "to Amazon S3, but was rejected with an error response for some reason.",
					Level.SEVERE, ase);
			LogUtil.log("HTTP Status Code: " + ase.getStatusCode(),
					Level.SEVERE, null);
			LogUtil.log("AWS Error Code:   " + ase.getErrorCode(),
					Level.SEVERE, null);
			LogUtil.log("Error Type:       " + ase.getErrorType(),
					Level.SEVERE, null);
			LogUtil.log("Request ID:       " + ase.getRequestId(),
					Level.SEVERE, null);
			throw new ExternalException();
		} catch (AmazonClientException ace) {
			LogUtil.log(
					"Caught an AmazonClientException, which means the client encountered "
							+ "a serious internal problem while trying to communicate with S3, "
							+ "such as not being able to access the network.",
					Level.SEVERE, ace);
			throw new InternalException();
		}
	}

	private PutObjectRequest createPutRequest(String subDirectory,
			String fileIndex, InputStream stream, ObjectMetadata metadata) {
		String filePath = dbConf.getConfigEntry(S3_FOLDERNAME_KEY) + "/"
				+ subDirectory + "/" + fileIndex + ".png";
		PutObjectRequest putRequest = new PutObjectRequest(
				dbConf.getConfigEntry(S3_BUCKETNAME_KEY), filePath, stream,
				metadata).withCannedAcl(CannedAccessControlList.PublicRead);
		return putRequest;
	}

	private CopyObjectRequest createCopyObjectRequest(String oldFilePath,
			String newFilePath) {
		CopyObjectRequest copyRequest = new CopyObjectRequest(
				dbConf.getConfigEntry(S3_BUCKETNAME_KEY), oldFilePath,
				dbConf.getConfigEntry(S3_BUCKETNAME_KEY), newFilePath);
		return copyRequest;
	}

	private DeleteObjectRequest createDeleteObjectRequest(String oldFilePath) {
		DeleteObjectRequest deleteRequest = new DeleteObjectRequest(
				dbConf.getConfigEntry(S3_BUCKETNAME_KEY), oldFilePath);
		return deleteRequest;
	}

	private ObjectMetadata createMetadata(long contentLength, String contentType) {
		ObjectMetadata meta = new ObjectMetadata();
		meta.setContentLength(contentLength);
		meta.setContentType(contentType);
		return meta;
	}

	public void replaceImage(String subDirectory, String fileIndex,
			InputStream imageStream, long contentLength, String contentType)
			throws IOException, ExternalException, InternalException {
		LogUtil.logInfo("trying replacing Images ....");
		performImageOperation("replaced", subDirectory, fileIndex);
	}

	public boolean performImageOperation(String action, String subDirectory,
			String fileIndex) throws IOException, ExternalException,
			InternalException {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"dd-MM-yyyy_HH-mm");
			String date = dateFormat.format(new Date());
			String oldFilePath = dbConf.getConfigEntry(S3_FOLDERNAME_KEY) + "/"
					+ subDirectory + "/" + fileIndex + ".png";
			String newFilePath = dbConf.getConfigEntry(S3_FOLDERNAME_KEY) + "/"
					+ subDirectory + "/" + action + "_" + fileIndex + "_"
					+ date + ".png";
			LogUtil.logInfo("Strating copying " + oldFilePath);
			s3Client.copyObject(createCopyObjectRequest(oldFilePath,
					newFilePath));
			LogUtil.logInfo(oldFilePath + " copied successful..");
			if (action.equals("deleted")) {
				LogUtil.logInfo("Strating deleting " + oldFilePath);
				s3Client.deleteObject(createDeleteObjectRequest(oldFilePath));
				LogUtil.logInfo(oldFilePath + " deleted successful...");
			}
			LogUtil.logInfo("Image operations successfull ...");
			return true;
		} catch (AmazonServiceException ase) {
			LogUtil.log(
					"Caught an AmazonServiceException, which means your request made it "
							+ "to Amazon S3, but was rejected with an error response for some reason.",
					Level.SEVERE, ase);
			LogUtil.log("HTTP Status Code: " + ase.getStatusCode(),
					Level.SEVERE, null);
			LogUtil.log("AWS Error Code:   " + ase.getErrorCode(),
					Level.SEVERE, null);
			LogUtil.log("Error Type:       " + ase.getErrorType(),
					Level.SEVERE, null);
			LogUtil.log("Request ID:       " + ase.getRequestId(),
					Level.SEVERE, null);
			throw new ExternalException();
		} catch (AmazonClientException ace) {
			LogUtil.log(
					"Caught an AmazonClientException, which means the client encountered "
							+ "a serious internal problem while trying to communicate with S3, "
							+ "such as not being able to access the network.",
					Level.SEVERE, ace);
			throw new InternalException();
		}
	}
}
