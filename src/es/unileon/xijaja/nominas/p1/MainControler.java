/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.unileon.xijaja.nominas.p1;

import Excel.ExcelFile;
import Excel.CalcularIBAN;
import es.unileon.xijaja.nominas.dao.GlovalDAO;
import es.unileon.xijaja.nominas.dao.PayrollDAO;
import es.unileon.xijaja.nominas.dao.WorkerDAO;
import es.unileon.xijaja.nominas.modelo.Trabajadorbbdd;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.nashorn.internal.objects.Global;

/**
 *
 * @author Javier
 */
public class MainControler implements ActionListener {

    private MainWindow ventana;
           
    private ExcelFile excel;

    public MainControler(MainWindow ventana) {

        this.ventana = ventana;
         excel = new ExcelFile();

    }

    @Override
    public void actionPerformed(ActionEvent arg0) {

        switch (arg0.getActionCommand()) {
            
           
            case "Nominas":
                ventana.jLabel1.setForeground(Color.black);
                 ventana.jLabel1.setText("CALCULANDO NOMINA");
                  try {
                //System.out.println(ventana.monthField.getText() +"--"+ventana.yearField .getText());
               
                excel.editExcel(ventana.monthField.getText(), ventana.yearField.getText());
                ventana.jLabel1.setText("FINALIZADO");
                    
        //        excel.calcularNóminas(ventana.monthField.getText(), ventana.yearField.getText());
                 } catch (IOException ex) {
                     ventana.jLabel1.setForeground(Color.red);
                      ventana.jLabel1.setText("ERROR");
                break;
                 }
        }

    }

}
