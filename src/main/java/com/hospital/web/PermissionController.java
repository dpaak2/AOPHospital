package com.hospital.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.hospital.web.domain.Doctor;
import com.hospital.web.domain.Info;
import com.hospital.web.domain.Nurse;
import com.hospital.web.domain.Patient;
import com.hospital.web.domain.Person;
import com.hospital.web.domain.Enums;
import com.hospital.web.mapper.Mapper;
import com.hospital.web.service.CRUD;

@Controller
@SessionAttributes("permission")
public class PermissionController {
	private static final Logger logger = LoggerFactory.getLogger(PermissionController.class);
	@Autowired
	Mapper mapper;

	@RequestMapping("/login")
	public String login() {
		logger.info("Permission -login() {}",
				"ENTER"); /* goLogin이라는 method안으로 진입하였다 */
		logger.info("Permission -login() {}", "ENTER");
		return "public:common/loginForm";
	}

	@RequestMapping(value = "/{permission}/login", method = RequestMethod.POST) /*
																				 * pathe
																				 * variable을
																				 * 건다
																				 */
	public String goLogin(@RequestParam("id") String id, @RequestParam("password") String password,
			/** 이미 값을 주입받았다 ,그냥 존재한다 */
			@PathVariable String permission, HttpSession session, Model model) throws Exception { /*
																									 * mvc가
																									 * 값을
																									 * 머금고
																									 * 있다가
																									 * id에
																									 * 던지고
																									 * 간다
																									 */
		logger.info("Permission -goLogin() {}",
				"POST"); /* goLogin이라는 method안으로 진입하였다 */
		logger.info("Permission -id, pw () {}", id + "," + password);
		String movePostion = "";
		switch (permission) {
		case "patient":
			Person<?> person = new Person<Info>(new Patient());
			Patient patient = (Patient) person.getInfo();
			patient.setId(id);
			patient.setPass(password);
			Map<String, Object> map = new HashMap<>();
			map.put("group", patient.getGroup());
			map.put("key", Enums.PATIENT.getName());
			map.put("value", id);
			CRUD.Service ex = new CRUD.Service() {
				@Override
				public Object execute(Object o) throws Exception {
					logger.info("=======what is ID?{}====", o);
					return mapper.exist(map);
				}
			};
			Integer count = (Integer) ex.execute(id);
			logger.info("Dose id exsit at DB? () {}", count);

			if (count == 0) {
				logger.info("DB RESULT: {}", "ID dose not exsit");
				movePostion = "public:common/loginForm";
			} else {
				/* map.clear(); map을 재활용할꺼임! */

				CRUD.Service service = new CRUD.Service() {

					@Override
					public Object execute(Object o) throws Exception {
						return mapper.findPatient(map);
					}
				};
				logger.info("DB RESULT: {}", "success");
				patient = (Patient) service.execute(patient);
				if (patient.getPass().equals(password)) {
					session.setAttribute("permission", patient);
					model.addAttribute("user",patient); /* patient에 patient객체를 넣어줌 */
					movePostion = "patient:patient/containerDetail";
				} else {
					logger.info("DB RESULT: {}", "password not match");
					movePostion = "public:common/loginForm";

				}
				patient = (Patient) service.execute(patient); /* db연결 */

			}
			break;

		case "doctor":
			Person<?> docPerson = new Person<Info>(new Doctor());
			Doctor doctor = (Doctor) docPerson.getInfo();
			doctor.setId(id);
			doctor.setPass(password);
			Map<String, Object> docMap = new HashMap<>();
			docMap.put("group", doctor.getGroup());
			docMap.put("key", Enums.DOCTOR.getName());
			docMap.put("value", id);
			CRUD.Service docEx = new CRUD.Service() {
				@Override
				public Object execute(Object o) throws Exception {
					logger.info("===ID ? : {}===", o);
					return mapper.exist(docMap);
				}
			};
			Integer docCount = (Integer) docEx.execute(id);
			logger.info("ID exist? : {}", docCount);

			if (docCount == 0) {
				logger.info("DB RESULT : {}", "ID not exist");
				movePostion = "public:common/loginForm";
			} else {

				CRUD.Service service = new CRUD.Service() {
					@Override
					public Object execute(Object o) throws Exception {
						return mapper.findDoctor(docMap);
					}
				};
				doctor = (Doctor) service.execute(doctor);

				if (doctor.getPass().equals(password)) {
					logger.info("DB RESULT : {}", "success");
					session.setAttribute("permission", doctor);
					model.addAttribute("doctor", doctor);
					movePostion = "doctor:doctor/containerDetail";
				} else {
					logger.info("DB RESULT : {}", "password not match");
					movePostion = "public:common/loginForm";
				}
			}
			break;
		case "nurse":
			Person<?> nurPerson = new Person<Info>(new Nurse());
			Nurse nurse = (Nurse) nurPerson.getInfo();
			nurse.setId(id);
			nurse.setPass(password);
			Map<String, Object> nurMap = new HashMap<>(); /* nurse 값을 받아주는곳 */
			nurMap.put("group", nurse.getGroup());
			nurMap.put("key", Enums.NURSE.getName());
			nurMap.put("value", id);
			CRUD.Service nurEx = new CRUD.Service() {
				@Override
				public Object execute(Object o) throws Exception {
					logger.info("===ID ? : {}===", o);
					return mapper.exist(nurMap);
				}
			};
			Integer nurCount = (Integer) nurEx.execute(id);
			logger.info("ID exist? : {}", nurCount);

			if (nurCount == 0) {
				logger.info("DB RESULT : {}", "ID not exist");
				movePostion = "public:common/loginForm";
			} else {

				CRUD.Service service = new CRUD.Service() {
					@Override
					public Object execute(Object o) throws Exception {
						return mapper.findNurse(nurMap);
					}
				};
				nurse = (Nurse) service.execute(nurse);

				if (nurse.getPass().equals(password)) {
					logger.info("DB RESULT : {}", "success");
					session.setAttribute("permission", nurse);
					model.addAttribute("nurse", nurse);
					movePostion = "nurse:nurse/containerList";
				} else {
					logger.info("DB RESULT : {}", "password not match");
					movePostion = "public:common/loginForm";
				}
				break;

			}

		}

		return movePostion;
	}

	@RequestMapping("/logout")
	public String logout(HttpSession session) { /* logout */
		session.invalidate(); /* session에 있는값을 다 지우는것 */
		return "redirect:/"; /*
								 * 다른곳을 보내는것 (/)를 사용하게 되면 경로를 알아서 쫒아간다 home
								 * controller
								 */
	}
}
