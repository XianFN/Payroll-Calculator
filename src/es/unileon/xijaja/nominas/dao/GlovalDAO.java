/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.unileon.xijaja.nominas.dao;

import es.unileon.xijaja.nominas.modelo.Trabajadorbbdd;
import es.unileon.xijaja.nominas.modelo.HibernateUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import org.hibernate.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * /**
 *
 * @author xiann
 */
public class GlovalDAO {

    private SessionFactory sf;
    private Session session;
    private Transaction tx;

    public GlovalDAO() {
        sf = HibernateUtil.getSessionFactory();//Abrir sesión una vez y queda abierta
        session = sf.openSession();//SessionFactory nos trae la informacion
    }

    public void reset() throws IOException {

        System.out.println("Eliminando y creando base de datos..");
        //File a = new File(PersonalDAO.class.getClassLoader().getResource("resources/xijoja_base_de_datos.sql"));
        InputStream resourceStream = GlovalDAO.class.getResourceAsStream("/resources/BaseDatos.sql");
        InputStreamReader r = new InputStreamReader(resourceStream);
        BufferedReader in = new BufferedReader(r);
        String str;
        StringBuffer sb = new StringBuffer();

        
       
        try {
            

            String line = null;
            // read script line by line
            while ((line = in.readLine()) != null) {
                // execute query
                System.out.print("." +line.length());
                System.out.println(line);
                session.createSQLQuery(line).executeUpdate();

                tx.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // close file reader
            if (in != null) {
                in.close();
            }
        

    }
 
    System.out.println (

"---------------------BASE DE DATOS RESETEADA----------------------");

    }
}
