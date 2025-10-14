package application.service;

import java.util.List;

public interface IService<T> {
    List<T> getAll();
    T getById(int id);
    void add(T entity);
    void update(T entity);
}
