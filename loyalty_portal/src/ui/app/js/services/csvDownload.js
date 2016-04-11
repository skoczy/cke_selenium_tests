'use strict';

var servicesModule = require('./_index.js');

/**
 * @ngInject
 */
function CsvDownload($location) {
  return  {
    /**
     * Download a CSV file based on a two-level array of values.
     */
    getCsv: function(csvContent, fileName) {
      var content = '';

      for (var i=0; i<csvContent.length; i++) {
        content += csvContent[i].join(';');

        if (i<csvContent.length - 1) {
          content += '\n';
        }
      }

      var a = document.createElement('a'),
          mimeType = 'application/octet-stream';


      if (navigator.msSaveBlob) { // IE10
        return navigator.msSaveBlob(new Blob([content], { type: mimeType }),     fileName);
      } else if ('download' in a) { //html5 A[download]
        a.href = 'data:' + mimeType + ',' + encodeURIComponent(content);
        a.setAttribute('download', fileName);
        document.body.appendChild(a);
        setTimeout(function() {
          a.click();
          document.body.removeChild(a);
        }, 66);
        return true;
      } else { //do iframe dataURL download (old ch+FF):
        var f = document.createElement('iframe');
        document.body.appendChild(f);
        f.src = 'data:' + mimeType + ',' + encodeURIComponent(content);

        setTimeout(function() {
          document.body.removeChild(f);
        }, 333);

        return true;
      }
    }
  };
}

servicesModule.service('CsvDownload', CsvDownload);
