package vn.techmaster.topcar.repository;

import java.util.List;
import java.util.Optional;

public interface CRUD<T> {
    public void readCSV(String file);
    public List<T> getAll();
    public Optional<T> get(int id);
    public void add(T t);
    public void update(T t);
    public void delete(T t);
    public void deleteById(int id);
    public List<T> searchByKeyword(String keyword);
}
