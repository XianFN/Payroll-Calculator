
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.unileon.xijaja.nominas.dao;

import es.unileon.xijaja.nominas.modelo.Categorias;
import es.unileon.xijaja.nominas.modelo.Empresas;
import es.unileon.xijaja.nominas.modelo.Trabajadorbbdd;
import es.unileon.xijaja.nominas.modelo.HibernateUtil;
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
public class WorkerDAO {

    private SessionFactory sf;
    private Session session;
    private Transaction tx;

 //   private PayrollDAO nDAO;
    private CategoryDAO cDAO;
     private CompanyDAO comDAO;
  
    public WorkerDAO() {
        sf = HibernateUtil.getSessionFactory();//Abrir sesión una vez y queda abierta
        session = sf.openSession();//SessionFactory nos trae la informacion
   //     this.nDAO = new PayrollDAO();
        this.cDAO = new CategoryDAO();
        this.comDAO = new CompanyDAO();
    }

    //Traer un trabajador mediante nif
    public Trabajadorbbdd getTrabajador(String nif) {
        Trabajadorbbdd seleccionado = null;

        try {
            //CONSULTA HQL
            String HQL = "FROM Trabajadorbbdd t WHERE t.nifnie=:param1";//Me traigo TODA la tabla de trabajadores, llamo t como alias
            Query query = session.createQuery(HQL);//Va a devolver UNA LISTA
            query.setParameter("param1", nif);
            List workersList = query.list();
            //Comprobacion si trae uno, ninguno…
            if (!workersList.isEmpty()) {
                seleccionado = (Trabajadorbbdd) workersList.get(0);
            }//Si la lista no está vacia

        } catch (Exception e) {
            System.out.println("Error al consultar el trabajador: " + e.getMessage());
        }

        return seleccionado;
    }

    /**
     *
     * @param idCompany
     *
     * Metodo
     */
    /*
    public void deleteWorkers(int idCompany) {

        Trabajadorbbdd worker = null;

        try {

            String HQL = "FROM Trabajadorbbdd t WHERE t.empresas.id=:param1";
            Query query = session.createQuery(HQL);
            query.setParameter("param1", idCompany);
            List workersList = query.list();
            System.out.println("longitud: " + workersList.size());
            for (int i = 0; i < workersList.size(); i++) {

                tx = session.beginTransaction();
                worker = ((Trabajadorbbdd) workersList.get(i));
                System.out.println("idTrabajador: " + worker.getIdTrabajador() + " " + i);

                nDAO.deletePayroll(worker.getIdTrabajador());//LLamammos al DAO de nomimas y borramos las nominass

                String HQLBorrarTrabajador = "DELETE Trabajadorbbdd t WHERE t.id=:param2";
                session.createQuery(HQLBorrarTrabajador).setParameter("param2", worker.getIdTrabajador()).executeUpdate();

                tx.commit();
            }

        } catch (Exception e) {
            System.out.println("Error al Borrar el trabajador " + e.getMessage());
        }

    }
*/
    public int ExistWorker(String name, String surname1, String surname2, String NIF, String date) {

        //01/01/2009
        Trabajadorbbdd worker = null;

        String[] a = date.split("/");

        String b = a[2] + "-" + a[1] + "-" + a[0];

        Date d = Date.valueOf(b);


        String hql = "FROM Trabajadorbbdd t WHERE t.nombre=:param1 and t.apellido1=:param2 and t.apellido2=:param3 and t.nifnie=:param4 and t.fechaAlta=:param5";
        Query query = session.createQuery(hql);//Va a devolver UNA LISTA
        query.setParameter("param1", name);
        query.setParameter("param2", surname1);
        query.setParameter("param3", surname2);
        query.setParameter("param4", NIF);
        query.setParameter("param5", d);

        List<Trabajadorbbdd> workerList = query.list();
  
        //Comprobacion si trae uno, ninguno…
        if (workerList.isEmpty()) {

                      return 0;
        } else {
            return workerList.get(0).getIdTrabajador();
        }
    }

    public Trabajadorbbdd getworker(String name, String surname1, String surname2, String NIF, String date) {

        //01/01/2009
        Trabajadorbbdd worker = null;

        String[] a = date.split("/");

        String b = a[2] + "-" + a[1] + "-" + a[0];

        Date d = Date.valueOf(b);
        Trabajadorbbdd seleccionado = null;

        String hql = "FROM Trabajadorbbdd t WHERE t.nombre=:param1 and t.apellido1=:param2 and t.apellido2=:param3 and t.nifnie=:param4 and t.fechaAlta=:param5";
        Query query = session.createQuery(hql);//Va a devolver UNA LISTA
        query.setParameter("param1", name);
        query.setParameter("param2", surname1);
        query.setParameter("param3", surname2);
        query.setParameter("param4", NIF);
        query.setParameter("param5", d);


        List<Trabajadorbbdd> workerList = query.list();
        //Comprobacion si trae uno, ninguno…
        if (!workerList.isEmpty()) {

            worker = ( workerList.get(0));
        }else{
            System.out.println("no existe el trabajador¿");
        }

        return worker;

    }
     public void addWorker(ArrayList<String> trabajador,int id) {

        try {
            String[] a = trabajador.get(3).split("/");

         String b = a[2] + "-" + a[1] + "-" + a[0];

         java.util.Date d = Date.valueOf(b);
            Transaction tx1 = session.beginTransaction();
            Categorias categoria = cDAO.getCategory(trabajador.get(2));
            Empresas empresa = comDAO.getCompany(trabajador.get(0));
          
            Trabajadorbbdd e = new Trabajadorbbdd(categoria, empresa,trabajador.get(4),trabajador.get(5),trabajador.get(6),trabajador.get(7),trabajador.get(8),d,trabajador.get(9),trabajador.get(11));
            /*
            List<Trabajadorbbdd> listatrabjadores_bbdd = session.createQuery("from Trabajadorbbdd ").list();
        
            listatrabjadores_bbdd.add(e);
            
            for (Trabajadorbbdd em : listatrabjadores_bbdd){ 
        
                session.save(em);
            }
            */
          if (id>0) {
                e.setIdTrabajador(id);
                session.update(e);
            }else{
                session.saveOrUpdate(e);
            }
            tx1.commit();
           
        } catch (Exception e) {
            System.out.println("Fallo al añadir la trabajador " + e.getMessage());
        }

    }

}