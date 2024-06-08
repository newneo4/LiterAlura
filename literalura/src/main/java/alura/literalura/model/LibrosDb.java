package alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;

import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "libros")
public class LibrosDb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String idiomas;
    private Integer descargas;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "libros_autores",
            joinColumns = @JoinColumn(name = "libro_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id", referencedColumnName = "id")
    )
    private List<AutoresDb> autores;

    public LibrosDb() {}

    public LibrosDb(Libros l){
        this.titulo = l.titulo();
        setIdiomas(l.idiomas());
        this.descargas = l.descargas();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<String> getIdiomas() {
        return Arrays.asList(idiomas.split(","));
    }

    public void setIdiomas(List<String> idiomas) {
        this.idiomas = String.join(",", idiomas);
    }

    public Integer getDescargas() {
        return descargas;
    }

    public void setDescargas(Integer descargas) {
        this.descargas = descargas;
    }

    public List<AutoresDb> getAutores() {
        return autores;
    }

    public void setAutores(List<AutoresDb> autores) {
        this.autores = autores;
    }

    @Override
    public String toString() {
        return "---------LIBRO-----------" + '\n' +
                ", Titulo :'" + titulo + '\n' +
                ", Idiomas :" + idiomas + '\n' +
                ", Descargas :" + descargas + '\n' +
                ", Autores :" + autores + '\n';
    }
}
