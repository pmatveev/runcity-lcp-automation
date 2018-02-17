package org.runcity.db.entity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.persistence.*;

import org.apache.commons.codec.binary.Hex;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "token")
public class Token {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id", columnDefinition = "int", length = 18, nullable = false)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "consumer__id", nullable = false)
	private Consumer consumer;

	@Column(name = "date_from", columnDefinition = "datetime", nullable = false)
	private Date dateFrom;

	@Column(name = "date_to", columnDefinition = "datetime", nullable = false)
	private Date dateTo;

	@Column(name = "token", length = 32, unique = true, nullable = false)
	private String token;
	
	public Token() {
	}

	public Token(Long id, Consumer consumer, Date dateFrom, Date dateTo, String token) {
		setId(id);
		setConsumer(consumer);
		setDateFrom(dateFrom);
		setDateTo(dateTo);
		setToken(token);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Consumer getConsumer() {
		return consumer;
	}

	public void setConsumer(Consumer consumer) {
		this.consumer = consumer;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Token other = (Token) obj;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		return true;
	}
	
	public String check() {
		if (id == null) {
			return null;
		}
		
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		    md.update(id.toString().getBytes());
		    return Hex.encodeHexString(md.digest());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}
