package org.iesvdm.proyecto.model.view;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TareaDetail {
    private long id;
    private String nombre;
    private Boolean visible;
}
