package com.sfr.sitemaster.commons

import com.sfr.sitemaster.dto.SMProperty

import java.lang.reflect.Field
import java.time.LocalDate

/**
 * Created by piotr on 16/10/15.
 */
class DtoMetaGenerator {
    def fields
    def types


    private DtoMetaGenerator() {
        types = [:]
        fields = [:]
    }

    public static Map generate(Class dtoClass) {
        def final generator = new DtoMetaGenerator()
        generator.doGenerate(dtoClass)
    }

    def doGenerate(Class dtoClass) {
        fields = getMetaForClass(dtoClass)
        return [types: types, fields: fields]
    }

    private getMetaForClass(Class dtoClass) {
        def fields = [:]
        dtoClass.declaredFields.findAll {
            it.getAnnotation(SMProperty.class)
        }.each { Field field ->
            fields.put(field.name, getMetaForField(field))
        }
        fields
    }

    private getMetaForField(Field field) {
        def annotation = field.getAnnotation(SMProperty.class)
        [
                label: annotation.label(),
                type : getType(field),
                owner: annotation.owner().name().toUpperCase()
        ]
    }

    private getType(Field field) {
        def clazz = field.getAnnotation(SMProperty.class).target()
        switch (clazz) {
            case LocalDate.class:
                return 'date'
            case String.class:
            case Integer.class:
            case Boolean.class:
                return clazz.simpleName.toLowerCase()
            default:
                if(clazz == Map.class) {
                    clazz = ((java.lang.reflect.ParameterizedType)field.genericType).actualTypeArguments[1]
                } else if(clazz== List.class) {
                    clazz = ((java.lang.reflect.ParameterizedType)field.genericType).actualTypeArguments[0]
                }
                types.put(field.name, getMetaForClass(clazz))
                return field.name
        }
    }

}
