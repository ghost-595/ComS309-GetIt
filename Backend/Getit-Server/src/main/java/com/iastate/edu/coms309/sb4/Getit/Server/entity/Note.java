package com.iastate.edu.coms309.sb4.Getit.Server.entity;


import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

@Entity
@Table(name="NOTE_LOB_TABLE")
public class Note {

	@Id
	private Integer id;

	//private Course course;

	@Column(name="COURSE")
	private String course;
	
	@Column(name="DATE")
	private String date;
	
	@Column(name="TEXT")
	private String text;
	
	@Column(name="PAGES")
	private int pages;
	
	@Lob
	@Column(name="DATA")
	private byte[] data;

	public Note(int id, String course, String date, String text, int pages, String Data) {
		this.id = new Integer(id);
		this.course = course;
		this.date = date;
		this.text = text;
		this.pages = pages;
		this.data = Data.getBytes ();
		System.out.println (new String (data));
	}

	public Note () {
		
	}
	
	public String getCourse () {
		return course;
	}
	
	public String getDate () {
		return date;
	}
	
	public String getText () {
		return text;
	}
	
	public int getPages () {
		return pages;
	}
	
	public byte[] getData () {
		return data;
	}
	
	public long getId () {
		return id;
	}
}
