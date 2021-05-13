/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.unileon.xijaja.nominas.dao;

import es.unileon.xijaja.nominas.modelo.Categorias;
import es.unileon.xijaja.nominas.modelo.HibernateUtil;
import es.unileon.xijaja.nominas.modelo.Trabajadorbbdd;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author Javier
 */
public class CategoryDAO {

    private SessionFactory sf;
    private Session session;
    private Transaction tx;

    private PayrollDAO nDAO;

    public CategoryDAO() {
        sf = HibernateUtil.getSessionFactory();//Abrir sesión una vez y queda abierta
        session = sf.openSession();//SessionFactory nos trae la informacion
        this.nDAO = new PayrollDAO();
    }

    public int ExistCategory(String name) {

        String hql = "FROM Categorias c WHERE c.nombreCategoria=:param1";
        Query query = session.createQuery(hql);//Va a devolver UNA LISTA
        query.setParameter("param1", name);

        List<Categorias> categoryList = query.list();
        //Comprobacion si trae uno, ninguno…
        if (categoryList.isEmpty()) {
            return 0;
        } else {
            return categoryList.get(0).getIdCategoria();
        }

    }
      public Categorias getCategory(String name) {

        String hql = "FROM Categorias c WHERE c.nombreCategoria=:param1";
        Query query = session.createQuery(hql);//Va a devolver UNA LISTA
        query.setParameter("param1", name);

        List<Categorias> categoryList = query.list();
        //Comprobacion si trae uno, ninguno…
      return categoryList.get(0);

    }
     public void addCategory(String nombreCategoria, double salarioBaseCategoria, double complementoCategoria, int id) {

        try {
           
            Transaction tx1 = session.beginTransaction();
           
            Categorias e = new Categorias(nombreCategoria, salarioBaseCategoria,complementoCategoria);
            /*
              List<Categorias> categorias_bbdd = session.createQuery("from Categorias ").list();
        
            categorias_bbdd.add(e);
            
            for (Categorias cat : categorias_bbdd){ 
         
          
                session.save(cat);
            }
        */  
            if (id>0) {
                e.setIdCategoria(id);
                session.update(e);
            }else{
                session.saveOrUpdate(e);
            }
            
            tx1.commit();
           
        } catch (Exception e) {
            System.out.println("Fallo al añadir la Categoria " + e.getMessage());
        }

    }

}
