/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.sgi.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author intel core i 7
 */
@Entity
@Table(name = "relsolbco")
@NamedQueries({
    @NamedQuery(name = "Relsolbco.findAll", query = "SELECT r FROM Relsolbco r")})
public class Relsolbco implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdBco")
    private Integer idBco;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdSol")
    private int idSol;
    @Column(name = "Status")
    private Integer status;
    @Column(name = "FecReg")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecReg;

    public Relsolbco() {
    }

    public Relsolbco(Integer idBco) {
        this.idBco = idBco;
    }

    public Relsolbco(Integer idBco, int idSol) {
        this.idBco = idBco;
        this.idSol = idSol;
    }

    public Integer getIdBco() {
        return idBco;
    }

    public void setIdBco(Integer idBco) {
        this.idBco = idBco;
    }

    public int getIdSol() {
        return idSol;
    }

    public void setIdSol(int idSol) {
        this.idSol = idSol;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getFecReg() {
        return fecReg;
    }

    public void setFecReg(Date fecReg) {
        this.fecReg = fecReg;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idBco != null ? idBco.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Relsolbco)) {
            return false;
        }
        Relsolbco other = (Relsolbco) object;
        if ((this.idBco == null && other.idBco != null) || (this.idBco != null && !this.idBco.equals(other.idBco))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Relsolbco{" + "idBco=" + idBco + ", idSol=" + idSol + ", status=" + status + ", fecReg=" + fecReg + '}';
    }    
    
}
