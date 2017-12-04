package org.runcity.db.entity;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.runcity.util.StringUtils;

@Entity
@Table(name = "consumer_role")
public class ConsumerRole {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id", columnDefinition = "int", length = 18, nullable = false)
	private Long id;

	@Column(name = "code", length = 32, nullable = false)
	private String code;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "consumer__id", nullable = false)
	private Consumer consumer;

	public ConsumerRole() {
	}

	public ConsumerRole(Long id, String code, Consumer consumer) {
		this.id = id;
		this.code = code;
		this.consumer = consumer;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Consumer getConsumer() {
		return consumer;
	}

	public void setConsumer(Consumer consumer) {
		this.consumer = consumer;
	}
	
	@Override
	public int hashCode() {
		return  (code + consumer.getId()).hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ConsumerRole)) {
			return false;
		}
		
		ConsumerRole r = (ConsumerRole) o;
		return StringUtils.isEqual(code, r.code) && consumer.getId().equals(r.consumer.getId());
	}
}
