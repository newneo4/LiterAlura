package alura.literalura.repository;

import alura.literalura.model.Libros;
import alura.literalura.model.LibrosDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ILibrosDbRepository extends JpaRepository<LibrosDb, Long> {
    List<LibrosDb> findAll();

    List<LibrosDb> findByIdiomasContaining(String idioma);

    @Query(value = "SELECT l FROM LibrosDb l ORDER BY l.descargas DESC LIMIT 10")
    List<LibrosDb> buscarTopDiez();
}
