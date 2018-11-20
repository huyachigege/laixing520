app.controller('brandController', function ($scope,$controller,brandService) {
    $controller('baseController', {$scope: $scope});
    $scope.findAll = function () {
        brandService.findAll().success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.itemsPerPage = response.total;
            }
        );
    };


    $scope.findPage = function () {
        brandService.findPage($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;
            }
        );
    };
    $scope.searchBrand = {};
    $scope.search = function () {
        brandService.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage, $scope.searchBrand).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;
            }
        );
    };
    $scope.save = function () {
        var method;
        if ($scope.brand.id != null) {
            method = brandService.update($scope.brand);
        } else {
            method = brandService.add($scope.brand);
        }
        method.success(function (response) {
            if (response.success) {
                $scope.reloadList();
            } else {
                alert(response.message);
            }
        });
    };
    $scope.findOne = function (id) {
        brandService.findOne(id).success(function (response) {
            $scope.brand = response;
        });
    };
    $scope.selectIds = [];
    $scope.delete = function () {
        brandService.delete($scope.selectIds).success(function (response) {
            if (response.success) {
                $scope.reloadList();
            } else {
                alert(response.message);
            }
        });
    };

});
