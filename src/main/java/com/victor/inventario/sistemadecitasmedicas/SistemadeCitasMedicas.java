package com.victor.inventario.sistemadecitasmedicas;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// ==========================================
// 1. CONTRATOS / INTERFACES
// ==========================================
interface Autenticable {
    boolean login(String correo, String contrasena);
}

interface Persistente {
    void guardarDatos(String rutaArchivo);
    void cargarDatos(String rutaArchivo);
}

// ==========================================
// 2. CLASE ABSTRACTA BASE
// ==========================================
abstract class Usuario implements Autenticable {
    protected int id;
    protected String nombre;
    protected String correo;
    protected String contrasena;
    protected String rol;

    public Usuario(int id, String nombre, String correo, String contrasena, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    @Override
    public boolean login(String correo, String contrasena) {
        return this.correo.equals(correo) && this.contrasena.equals(contrasena);
    }

    public abstract void mostrarDetalles();
}

// ==========================================
// 3. CLASES ESPECIALIZADAS
// ==========================================
class Doctor extends Usuario {
    private String javaEspecialidad; 

    public Doctor(int id, String nombre, String correo, String contrasena, String specialty) {
        super(id, nombre, correo, contrasena, "Doctor");
        this.javaEspecialidad = specialty; 
    }

    @Override
    public void mostrarDetalles() {
        System.out.println("Doctor ID: " + this.id);
        System.out.println("Nombre: " + this.nombre);
        System.out.println("Especialidad: " + this.javaEspecialidad);
        System.out.println("Correo: " + this.correo);
    }
}

class Paciente extends Usuario {
    private String historialClinico;

    public Paciente(int id, String nombre, String correo, String contrasena, String historialClinico) {
        super(id, nombre, correo, contrasena, "Paciente");
        this.historialClinico = historialClinico;
    }

    @Override
    public void mostrarDetalles() {
        System.out.println("Paciente ID: " + this.id);
        System.out.println("Nombre: " + this.nombre);
        System.out.println("Historial Clínico: " + this.historialClinico);
        System.out.println("Correo: " + this.correo);
    }
}

// ==========================================
// 4. GESTOR DE CITAS
// ==========================================
class GestorCitas {
    private List<Usuario> usuarios;

    public GestorCitas() {
        this.usuarios = new ArrayList<>();
    }

    public void registrarUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
    }
    
    public void listarDoctores() {
        boolean hayDocs = false;
        for (Usuario u : usuarios) {
            if (u instanceof Doctor) {
                u.mostrarDetalles();
                System.out.println("--------------------");
                hayDocs = true;
            }
        }
        if (!hayDocs) System.out.println("No hay doctores registrados.");
    }

    public void listarPacientes() {
        boolean hayPacs = false;
        for (Usuario u : usuarios) {
            if (u instanceof Paciente) {
                u.mostrarDetalles();
                System.out.println("--------------------");
                hayPacs = true;
            }
        }
        if (!hayPacs) System.out.println("No hay pacientes registrados.");
    }
}

// ==========================================
// 5. CLASE PRINCIPAL CON MENÚ INTERACTIVO
// ==========================================
public class SistemadeCitasMedicas {

    public static void main(String[] args) {
        GestorCitas gestor = new GestorCitas();
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n=== SISTERMA DE CITAS MÉDICAS ===");
            System.out.println("1. Registrar Doctor");
            System.out.println("2. Registrar Paciente");
            System.out.println("3. Listar Doctores");
            System.out.println("4. Listar Pacientes");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");
            
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer del scanner

            switch (opcion) {
                case 1:
                    System.out.println("\n--- Registro de Doctor ---");
                    System.out.print("ID: ");
                    int idDoc = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Nombre: ");
                    String nomDoc = scanner.nextLine();
                    System.out.print("Correo: ");
                    String corrDoc = scanner.nextLine();
                    System.out.print("Contraseña: ");
                    String passDoc = scanner.nextLine();
                    System.out.print("Especialidad: ");
                    String espDoc = scanner.nextLine();

                    gestor.registrarUsuario(new Doctor(idDoc, nomDoc, corrDoc, passDoc, espDoc));
                    System.out.println("¡Doctor registrado con éxito!");
                    break;

                case 2:
                    System.out.println("\n--- Registro de Paciente ---");
                    System.out.print("ID: ");
                    int idPac = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Nombre: ");
                    String nomPac = scanner.nextLine();
                    System.out.print("Correo: ");
                    String corrPac = scanner.nextLine();
                    System.out.print("Contraseña: ");
                    String passPac = scanner.nextLine();
                    System.out.print("Historial Clínico / Alergias: ");
                    String histPac = scanner.nextLine();

                    gestor.registrarUsuario(new Paciente(idPac, nomPac, corrPac, passPac, histPac));
                    System.out.println("¡Paciente registrado con éxito!");
                    break;

                case 3:
                    System.out.println("\n--- Lista de Doctores ---");
                    gestor.listarDoctores();
                    break;

                case 4:
                    System.out.println("\n--- Lista de Pacientes ---");
                    gestor.listarPacientes();
                    break;

                case 5:
                    System.out.println("Saliendo del sistema... ¡Hasta luego!");
                    break;

                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 5);

        scanner.close();
    }
}