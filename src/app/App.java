/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.math.BigInteger;
import javax.swing.JOptionPane;

/**
 *
 * @author limbo
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        BigInteger i = new BigInteger(JOptionPane.showInputDialog("Enter Value of p :"));
        
        BigInteger j = new BigInteger(JOptionPane.showInputDialog("Enter Value of e :"));
        
        JOptionPane.showMessageDialog(null,"The number i"+i+"number of j"+j);
        
    }
    
}
