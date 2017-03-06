/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onetomanypoc.datalayer;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import onetomanypoc.entity.Customer;
import onetomanypoc.entity.Customer_;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import onetomanypoc.entity.PurchaseOrder;
import onetomanypoc.entity.DiscountCode;
import onetomanypoc.entity.MicroMarket;

/**
 *
 * @author kuw
 */
@Stateless
public class CustomerFacade extends AbstractFacade<Customer> {

    @PersistenceContext(unitName = "OneToMany-PoCPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CustomerFacade() {
        super(Customer.class);
    }

    public boolean isPurchaseOrderListEmpty(Customer entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Customer> customer = cq.from(Customer.class);
        cq.select(cb.literal(1L)).distinct(true).where(cb.equal(customer, entity), cb.isNotEmpty(customer.get(Customer_.purchaseOrderList)));
        return em.createQuery(cq).getResultList().isEmpty();
    }

    public List<PurchaseOrder> findPurchaseOrderList(Customer entity) {
        return this.getMergedEntity(entity).getPurchaseOrderList();
    }

    public boolean isDiscountCodeEmpty(Customer entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Customer> customer = cq.from(Customer.class);
        cq.select(cb.literal(1L)).distinct(true).where(cb.equal(customer, entity), cb.isNotNull(customer.get(Customer_.discountCode)));
        return em.createQuery(cq).getResultList().isEmpty();
    }

    public DiscountCode findDiscountCode(Customer entity) {
        return this.getMergedEntity(entity).getDiscountCode();
    }

    public boolean isZipEmpty(Customer entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Customer> customer = cq.from(Customer.class);
        cq.select(cb.literal(1L)).distinct(true).where(cb.equal(customer, entity), cb.isNotNull(customer.get(Customer_.zip)));
        return em.createQuery(cq).getResultList().isEmpty();
    }

    public MicroMarket findZip(Customer entity) {
        return this.getMergedEntity(entity).getZip();
    }

    @Override
    public Customer findWithParents(Customer entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
        Root<Customer> customer = cq.from(Customer.class);
        customer.fetch(Customer_.discountCode);
        customer.fetch(Customer_.zip);
        cq.select(customer).where(cb.equal(customer, entity));
        return em.createQuery(cq).getSingleResult();
    }
    
}
