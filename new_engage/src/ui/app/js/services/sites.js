'use strict';

var servicesModule = require('./_index.js');
var angular        = require('angular');

/**
* @ngInject
*/
function Sites($http, $translate, AppSettings) {
  return  {
    self: this,
    fieldDefinitions: {},
    fieldInfo: {},

    find: function(siteId) {
      return $http.get('/api/v1/res/site/' + siteId);
    },

    save: function(site) {
      return $http.put('/api/v1/res/site/' + site.siteId, site);
    },

    /**
    * Get custom field by name.
    */
    getCustomFieldByName: function(name, site) {
      return site[name];
    },

    /**
     * Get field info.
     */
    getDisplayFields: function(site, definitions, displayFields) {
      var fields = [],
         fieldName;

      displayFields = displayFields || AppSettings.stationGeneralDataFields;

      for (var i=0; i<displayFields.length; i++) {
        fieldName = displayFields[i];
        this.addFieldInfo(fields, '', fieldName, site, definitions.fields[fieldName], definitions.types[fieldName]);
      }

      return fields;
    },

    /**
     * Add field info to a given array of fields. This adds field info recursively, to add
     * any level of sub-fields and composite field types.
     */
    addFieldInfo: function(fields, parent, fieldName, site, definition, type) {
      var fieldName = parent != '' ? parent + '.' + fieldName : fieldName;

      if (type && type.type === undefined && Object.keys(type).length > 0) {
        for (var o in type) {
          this.addFieldInfo(fields, fieldName, o, site, type[o], type[o]);
        }
      }
      else {
        var field = definition;

        field.fieldName = fieldName;
        field.value = eval('site.' + field.fieldName);

        if (field.value instanceof Array) {
          for (var i=0; i<field.value.length; i++) {
            var subField = angular.copy(field);
            subField.value = subField.value[i];
            subField.label = subField.label + ' ' + (i + 1);
            fields.push(subField);
          }

          return;
        }

        fields.push(field);
      }
    },

    /**
     * Get the meta data for a field, based on the path to the field.
     */
    getFieldMeta: function(fieldPath, meta) {
      var topPath = fieldPath[0];

      if (fieldPath.length == 1) {
        if (meta.types[meta.fields[topPath].type] === undefined) {
          return meta.fields[topPath];
        }

        return meta.types[meta.fields[topPath].type];
      }

      var path = fieldPath.filter(function(p) {
        return !p.match(/[{}]/);
      });

      var parts = meta.types[meta.fields[topPath].type];

      for (var i=1; i<path.length; i++) {
        if (meta.types[parts[path[i]].type] !== undefined) {
          parts = meta.types[parts[path[i]].type];
        }
        else {
          parts = parts[path[i]];
        }
      }

      return parts;
    }
  };
}

servicesModule.service('Sites', Sites);
