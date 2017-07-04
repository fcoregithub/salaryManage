package cn.com.flaginfo.service.impl;

import cn.com.flaginfo.dao.BaseDao;
import cn.com.flaginfo.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract class BaseServiceImpl<T, PK extends Serializable> implements BaseService<T, PK> {

    @Autowired
    private BaseDao<T, PK> baseDao;


    public void setBaseDao(BaseDao<T, PK> baseDao) {
        this.baseDao = baseDao;
    }

    @Transactional
    @Override
    public void add(T t) {
        baseDao.add(t);
    }

    @Transactional
    @Override
    public void update(T t) {
        baseDao.update(t);
    }

    @Override
    public T getById(PK id) {
        return baseDao.getById(id);
    }

    @Override
    public List<T> getByParams(Map<String, Object> params) {
        return baseDao.getByParams(params);
    }

    @Override
    public List<T> getList(T t) {
        return baseDao.getList(t);
    }

    @Override
    public int getCount(T t) {
        return baseDao.getCount(t);
    }

    @Transactional
    @Override
    public void delete(Map<String, Object> params) {
        baseDao.delete(params);
    }

    @Override
    public void setBaseDao() {

    }

}
