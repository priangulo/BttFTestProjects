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
public class NoTableFound extends RuntimeException {
    public NoTableFound(String error) {
        super(error);
    }
}
