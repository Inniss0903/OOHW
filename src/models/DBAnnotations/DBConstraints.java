package models.DBAnnotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DBConstraints {
    boolean primaryKey() default false;
    boolean allowNull() default  true;
    boolean unique() default false;
    boolean autoIncrement() default false;
}
