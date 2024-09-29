package org.example.homeservice.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.entites.Service;
import org.example.entites.Specialist;
import org.example.repositories.baseentity.BaseEnittiyRepoImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServiceRepoImpl extends BaseEnittiyRepoImpl<Service, Long> implements ServiceRepo {
    public ServiceRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Class<Service> getEntityClass() {
        return Service.class;
    }

    @Override
    public Optional<Service> findByName(String name) {
        TypedQuery<Service> query = entityManager.createQuery("SELECT s FROM Service s WHERE s.name = :servicename", Service.class);
        query.setParameter("servicename", name);
        return query.getResultList().stream().findFirst();
    }
@Deprecated
    @Override
    public boolean addSubService(Service parentService, Service subService) {
//subService.setParentService(parentService);
        if (parentService!=null){

            parentService.addSubService(subService);
        }

        entityManager.getTransaction().begin();
        entityManager.persist(subService);
        entityManager.getTransaction().commit();

        return true;
    }

    @Override
    public boolean addSubService(Long parentId, Service subService) {
        if (parentId!=null){
            try {
                Service parent = entityManager.getReference(Service.class, parentId);
                parent.addSubService(subService);

            }catch (RuntimeException e){
                System.err.println("no such parent service is avilable");
                return false;
            }

           // Link the sub-service to the parent
        }
        entityManager.getTransaction().begin();
        entityManager.persist(subService);
        entityManager.getTransaction().commit();
        return true;
    }


    @Override
    public boolean removeSubService(Service service) {

        //search hole database to find other services with this parent id
        //while


        return false;
    }

    @Override
    public List<Service> findAllByParentId(Long parentId) {

        TypedQuery<Service> query = entityManager.createQuery("SELECT s FROM Service s WHERE s.parentService.id = :parentid", Service.class);
        query.setParameter("parentid", parentId);
        List<Service> childerns = query.getResultList();
        List<Service> allDescendants = new ArrayList<>(childerns);
        for (Service child : childerns) {
            List<Service> subChilderns = findAllByParentId(child.getId());
            allDescendants.addAll(subChilderns);
        }
        return allDescendants;

    }

    @Override
    public List<Service> findFirstLayerServices() {
        TypedQuery<Service> query = entityManager.createQuery("SELECT s FROM Service s WHERE s.parentService.id IS NULL", Service.class);
  return query.getResultList();    }

    @Override
    public void addingSpecialistToSubService(Specialist specialist, Service subService) {
        subService.getAvilableSpecialists().add(specialist);
        update(subService);
    }
}
