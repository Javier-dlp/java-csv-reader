package com.javacsvreader.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_visit")
@IdClass(UserVisitId.class)
public class UserVisit implements Serializable {

	@Id
	private String email;

	@Id
	private String phone;

	@Id
	private String source;

}
