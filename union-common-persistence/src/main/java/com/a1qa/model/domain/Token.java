package com.a1qa.model.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "token")
@NamedQueries({
        @NamedQuery(name = Token.TOKEN_BY_VALUE, query = "SELECT t FROM Token t WHERE t.value = :tokenValue"),
})

public class Token extends ABaseEntity {
    public static final String TOKEN_BY_VALUE = "Token.findTokenByValue";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "value")
    private String value;

    @JoinColumn(name = "variant_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    @JsonBackReference
    private Variant variant;

    @Column(name = "creation_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Variant getVariant() {
        return variant;
    }

    public void setVariant(Variant variant) {
        this.variant = variant;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }
}
