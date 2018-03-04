package org.runcity.db.service;

import java.util.List;

import org.runcity.db.entity.Category;
import org.runcity.db.entity.Game;
import org.runcity.exception.DBException;

public interface CategoryService {
	public Category selectById(Long id, Category.SelectMode selectMode);
	
	public Iterable<Category> selectById(Iterable<Long> id, Category.SelectMode selectMode);
	
	public List<Category> selectAll(Category.SelectMode selectMode);
	
	public List<Category> selectUnused(Game g, Category.SelectMode selectMode);
	
	public Category addOrUpdate(Category c) throws DBException;
	
	public void delete(List<Long> id);
}
