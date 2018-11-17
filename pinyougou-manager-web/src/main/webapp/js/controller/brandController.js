app.controller('brandController', function ($scope, $http, brandService) {
    $scope.findAll = function () {
        brandService.findAll().success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.itemsPerPage = response.total;
            }
        );
    };

    $scope.reloadList = function () {
        if ($scope.paginationConf.itemsPerPage == $scope.paginationConf.totalItems) {
            $scope.findAll();
        }

        else {
            $scope.search();
        }

    };
    $scope.isSelected = function (id) {
        if ($scope.ids.indexOf(id) > -1) {
            return true;
        }
    };
    $scope.paginationConf = {
        currentPage: 1,
        // totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function () {
            $scope.reloadList();

        }//重新加载
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
    $scope.ids = [];
    $scope.delete = function () {
        brandService.delete($scope.ids).success(function (response) {
            if (response.success) {
                $scope.reloadList();
            } else {
                alert(response.message);
            }
        });
    };
    $scope.changeChecked = function ($event, id) {
        if ($event.target.checked) {
            $scope.ids.push(id);
        } else {
            var index = $scope.ids.indexOf(id);
            $scope.ids.splice(index, 1);
        }

    };
    $scope.changeAll = function ($event) {
        if ($event.target.checked) {
            $("#dataList input").each(function () {
                this.checked = true;
            });
            $scope.ids = [];
            for (var i = 0; i < $scope.list.length; i++) {
                $scope.ids.push($scope.list[i].id);
            }
        } else {
            $("#dataList input").each(function () {
                this.checked = false;
            });
            $scope.ids = [];
        }

    };
});
