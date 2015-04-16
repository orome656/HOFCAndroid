package com.hofc.hofc.vo;

import java.util.Date;

public class AgendaLineVO {
	private String equipe1;
	private Integer score1;
	private Integer score2;
	private String equipe2;
	private Date date;
    private String title;
	private String idInfos;

	public String getEquipe1() {
		return equipe1;
	}
	public void setEquipe1(String equipe1) {
		this.equipe1 = equipe1;
	}
	public Integer getScore1() {
		return score1;
	}
	public void setScore1(Integer score1) {
		this.score1 = score1;
	}
	public Integer getScore2() {
		return score2;
	}
	public void setScore2(Integer score2) {
		this.score2 = score2;
	}
	public String getEquipe2() {
		return equipe2;
	}
	public void setEquipe2(String equipe2) {
		this.equipe2 = equipe2;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
	public String getIdInfos() {
		return idInfos;
	}
	public void setIdInfos(String idInfos) {
		this.idInfos = idInfos;
	}

}
