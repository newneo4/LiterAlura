package alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores")
public class AutoresDb {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nombre;
    private Integer nacimiento;
    private Integer fallecimiento;

    @ManyToMany(mappedBy = "autores", fetch = FetchType.EAGER)
    private List<LibrosDb> libros = new ArrayList<>();;

    public AutoresDb() {}

    public AutoresDb(Autores a) {
        this.nombre = a.nombre();
        this.nacimiento = a.nacimiento();
        this.fallecimiento = a.fallecimiento();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getNacimiento() {
        return nacimiento;
    }

    public void setNacimiento(Integer nacimiento) {
        this.nacimiento = nacimiento;
    }

    public Integer getFallecimiento() {
        return fallecimiento;
    }

    public void setFallecimiento(Integer fallecimiento) {
        this.fallecimiento = fallecimiento;
    }

    public List<LibrosDb> getLibros() {
        return libros;
    }

    public void setLibros(List<LibrosDb> libros) {
        this.libros = libros;
    }

    @Override
    public String toString() {
        return '\n' + "--------AUTORES--------" +  '\n' +
                ". Nombre :" + nombre + '\n' +
                ". Nacimiento :" + nacimiento + '\n' +
                ". Fallecimiento :" + fallecimiento;
    }
}
