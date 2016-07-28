package Errors;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author don
 */
public class NoPinFound extends RuntimeException {
    public NoPinFound(String error) {
        super(error);
    }
}
