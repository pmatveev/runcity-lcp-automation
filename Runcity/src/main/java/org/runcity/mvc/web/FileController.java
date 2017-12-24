package org.runcity.mvc.web;

import org.apache.log4j.Logger;
import org.runcity.db.service.ControlPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FileController {
	private static final Logger logger = Logger.getLogger(FileController.class);
	
	@Autowired 
	private ControlPointService controlPointService;

	@RequestMapping(value = "/secure/controlPointImage", method = RequestMethod.GET)
	public @ResponseBody byte[] getImageAsByteArray(@RequestParam(required = true) Long id) {
		logger.info("GET /secure/controlPointImage");
		logger.debug("\tid=" + id);
		
		return controlPointService.selectById(id, true).getImageData();
	}
}