package org.iesvdm.proyecto.exeption;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message) {
        super(message);
    }
    public NotFoundException(long id,String c) {
        super("Not found "+ c +" with id: " + id);
    }
}
