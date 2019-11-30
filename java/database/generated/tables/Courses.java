/*
 * This file is generated by jOOQ.
 */
package database.generated.tables;


import database.generated.Indexes;
import database.generated.Keys;
import database.generated.Public;
import database.generated.tables.records.CoursesRecord;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Courses extends TableImpl<CoursesRecord> {

    private static final long serialVersionUID = 1883178505;

    /**
     * The reference instance of <code>public.courses</code>
     */
    public static final Courses COURSES = new Courses();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<CoursesRecord> getRecordType() {
        return CoursesRecord.class;
    }

    /**
     * The column <code>public.courses.id</code>.
     */
    public final TableField<CoursesRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.field("nextval('courses_id_seq'::regclass)", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>public.courses.name</code>.
     */
    public final TableField<CoursesRecord, String> NAME = createField("name", org.jooq.impl.SQLDataType.VARCHAR(128).nullable(false), this, "");

    /**
     * The column <code>public.courses.school</code>.
     */
    public final TableField<CoursesRecord, String> SCHOOL = createField("school", org.jooq.impl.SQLDataType.VARCHAR(4).nullable(false), this, "");

    /**
     * The column <code>public.courses.subject</code>.
     */
    public final TableField<CoursesRecord, String> SUBJECT = createField("subject", org.jooq.impl.SQLDataType.VARCHAR(4).nullable(false), this, "");

    /**
     * The column <code>public.courses.dept_course_number</code>.
     */
    public final TableField<CoursesRecord, Integer> DEPT_COURSE_NUMBER = createField("dept_course_number", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.courses.term_id</code>.
     */
    public final TableField<CoursesRecord, Integer> TERM_ID = createField("term_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * Create a <code>public.courses</code> table reference
     */
    public Courses() {
        this(DSL.name("courses"), null);
    }

    /**
     * Create an aliased <code>public.courses</code> table reference
     */
    public Courses(String alias) {
        this(DSL.name(alias), COURSES);
    }

    /**
     * Create an aliased <code>public.courses</code> table reference
     */
    public Courses(Name alias) {
        this(alias, COURSES);
    }

    private Courses(Name alias, Table<CoursesRecord> aliased) {
        this(alias, aliased, null);
    }

    private Courses(Name alias, Table<CoursesRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> Courses(Table<O> child, ForeignKey<O, CoursesRecord> key) {
        super(child, key, COURSES);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.COURSE_IDX, Indexes.COURSES_ID_KEY, Indexes.COURSES_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<CoursesRecord, Integer> getIdentity() {
        return Keys.IDENTITY_COURSES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<CoursesRecord> getPrimaryKey() {
        return Keys.COURSES_PKEY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<CoursesRecord>> getKeys() {
        return Arrays.<UniqueKey<CoursesRecord>>asList(Keys.COURSES_ID_KEY, Keys.COURSES_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Courses as(String alias) {
        return new Courses(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Courses as(Name alias) {
        return new Courses(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Courses rename(String name) {
        return new Courses(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Courses rename(Name name) {
        return new Courses(name, null);
    }
}
