package es.unileon.xijaja.nominas;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


//import Excel.ArregloDNINIF;
import Excel.ExcelFile;
import es.unileon.xijaja.nominas.modelo.HibernateUtil;
import es.unileon.xijaja.nominas.dao.WorkerDAO;
import es.unileon.xijaja.nominas.p1.MainWindow;
import java.io.IOException;
import javax.swing.JFrame;




/**
 *
 * @author xiann
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        
       
        MainWindow ventana = new MainWindow();
        
        ventana.setVisible(true);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
       
        
        HibernateUtil.shutdown();
    }
    
}
