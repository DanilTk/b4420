package com.example.shared.infrastructure;

import io.vavr.collection.Traversable;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.metamodel.SingularAttribute;

@Component
public class BaseSpecificationFactory {

    private static final String WILDCARD = "%";

    public <T> Specification<T> whereLikeIgnoreCase(
        SingularAttribute<T, String> attribute,
        @NonNull String value
    ) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
            criteriaBuilder.lower(root.get(attribute)),
            WILDCARD + value.toLowerCase() + WILDCARD
        );
    }

    public <T, U> Specification<T> whereEquals(
        SingularAttribute<? super T, U> attribute,
        @NonNull U value
    ) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
            root.get(attribute),
            value
        );
    }

    public <T, U> Specification<T> whereNotEquals(
        SingularAttribute<? super T, U> attribute,
        @NonNull U value
    ) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(
            root.get(attribute),
            value
        );
    }

    public <T, U> Specification<T> whereIn(
        SingularAttribute<? super T, U> attribute,
        @NonNull Traversable<U> values
    ) {
        if (values.isEmpty()) {
            return constantBoolean(false);
        }
        return (root, query, criteriaBuilder) -> root.get(attribute).in(values.toJavaList());
    }

    public <T, U> Specification<T> whereNotIn(
        SingularAttribute<? super T, U> attribute,
        @NonNull Traversable<U> values
    ) {
        if (values.isEmpty()) {
            return constantBoolean(false);
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.not(root.get(attribute).in(values.toJavaList()));
    }

    public <T, U> Specification<T> whereChildIn(String attribute, String childAttribute,
                                                @NonNull Traversable<U> values) {
        if (values.isEmpty()) {
            return constantBoolean(false);
        }
        return (root, query, criteriaBuilder) -> root.get(attribute).get(childAttribute).in(values.toJavaList());
    }

    public <T> Specification<T> constantBoolean(Boolean value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isTrue(criteriaBuilder.literal(value));
    }

    public <T> Specification<T> whereChildEquals(@NonNull String parentField, @NonNull String childField,
                                                 @NonNull Object value) {
        return Specification.where((root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.join(parentField).get(childField), value));
    }

    public <T> Specification<T> whereGrandChildEquals(@NonNull String parentField, @NonNull String childField,
                                                      @NonNull String grandChildField,
                                                      @NonNull Object value) {
        return Specification.where((root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.join(parentField).get(childField).get(grandChildField), value));
    }
}
