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
import javax.validation.constraints.Size;

/**
 *
 * @author intel core i 7
 */
@Entity
@Table(name = "revisiontemporal")
@NamedQueries({
    @NamedQuery(name = "Revisiontemporal.findAll", query = "SELECT r FROM Revisiontemporal r")})
public class Revisiontemporal implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idRevision")
    private Integer idRevision;
    @Column(name = "fechaRevision")
    @Temporal(TemporalType.DATE)
    private Date fechaRevision;
    @Column(name = "revisado")
    private Boolean revisado;
    @Size(max = 45)
    @Column(name = "tipoRevision")
    private String tipoRevision;

    public Revisiontemporal() {
    }

    public Revisiontemporal(Integer idRevision) {
        this.idRevision = idRevision;
    }

    public Integer getIdRevision() {
        return idRevision;
    }

    public void setIdRevision(Integer idRevision) {
        this.idRevision = idRevision;
    }

    public Date getFechaRevision() {
        return fechaRevision;
    }

    public void setFechaRevision(Date fechaRevision) {
        this.fechaRevision = fechaRevision;
    }

    public Boolean getRevisado() {
        return revisado;
    }

    public void setRevisado(Boolean revisado) {
        this.revisado = revisado;
    }

    public String getTipoRevision() {
        return tipoRevision;
    }

    public void setTipoRevision(String tipoRevision) {
        this.tipoRevision = tipoRevision;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRevision != null ? idRevision.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Revisiontemporal)) {
            return false;
        }
        Revisiontemporal other = (Revisiontemporal) object;
        if ((this.idRevision == null && other.idRevision != null) || (this.idRevision != null && !this.idRevision.equals(other.idRevision))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Revisiontemporal{" + "idRevision=" + idRevision + ", fechaRevision=" + fechaRevision + ", revisado=" + revisado + ", tipoRevision=" + tipoRevision + '}';
    }    
    
}
