package com.madeinw.registration.product.business;

import java.io.IOException;
import java.io.InputStream;

import com.madeinw.registration.product.business.exceptions.ExternalException;
import com.madeinw.registration.product.business.exceptions.InternalException;

public interface ImageSaver {

	public void saveImage(String subDirectory, String fileIndex,
			InputStream imageStream, long contentLength, String contentType)
			throws IOException, ExternalException, InternalException;

	public void replaceImage(String subDirectory, String fileIndex,
			InputStream imageStream, long contentLength, String contentType)
			throws IOException, ExternalException, InternalException;

	public boolean performImageOperation(String action, String subDirectory,
			String fileIndex) throws IOException, ExternalException,
			InternalException, InterruptedException;
}
