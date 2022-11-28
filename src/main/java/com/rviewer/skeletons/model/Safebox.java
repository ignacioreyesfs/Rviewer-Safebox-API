package com.rviewer.skeletons.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "safebox")
public class Safebox {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name="uuid", strategy="uuid2")
	private String id;
	private String name;
	private String password;
	@ElementCollection
	@CollectionTable(name="safebox_items", joinColumns = @JoinColumn(name="safebox_id"))
	@Column(name="item")
	private List<String> items;
	
	public Safebox() {
		items = new ArrayList<>();
	}
}
