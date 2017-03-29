package com.hospital.web.domain;

public enum Schema {
	PATIENT("pat_id"),DOCTOR("doc_id"),NURSE("nur_id"),ADMIN("id"); /*key값이 된다*/
	private String name;
	Schema(String val){
		this.name=val;
	}
	public String getName(){
		return name; 
	}

}