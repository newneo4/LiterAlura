package alura.literalura.repository;

import alura.literalura.model.AutoresDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IAutoresDbRepository extends JpaRepository<AutoresDb, Long> {
    AutoresDb findByNombre(String name);
    List<AutoresDb> findAll();

    @Query(value = "SELECT a FROM AutoresDb a WHERE :anio BETWEEN nacimiento AND fallecimiento ")
    List<AutoresDb>autorPorAnio(Integer anio);

    @Query(value = "SELECT a FROM AutoresDb a WHERE a.nombre LIKE %:nombre%")
    List<AutoresDb> buscarPorNombre(String nombre);
}
