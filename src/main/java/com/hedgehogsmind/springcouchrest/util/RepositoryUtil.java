package com.hedgehogsmind.springcouchrest.util;

import net.jodah.typetools.TypeResolver;
import org.springframework.data.repository.CrudRepository;

public final class RepositoryUtil {

    private RepositoryUtil() {}

    /**
     * Uses {@link TypeResolver} to fetch generic class type.
     *
     * @param repository Repository.
     * @return Generic entity type of repository.
     */
    public static Class<?> getEntityClassOfRepositoryClass(final Class<? extends CrudRepository> repository) {
        final Class<?>[] typeArgs = TypeResolver.resolveRawArguments(
                CrudRepository.class,
                repository
        );

        if ( typeArgs.length < 2 ) throw new IllegalStateException("Could not fetch generic types properly");
        return typeArgs[0];
    }

}
