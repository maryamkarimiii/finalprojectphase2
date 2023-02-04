package ir.maktab.finalprojectphase2.service;

public interface BaseService<T> {
    void save(T t);

    void update(T t);
}
