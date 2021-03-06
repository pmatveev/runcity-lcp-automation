package org.runcity.mvc.rest;

import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

import org.runcity.mvc.rest.util.Views;
import org.runcity.util.FileCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;

@RestController
public class RestFileUploader {
	@Autowired
	protected MessageSource messageSource;
	
	@Autowired
	protected FileCache fileCache;

	protected class FileUploadResponse {
		private MessageSource messageSource;
		private Locale locale = LocaleContextHolder.getLocale();

		@JsonView(Views.Public.class)
		private String idt;

		@JsonView(Views.Public.class)
		private String error;

		public FileUploadResponse(MessageSource messageSource) {
			this.messageSource = messageSource;
		}

		public String getIdt() {
			return idt;
		}

		public void setIdt(String idt) {
			this.idt = idt;
		}

		public String getError() {
			return error;
		}
		
		public void setError(String error) {
			setError(error, null);
		}
		
		public void setError(String error, Object[] arguments) {
			this.error = messageSource.getMessage(error, arguments, locale);
		}
	}
	
	@JsonView(Views.Public.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/uploadImage", method = RequestMethod.POST)
	public FileUploadResponse handleImage(@RequestParam MultipartFile image) {
		FileUploadResponse result = new FileUploadResponse(messageSource);
		if (image.isEmpty()) {
			result.setError("common.emptyFile");
			return result;
		}

		try {
			byte[] imageBytes = image.getBytes();
			int tries = 10;
			boolean ok = false;
			
			while (tries > 0 && !ok) {
				String idt = UUID.randomUUID().toString();
				ok = fileCache.put(idt, imageBytes);
				
				if (ok) {
					result.setIdt(idt);
				}
				
				tries--;
			}
		} catch (IOException e) {
			result.setError("common.popupProcessError");
		}
		
		if (result.getIdt() == null) {
			result.setError("common.popupProcessError");
		}
			
		return result;
	}
}
