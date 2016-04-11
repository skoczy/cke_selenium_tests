package com.sfr.sitemaster.dao.impl;

import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.StringPath;
import com.sfr.sitemaster.entities.SearchFilter;
import com.sfr.sitemaster.entities.QSite;
import com.sfr.sitemaster.exceptions.SearchFilterException;

import java.util.List;

/**
 * Created by piotr on 01/10/15.
 */
public final class FilterBuilder {
    private final QSite site;
    private BooleanExpression booleanExpression;


    private FilterBuilder(final QSite site) {
        this.site = site;
    }

    public static FilterBuilder create(final QSite site) {
        return new FilterBuilder(site);
    }

    private void addFilter(final BooleanExpression booleanExpression) {
        this.booleanExpression = this.booleanExpression == null ? this.booleanExpression = booleanExpression : this.booleanExpression.and(booleanExpression);
    }

    public FilterBuilder and(final SearchFilter searchFilter) throws NoSuchFieldException, IllegalAccessException, SearchFilterException {
        final StringPath path = getField(searchFilter.getField());
        BooleanExpression booleanExpression = null;
        switch (searchFilter.getOperator()) {
            case EQUALS:
                booleanExpression = filterIn(path, searchFilter.getValue());
                break;
            case NOT_EQUALS:
                booleanExpression = filterNotIn(path, searchFilter.getValue());
                break;
            case LIKE:
                booleanExpression = filterLike(path, searchFilter.getValue());
                break;
            case NOT_LIKE:
                booleanExpression = filterNotLike(path, searchFilter.getValue());
                break;
            case STARTS_WITH:
                booleanExpression = filterStartsWith(path, searchFilter.getValue());
                break;
            default:
                throw new SearchFilterException("Unsupported operator.");
        }
        addFilter(booleanExpression);
        return this;
    }


    private BooleanExpression filterIn(final StringPath stringPath, final List<String> values) {
        return stringPath.in(values);
    }

    private BooleanExpression filterNotIn(final StringPath stringPath, final List<String> values) {
        return stringPath.notIn(values);
    }

    private String wrapLike(final String value) {
        return "%".concat(value).concat("%");
    }

    private BooleanExpression filterLike(final StringPath stringPath, final List<String> values) {
        BooleanExpression booleanExpression = null;
        for (final String value : values) {
            if (booleanExpression == null) {
                booleanExpression = stringPath.like(wrapLike(value));
            } else {
                booleanExpression = booleanExpression.or(stringPath.like(wrapLike(value)));
            }
        }
        return booleanExpression;
    }

    private BooleanExpression filterNotLike(final StringPath stringPath, final List<String> values) {
        BooleanExpression booleanExpression = null;
        for (final String value : values) {
            if (booleanExpression == null) {
                booleanExpression = stringPath.notLike(wrapLike(value));
            } else {
                booleanExpression = booleanExpression.or(stringPath.notLike(wrapLike(value)));
            }
        }
        return booleanExpression;
    }

    private BooleanExpression filterStartsWith(final StringPath stringPath, final List<String> values) {
        BooleanExpression booleanExpression = null;
        for (final String value : values) {
            if (booleanExpression == null) {
                booleanExpression = stringPath.startsWith(value);
            } else {
                booleanExpression = booleanExpression.or(stringPath.startsWith(value));
            }
        }
        return booleanExpression;
    }

    private StringPath getField(final String name) throws NoSuchFieldException, IllegalAccessException, SearchFilterException {
        final Object obj = site.getClass().getField(name).get(site);
        if (obj instanceof StringPath) {
            return (StringPath) obj;
        }
        throw new SearchFilterException(" Unsupported field.");
    }

    public BooleanExpression getBooleanExpression() {
        return booleanExpression;
    }
}

