package Modelo;

public class Permiso {
    private long idPermiso;
    private String rol;
    private String acceso;

    public Permiso() {}

    public Permiso(long idPermiso, String rol, String acceso) {
        this.idPermiso = idPermiso;
        this.rol = rol;
        this.acceso = acceso;
    }

    public long getIdPermiso() {
        return idPermiso;
    }

    public void setIdPermiso(long idPermiso) {
        this.idPermiso = idPermiso;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getAcceso() {
        return acceso;
    }

    public void setAcceso(String acceso) {
        this.acceso = acceso;
    }
}
