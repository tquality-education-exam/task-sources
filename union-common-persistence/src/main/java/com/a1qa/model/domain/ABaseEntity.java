package com.a1qa.model.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * Created by p.ordenko on 14.05.2015, 10:52.
 */
@MappedSuperclass
public class ABaseEntity implements Serializable {

    private static final long serialVersionUID = 2861779285806075202L;

    @Override
    public String toString() {
        Field[] fields =  this.getClass().getDeclaredFields();
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName()).append(" [");
        int fieldsCount = fields.length;
        int i = 1;
        for (Field field : fields) {
            JsonBackReference backReference = field.getAnnotation(JsonBackReference.class);
            boolean isAccessible = field.isAccessible();
            field.setAccessible(true);
            try {
                // Avoid StackOverflowException
                sb.append(field.getName()).append("=").append((backReference == null) ? field.get(this).toString() :
                        "*back reference*");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NullPointerException npe) {
                // OMG, ignore.
            }
            field.setAccessible(isAccessible);
            if (i < fieldsCount) {
                sb.append(", ");
            }
            i++;
        }
        sb.append("]");
        return sb.toString();
    }
}
