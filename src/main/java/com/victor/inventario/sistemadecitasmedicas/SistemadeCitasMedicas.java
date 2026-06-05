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
    public int getId() { return id; }
    public String getNombre() { return nombre; }
}

// ==========================================
// 3. CLASES ESPECIALIZADAS (USUARIOS)
// ==========================================
class Doctor extends Usuario {
    private String javaEspecialidad; 

    public Doctor(int id, String nombre, String correo, String contrasena, String specialty) {
        super(id, nombre, correo, contrasena, "Doctor");
        this.javaEspecialidad = specialty; 
    }

    @Override
    public void mostrarDetalles() {
        System.out.println("Doctor ID: " + this.id + " | Nombre: " + this.nombre + " | Especialidad: " + this.javaEspecialidad);
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
        System.out.println("Paciente ID: " + this.id + " | Nombre: " + this.nombre + " | Historial: " + this.historialClinico);
    }
}

// ==========================================
// 4. CLASE COMPONENTE: CITA MÉDICA
// ==========================================
class Cita {
    private int idCita;
    private Doctor doctor;
    private Paciente paciente;
    private String fecha;
    private String hora;

    public Cita(int idCita, Doctor doctor, Paciente paciente, String fecha, String hora) {
        this.idCita = idCita;
        this.doctor = doctor;
        this.paciente = paciente;
        this.fecha = fecha;
        this.hora = hora;
    }

    public void mostrarCita() {
        System.out.println("Cita ID: " + idCita + " | Fecha: " + fecha + " | Hora: " + hora);
        System.out.println("   -> Doctor: " + doctor.getNombre());
        System.out.println("   -> Paciente: " + paciente.getNombre());
        System.out.println("----------------------------------------");
    }
}

// ==========================================
// 5. GESTOR DE CITAS (LÓGICA DE NEGOCIO)
// ==========================================
class GestorCitas {
    private List<Usuario> usuarios;
    private List<Cita> citas;

    public GestorCitas() {
        this.usuarios = new ArrayList<>();
        this.citas = new ArrayList<>();
    }

    public void registrarUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
    }

    public void agendarCita(Cita cita) {
        this.citas.add(cita);
    }
    
    // Buscar usuarios específicos por ID para enlazarlos en la cita
    public Doctor buscarDoctor(int id) {
        for (Usuario u : usuarios) {
            if (u instanceof Doctor && u.getId() == id) {
                return (Doctor) u;
            }
        }
        return null;
    }

    public Paciente buscarPaciente(int id) {
        for (Usuario u : usuarios) {
            if (u instanceof Paciente && u.getId() == id) {
                return (Paciente) u;
            }
        }
        return null;
    }
    
    public void listarDoctores() {
        for (Usuario u : usuarios) {
            if (u instanceof Doctor) u.mostrarDetalles();
        }
    }

    public void listarPacientes() {
        for (Usuario u : usuarios) {
            if (u instanceof Paciente) u.mostrarDetalles();
        }
    }

    public void listarCitas() {
        if (citas.isEmpty()) {
            System.out.println("No hay citas agendadas en el sistema.");
            return;
        }
        for (Cita c : citas) {
            c.mostrarCita();
        }
    }
}

// ==========================================
// 6. CLASE PRINCIPAL CON MENÚ COMPLETO
// ==========================================
public class SistemadeCitasMedicas {

    public static void main(String[] args) {
        GestorCitas gestor = new GestorCitas();
        Scanner scanner = new Scanner(System.in);
        int opcion;

        // Datos de prueba iniciales para facilitar tus pruebas
        gestor.registrarUsuario(new Doctor(101, "Dr. Jesus Cazares", "cazares@citas.com", "123", "Cardiologia"));
        gestor.registrarUsuario(new Paciente(201, "Miguel Lopez", "miguel@mail.com", "456", "Ninguna"));

        do {
            System.out.println("\n=== SISTEMA DE CITAS MÉDICAS ===");
            System.out.println("1. Registrar Doctor");
            System.out.println("2. Registrar Paciente");
            System.out.println("3. Listar Doctores");
            System.out.println("4. Listar Pacientes");
            System.out.println("5. Agendar Cita Médica");
            System.out.println("6. Listar Citas Agendadas");
            System.out.println("7. Salir");
            System.out.print("Seleccione una opción: ");
            
            opcion = scanner.nextInt();
            scanner.nextLine(); 

            switch (opcion) {
                case 1:
                    System.out.println("\n--- Registro de Doctor ---");
                    System.out.print("ID: "); int idDoc = scanner.nextInt(); scanner.nextLine();
                    System.out.print("Nombre: "); String nomDoc = scanner.nextLine();
                    System.out.print("Correo: "); String corrDoc = scanner.nextLine();
                    System.out.print("Contraseña: "); String passDoc = scanner.nextLine();
                    System.out.print("Especialidad: "); String espDoc = scanner.nextLine();
                    gestor.registrarUsuario(new Doctor(idDoc, nomDoc, corrDoc, passDoc, espDoc));
                    System.out.println("¡Doctor registrado!");
                    break;

                case 2:
                    System.out.println("\n--- Registro de Paciente ---");
                    System.out.print("ID: "); int idPac = scanner.nextInt(); scanner.nextLine();
                    System.out.print("Nombre: "); String nomPac = scanner.nextLine();
                    System.out.print("Correo: "); String corrPac = scanner.nextLine();
                    System.out.print("Contraseña: "); String passPac = scanner.nextLine();
                    System.out.print("Historial Clínico: "); String histPac = scanner.nextLine();
                    gestor.registrarUsuario(new Paciente(idPac, nomPac, corrPac, passPac, histPac));
                    System.out.println("¡Paciente registrado!");
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
                    System.out.println("\n--- Agendar Nueva Cita ---");
                    System.out.print("ID de la Cita: "); int idCita = scanner.nextInt();
                    System.out.print("ID del Doctor: "); int docId = scanner.nextInt();
                    System.out.print("ID del Paciente: "); int pacId = scanner.nextInt();
                    scanner.nextLine(); // Limpiar buffer
                    System.out.print("Fecha (DD/MM/AAAA): "); String fecha = scanner.nextLine();
                    System.out.print("Hora (HH:MM): "); String hora = scanner.nextLine();

                    Doctor docAsignado = gestor.buscarDoctor(docId);
                    Paciente pacAsignado = gestor.buscarPaciente(pacId);

                    if (docAsignado != null && pacAsignado != null) {
                        gestor.agendarCita(new Cita(idCita, docAsignado, pacAsignado, fecha, hora));
                        System.out.println("¡Cita agendada con éxito!");
                    } else {
                        System.out.println("Error: El ID del Doctor o del Paciente no existen.");
                    }
                    break;

                case 6:
                    System.out.println("\n--- Citas Médicas Programadas ---");
                    gestor.listarCitas();
                    break;

                case 7:
                    System.out.println("Saliendo del sistema...");
                    break;

                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 7);

        scanner.close();
    }
}