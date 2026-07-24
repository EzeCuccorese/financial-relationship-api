package com.fintech.relationship.repository;

import com.fintech.relationship.model.PeopleStats;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository("personHqlRepository")
public class PersonHqlRepository {

    private final EntityManager entityManager;

    public PersonHqlRepository(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<PeopleStats> getPersonPercentageByCountry() {
        final String hql = "SELECT c.name AS country, " +
                "ROUND((COUNT(*) / (SELECT COUNT(*) FROM Person)) * 100, 2) AS percentage " +
                "FROM Person p " +
                "JOIN p.country c " +
                "GROUP BY c.name";

        final Query query = entityManager.createQuery(hql);
        final List<Object[]> resultList = query.getResultList();

        final List<PeopleStats> peopleStatsList = new ArrayList<>();
        for (final Object[] result : resultList) {
            final String country = (String) result[0];
            final Long percentage = (Long) result[1];
            final PeopleStats peopleStats = new PeopleStats(country, percentage);
            peopleStatsList.add(peopleStats);
        }

        return peopleStatsList;
    }
}


