package Modelo;

public class Cliente {
    private long idCliente;
    private String rucDni;
    private String tipoEmpresa;
    private String ciudad;
    private String nombreEmpresa;
    private String codigoPostal;
    private String direccion;
    private String correo;
    private String telefonoContacto;
    private String personalContacto;

    public Cliente() {}

    public Cliente(long idCliente, String rucDni, String tipoEmpresa, String ciudad,
                   String nombreEmpresa, String codigoPostal, String direccion,
                   String correo, String telefonoContacto, String personalContacto) {
        this.idCliente = idCliente;
        this.rucDni = rucDni;
        this.tipoEmpresa = tipoEmpresa;
        this.ciudad = ciudad;
        this.nombreEmpresa = nombreEmpresa;
        this.codigoPostal = codigoPostal;
        this.direccion = direccion;
        this.correo = correo;
        this.telefonoContacto = telefonoContacto;
        this.personalContacto = personalContacto;
    }

    public long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(long idCliente) {
        this.idCliente = idCliente;
    }

    public String getRucDni() {
        return rucDni;
    }

    public void setRucDni(String rucDni) {
        this.rucDni = rucDni;
    }

    public String getTipoEmpresa() {
        return tipoEmpresa;
    }

    public void setTipoEmpresa(String tipoEmpresa) {
        this.tipoEmpresa = tipoEmpresa;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public String getPersonalContacto() {
        return personalContacto;
    }

    public void setPersonalContacto(String personalContacto) {
        this.personalContacto = personalContacto;
    }
}
