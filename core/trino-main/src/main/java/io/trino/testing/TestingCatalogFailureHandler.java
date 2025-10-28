/*
 * Copyright Starburst Data, Inc. All rights reserved.
 *
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF STARBURST DATA.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 *
 * Redistribution of this material is strictly prohibited.
 */
package io.trino.testing;

import com.google.common.collect.ImmutableList;
import io.trino.connector.CatalogFailureHandler;
import io.trino.metadata.Catalog;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import static java.util.Objects.requireNonNull;

public class TestingCatalogFailureHandler
        implements CatalogFailureHandler
{
    private final Deque<CatalogFailure> failures = new ConcurrentLinkedDeque<>();

    public void reset()
    {
        failures.clear();
    }

    public List<CatalogFailure> getFailures()
    {
        return ImmutableList.copyOf(failures);
    }

    @Override
    public void handleCatalogFailure(Catalog catalog, Throwable cause)
    {
        failures.addLast(new CatalogFailure(catalog, cause));
    }

    public record CatalogFailure(Catalog catalog, Throwable cause)
    {
        public CatalogFailure
        {
            requireNonNull(catalog, "catalog is null");
            requireNonNull(cause, "cause is null");
        }
    }
}
