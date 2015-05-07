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
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author intel core i 7
 */
@Entity
@Table(name = "movbco")
@NamedQueries({
    @NamedQuery(name = "Movbco.findAll", query = "SELECT m FROM Movbco m")})
public class Movbco implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idmovbco")
    private Integer idmovbco;
    @Column(name = "IdBco")
    private Integer idBco;
    @Column(name = "fecMov")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecMov;
    @Size(max = 45)
    @Column(name = "tipMov")
    private String tipMov;
    @Lob
    @Size(max = 16777215)
    @Column(name = "obs")
    private String obs;
    @Size(max = 45)
    @Column(name = "status")
    private String status;

    public Movbco() {
    }

    public Movbco(Integer idmovbco) {
        this.idmovbco = idmovbco;
    }

    public Integer getIdmovbco() {
        return idmovbco;
    }

    public void setIdmovbco(Integer idmovbco) {
        this.idmovbco = idmovbco;
    }

    public Integer getIdBco() {
        return idBco;
    }

    public void setIdBco(Integer idBco) {
        this.idBco = idBco;
    }

    public Date getFecMov() {
        return fecMov;
    }

    public void setFecMov(Date fecMov) {
        this.fecMov = fecMov;
    }

    public String getTipMov() {
        return tipMov;
    }

    public void setTipMov(String tipMov) {
        this.tipMov = tipMov;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idmovbco != null ? idmovbco.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Movbco)) {
            return false;
        }
        Movbco other = (Movbco) object;
        if ((this.idmovbco == null && other.idmovbco != null) || (this.idmovbco != null && !this.idmovbco.equals(other.idmovbco))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gob.sgi.model.Movbco[ idmovbco=" + idmovbco + " ]";
    }
    
}
