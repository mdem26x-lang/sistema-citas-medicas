package com.victor.inventario.sistemadecitasmedicas;

import java.io.*;
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
    public String getCorreo() { return correo; }
    public String getContrasena() { return contrasena; }
    public String getRol() { return rol; }
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

    public String getEspecialidad() { return javaEspecialidad; }

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

    public String getHistorialClinico() { return historialClinico; }

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

    public int getIdCita() { return idCita; }
    public Doctor getDoctor() { return doctor; }
    public Paciente getPaciente() { return paciente; }
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }

    public void mostrarCita() {
        System.out.println("Cita ID: " + idCita + " | Fecha: " + fecha + " | Hora: " + hora);
        System.out.println("   -> Doctor: " + doctor.getNombre());
        System.out.println("   -> Paciente: " + paciente.getNombre());
        System.out.println("----------------------------------------");
    }
}

// ==========================================
// 5. GESTOR DE CITAS (CON PERSISTENCIA DE ARCHIVOS)
// ==========================================
class GestorCitas implements Persistente {
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
    
    public Doctor buscarDoctor(int id) {
        for (Usuario u : usuarios) {
            if (u instanceof Doctor && u.getId() == id) return (Doctor) u;
        }
        return null;
    }

    public Paciente buscarPaciente(int id) {
        for (Usuario u : usuarios) {
            if (u instanceof Paciente && u.getId() == id) return (Paciente) u;
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
        for (Cita c : citas) c.mostrarCita();
    }

    // INTERFAZ PERSISTENTE: GUARDAR DATOS EN TXT
    @Override
    public void guardarDatos(String rutaArchivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo))) {
            // Guardar Usuarios
            for (Usuario u : usuarios) {
                if (u instanceof Doctor) {
                    Doctor d = (Doctor) u;
                    writer.println("DOCTOR," + d.getId() + "," + d.getNombre() + "," + d.getCorreo() + "," + d.getContrasena() + "," + d.getEspecialidad());
                } else if (u instanceof Paciente) {
                    Paciente p = (Paciente) u;
                    writer.println("PACIENTE," + p.getId() + "," + p.getNombre() + "," + p.getCorreo() + "," + p.getContrasena() + "," + p.getHistorialClinico());
                }
            }
            // Guardar Citas
            for (Cita c : citas) {
                writer.println("CITA," + c.getIdCita() + "," + c.getDoctor().getId() + "," + c.getPaciente().getId() + "," + c.getFecha() + "," + c.getHora());
            }
            System.out.println("¡Datos guardados correctamente en " + rutaArchivo + "!");
        } catch (IOException e) {
            System.out.println("Error al guardar datos: " + e.getMessage());
        }
    }

    // INTERFAZ PERSISTENTE: CARGAR DATOS DESDE TXT
    @Override
    public void cargarDatos(String rutaArchivo) {
        File file = new File(rutaArchivo);
        if (!file.exists()) {
            System.out.println("Archivo de datos no encontrado. Iniciando sistema vacío.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");
                String tipo = datos[0];

                if (tipo.equals("DOCTOR")) {
                    registrarUsuario(new Doctor(Integer.parseInt(datos[1]), datos[2], datos[3], datos[4], datos[5]));
                } else if (tipo.equals("PACIENTE")) {
                    registrarUsuario(new Paciente(Integer.parseInt(datos[1]), datos[2], datos[3], datos[4], datos[5]));
                } else if (tipo.equals("CITA")) {
                    int idCita = Integer.parseInt(datos[1]);
                    Doctor doc = buscarDoctor(Integer.parseInt(datos[2]));
                    Paciente pac = buscarPaciente(Integer.parseInt(datos[3]));
                    if (doc != null && pac != null) {
                        agendarCita(new Cita(idCita, doc, pac, datos[4], datos[5]));
                    }
                }
            }
            System.out.println("¡Datos cargados exitosamente desde " + rutaArchivo + "!");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error al cargar datos: " + e.getMessage());
        }
    }
}

// ==========================================
// 6. CLASE PRINCIPAL DEL SISTEMA
// ==========================================
public class SistemadeCitasMedicas {

    public static void main(String[] args) {
        GestorCitas gestor = new GestorCitas();
        Scanner scanner = new Scanner(System.in);
        String archivoDatos = "citas_medicas.txt";
        int opcion;

        // Cargar datos automáticamente al iniciar el programa
        gestor.cargarDatos(archivoDatos);

        do {
            System.out.println("\n=== SISTEMA DE CITAS MÉDICAS ===");
            System.out.println("1. Registrar Doctor");
            System.out.println("2. Registrar Paciente");
            System.out.println("3. Listar Doctores");
            System.out.println("4. Listar Pacientes");
            System.out.println("5. Agendar Cita Médica");
            System.out.println("6. Listar Citas Agendadas");
            System.out.println("7. Guardar y Salir");
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
                    System.out.println("¡Doctor registrado localmente!");
                    break;

                case 2:
                    System.out.println("\n--- Registro de Paciente ---");
                    System.out.print("ID: "); int idPac = scanner.nextInt(); scanner.nextLine();
                    System.out.print("Nombre: "); String nomPac = scanner.nextLine();
                    System.out.print("Correo: "); String corrPac = scanner.nextLine();
                    System.out.print("Contraseña: "); String passPac = scanner.nextLine();
                    System.out.print("Historial Clínico: "); String histPac = scanner.nextLine();
                    gestor.registrarUsuario(new Paciente(idPac, nomPac, corrPac, passPac, histPac));
                    System.out.println("¡Paciente registrado localmente!");
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
                    scanner.nextLine(); 
                    System.out.print("Fecha (DD/MM/AAAA): "); String fecha = scanner.nextLine();
                    System.out.print("Hora (HH:MM): "); String hora = scanner.nextLine();

                    Doctor docAsignado = gestor.buscarDoctor(docId);
                    Paciente pacAsignado = gestor.buscarPaciente(pacId);

                    if (docAsignado != null && pacAsignado != null) {
                        gestor.agendarCita(new Cita(idCita, docAsignado, pacAsignado, fecha, hora));
                        System.out.println("¡Cita agendada localmente!");
                    } else {
                        System.out.println("Error: El ID del Doctor o del Paciente no existen.");
                    }
                    break;

                case 6:
                    System.out.println("\n--- Citas Médicas Programadas ---");
                    gestor.listarCitas();
                    break;

                case 7:
                    System.out.println("\nGuardando datos en el archivo antes de salir...");
                    gestor.guardarDatos(archivoDatos);
                    System.out.println("Saliendo del sistema... ¡Proyecto terminado!");
                    break;

                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 7);

        scanner.close();
    }
}