'use strict';

var AppSettings = {
  appTitle: 'Sitemaster',
  apiUrl: '/data',
  stationGeneralDataFields: [
    'stationMan',
    'address',
    'phone',
    'fax',
    'country',
    'category',
    'stationType',
    'compName',
    'orgNumber',
    'email',
    'stationFormat',
    'salesZone',
    'district',
    // 'chainAgreement',
    'eanCode',
    'clusterName',
    'status',
    'xCoord',
    'yCoord',
    'chainConvenience'
  ],
  searchFields: [
    'id',
    'marketingName',
    'address',
    'xCoord',
    'yCoord'
  ],
  defaultSearchFields: [
    'id',
    'marketingName',
    'addressstreet',
    'addresspostalCode',
    'addresscity',
  ],
  searchFilterFields: [
    'siteId',
    'stationName',
    'stationMan',
    'salesZone',
    'district',
    'status'
  ],
  searchMeta: {
    "types" : {
      "address" : {
        "street" : {
          "label" : "Street",
          "type" : "string",
          "owner" : "JDE"
        },
        "postalCode" : {
          "label" : "Postal code",
          "type" : "string",
          "owner" : "JDE"
        },
        "city" : {
          "label" : "City",
          "type" : "string",
          "owner" : "JDE"
        },
        "county" : {
          "label" : "County",
          "type" : "string",
          "owner" : "JDE"
        },
        "country" : {
          "label" : "Country",
          "type" : "string",
          "owner" : "JDE"
        },
        "phone": {
          "label" : "Phone",
          "type" : "string",
          "owner" : "JDE"
        }
      },
      "openingTimes" : {
        "open" : {
          "label" : "Open",
          "type" : "string",
          "owner" : "SITEMASTER"
        },
        "close" : {
          "label" : "Close",
          "type" : "string",
          "owner" : "SITEMASTER"
        }
      },
      "temporarilyClosed" : { },
      "openingInfo" : {
        "alwaysOpen" : {
          "label" : "Always open",
          "type" : "boolean",
          "owner" : "SITEMASTER"
        },
        "openingTimes" : {
          "label" : "Opening times",
          "type" : "openingTimes",
          "owner" : "SITEMASTER"
        },
        "temporarilyClosed" : {
          "label" : "Temporarily closed",
          "type" : "temporarilyClosed",
          "owner" : "SITEMASTER"
        }
      },
      "phone" : {
        "prefix" : {
          "label" : "Prefix",
          "type" : "string",
          "owner" : "JDE"
        },
        "number" : {
          "label" : "Number",
          "type" : "string",
          "owner" : "JDE"
        },
        "type" : {
          "label" : "Phone type",
          "type" : "string",
          "owner" : "JDE"
        }
      }
    },
    "fields" : {
      "id" : {
        "label" : "ID",
        "type" : "string",
        "owner" : "SITEMASTER"
      },
      "siteId" : {
        "label" : "Station ID",
        "type" : "string",
        "owner" : "JDE"
      },
      "stationId": {
        "label" : "Station ID",
        "type" : "string",
        "owner" : "JDE"
      },
      "stationName" : {
        "label" : "Station name",
        "type" : "string",
        "owner" : "JDE"
      },
      "marketingName": {
        "label" : "Station name",
        "type" : "string",
        "owner" : "JDE"
      },
      "stationMan" : {
        "label" : "Station Man",
        "type" : "string",
        "owner" : "JDE"
      },
      "category" : {
        "label" : "Category",
        "type" : "string",
        "owner" : "JDE"
      },
      "owner" : {
        "label" : "Owner",
        "type" : "string",
        "owner" : "JDE"
      },
      "district" : {
        "label" : "District",
        "type" : "string",
        "owner" : "JDE"
      },
      "siteType" : {
        "label" : "Site type",
        "type" : "string",
        "owner" : "JDE"
      },
      "salesZone" : {
        "label" : "Sales zone",
        "type" : "string",
        "owner" : "JDE"
      },
      "compName" : {
        "label" : "Company name",
        "type" : "string",
        "owner" : "JDE"
      },
      "orgNumber" : {
        "label" : "Organisation number",
        "type" : "string",
        "owner" : "JDE"
      },
      "clusterName" : {
        "label" : "Cluster name",
        "type" : "string",
        "owner" : "JDE"
      },
      "dealerStationManName" : {
        "label" : "Dealer Station Man",
        "type" : "string",
        "owner" : "JDE"
      },
      "areaSalesManagerName" : {
        "label" : "Area Sales Manager",
        "type" : "string",
        "owner" : "JDE"
      },
      "status" : {
        "label" : "Status",
        "type" : "string",
        "owner" : "JDE"
      },
      "eanCode" : {
        "label" : "EAN Code",
        "type" : "string",
        "owner" : "JDE"
      },
      "email" : {
        "label" : "E-mail",
        "type" : "string",
        "owner" : "JDE"
      },
      "address" : {
        "label" : "Address",
        "type" : "address",
        "owner" : "JDE"
      },
      "openingInfo" : {
        "label" : "Opening info",
        "type" : "openingInfo",
        "owner" : "SITEMASTER"
      },
      "phone" : {
        "label" : "Phone",
        "type" : "phone",
        "owner" : "JDE"
      },
      "xCoord" : {
        "label" : "X Coordinates",
        "type": "string",
        "owner": "SITEMASTER"
      },
      "yCoord" : {
        "label" : "Y Coordinates",
        "type": "string",
        "owner": "SITEMASTER"
      }
    }
  },
  "sitePersonRoles":[
    {
      "name" : "SMA",
      "label" : "Station Manager Assistant",
      "fromJDE" : false
    },
    {
      "name" : "CT1",
      "label" : "Caretaker1",
      "fromJDE" : false
    },
    {
      "name" : "CT2",
      "label" : "Caretaker2",
      "fromJDE" : false
    }
  ],
  "siteServiceTypes": [
    {
      "name": "FeatureCarRental",
      "label": "Car Rental",
      "icon": "FeatureCarRental",
    },
    {
      "name": "FeatureTrailerRental",
      "label": "Trailer rental",
      "icon": "FeatureTrailerRental",
    },
    {
      "name": "FeatureCoffee",
      "label": "Simply Great Coffee",
      "icon": "FeatureCoffee",
    },
    {
      "name": "FeatureRoutexAtlas",
      "label": "Routex Atlas",
      "icon": "FeatureRoutexAtlas",
    },
    {
      "name": "FeatureTruckDiesel",
      "label": "TruckDiesel Network",
      "icon": "FeatureTruckDiesel",
    },
    {
      "name": "FeatureShower",
      "label": "Shower",
      "icon": "FeatureShower",
    },
    {
      "name": "FeatureCarWash",
      "label": "Car wash",
      "icon": "FeatureCarWash",
    },
    {
      "name": "FeatureCarWashJetWash",
      "label": "Car wash jetwash",
      "icon": "FeatureCarWashJetWash",
    },
    {
      "name": "FeatureTruckParking",
      "label": "Truck parking",
      "icon": "FeatureTruckParking",
    },
    {
      "name": "FeatureTruckersLounge",
      "label": "Lounge",
      "icon": "FeatureTruckersLounge",
    },
    {
      "name": "FeatureAdBlue",
      "label": "AdBlue",
      "icon": "FeatureAdBlue",
    },
    {
      "name": "FeatureHotDog",
      "label": "Made to Go fast food",
      "icon": "FeatureHotDog",
    },
    {
      "name": "FeatureWasherFluidPump",
      "label": "Windshield liquid on pump",
      "icon": "FeatureWasherFluidPump",
    },
    {
      "name": "FeatureHighSpeedCharger",
      "label": "High speed charger",
      "icon": "FeatureHighSpeedCharger",
    },
    {
      "name": "FeatureWifi",
      "label": "Wifi",
      "icon": "FeatureWifi",
    },
    {
      "name": "FeatureCashPayment",
      "label": "ATM",
      "icon": "FeatureCashPayment",
    },
  ]
};

module.exports = AppSettings;
