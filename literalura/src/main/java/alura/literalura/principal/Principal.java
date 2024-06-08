package alura.literalura.principal;

import alura.literalura.model.*;
import alura.literalura.repository.IAutoresDbRepository;
import alura.literalura.repository.ILibrosDbRepository;
import alura.literalura.service.ConsumoApi;
import alura.literalura.service.ConvertirDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private ConvertirDatos convertidor = new ConvertirDatos();
    private ConsumoApi consumoApi = new ConsumoApi();

    private ILibrosDbRepository libroRepositorio;
    private IAutoresDbRepository autoresRepositorio;

    @Autowired
    public Principal(ILibrosDbRepository librosRepository, IAutoresDbRepository autoresDbRepository) {
        this.libroRepositorio = librosRepository;
        this.autoresRepositorio = autoresDbRepository;
    }

    public void muestraElMenu(){
        var opcion = -1;

        while(opcion != 0){
            var menu = """
                 1.- Buscar libro por titulo
                 2.- Listar libros registrados
                 3.- Listar autores registrados
                 4.- Listar autores vivos en determinado año
                 5.- Listar libros por idioma
                 6.- Top 10 libros mas buscados
                 7.- Buscar autor por nombre
                 8.- Obtener estadisticas
                 0.- Salir
            """;

            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion){
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listarLibros();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    listarAutoreVivos();
                    break;
                case 5:
                    listarPorIdioma();
                    break;
                case 6:
                    Top10LibrosMasBuscados();
                    break;
                case 7:
                    BuscarAutorPorNombre();
                    break;
                case 8:
                    ObtenerEstadisticas();
                case 0:
                    System.out.println("GRACIAS !! HASTA LA PROXIMA !!!");
                    break;
                default:
                    System.out.println("Introduce una opcion valida");
                    break;
            }
        }
    }

    private void Top10LibrosMasBuscados() {
        List<LibrosDb> topDiez = libroRepositorio.buscarTopDiez();
        if(!topDiez.isEmpty()){
            topDiez.stream()
                    .forEach(System.out::println);
        }
        else{
            System.out.println("No hay libros en la base de datos");
        }
    }

    private void BuscarAutorPorNombre() {
        System.out.println("Introduce el nombre: ");
        String nombre = teclado.nextLine();
        teclado.nextLine();
        List<AutoresDb> autores = autoresRepositorio.buscarPorNombre(nombre);
        if(!autores.isEmpty()){
            autores.stream()
                    .forEach(System.out::println);
        }
        else{
            System.out.println("No hay autores con dicho nombre en la base de datos");
        }
    }

    private void ObtenerEstadisticas() {
        System.out.println("De donde quieres obtener las estadísiticas: ");
        String menu = """
                1 - Gutendex
                2 - Base de datos
                """;
        System.out.println(menu);
        var opcion = teclado.nextInt();
        teclado.nextLine();

        if (opcion == 1) {
            System.out.println("----- ESTADÍSTICAS DE DESCARGAS EN GUTENDEX -----\n");
            var json = consumoApi.ObtenerDatos(URL_BASE);
            Resultados datos = convertidor.obtenerDatos(json, Resultados.class);
            DoubleSummaryStatistics estadisticas = datos.libros().stream()
                    .collect(Collectors.summarizingDouble(Libros::descargas));
            System.out.println("📈 Libro con más descargas: " + estadisticas.getMax());
            System.out.println("📉 Libro con menos descargas: " + estadisticas.getMin());
            System.out.println("📊 Promedio de descargas: " + estadisticas.getAverage());
            System.out.println("\n");
        } else if (opcion == 2) {
            System.out.println("----- ESTADÍSTICAS DE DESCARGAS EN LA BASE DE DATOS -----\n");
            List<LibrosDb> libros = libroRepositorio.findAll();
            if (libros.isEmpty()) {
                System.out.println("No hay libros registrados");
                return;
            }
            DoubleSummaryStatistics estadisticas = libros.stream()
                    .collect(Collectors.summarizingDouble(LibrosDb::getDescargas));
            System.out.println("📈 Libro con más descargas: " + estadisticas.getMax());
            System.out.println("📉 Libro con menos descargas: " + estadisticas.getMin());
            System.out.println("📊 Promedio de descargas: " + estadisticas.getAverage());
            System.out.println("\n");
        } else {
            System.out.println("Opción no válida, intenta de nuevo");
        }
    }

    private void listarPorIdioma() {
        System.out.println("Introduce el idioma : ");
        String idioma = teclado.nextLine();
        teclado.nextLine();
        List<LibrosDb> librosIdioma = libroRepositorio.findByIdiomasContaining(idioma);

        if(!librosIdioma.isEmpty()){
            librosIdioma.stream()
                    .forEach(System.out::println);
        }
        else{
            System.out.println("Aun no hay libros en este idioma");
        }
    }

    private void listarAutoreVivos() {
        System.out.println("Introduce el año : ");
        Integer anio = teclado.nextInt();
        teclado.nextLine();

        List<AutoresDb> autoresVivos = autoresRepositorio.autorPorAnio(anio);

        if (!autoresVivos.isEmpty()){
            autoresVivos.stream()
                    .forEach(System.out::println);
        }
        else{
            System.out.println("Aun no hay autores registrados");
        }
    }

    private void listarAutores() {
        List<AutoresDb> autores = autoresRepositorio.findAll();

        if(!autores.isEmpty()){
            autores.stream()
                    .forEach(System.out::println);
        }
    }

    private void buscarLibroPorTitulo(){
        System.out.println("Escriba el libro que deseas buscar");
        var nombreLibro = teclado.nextLine();
        var json = consumoApi.ObtenerDatos(URL_BASE + nombreLibro.replace(" ","%20"));
        Resultados resultados = convertidor.obtenerDatos(json, Resultados.class);

        if(resultados != null && resultados.libros()!= null && !resultados.libros().isEmpty()){
            Libros libros = resultados.libros().get(0);
            agregarLibro(libros);
        } else {
            System.out.println("""
                    -----------RESULTADOS----------------
                    Libro no encontrado o no existe
                    -------------------------------------
                    """);
        }
    }

    public void agregarLibro(Libros listaLibros) {
        LibrosDb n_libros = new LibrosDb(listaLibros);
        List<AutoresDb> autoresDbList = new ArrayList<>();

        for (Autores autores : listaLibros.autores()) {
            AutoresDb autoresDb = autoresRepositorio.findByNombre(autores.nombre());
            if (autoresDb == null) {
                autoresDb = new AutoresDb(autores);
                autoresRepositorio.save(autoresDb);
            }
            autoresDbList.add(autoresDb);
        }

        n_libros.setAutores(autoresDbList);
        libroRepositorio.save(n_libros);

        for (AutoresDb autoresDb : autoresDbList) {
            if (!autoresDb.getLibros().contains(n_libros)) {
                autoresDb.getLibros().add(n_libros);
            }
        }
    }

    public void listarLibros(){
        List<LibrosDb> libros = libroRepositorio.findAll();

        if(!libros.isEmpty()){
            libros.stream()
                    .forEach(System.out::println);
        }
    }
}
