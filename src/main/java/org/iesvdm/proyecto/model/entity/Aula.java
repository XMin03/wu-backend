package org.iesvdm.proyecto.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.iesvdm.proyecto.model.view.Option;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
public class Aula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;
    private String grupo;
    @Column(nullable = false)
    private String anio;
    @ManyToMany
    @JsonIgnore
    private Set<Profesor> profesores=new HashSet<>();
    @ManyToOne(optional = false)
    private Profesor propietario;
    @ManyToMany
    @JsonIgnore
    private Set<Estudiante> estudiantes=new HashSet<>();
    @ManyToOne(optional = false)
    private Clase clase;
    @OneToMany(mappedBy = "aula",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Tema> temas;
    @JsonIgnore
    private boolean eliminado;
    @JsonProperty("clase")
    public Option getClaseOption() {
        return new Option(){
            @Override
            public Long getId() {
                return Aula.this.getClase().getId();
            }
            @Override
            public String getNombre() {
                return Aula.this.getClase().getNombre();
            }
        };
    }
    @JsonProperty("propietario")
    public Long getPropietarioId() {
        return this.propietario.getId();
    }
    public String getNombre(){
        return this.clase.getNombre()+" "+this.grupo+" "+this.anio;
    }
}
