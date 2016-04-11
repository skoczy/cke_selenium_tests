package com.sfr.sitemaster.commons

import com.sfr.sitemaster.dto.SMProperty
import com.sfr.sitemaster.enums.Owner
import groovy.util.slurpersupport.GPathResult

import java.lang.reflect.Field
import java.time.LocalDate
import java.time.ZoneId

/**
 * Created by piotr on 16/10/15.
 */
class SiteMergeUtil {

    final Map<Owner, Object> dataSources
    final Object dataSource

    private SiteMergeUtil(Map<Owner, Object> dataSources) {
        this.dataSources = dataSources
    }

    private SiteMergeUtil(dataSource) {
        this.dataSource = dataSource
    }

    static SiteMergeUtil from(Map<Owner, Object> dataSources) {
        return new SiteMergeUtil(dataSources)
    }

    static SiteMergeUtil from(Object dataSource) {
        return new SiteMergeUtil(dataSource)
    }

    public def <T> T to(Class<T> clazz) {
        merge(clazz, null)
    }

    public def <T> T update(T target) {
        doUpdate(dataSource, target)
    }

    public def <T> T create(Class<T> target) {
        doUpdate(dataSource, target.newInstance())
    }

    def merge(Class targetClass, Object dataSource) {
        def final targetObject = targetClass.newInstance()
        targetClass.declaredFields.findAll { it.getAnnotation(SMProperty.class) }.each {
            final SMProperty annotation = it.getAnnotation(SMProperty.class)
            final def owner = annotation.owner()
            def final source = dataSource ? dataSource : dataSources[owner]
            if (source == null) {
                return null;
            }
            def newSource = source[annotation.path()];
            if (newSource != null) {
                if (annotation?.target() == String.class) {
                    targetObject[it.name] = getStringFromSource(newSource, annotation.get())
                } else if (annotation?.target() == Long.class) {
                    targetObject[it.name] = getLongFromSource(newSource, annotation.get())
                } else if (annotation?.target() == Map.class) {
                    targetObject[it.name] = getMapFromSource(source[annotation.path()], it, annotation.get())
                } else if (annotation?.target() == List.class) {
                    targetObject[it.name] = getListFromSource(source[annotation.path()], it, annotation.get())
                } else if (annotation?.target() == Boolean.class) {
                    targetObject[it.name] = getBooleanFromSource(source[annotation.path()], annotation.get())
                } else if (annotation?.target() == LocalDate.class) {
                    targetObject[it.name] = getLocalDateFromSource(source[annotation.path()], annotation.get())
                } else if (annotation?.target()) {
                    if (annotation.get() != Void) {
                        newSource = annotation.get().newInstance(null, null).call(source[annotation.path()]);
                    }
                    targetObject[it.name] = merge(annotation?.target(), newSource)
                }
            } else {
                targetObject[it.name] = null
            }
        }

        targetObject
    }

    def getStringFromSource(final Object src, final Class closure) {
        if (closure != Void) {
            closure.newInstance(null, null).call(src)
        } else {
            if (src instanceof GPathResult) {
                ((GPathResult) src).text().trim()
            } else {
                src
            }
        }
    }

    def getLongFromSource(final Object src, final Class closure) {
        if (closure != Void) {
            closure.newInstance(null, null).call(src)
        } else {
            if (src instanceof GPathResult) {
                ((GPathResult) src).text() as Long
            } else {
                src as Long
            }
        }
    }

    def getBooleanFromSource(final Object src, final Class closure) {
        if (closure != Void) {
            closure.newInstance(null, null).call(src)
        } else {
            if (src instanceof GPathResult) {
                ((GPathResult) src).text()?.trim() == 'Y'
            } else {
                new Boolean(src)
            }
        }
    }

    def getLocalDateFromSource(final Object src, final Class closure) {
        if (closure != Void) {
            closure.newInstance(null, null).call(src)
        } else {
            if (src instanceof GPathResult) {
                LocalDate.parse(((GPathResult) src).text()?.trim())
            } else if (src instanceof LocalDate) {
                src
            } else if (src instanceof Date) {
                src.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }
        }
    }

    def getMapFromSource(Object src, Field targetField, Class closure) {
        if (closure != Void) {
            closure.newInstance(null, null).call(src)
        } else {
            def final target = [:]
            ((Map) src).each { k, v ->
                def vClass = ((java.lang.reflect.ParameterizedType) targetField.getGenericType()).actualTypeArguments[1]
                ((Map) target).put(k, merge(vClass, src[k]))
            }
            target
        }
    }

    def getListFromSource(Object src, Field targetField, Class closure) {
        if (closure != Void) {
            closure.newInstance(null, null).call(src)
        } else {
            def final target = []
            ((List) src).each { srcList ->
                def vClass = ((java.lang.reflect.ParameterizedType) targetField.getGenericType()).actualTypeArguments[0]
                ((List) target) << merge(vClass, srcList)
            }
            target
        }
    }


    def doUpdate(final Object src, final Object target) {
        if (src == null) {
            return null;
        }
        src.class.declaredFields.findAll {
            it.getAnnotation(SMProperty.class)?.owner() == Owner.SITEMASTER
        }.each {
            final SMProperty annotation = it.getAnnotation(SMProperty.class)
            def final targetField = target[annotation.path()]
            def final targetFieldClass = target.class.getDeclaredFields().findAll {
                it.name == annotation.path()
            }[0]?.getGenericType()
            def final srcField = src[it.name]
            if (srcField != null && targetFieldClass) {
                if (annotation?.target() == String.class || annotation?.target() == Boolean.class || annotation?.target() == Long.class || annotation?.target() == LocalDate.class) {
                    target[annotation.path()] = srcField
                } else if (annotation?.target() == Map.class) {
                    if (targetField == null) {
                        targetField = [:]
                    }
                    ((Map) targetField).clear();
                    def _dstClass = ((java.lang.reflect.ParameterizedType) targetFieldClass).getActualTypeArguments()[1]
                    ((Map) srcField).each { key, _src ->
                        ((Map) targetField).put(key, doUpdate(_src, _dstClass.newInstance()))
                    }
                    target[annotation?.path()] = targetField
                } else if (annotation?.target() == List.class) {
                    if (targetField == null) {
                        targetField = []
                    }
                    ((List) targetField).clear();
                    def _dstClass = ((java.lang.reflect.ParameterizedType) targetFieldClass).getActualTypeArguments()[0]
                    target[annotation?.path()] = ((List) srcField).each { _src ->
                        ((List) targetField) << doUpdate(_src, _dstClass.newInstance())
                    }
                    target[annotation?.path()] = targetField

                } else {
                    if (!targetField) {
                        targetField = targetFieldClass.newInstance()
                    }
                    target[annotation?.path()] = doUpdate(srcField, targetField)
                }
            } else {
                targetField = null
            }
        }
        target
    }

}
