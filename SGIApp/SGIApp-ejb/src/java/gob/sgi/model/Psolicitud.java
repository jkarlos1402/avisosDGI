/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.sgi.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "psolicitud")
public class Psolicitud implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdSol")
    private Integer idSol;
    @Column(name = "EvaSoc")
    private Short evaSoc;
    @Column(name = "Ejercicio")
    private Short ejercicio;
    @Column(name = "IdSolPre")
    private Short idSolPre;
    @Column(name = "ObjEstSoc")
    private Short objEstSoc;
    @Column(name = "ObjPryEje")
    private Short objPryEje;
    @Column(name = "ObjDerVia")
    private Short objDerVia;
    @Column(name = "ObjMIA")
    private Short objMIA;
    @Column(name = "ObjObr")
    private Short objObr;
    @Column(name = "ObjAcc")
    private Short objAcc;
    @Column(name = "ObjOtr")
    private Short objOtr;
    @Size(max = 100)
    @Column(name = "ObjOtrObs")
    private String objOtrObs;
    @Column(name = "IdUE")
    private Short idUE;
    @Column(name = "IdSec")
    private Short idSec;
    @Column(name = "IdObr")
    private Integer idObr;
    @Size(max = 255)
    @Column(name = "NomObr")
    private String nomObr;
    @Column(name = "IdModEje")
    private Short idModEje;
    @Column(name = "IdTipObr")
    private Short idTipObr;
    @Column(name = "Monto")
    private BigDecimal monto;
    @Column(name = "MonFed")
    private BigDecimal monFed;
    @Column(name = "MonEst")
    private BigDecimal monEst;
    @Column(name = "MonMun")
    private BigDecimal monMun;
    @Column(name = "IdFteFed")
    private Short idFteFed;
    @Column(name = "IdFteEst")
    private Short idFteEst;
    @Size(max = 40)
    @Column(name = "FteMun")
    private String fteMun;
    @Lob
    @Size(max = 65535)
    @Column(name = "PriCar")
    private String priCar;
    @Column(name = "IdCob")
    private Short idCob;
    @Size(max = 255)
    @Column(name = "NomLoc")
    private String nomLoc;
    @Column(name = "CooGeo")
    private Short cooGeo;
    @Size(max = 100)
    @Column(name = "ObsCoo")
    private String obsCoo;
    @Column(name = "LatIni")
    private BigDecimal latIni;
    @Column(name = "LonIni")
    private BigDecimal lonIni;
    @Column(name = "LatFin")
    private BigDecimal latFin;
    @Column(name = "LonFin")
    private BigDecimal lonFin;
    @Size(max = 200)
    @Column(name = "Imagen")
    private String imagen;
    @Column(name = "Ene")
    private BigDecimal ene;
    @Column(name = "Feb")
    private BigDecimal feb;
    @Column(name = "Mar")
    private BigDecimal mar;
    @Column(name = "Abr")
    private BigDecimal abr;
    @Column(name = "May")
    private BigDecimal may;
    @Column(name = "Jun")
    private BigDecimal jun;
    @Column(name = "Jul")
    private BigDecimal jul;
    @Column(name = "Ago")
    private BigDecimal ago;
    @Column(name = "Sep")
    private BigDecimal sep;
    @Column(name = "Oct")
    private BigDecimal oct;
    @Column(name = "Nov")
    private BigDecimal nov;
    @Column(name = "Dic")
    private BigDecimal dic;
    @Size(max = 255)
    @Column(name = "ObsUE")
    private String obsUE;
    @Size(max = 255)
    @Column(name = "ObsDgi")
    private String obsDgi;
    @Size(max = 255)
    @Column(name = "CriSoc")
    private String criSoc;
    @Size(max = 255)
    @Column(name = "DepNor")
    private String depNor;
    @Lob
    @Size(max = 65535)
    @Column(name = "Justifi")
    private String justifi;
    @Column(name = "DurAgs")
    private Short durAgs;
    @Column(name = "DurMes")
    private Short durMes;
    @Column(name = "IdGpo")
    private Short idGpo;
    @Column(name = "IdTipEva")
    private Short idTipEva;
    @Column(name = "IdEdoSol")
    private Short idEdoSol;
    @Column(name = "Firmado")
    private Short firmado;
    @Column(name = "FecCap")
    @Temporal(TemporalType.DATE)
    private Date fecCap;
    @Column(name = "IdUsu")
    private Short idUsu;
    @Column(name = "Dictamen")
    private Short dictamen;
    @Column(name = "Firmado2")
    private Short firmado2;
    @Column(name = "FecVig")
    @Temporal(TemporalType.DATE)
    private Date fecVig;
    @Column(name = "EstSoc")
    private Boolean estSoc;
    @Column(name = "PryEje")
    private Boolean pryEje;
    @Column(name = "LibTer")
    private Boolean libTer;
    @Column(name = "DocProp")
    private Boolean docProp;
    @Lob
    @Size(max = 16777215)
    @Column(name = "FactLeg")
    private String factLeg;
    @Lob
    @Size(max = 16777215)
    @Column(name = "FactAmb")
    private String factAmb;
    @Lob
    @Size(max = 16777215)
    @Column(name = "FactTec")
    private String factTec;
    @Column(name = "idTipLoc")
    private Boolean idTipLoc;
    @Column(name = "PryImObr")
    private Boolean pryImObr;
    @Column(name = "TerRef")
    private Boolean terRef;
    @Column(name = "DerVia")
    private Boolean derVia;
    @Column(name = "MecSue")
    private Boolean mecSue;
    @Column(name = "TotalPre")
    private BigDecimal totalPre;
    @Column(name = "IdBen")
    private Boolean idBen;
    @Column(name = "IdMet")
    private Short idMet;
    @Column(name = "CanMet")
    private Integer canMet;
    @Column(name = "CanBen")
    private Integer canBen;
    @Column(name = "FecFir1")
    @Temporal(TemporalType.DATE)
    private Date fecFir1;
    @Column(name = "FecFir2")
    @Temporal(TemporalType.DATE)
    private Date fecFir2;
    @Column(name = "FecMod")
    @Temporal(TemporalType.DATE)
    private Date fecMod;
    @Column(name = "FecEnv")
    @Temporal(TemporalType.DATE)
    private Date fecEnv;
    @Column(name = "FecIng")
    @Temporal(TemporalType.DATE)
    private Date fecIng;
    @Column(name = "FecEval")
    @Temporal(TemporalType.DATE)
    private Date fecEval;
    @Column(name = "idTurExp")
    private Integer idTurExp;
    @Column(name = "FecAlt")
    @Temporal(TemporalType.DATE)
    private Date fecAlt;
    @Column(name = "FecRec")
    @Temporal(TemporalType.DATE)
    private Date fecRec;
    @Column(name = "Reingreso")
    private Short reingreso;

    public Psolicitud() {
    }

    public Psolicitud(Integer idSol) {
        this.idSol = idSol;
    }

    public Integer getIdSol() {
        return idSol;
    }

    public void setIdSol(Integer idSol) {
        this.idSol = idSol;
    }

    public Short getEvaSoc() {
        return evaSoc;
    }

    public void setEvaSoc(Short evaSoc) {
        this.evaSoc = evaSoc;
    }

    public Short getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(Short ejercicio) {
        this.ejercicio = ejercicio;
    }

    public Short getIdSolPre() {
        return idSolPre;
    }

    public void setIdSolPre(Short idSolPre) {
        this.idSolPre = idSolPre;
    }

    public Short getObjEstSoc() {
        return objEstSoc;
    }

    public void setObjEstSoc(Short objEstSoc) {
        this.objEstSoc = objEstSoc;
    }

    public Short getObjPryEje() {
        return objPryEje;
    }

    public void setObjPryEje(Short objPryEje) {
        this.objPryEje = objPryEje;
    }

    public Short getObjDerVia() {
        return objDerVia;
    }

    public void setObjDerVia(Short objDerVia) {
        this.objDerVia = objDerVia;
    }

    public Short getObjMIA() {
        return objMIA;
    }

    public void setObjMIA(Short objMIA) {
        this.objMIA = objMIA;
    }

    public Short getObjObr() {
        return objObr;
    }

    public void setObjObr(Short objObr) {
        this.objObr = objObr;
    }

    public Short getObjAcc() {
        return objAcc;
    }

    public void setObjAcc(Short objAcc) {
        this.objAcc = objAcc;
    }

    public Short getObjOtr() {
        return objOtr;
    }

    public void setObjOtr(Short objOtr) {
        this.objOtr = objOtr;
    }

    public String getObjOtrObs() {
        return objOtrObs;
    }

    public void setObjOtrObs(String objOtrObs) {
        this.objOtrObs = objOtrObs;
    }

    public Short getIdUE() {
        return idUE;
    }

    public void setIdUE(Short idUE) {
        this.idUE = idUE;
    }

    public Short getIdSec() {
        return idSec;
    }

    public void setIdSec(Short idSec) {
        this.idSec = idSec;
    }

    public Integer getIdObr() {
        return idObr;
    }

    public void setIdObr(Integer idObr) {
        this.idObr = idObr;
    }

    public String getNomObr() {
        return nomObr;
    }

    public void setNomObr(String nomObr) {
        this.nomObr = nomObr;
    }

    public Short getIdModEje() {
        return idModEje;
    }

    public void setIdModEje(Short idModEje) {
        this.idModEje = idModEje;
    }

    public Short getIdTipObr() {
        return idTipObr;
    }

    public void setIdTipObr(Short idTipObr) {
        this.idTipObr = idTipObr;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public BigDecimal getMonFed() {
        return monFed;
    }

    public void setMonFed(BigDecimal monFed) {
        this.monFed = monFed;
    }

    public BigDecimal getMonEst() {
        return monEst;
    }

    public void setMonEst(BigDecimal monEst) {
        this.monEst = monEst;
    }

    public BigDecimal getMonMun() {
        return monMun;
    }

    public void setMonMun(BigDecimal monMun) {
        this.monMun = monMun;
    }

    public Short getIdFteFed() {
        return idFteFed;
    }

    public void setIdFteFed(Short idFteFed) {
        this.idFteFed = idFteFed;
    }

    public Short getIdFteEst() {
        return idFteEst;
    }

    public void setIdFteEst(Short idFteEst) {
        this.idFteEst = idFteEst;
    }

    public String getFteMun() {
        return fteMun;
    }

    public void setFteMun(String fteMun) {
        this.fteMun = fteMun;
    }

    public String getPriCar() {
        return priCar;
    }

    public void setPriCar(String priCar) {
        this.priCar = priCar;
    }

    public Short getIdCob() {
        return idCob;
    }

    public void setIdCob(Short idCob) {
        this.idCob = idCob;
    }

    public String getNomLoc() {
        return nomLoc;
    }

    public void setNomLoc(String nomLoc) {
        this.nomLoc = nomLoc;
    }

    public Short getCooGeo() {
        return cooGeo;
    }

    public void setCooGeo(Short cooGeo) {
        this.cooGeo = cooGeo;
    }

    public String getObsCoo() {
        return obsCoo;
    }

    public void setObsCoo(String obsCoo) {
        this.obsCoo = obsCoo;
    }

    public BigDecimal getLatIni() {
        return latIni;
    }

    public void setLatIni(BigDecimal latIni) {
        this.latIni = latIni;
    }

    public BigDecimal getLonIni() {
        return lonIni;
    }

    public void setLonIni(BigDecimal lonIni) {
        this.lonIni = lonIni;
    }

    public BigDecimal getLatFin() {
        return latFin;
    }

    public void setLatFin(BigDecimal latFin) {
        this.latFin = latFin;
    }

    public BigDecimal getLonFin() {
        return lonFin;
    }

    public void setLonFin(BigDecimal lonFin) {
        this.lonFin = lonFin;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public BigDecimal getEne() {
        return ene;
    }

    public void setEne(BigDecimal ene) {
        this.ene = ene;
    }

    public BigDecimal getFeb() {
        return feb;
    }

    public void setFeb(BigDecimal feb) {
        this.feb = feb;
    }

    public BigDecimal getMar() {
        return mar;
    }

    public void setMar(BigDecimal mar) {
        this.mar = mar;
    }

    public BigDecimal getAbr() {
        return abr;
    }

    public void setAbr(BigDecimal abr) {
        this.abr = abr;
    }

    public BigDecimal getMay() {
        return may;
    }

    public void setMay(BigDecimal may) {
        this.may = may;
    }

    public BigDecimal getJun() {
        return jun;
    }

    public void setJun(BigDecimal jun) {
        this.jun = jun;
    }

    public BigDecimal getJul() {
        return jul;
    }

    public void setJul(BigDecimal jul) {
        this.jul = jul;
    }

    public BigDecimal getAgo() {
        return ago;
    }

    public void setAgo(BigDecimal ago) {
        this.ago = ago;
    }

    public BigDecimal getSep() {
        return sep;
    }

    public void setSep(BigDecimal sep) {
        this.sep = sep;
    }

    public BigDecimal getOct() {
        return oct;
    }

    public void setOct(BigDecimal oct) {
        this.oct = oct;
    }

    public BigDecimal getNov() {
        return nov;
    }

    public void setNov(BigDecimal nov) {
        this.nov = nov;
    }

    public BigDecimal getDic() {
        return dic;
    }

    public void setDic(BigDecimal dic) {
        this.dic = dic;
    }

    public String getObsUE() {
        return obsUE;
    }

    public void setObsUE(String obsUE) {
        this.obsUE = obsUE;
    }

    public String getObsDgi() {
        return obsDgi;
    }

    public void setObsDgi(String obsDgi) {
        this.obsDgi = obsDgi;
    }

    public String getCriSoc() {
        return criSoc;
    }

    public void setCriSoc(String criSoc) {
        this.criSoc = criSoc;
    }

    public String getDepNor() {
        return depNor;
    }

    public void setDepNor(String depNor) {
        this.depNor = depNor;
    }

    public String getJustifi() {
        return justifi;
    }

    public void setJustifi(String justifi) {
        this.justifi = justifi;
    }

    public Short getDurAgs() {
        return durAgs;
    }

    public void setDurAgs(Short durAgs) {
        this.durAgs = durAgs;
    }

    public Short getDurMes() {
        return durMes;
    }

    public void setDurMes(Short durMes) {
        this.durMes = durMes;
    }

    public Short getIdGpo() {
        return idGpo;
    }

    public void setIdGpo(Short idGpo) {
        this.idGpo = idGpo;
    }

    public Short getIdTipEva() {
        return idTipEva;
    }

    public void setIdTipEva(Short idTipEva) {
        this.idTipEva = idTipEva;
    }

    public Short getIdEdoSol() {
        return idEdoSol;
    }

    public void setIdEdoSol(Short idEdoSol) {
        this.idEdoSol = idEdoSol;
    }

    public Short getFirmado() {
        return firmado;
    }

    public void setFirmado(Short firmado) {
        this.firmado = firmado;
    }

    public Date getFecCap() {
        return fecCap;
    }

    public void setFecCap(Date fecCap) {
        this.fecCap = fecCap;
    }

    public Short getIdUsu() {
        return idUsu;
    }

    public void setIdUsu(Short idUsu) {
        this.idUsu = idUsu;
    }

    public Short getDictamen() {
        return dictamen;
    }

    public void setDictamen(Short dictamen) {
        this.dictamen = dictamen;
    }

    public Short getFirmado2() {
        return firmado2;
    }

    public void setFirmado2(Short firmado2) {
        this.firmado2 = firmado2;
    }

    public Date getFecVig() {
        return fecVig;
    }

    public void setFecVig(Date fecVig) {
        this.fecVig = fecVig;
    }

    public Boolean getEstSoc() {
        return estSoc;
    }

    public void setEstSoc(Boolean estSoc) {
        this.estSoc = estSoc;
    }

    public Boolean getPryEje() {
        return pryEje;
    }

    public void setPryEje(Boolean pryEje) {
        this.pryEje = pryEje;
    }

    public Boolean getLibTer() {
        return libTer;
    }

    public void setLibTer(Boolean libTer) {
        this.libTer = libTer;
    }

    public Boolean getDocProp() {
        return docProp;
    }

    public void setDocProp(Boolean docProp) {
        this.docProp = docProp;
    }

    public String getFactLeg() {
        return factLeg;
    }

    public void setFactLeg(String factLeg) {
        this.factLeg = factLeg;
    }

    public String getFactAmb() {
        return factAmb;
    }

    public void setFactAmb(String factAmb) {
        this.factAmb = factAmb;
    }

    public String getFactTec() {
        return factTec;
    }

    public void setFactTec(String factTec) {
        this.factTec = factTec;
    }

    public Boolean getIdTipLoc() {
        return idTipLoc;
    }

    public void setIdTipLoc(Boolean idTipLoc) {
        this.idTipLoc = idTipLoc;
    }

    public Boolean getPryImObr() {
        return pryImObr;
    }

    public void setPryImObr(Boolean pryImObr) {
        this.pryImObr = pryImObr;
    }

    public Boolean getTerRef() {
        return terRef;
    }

    public void setTerRef(Boolean terRef) {
        this.terRef = terRef;
    }

    public Boolean getDerVia() {
        return derVia;
    }

    public void setDerVia(Boolean derVia) {
        this.derVia = derVia;
    }

    public Boolean getMecSue() {
        return mecSue;
    }

    public void setMecSue(Boolean mecSue) {
        this.mecSue = mecSue;
    }

    public BigDecimal getTotalPre() {
        return totalPre;
    }

    public void setTotalPre(BigDecimal totalPre) {
        this.totalPre = totalPre;
    }

    public Boolean getIdBen() {
        return idBen;
    }

    public void setIdBen(Boolean idBen) {
        this.idBen = idBen;
    }

    public Short getIdMet() {
        return idMet;
    }

    public void setIdMet(Short idMet) {
        this.idMet = idMet;
    }

    public Integer getCanMet() {
        return canMet;
    }

    public void setCanMet(Integer canMet) {
        this.canMet = canMet;
    }

    public Integer getCanBen() {
        return canBen;
    }

    public void setCanBen(Integer canBen) {
        this.canBen = canBen;
    }

    public Date getFecFir1() {
        return fecFir1;
    }

    public void setFecFir1(Date fecFir1) {
        this.fecFir1 = fecFir1;
    }

    public Date getFecFir2() {
        return fecFir2;
    }

    public void setFecFir2(Date fecFir2) {
        this.fecFir2 = fecFir2;
    }

    public Date getFecMod() {
        return fecMod;
    }

    public void setFecMod(Date fecMod) {
        this.fecMod = fecMod;
    }

    public Date getFecEnv() {
        return fecEnv;
    }

    public void setFecEnv(Date fecEnv) {
        this.fecEnv = fecEnv;
    }

    public Date getFecIng() {
        return fecIng;
    }

    public void setFecIng(Date fecIng) {
        this.fecIng = fecIng;
    }

    public Date getFecEval() {
        return fecEval;
    }

    public void setFecEval(Date fecEval) {
        this.fecEval = fecEval;
    }

    public Integer getIdTurExp() {
        return idTurExp;
    }

    public void setIdTurExp(Integer idTurExp) {
        this.idTurExp = idTurExp;
    }

    public Date getFecAlt() {
        return fecAlt;
    }

    public void setFecAlt(Date fecAlt) {
        this.fecAlt = fecAlt;
    }

    public Date getFecRec() {
        return fecRec;
    }

    public void setFecRec(Date fecRec) {
        this.fecRec = fecRec;
    }

    public Short getReingreso() {
        return reingreso;
    }

    public void setReingreso(Short reingreso) {
        this.reingreso = reingreso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSol != null ? idSol.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Psolicitud)) {
            return false;
        }
        Psolicitud other = (Psolicitud) object;
        if ((this.idSol == null && other.idSol != null) || (this.idSol != null && !this.idSol.equals(other.idSol))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Psolicitud{" + "idSol=" + idSol + ", evaSoc=" + evaSoc + ", ejercicio=" + ejercicio + ", idSolPre=" + idSolPre + ", objEstSoc=" + objEstSoc + ", objPryEje=" + objPryEje + ", objDerVia=" + objDerVia + ", objMIA=" + objMIA + ", objObr=" + objObr + ", objAcc=" + objAcc + ", objOtr=" + objOtr + ", objOtrObs=" + objOtrObs + ", idUE=" + idUE + ", idSec=" + idSec + ", idObr=" + idObr + ", nomObr=" + nomObr + ", idModEje=" + idModEje + ", idTipObr=" + idTipObr + ", monto=" + monto + ", monFed=" + monFed + ", monEst=" + monEst + ", monMun=" + monMun + ", idFteFed=" + idFteFed + ", idFteEst=" + idFteEst + ", fteMun=" + fteMun + ", priCar=" + priCar + ", idCob=" + idCob + ", nomLoc=" + nomLoc + ", cooGeo=" + cooGeo + ", obsCoo=" + obsCoo + ", latIni=" + latIni + ", lonIni=" + lonIni + ", latFin=" + latFin + ", lonFin=" + lonFin + ", imagen=" + imagen + ", ene=" + ene + ", feb=" + feb + ", mar=" + mar + ", abr=" + abr + ", may=" + may + ", jun=" + jun + ", jul=" + jul + ", ago=" + ago + ", sep=" + sep + ", oct=" + oct + ", nov=" + nov + ", dic=" + dic + ", obsUE=" + obsUE + ", obsDgi=" + obsDgi + ", criSoc=" + criSoc + ", depNor=" + depNor + ", justifi=" + justifi + ", durAgs=" + durAgs + ", durMes=" + durMes + ", idGpo=" + idGpo + ", idTipEva=" + idTipEva + ", idEdoSol=" + idEdoSol + ", firmado=" + firmado + ", fecCap=" + fecCap + ", idUsu=" + idUsu + ", dictamen=" + dictamen + ", firmado2=" + firmado2 + ", fecVig=" + fecVig + ", estSoc=" + estSoc + ", pryEje=" + pryEje + ", libTer=" + libTer + ", docProp=" + docProp + ", factLeg=" + factLeg + ", factAmb=" + factAmb + ", factTec=" + factTec + ", idTipLoc=" + idTipLoc + ", pryImObr=" + pryImObr + ", terRef=" + terRef + ", derVia=" + derVia + ", mecSue=" + mecSue + ", totalPre=" + totalPre + ", idBen=" + idBen + ", idMet=" + idMet + ", canMet=" + canMet + ", canBen=" + canBen + ", fecFir1=" + fecFir1 + ", fecFir2=" + fecFir2 + ", fecMod=" + fecMod + ", fecEnv=" + fecEnv + ", fecIng=" + fecIng + ", fecEval=" + fecEval + ", idTurExp=" + idTurExp + ", fecAlt=" + fecAlt + ", fecRec=" + fecRec + ", reingreso=" + reingreso + '}';
    }    

}
