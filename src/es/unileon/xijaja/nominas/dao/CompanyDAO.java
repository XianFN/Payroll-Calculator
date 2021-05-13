/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.unileon.xijaja.nominas.dao;

import es.unileon.xijaja.nominas.modelo.Empresas;
import es.unileon.xijaja.nominas.modelo.HibernateUtil;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author Javier
 */
public class CompanyDAO {

    private SessionFactory sf;
    private Session session;
    private Transaction tx;

    private PayrollDAO nDAO;

    public CompanyDAO() {
        sf = HibernateUtil.getSessionFactory();//Abrir sesión una vez y queda abierta
        session = sf.openSession();//SessionFactory nos trae la informacion
        this.nDAO = new PayrollDAO();
    }

    public int ExistCompany(String cif) {

        String hql = "FROM Empresas e WHERE e.cif=:param1";
        Query query = session.createQuery(hql);//Va a devolver UNA LISTA
        query.setParameter("param1", cif);

        List<Empresas> companyList = query.list();
        //Comprobacion si trae uno, ninguno…
        if (companyList.isEmpty()) {
                return 0;
        } else {
            return companyList.get(0).getIdEmpresa();
        }

    }
      public Empresas getCompany(String cif) {

        String hql = "FROM Empresas e WHERE e.cif=:param1";
        Query query = session.createQuery(hql);//Va a devolver UNA LISTA
        query.setParameter("param1", cif);

        List<Empresas> companyList = query.list();
        //Comprobacion si trae uno, ninguno…
       return companyList.get(0);

    }

    public void addCompany(String cif, String name,int id) {

        try {
           
            Transaction tx1 = session.beginTransaction();
           
            Empresas e = new Empresas(name, cif);
            
            /*
              List<Empresas> empresas_bbdd = session.createQuery("from Empresas ").list();
        
            empresas_bbdd.add(e);
            
            for (Empresas em : empresas_bbdd){ 
        
                session.save(em);
            }
            */
            if (id>0) {
                e.setIdEmpresa(id);
                session.update(e);
            }else{
                session.saveOrUpdate(e);
            }
            
        
            tx1.commit();
           
        } catch (Exception e) {
            System.out.println("Fallo al añadir la empresa " + e.getMessage());
        }

    }

}
