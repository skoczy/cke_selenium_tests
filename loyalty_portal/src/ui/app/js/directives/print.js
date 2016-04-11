'use strict';

var directivesModule = require('./_index.js');

function printDirective() {
  var printSection = document.getElementById('print-section');

  // if there is no printing section, create one
  if (!printSection) {
    printSection = document.createElement('div');
    printSection.id = 'print-section';
    document.body.appendChild(printSection);
  }

  function link(scope, element, attrs) {
    element.on('click', function () {
      var elemToPrint = document.getElementById(attrs.printElementId);
      if (elemToPrint) {
        printElement(elemToPrint);
      }
    });
  }

  function printElement(elem) {
    // clones the element you want to print
    var domClone = elem.cloneNode(true);
    printSection.innerHTML = '';
    printSection.appendChild(domClone);
    window.print();
  }

  return {
    link: link,
    restrict: 'A'
  };
}

directivesModule.directive('ngPrint', printDirective);
