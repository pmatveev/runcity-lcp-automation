package org.runcity.db.entity;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.runcity.db.entity.enumeration.SecureUserRole;

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

	public ConsumerRole(Long id, SecureUserRole role, Consumer consumer) {
		setId(id);
		setRole(role);
		setConsumer(consumer);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SecureUserRole getRole() {
		return SecureUserRole.getByStoredValue(code);
	}

	public void setRole(SecureUserRole role) {
		this.code = SecureUserRole.getStoredValue(role);
	}

	public Consumer getConsumer() {
		return consumer;
	}

	public void setConsumer(Consumer consumer) {
		this.consumer = consumer;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((consumer == null) ? 0 : consumer.hashCode());
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
		ConsumerRole other = (ConsumerRole) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (consumer == null) {
			if (other.consumer != null)
				return false;
		} else if (!consumer.equals(other.consumer))
			return false;
		return true;
	}
}
