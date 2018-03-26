package org.runcity.db.entity;

import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "route",  
	uniqueConstraints = @UniqueConstraint(columnNames = {"game__id", "category__id"}))
public class Route {
	public enum SelectMode {
		NONE, WITH_ITEMS;
	}
	
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id", columnDefinition = "int", length = 18, nullable = false)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "game__id", nullable = false)
	private Game game;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "category__id", nullable = false)
	private Category category;
	
	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "route")
	private List<RouteItem> routeItems;
	
	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "route")
	private Set<Team> teams;
	
	@Transient
	private Long maxLeg;
	
	public Route() {
	}

	public Route(Long id, Game game, Category category) {
		setId(id);
		setGame(game);
		setCategory(category);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	public List<RouteItem> getRouteItems() {
		return routeItems;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((game == null) ? 0 : game.hashCode());
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
		Route other = (Route) obj;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (game == null) {
			if (other.game != null)
				return false;
		} else if (!game.equals(other.game))
			return false;
		return true;
	}
	
	public String getControlPointName() {
		if (game == null || category == null) {
			return null;
		}
		return category.getLocalizedName(game.getLocale());
	}
}
