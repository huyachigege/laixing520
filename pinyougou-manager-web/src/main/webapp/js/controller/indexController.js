app.controller('indexController',function ($scope,loginService) {
    $scope.loginName=function () {
        loginService.loginName().success(function (respone) {
            $scope.loginName = respone.loginName;

        });
    }
})