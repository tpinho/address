var app = angular.module('addresses', ['ngResource', 'ngGrid', 'ui.bootstrap']);

app.controller('addressesListController', function ($scope, $rootScope, addressService, searchService) {
    $scope.sortInfo = {fields: ['id'], directions: ['asc']};
    $scope.addresses = {currentPage: 1};

    $scope.gridOptions = {
        data: 'addresses.list',
        useExternalSorting: false,
        sortInfo: $scope.sortInfo,

        columnDefs: [
            { field: 'id', displayName: 'Id' },
            { field: 'street', displayName: 'Street' },
            { field: 'number', displayName: 'Number' },
            { field: 'neighborhood', displayName: 'Neighborhood' },
            { field: '', width: 30, cellTemplate: '<span class="glyphicon glyphicon-remove remove" ng-click="deleteRow(row)"></span>' }
        ],

        multiSelect: false,
        selectedItems: [],
        afterSelectionChange: function (rowItem) {
            if (rowItem.selected) {
                $rootScope.$broadcast('addressSelected', $scope.gridOptions.selectedItems[0].id);
            }
        }
    };

    $scope.refreshGrid = function () {
        var listAddressesArgs = {
            page: $scope.addresses.currentPage,
            sortFields: $scope.sortInfo.fields[0],
            sortDirections: $scope.sortInfo.directions[0]
        };

        addressService.get(listAddressesArgs, function (data) {
            $scope.addresses = data;
        })
    };

    $scope.deleteRow = function (row) {
        $rootScope.$broadcast('deleteAddress', row.entity.id);
    };

    $scope.$watch('sortInfo.fields[0]', function () {
        $scope.refreshGrid();
    }, true);

    $scope.$on('ngGridEventSorted', function (event, sortInfo) {
        $scope.sortInfo = sortInfo;
    });

    $scope.$on('refreshGrid', function () {
        $scope.refreshGrid();
    });

    $scope.$on('clear', function () {
        $scope.gridOptions.selectAll(false);
    });
});

app.controller('addressesFormController', function ($scope, $rootScope, addressService, searchService) {
    $scope.clearForm = function () {
        $scope.address = null;
        $scope.addressForm.$setPristine();
        $rootScope.$broadcast('clear');
    };

    $scope.updateAddress = function () {
        addressService.save($scope.address).$promise.then(
            function () {
                $rootScope.$broadcast('refreshGrid');
                $rootScope.$broadcast('addressSaved');
                $scope.clearForm();
            },
            function () {
                $rootScope.$broadcast('error');
            });
    };

    $scope.$on('addressSelected', function (event, id) {
        $scope.address = addressService.get({id: id});
    });

    $scope.$on('deleteAddress', function (event, id) {
        addressService.delete({id: id}).$promise.then(
            function () {
                $rootScope.$broadcast('refreshGrid');
                $rootScope.$broadcast('addressDeleted');
                $scope.clearForm();
            },
            function () {
                $rootScope.$broadcast('error');
            });
    });
    
    $scope.searchZipcode = function () {
    	searchService.get({zipcode: $scope.address.zipcode}).$promise.then(
    		function (data) {
    			$scope.address.street = data.street;
    			$scope.address.neighborhood = data.neighborhood;
    			$scope.address.city = data.city;
    			$scope.address.state = data.state;
    			$scope.address.zipcode = data.zipcode;
    			$rootScope.$broadcast('resetAlerts');
    		},
    		function (error) {
    			$scope.address.street = null;
    			$scope.address.neighborhood = null;
    			$scope.address.city = null;
    			$scope.address.state = null;
                $rootScope.$broadcast('zipcodeUnknown');
            });
    };
});

app.controller('alertMessagesController', function ($scope) {
    $scope.$on('addressSaved', function () {
        $scope.alerts = [
            { type: 'success', msg: 'Record saved successfully!' }
        ];
    });

    $scope.$on('addressDeleted', function () {
        $scope.alerts = [
            { type: 'success', msg: 'Record deleted successfully!' }
        ];
    });

    $scope.$on('error', function () {
        $scope.alerts = [
            { type: 'danger', msg: 'There was a problem in the server!' }
        ];
    });
    
    $scope.$on('zipcodeUnknown', function () {
        $scope.alerts = [
            { type: 'danger', msg: 'Zipcode unknown!' }
        ];
    });
    
    $scope.$on('resetAlerts', function() {
    	$scope.alerts = [];
    });

    $scope.closeAlert = function (index) {
        $scope.alerts.splice(index, 1);
    };
});

app.factory('addressService', function ($resource) {
    return $resource('resources/addresses/:id');
});

app.factory('searchService', function ($resource) {
	return $resource('resources/addresses/searchAddress');
});

angular.module('exceptionOverride', []).factory('$exceptionHandler', function() {
	  return function(exception, cause) {
	    exception.message += ' (caused by "' + cause + '")';
	    throw exception;
	  };
	});
