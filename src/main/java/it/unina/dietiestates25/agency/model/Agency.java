package it.unina.dietiestates25.agency.model;

import jakarta.persistence.*;

@Entity
public class Agency {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;
    @Column(
            name = "ragione_sociale",
            unique = true,
            nullable = false
    )
    private String ragioneSociale;
    @Column(
            name = "partita_iva",
            unique = true,
            nullable = false
    )
    private String partitaIva;
    public Agency() {}

    public Agency(String ragioneSociale, String partitaIva) {
        this.ragioneSociale = ragioneSociale;
        this.partitaIva = partitaIva;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRagioneSociale() {
        return ragioneSociale;
    }

    public void setRagioneSociale(String ragioneSociale) {
        this.ragioneSociale = ragioneSociale;
    }

    public String getPartitaIva() {
        return partitaIva;
    }

    public void setPartitaIva(String partitaIva) {
        this.partitaIva = partitaIva;
    }

    @Override
    public String toString() {
        return "Agency{" +
                "id='" + id + '\'' +
                ", ragioneSociale='" + ragioneSociale + '\'' +
                ", partitaIva='" + partitaIva + '\'' +
                '}';
    }
}
