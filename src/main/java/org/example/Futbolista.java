package org.example;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Futbolista {

    private String nombre;
    private String apellidos;
    private Integer edad;
    private List<String> demarcacion;
    private Boolean internacional;

    public Futbolista() {
    }

    public Futbolista(String nombre, String apellidos, Integer edad, List<String> demarcacion, Boolean internacional) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.edad = edad;
        this.demarcacion = demarcacion;
        this.internacional = internacional;
    }

    public Futbolista(Document document) {
        this.nombre = document.getString("nombre");
        this.apellidos = document.getString("apellidos");
        this.edad = document.getInteger("edad");
        this.demarcacion = document.getList("demarcacion", String.class);
        this.internacional = document.getBoolean("internacional");
    }

    public Document toDocument() {
        return new Document("nombre", this.nombre)
                .append("apellidos", this.apellidos)
                .append("edad", this.edad)
                .append("demarcacion", this.demarcacion)
                .append("internacional", this.internacional);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public List<String> getDemarcacion() {
        return demarcacion;
    }

    public void setDemarcacion(List<String> demarcacion) {
        this.demarcacion = demarcacion;
    }

    public Boolean getInternacional() {
        return internacional;
    }

    public void setInternacional(Boolean internacional) {
        this.internacional = internacional;
    }

    @Override
    public String toString() {
        return "Nombre: " + this.getNombre() + " " + this.getApellidos() +
                " / Edad: " + this.edad +
                " / Demarcación: " + this.demarcacion.toString() +
                " / Internacional: " + this.internacional;
    }
}
