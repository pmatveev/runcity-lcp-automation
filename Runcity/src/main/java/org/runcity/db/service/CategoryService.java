package org.runcity.db.service;

import java.util.List;

import org.runcity.db.entity.Category;
import org.runcity.db.entity.Game;
import org.runcity.exception.DBException;

public interface CategoryService {
	public Category selectById(Long id, boolean games);
	
	public Iterable<Category> selectById(Iterable<Long> id, boolean games);
	
	public List<Category> selectAll(boolean games);
	
	public List<Category> selectUnused(Game g, boolean games);
	
	public Category addOrUpdate(Category c) throws DBException;
	
	public void delete(List<Long> id);
}
