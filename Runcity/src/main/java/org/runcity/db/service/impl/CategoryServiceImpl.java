package org.runcity.db.service.impl;

import java.util.List;

import org.hibernate.Hibernate;
import org.runcity.db.entity.Category;
import org.runcity.db.entity.Game;
import org.runcity.db.repository.CategoryRepository;
import org.runcity.db.service.CategoryService;
import org.runcity.exception.DBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = { DBException.class })
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;

	private void initialize(Category c, Category.SelectMode selectMode) {
		if (c == null) {
			return;
		}
		switch (selectMode) {
		case WITH_GAMES:
			Hibernate.initialize(c.getGames());
			break;
		default:
			break;
		}
	}
	
	@Override
	public Category selectById(Long id, Category.SelectMode selectMode) {
		Category c = categoryRepository.findOne(id);
		initialize(c, selectMode);		
		return c;
	}
	

	@Override
	public Iterable<Category> selectById(Iterable<Long> id, Category.SelectMode selectMode) {
		Iterable<Category> categories = categoryRepository.findAll(id);
		
		for (Category c : categories) {
			initialize(c, selectMode);
		}
		
		return categories;
	}

	@Override
	public List<Category> selectAll(Category.SelectMode selectMode) {
		List<Category> categories = categoryRepository.findAll();

		for (Category c : categories) {
			initialize(c, selectMode);
		}
		
		return categories;
	}
	
	@Override
	public List<Category> selectUnused(Game g, Category.SelectMode selectMode) {
		List<Category> categories = categoryRepository.selectUnused(g);
		
		for (Category c : categories) {
			initialize(c, selectMode);
		}
		
		return categories;		
	}

	@Override
	public Category addOrUpdate(Category c) throws DBException {
		try {
			if (c.getId() != null) {
				Category prev = selectById(c.getId(), Category.SelectMode.NONE);
				prev.update(c);
				return categoryRepository.save(prev);
			} else {
				Category n = c.cloneForAdd();
				n = categoryRepository.save(n);
				n.update(c);
				return categoryRepository.save(n);
			}
		} catch (Throwable t) {
			throw new DBException(t);
		}
	}

	private void delete(Long id) {
		categoryRepository.delete(id);
	}

	@Override
	public void delete(List<Long> id) {
		for (Long i : id) {
			delete(i);
		}
	}
}
