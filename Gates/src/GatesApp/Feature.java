package GatesApp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author dsb
 */
public @interface Feature {
    
   public static boolean   base        = true;
   public static boolean   tables      = true;
   public static boolean   constraints = true;
   public static boolean   eval        = true;
   
   boolean value();
}
