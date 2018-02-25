package org.runcity.mvc.web;

import org.apache.log4j.Logger;
import org.runcity.db.service.ControlPointService;
import org.runcity.util.CommonProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FileController {
	private static final Logger logger = Logger.getLogger(FileController.class);

	@Autowired
	private CommonProperties properties;

	@Autowired
	private ControlPointService controlPointService;

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/secure/controlPointImage", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<byte[]> getImageAsByteArray(@RequestParam(required = true) Long id) {
		logger.info("GET /secure/controlPointImage");
		logger.debug("\tid=" + id);

		HttpHeaders headers = new HttpHeaders();
		headers.setCacheControl("max-age=" + properties.getCacheTime() + ", must-revalidate");
		headers.setPragma("");
		return new ResponseEntity<byte[]>(controlPointService.selectById(id, true).getImageData(), headers, HttpStatus.OK);
	}
}