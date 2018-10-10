package com.madeinw.search.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.madeinw.configuration.business.DBConfigurationBean;
import com.madeinw.registration.product.business.ChosenImageSaver;
import com.madeinw.registration.product.business.DiskImageSaver;
import com.madeinw.registration.product.business.ImageSaver;
import com.madeinw.registration.product.business.S3AWSImageSaver;
import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLMapping;

@ManagedBean
@RequestScoped
@URLMapping(id = "images", viewId = "/empty.xhtml", pattern = "/images/#{images.productId}/#{images.image}")
public class Images {

	public static final String IMAGE_UPLOAD_PATH_CONFIG_KEY = "images.disk.uploadPath";
	public static final String DIRECTORY_SEPARATOR = "/";
	private static final String imagesUploadLocationKey = "images.location";
	private static final String imagesAccessProtocol = "images.s3.accessProtocol";
	private static final String s3ZoneName = "images.s3.zoneName";

	@Inject
	private DBConfigurationBean dbConf;

	private String productId;
	private String image;

	public String generateImageURL(String productId, String image) {
		if (dbConf.getConfigEntry(imagesUploadLocationKey).equals("s3")) {
			return getS3ImageURL(productId, image);
		} else
			return getDiskImageURL(productId, image);
	}

	private String getDiskImageURL(String productId, String image) {
		return DIRECTORY_SEPARATOR + "images" + DIRECTORY_SEPARATOR + productId + DIRECTORY_SEPARATOR + image;
	}

	private String getS3ImageURL(String productId, String image) {
		return dbConf.getConfigEntry(imagesAccessProtocol) + "://" + dbConf.getConfigEntry(s3ZoneName) + DIRECTORY_SEPARATOR
				+ dbConf.getConfigEntry(S3AWSImageSaver.S3_BUCKETNAME_KEY) + DIRECTORY_SEPARATOR
				+ dbConf.getConfigEntry(S3AWSImageSaver.S3_FOLDERNAME_KEY) + DIRECTORY_SEPARATOR + productId + DIRECTORY_SEPARATOR + image + ".png";
	}

	@URLAction
	public void render() throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();

		response.setContentType("image/png");
		try {
			response.getOutputStream().write(loadImageFromDisk(image));
		} catch (NumberFormatException e) {
			handleNotFound(context, response);
		} catch (FileNotFoundException e) {
			handleNotFound(context, response);
		}

		context.responseComplete();

	}

	private void handleNotFound(FacesContext context, HttpServletResponse response) throws IOException {
		String contextPath = ((HttpServletRequest) context.getExternalContext().getRequest()).getContextPath();
		response.sendRedirect(contextPath + "/404");
	}

	public byte[] loadImageFromDisk(String fileIndex) throws FileNotFoundException, IOException {
		File targetFile = new File(dbConf.getConfigEntry(IMAGE_UPLOAD_PATH_CONFIG_KEY) + "/" + productId + "/" + fileIndex + ".png");
		return IOUtils.toByteArray(new FileInputStream(targetFile));

	}

	@Produces
	@ChosenImageSaver
	@ApplicationScoped
	public ImageSaver imageSaver(DiskImageSaver diskImageSaver, S3AWSImageSaver s3ImageSaver) {
		String location = dbConf.getConfigEntry(imagesUploadLocationKey);
		if (location.equals("s3")) {
			return s3ImageSaver;
		} else
			return diskImageSaver;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImage() {
		return image;
	}

}