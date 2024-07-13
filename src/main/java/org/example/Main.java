package org.example;

import org.example.entidades.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("example-unit");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        System.out.println("en marcha la traza 1 Alberto");

        try {
            // Persistir una nueva entidad Person
            entityManager.getTransaction().begin();

            Pais pais1 = Pais.builder().nombre("Argentina").build();
            entityManager.persist(pais1);

            Provincia provincia1 = Provincia.builder().nombre("Mendoza").pais(pais1).build();
            Provincia provincia2 = Provincia.builder().nombre("Buenos Aires").pais(pais1).build();
            entityManager.persist(provincia1);
            entityManager.persist(provincia2);

            Localidad localidad1 = Localidad.builder().nombre("Lujan de Cuyo").provincia(provincia1).build();
            Localidad localidad2 = Localidad.builder().nombre("Guaymallen").provincia(provincia1).build();
            Localidad localidad3 = Localidad.builder().nombre("Mar del Plata").provincia(provincia2).build();
            Localidad localidad4 = Localidad.builder().nombre("Mar de las Pampas").provincia(provincia2).build();
            entityManager.persist(localidad1);
            entityManager.persist(localidad2);
            entityManager.persist(localidad3);
            entityManager.persist(localidad4);

            Empresa empresaCarlos = Empresa.builder().nombre("Lo de Messi").cuil(30546780L).razonSocial("Venta de Alimentos").build();
            entityManager.persist(empresaCarlos);

            Sucursal sucursalGuaymallen = Sucursal.builder()
                    .nombre("En Guaymallen")
                    .horarioApertura(LocalTime.of(17, 0))
                    .horarioCierre(LocalTime.of(23, 0))
                    .build();

            Sucursal sucursalMarDelPlata = Sucursal.builder()
                    .nombre("En MDQ")
                    .horarioApertura(LocalTime.of(16, 0))
                    .horarioCierre(LocalTime.of(23, 30))
                    .build();

            Domicilio domicilioBerutti = Domicilio.builder()
                    .cp(5519)
                    .calle("Berutti")
                    .numero(2684)
                    .piso(0)
                    .nroDpto(5)
                    .localidad(localidad1)
                    .build();

            Domicilio domicilioGaboto = Domicilio.builder()
                    .cp(7600)
                    .calle("Gaboto")
                    .numero(3475)
                    .localidad(localidad2)
                    .build();
            entityManager.persist(domicilioBerutti);
            entityManager.persist(domicilioGaboto);

            sucursalGuaymallen.setDomicilio(domicilioBerutti);
            sucursalMarDelPlata.setDomicilio(domicilioGaboto);

            empresaCarlos.getSucursales().add(sucursalGuaymallen);
            empresaCarlos.getSucursales().add(sucursalMarDelPlata);
            sucursalGuaymallen.setEmpresa(empresaCarlos);
            sucursalMarDelPlata.setEmpresa(empresaCarlos);

            entityManager.persist(sucursalGuaymallen);
            entityManager.persist(sucursalMarDelPlata);
            entityManager.persist(empresaCarlos);
       // BUSCAR POR ID

            // Recuperar la Empresa por su ID
            Long empresaId = 1L; // Cambia esto al ID de la empresa que deseas consultar
            Empresa empresa = entityManager.find(Empresa.class, empresaId);

            if (empresa != null) {
                // Obtener las sucursales de la empresa
                Set<Sucursal> sucursales = empresa.getSucursales();

                // Imprimir las sucursales
                System.out.println("Sucursales de la empresa " + empresa.getNombre() + ":");
                for (Sucursal sucursal : sucursales) {
                    System.out.println("- " + sucursal.getNombre());
                }
            } else {
                System.out.println("No se encontró ninguna empresa con el ID " + empresaId);
            }

           // CONSULTAR PARA SABER A QUE EMPRESA PERTENECE UNA SUCURSAL
            // ID de la sucursal a buscar
            Long sucursalId = 1L; // Cambia esto al ID de la sucursal que deseas consultar

            // Recuperar la Sucursal por su ID
            Sucursal sucursal = entityManager.find(Sucursal.class, sucursalId);

            if (sucursal != null) {
                // Obtener la Empresa asociada a la Sucursal
                Empresa empresa1 = sucursal.getEmpresa();

                if (empresa1 != null) {
                    // Imprimir la información de la Empresa
                    System.out.println("La sucursal " + sucursal.getNombre() + " pertenece a la empresa " + empresa.getNombre());
                } else {
                    System.out.println("La sucursal " + sucursal.getNombre() + " no tiene una empresa asociada.");
                }
            } else {
                System.out.println("No se encontró ninguna sucursal con el ID " + sucursalId);
            }


           // CONSULTAR UNA EMPRESA POR EL NOMBRE
            // Nombre de la empresa a buscar
            String nombreEmpresa = "Lo de Messi"; // Cambia esto al nombre de la empresa que deseas consultar
            // Consulta JPQL para obtener la empresa por su nombre
            String jpql = "SELECT e FROM Empresa e WHERE e.nombre = :nombre";
            TypedQuery<Empresa> query = entityManager.createQuery(jpql, Empresa.class);
            query.setParameter("nombre", nombreEmpresa);

            // Ejecutar la consulta
            Empresa empresa2 = query.getSingleResult();

            if (empresa2 != null) {
                // Obtener las sucursales de la empresa
                Set<Sucursal> sucursales = empresa.getSucursales();

                // Imprimir las sucursales
                System.out.println("Sucursales de la empresa " + empresa.getNombre() + ":");
                for (Sucursal sucursalB : sucursales) {
                    System.out.println("- " + sucursalB.getNombre());
                }
            } else {
                System.out.println("No se encontró ninguna empresa con el nombre " + nombreEmpresa);
            }


            // Consulta JPQL para obtener las empresas con sus sucursales y detalles de domicilio
            String jpqlc = "SELECT e FROM Empresa e " +
                    "JOIN FETCH e.sucursales s " +
                    "JOIN FETCH s.domicilio d " +
                    "JOIN FETCH d.localidad l " +
                    "JOIN FETCH l.provincia p " +
                    "JOIN FETCH p.pais";
            TypedQuery<Empresa> query1 = entityManager.createQuery(jpqlc, Empresa.class);

            // Ejecutar la consulta
            List<Empresa> empresas = query1.getResultList();

            // Imprimir los resultados
            for (Empresa empresac : empresas) {
                System.out.println("Empresa: " + empresac.getNombre());
                for (Sucursal sucursalc : empresac.getSucursales()) {
                    Domicilio domicilio = sucursalc.getDomicilio();
                    Localidad localidad = domicilio.getLocalidad();
                    Provincia provincia = localidad.getProvincia();
                    Pais pais = provincia.getPais();

                    System.out.println("  Sucursal: " + sucursalc.getNombre());
                    System.out.println("  Domicilio: " + domicilio.getCalle() + " " + domicilio.getNumero());
                    System.out.println("  Localidad: " + localidad.getNombre());
                    System.out.println("  Provincia: " + provincia.getNombre());
                    System.out.println("  Pais: " + pais.getNombre());
                }
            }















            entityManager.getTransaction().commit();


        }catch (Exception e){

            entityManager.getTransaction().rollback();
            System.out.println(e.getMessage());
            System.out.println("No se pudo grabar ");}

        // Cerrar el EntityManager y el EntityManagerFactory
        entityManager.close();
        entityManagerFactory.close();
    }
}
