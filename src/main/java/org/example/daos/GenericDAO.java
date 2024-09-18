package org.example.daos;

import java.util.List;

// Interface til at definere generiske CRUD-operationer (bruges til genbrug i dette tilfælde)
public interface GenericDAO<T> {
    // Finder en entitet ved ID (READ)
    T findById(Long id);

    // Henter alle entiteter fra databasen (READ ALL)
    List<T> findAll();

    // Opretter en ny entitet (CREATE)
    T create(T entity);

    // Opdaterer en eksisterende entitet (UPDATE)
    T update(T entity);

    // Sletter en entitet ved ID (DELETE)
    void delete(Long id);
}
