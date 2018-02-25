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

	@Override
	public Category selectById(Long id, boolean games) {
		Category c = categoryRepository.findOne(id);
		if (games) {
			Hibernate.initialize(c.getGames());
		}
		return c;
	}
	

	@Override
	public Iterable<Category> selectById(Iterable<Long> id, boolean games) {
		Iterable<Category> categories = categoryRepository.findAll(id);
		
		if (games) {
			for (Category c : categories) {
				Hibernate.initialize(c.getGames());				
			}
		}
		
		return categories;
	}

	@Override
	public List<Category> selectAll(boolean games) {
		List<Category> categories = categoryRepository.findAll();
		
		if (games) {
			for (Category c : categories) {
				Hibernate.initialize(c.getGames());				
			}
		}
		
		return categories;
	}
	
	@Override
	public List<Category> selectUnused(Game g, boolean games) {
		List<Category> categories = categoryRepository.selectUnused(g);
		
		if (games) {
			for (Category c : categories) {
				Hibernate.initialize(c.getGames());				
			}
		}
		
		return categories;		
	}

	@Override
	public Category addOrUpdate(Category c) throws DBException {
		try {
			if (c.getId() != null) {
				Category prev = selectById(c.getId(), false);
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
