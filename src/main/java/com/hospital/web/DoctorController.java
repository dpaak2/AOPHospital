/*package com.hospital.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/doctor")
public class DoctorController {
	private static final Logger logger = LoggerFactory.getLogger(PatientController.class);
 @RequestMapping("/login")
 public String goLogin(){
		logger.info("DoctorController -goLogin() {}","ENTER");
		return "public:common/loginForm";
 }
 @RequestMapping("/detail/{docID}") docID-id 가 된다 
 public String detail(@PathVariable String docID){ 표시는해줘야 한다
		logger.info("DoctorController -goLogin() {}","ENTER");
		//docID=request.getParameter("id"); - 내가 가지고 오는것은 
		return "doctor:doctor/containerDetail";
 }
}

*/