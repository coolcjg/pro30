package com.myspring.pro30.board.vo;

import org.springframework.stereotype.Component;

@Component("Ticket")
public class Ticket {
	
	private int tno;
	private String owner;
	private String grade;
	public int getTno() {
		return tno;
	}
	public void setTno(int tno) {
		this.tno = tno;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	

}
