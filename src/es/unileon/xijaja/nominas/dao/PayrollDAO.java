/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.unileon.xijaja.nominas.dao;

import es.unileon.xijaja.nominas.modelo.HibernateUtil;
import es.unileon.xijaja.nominas.modelo.Nomina;
import es.unileon.xijaja.nominas.modelo.Trabajadorbbdd;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author Javier
 */
public class PayrollDAO {

    private SessionFactory sf;
    private Session session;
    private Transaction tx;


    public PayrollDAO() {
        sf = HibernateUtil.getSessionFactory();//Abrir sesión una vez y queda abierta
        session = sf.openSession();//SessionFactory nos trae la informacion
     
    }

    //TODO mover a categoriaDAO
    public boolean aumentaDoscientos(String categoria) {//Ejercicio 2

        try {
            tx = session.beginTransaction();
            String HQL = "UPDATE Categorias n SET SalarioBaseCategoria=SalarioBaseCategoria+200 WHERE n.nombreCategoria!=:param1";
            session.createQuery(HQL).setParameter("param1", categoria).executeUpdate();
            tx.commit();
            return true;
        } catch (Exception e) {
            System.out.println("Error al aumentar el salario base de las categorias: " + e.getMessage());
            return false;
        }

    }

    public void deletePayroll(int idWorker) {
        
        try {
            tx = session.beginTransaction();
            String HQLBorrarNomina = "DELETE Nomina n WHERE n.trabajadorbbdd.id=:param2";
            session.createQuery(HQLBorrarNomina).setParameter("param2", idWorker).executeUpdate();

            System.out.println(HQLBorrarNomina + " " + idWorker);
            
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error al borrar las nominas " + e.getMessage());

        }
    }
    
    public int ExistPayRoll(String month, String year,Trabajadorbbdd worker , double brut, double liquid) {

     
        int month2=Integer.parseInt(month);
        int year2=Integer.parseInt(year);
    

        String hql = "FROM Nomina n WHERE n.mes=:param1 and n.anio=:param2 and n.trabajadorbbdd=:param3 and n.brutoNomina=:param4 and n.liquidoNomina=:param5";
        Query query = session.createQuery(hql);//Va a devolver UNA LISTA
        query.setParameter("param1", month2);
        query.setParameter("param2", year2);
        query.setParameter("param3", worker);
        query.setParameter("param4", brut);
        query.setParameter("param5", liquid);

     //   System.out.println(query.toString());
        
        List<Nomina> payrollList = query.list();
      //  System.out.println("                            "+ cartegoryList.size());
        //Comprobacion si trae uno, ninguno…
        if (payrollList.isEmpty()) {
            
           return 0;
        } else {
            return payrollList.get(0).getIdNomina();
        }


    }
  public void addPayRoll(ArrayList<String> datosNomina, ArrayList<String> trabajador, String month, String year,int id) {
        WorkerDAO wDAO = new WorkerDAO();
      Trabajadorbbdd worker = wDAO.getworker(trabajador.get(4),trabajador.get(5),trabajador.get(6),trabajador.get(7),trabajador.get(3));
        try { 
         
            Transaction tx1 = session.beginTransaction();
            
            Nomina e = new Nomina(worker, Integer.parseInt(month), Integer.parseInt(year) ,Integer.parseInt(datosNomina.get(13)),Double.parseDouble(datosNomina.get(14)),Double.parseDouble(datosNomina.get(10)),Double.parseDouble(datosNomina.get(12)),Double.parseDouble(datosNomina.get(11)),Double.parseDouble(datosNomina.get(3)),Double.parseDouble(datosNomina.get(22)),Double.parseDouble(datosNomina.get(23)),Double.parseDouble(datosNomina.get(27)),Double.parseDouble(datosNomina.get(28)),Double.parseDouble(datosNomina.get(29)),Double.parseDouble(datosNomina.get(30)),Double.parseDouble(datosNomina.get(31)),Double.parseDouble(datosNomina.get(32)),Double.parseDouble(datosNomina.get(33)),Double.parseDouble(datosNomina.get(34)),Double.parseDouble(datosNomina.get(35)),Double.parseDouble(datosNomina.get(36)),Double.parseDouble(datosNomina.get(37)),Double.parseDouble(datosNomina.get(16)),Double.parseDouble(datosNomina.get(17)),Double.parseDouble(datosNomina.get(18)),Double.parseDouble(datosNomina.get(19)),Double.parseDouble(datosNomina.get(20)),Double.parseDouble(datosNomina.get(21)),Double.parseDouble(datosNomina.get(25)),Double.parseDouble(datosNomina.get(26)),Double.parseDouble(datosNomina.get(39)));
             /*
            List<Nomina> listanominas_bbdd = session.createQuery("from Nomina ").list();
        
            listanominas_bbdd.add(e);
            
            for (Nomina em : listanominas_bbdd){ 
        
                session.save(em);
            }
        */
              if (id>0) {
                e.setIdNomina(id);
                session.update(e);
            }else{
                session.saveOrUpdate(e);
            }
            tx1.commit();
           
        } catch (Exception e) {
            System.out.println("Fallo al añadir la nomina " + e.getMessage());
        }

    }
    

}
