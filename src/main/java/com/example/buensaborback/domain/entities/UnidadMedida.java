package com.example.buensaborback.domain.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Builder
public class UnidadMedida extends Base{

    private String denominacion;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "unidadMedida", fetch = FetchType.LAZY)
    Set<Articulo> articulos = new HashSet<>();

}
