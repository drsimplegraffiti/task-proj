package com.abcode.taskproject.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data // Lombok annotation to generate getters, setters, toString, equals and hashCode methods
@AllArgsConstructor //  the @AllArgsConstructor annotation generates a constructor with one parameter for each field in your class.
@NoArgsConstructor //  the @NoArgsConstructor annotation generates a constructor with no parameters.

@Entity  //  the @Entity annotation specifies that the class is an entity and is mapped to a database table.
@Table(name= "task") //  the @Table annotation specifies the name of the database table to be used for mapping.
public class Task {
    @Id //  the @Id annotation specifies the primary key of an entity.
    @GeneratedValue(strategy = GenerationType.IDENTITY) //  the @GeneratedValue annotation specifies the primary key generation strategy.
    private long id;

    @Column(name="taskname", nullable = false)
    private String taskname;

    //use many to one mapping i.e many tasks to one user
    @ManyToOne(fetch=FetchType.LAZY) // lazy loading is used to avoid loading of data when not needed
    @JoinColumn(name="users_id") //  the @JoinColumn annotation specifies the column that joins two tables.
    private Users users;
}
