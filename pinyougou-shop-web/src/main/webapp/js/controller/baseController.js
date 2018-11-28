app.controller('baseController', function ($scope) {
    $scope.reloadList = function () {
        if ($scope.paginationConf.itemsPerPage == $scope.paginationConf.totalItems) {
            // $scope.findAll();
            $scope.search();
            $scope.selectIds = [];
        }
        else {
            $scope.search();
            $scope.selectIds = [];
        }
    };
    $scope.isSelected = function (id) {
        if ($scope.selectIds.indexOf(id) > -1) {
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
    $scope.changeChecked = function ($event, id) {
        if ($event.target.checked) {
            $scope.selectIds.push(id);
        } else {
            var index = $scope.selectIds.indexOf(id);
            $scope.selectIds.splice(index, 1);
        }

    };
    $scope.changeAll = function ($event) {
        if ($event.target.checked) {
            $("#dataList input").each(function () {
                this.checked = true;
            });
            $scope.selectIds = [];
            for (var i = 0; i < $scope.list.length; i++) {
                $scope.selectIds.push($scope.list[i].id);
            }
        } else {
            $("#dataList input").each(function () {
                this.checked = false;
            });
            $scope.selectIds = [];
        }

    };
    $scope.searchObjectByKey = function (list, key, value) {
        for (var i = 0; i < list.length; i++) {
            if (list[i][key] ==value ) {
                return list[i];
            }
        }
        return null;
    };


});