package com.victor.inventario.sistemadecitasmedicas;

import java.util.ArrayList;
import java.util.List;

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
// 3. CLASES ESPECIALIZADAS (DOCTOR Y PACIENTE)
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
// 4. GESTOR DE CITAS (LÓGICA DE NEGOCIO)
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
        for (Usuario u : usuarios) {
            if (u instanceof Doctor) {
                u.mostrarDetalles();
                System.out.println("--------------------");
            }
        }
    }

    public void listarPacientes() {
        for (Usuario u : usuarios) {
            if (u instanceof Paciente) {
                u.mostrarDetalles();
                System.out.println("--------------------");
            }
        }
    }
}

// ==========================================
// 5. CLASE PRINCIPAL (EJECUCIÓN)
// ==========================================
public class SistemadeCitasMedicas {

    public static void main(String[] args) {
        GestorCitas gestor = new GestorCitas();
        
        // 1. Prueba de Alta de Doctor
        Doctor nuevoDoc = new Doctor(1, "Dr. Jesus Cazares", "cazares@citas.com", "secure123", "Cardiologia");
        gestor.registrarUsuario(nuevoDoc);
        
        // 2. Prueba de Alta de Paciente
        Paciente nuevoPac = new Paciente(2, "Victor Lopez", "victor@paciente.com", "pass456", "Ninguna alergia registrada");
        gestor.registrarUsuario(nuevoPac);
        
        // Mostrar Resultados en Consola
        System.out.println("=== PROBANDO SISTEMA DE CITAS MÉMICAS ===");
        System.out.println("\n--- Lista de Doctores ---");
        gestor.listarDoctores();
        
        System.out.println("\n--- Lista de Pacientes ---");
        gestor.listarPacientes();
    }
}