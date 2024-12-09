package expresslink.model;

import expresslink.model.*;
import java.sql.Date;

public class BalanceSucursal {
    private int id;
    private Sucursal sucursal;
    private double balanceActual;
    private Date fechaActualizacion;
    private double ingresosPeriodo;
    private double gastosPeriodos;

    //Constructor 

    public BalanceSucursal(int id, Sucursal sucursal, double balanceActual, Date fechaActualizacion, double ingresosPeriodo, double gastosPeriodos) {
        this.id = id;
        this.sucursal = sucursal;
        this.balanceActual = balanceActual;
        this.fechaActualizacion = fechaActualizacion;
        this.ingresosPeriodo = ingresosPeriodo;
        this.gastosPeriodos = gastosPeriodos;
    }

    public int getId() {
        return id;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public double getBalanceActual() {
        return balanceActual;
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public double getIngresosPeriodo() {
        return ingresosPeriodo;
    }

    public double getGastosPeriodos() {
        return gastosPeriodos;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public void setBalanceActual(double balanceActual) {
        this.balanceActual = balanceActual;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public void setIngresosPeriodo(double ingresosPeriodo) {
        this.ingresosPeriodo = ingresosPeriodo;
    }

    public void setGastosPeriodos(double gastosPeriodos) {
        this.gastosPeriodos = gastosPeriodos;
    }
}
