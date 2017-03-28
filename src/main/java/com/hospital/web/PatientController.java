package com.hospital.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.hospital.web.domain.Patient;
import com.hospital.web.mapper.PatientMapper;
import com.hospital.web.service.CRUD;

@Controller
@RequestMapping("/patient")
public class PatientController {
	private static final Logger logger = LoggerFactory.getLogger(PatientController.class);
	@Autowired
	Patient patient;/* 로딩하는순간 연결끝 -mvc가 던져주고 간다. */
	@Autowired
	PatientMapper mapper;

	@RequestMapping("/join") /* mvc가 차끌고 다니면서 wiring 하고 있다. */
	public String join() {
		logger.info("PatientController -join() {}",
				"ENTER"); /* 매 method마다 가져다 부친다 */
		return "public:patient/registerForm";
	}

	@RequestMapping("/login")
	public String login() {
		logger.info("PatientController -login() {}",
				"ENTER"); /* goLogin이라는 method안으로 진입하였다 */
		logger.info("PatientController -login() {}", "ENTER");
		return "public:common/loginForm";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String goLogin(@RequestParam("id") String id,
			@RequestParam("password") String password, /*
														 * 이미 값을 주입받았다 ,그냥 존재한다
														 */
			Model model) throws Exception { /* mvc가 값을 머금고 있다가 id에 던지고 간다 */
		logger.info("PatientController -goLogin() {}",
				"POST"); /* goLogin이라는 method안으로 진입하였다 */
		logger.info("PatientController -id, pw () {}", id + "," + password);
		patient.setPatID(id);
		patient.setPatPass(password);

		/* int count=service.count(); 존재여부를 확인 serviceImpl */
		CRUD.Service ex = new CRUD.Service() {
			@Override
			public Object excute(Object o) throws Exception {
				logger.info("=======what is ID?{}====", o);
				return mapper.exist(id);
			}
		};
		Integer count = (Integer) ex.excute(id);
		logger.info("Dose id exsit at DB? () {}", count);
		String movePostion = "";
		if (count == 0) {
			logger.info("DB RESULT: {}", "ID dose not exsit");
			movePostion = "public:common/loginForm";
		} else {
			CRUD.Service service = new CRUD.Service() {

				@Override
				public Object excute(Object o) throws Exception {
					return mapper.selectById(id);
				}
			};
			logger.info("DB RESULT: {}", "success");
			if (patient.getPatPass().equals(password)) {
				model.addAttribute("patient",
						patient); /* patient에 patient객체를 넣어줌 */
				movePostion = "patient:patient/containerDetail";
			} else {
				logger.info("DB RESULT: {}", "password not match");
				movePostion = "public:common/loginForm";

			}
			patient = (Patient) service.excute(patient); /* db연결 */

		}
		return movePostion;
	}

	@RequestMapping("/doctor/{docID}")
	public String getDoctorInfo(@PathVariable String docID) {
		logger.info("PatientController -goLogin() {}",
				"ENTER"); /* goLogin이라는 method안으로 진입하였다 */
		logger.info("PatientController -docID=() {}", docID);
		return "patient:patient/doctorInfo";
	}
}
