app.service("brandService", function ($http) {
    this.findAll = function () {
        return $http.get('../brand/findAll.do');
    };
    this.findPage = function (page, size) {
        return $http.get('../brand/findPage.do?page=' + page + '&size=' + size);
    }
    this.search = function (page, size, brand) {
        return $http.post('../brand/search.do?page=' + page + '&size=' + size, brand);

    };
    this.update = function (brand) {
        return $http.post('../brand/update.do', brand);
    }
    this.add = function (brand) {
        return $http.post('../brand/add.do', brand);
    }
    this.findOne = function (id) {
        return $http.get('../brand/findOne.do?id=' + id);
    };
    this.delete = function (ids) {
        return $http.get('../brand/delete.do?ids=' + ids);
    }
});