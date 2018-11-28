//控制层
app.controller('goodsController', function ($scope, $controller,$location, goodsService, uploadService, itemCatService, typeTemplateService) {

    $controller('baseController', {$scope: $scope});//继承


    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        goodsService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //分页
    $scope.findPage = function (page, rows) {
        goodsService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //查询实体
    $scope.findOne = function (id) {
        if (id == null) {
            var cid = $location.search()['id'];
            goodsService.findOne(cid).success(
                function (response) {
                    $scope.entity = response;
                    editor.html(response.goodsDesc.introduction);
                    $scope.entity.goodsDesc.itemImages = JSON.parse(response.goodsDesc.itemImages);
                    $scope.entity.goodsDesc.customAttributeItems = JSON.parse(response.goodsDesc.customAttributeItems);
                    $scope.entity.goodsDesc.specificationItems = JSON.parse(response.goodsDesc.specificationItems);
                    for (var i=0;i<response.itemList.length;i++) {
                        $scope.entity.itemList[i].spec = JSON.parse(response.itemList[i].spec);
                    }
                }
            );


        } else {
            return;
        }



    };

    //保存
    $scope.save=function(){
        //提取文本编辑器的值
        $scope.entity.goodsDesc.introduction=editor.html();
        var serviceObject;//服务层对象
        if($scope.entity.goods.id!=null){//如果有ID
            serviceObject=goodsService.update( $scope.entity ); //修改
        }else{
            serviceObject=goodsService.add( $scope.entity  );//增加
        }
        serviceObject.success(
            function(response){
                if(response.success){
                    alert('保存成功');
                    $scope.entity={};
                    editor.html("");
                }else{
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        goodsService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                }
            }
        );
    }

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function () {
        goodsService.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }
    $scope.uploadFile = function () {
        uploadService.uploadFile().success(
            function (response) {
                if (response.success) {
                    $scope.image_entity = {url: response.message};

                } else {
                    alert(response.message);
                }

            }
        );
    }
    $scope.entity = {goodsDesc: {itemImages: [], specificationItems: []}};
    $scope.add_image_entity = function () {

        $scope.entity.goodsDesc.itemImages.push($scope.image_entity);
    };
    $scope.selectItemCat1List = function () {
        itemCatService.findByParentId(0).success(
            function (response) {
                $scope.itemCat1List = response;
            }
        );
    };
    $scope.$watch('entity.goods.category1Id', function (newValue, oldValue) {
        itemCatService.findByParentId(newValue).success(
            function (response) {
                $scope.itemCat2List = response;
                $scope.itemCat3List = null;
                if ($location.search()['id'] == null) {
                    $scope.entity.goods.typeTemplateId = null;
                }
            }
        );

    });
    $scope.$watch('entity.goods.category2Id', function (newValue, oldValue) {
            itemCatService.findByParentId(newValue).success(
                function (response) {
                    $scope.itemCat3List = response;
                    if ($location.search()['id'] == null) {
                        $scope.entity.goods.typeTemplateId = null;
                    }
                }
            );

    });
    $scope.$watch('entity.goods.category3Id', function (newValue, oldValue) {
            itemCatService.findOne(newValue).success(
                function (response) {
                    $scope.entity.goods.typeTemplateId = response.typeId;
                }
            );

    });
    $scope.$watch('entity.goods.typeTemplateId', function (newValue, oldValue) {
        if (newValue != null) {
            typeTemplateService.findOne(newValue).success(
                function (response) {
                    $scope.typeTemplate = response;
                    $scope.typeTemplate.brandIds = JSON.parse(response.brandIds);
                    if ($location.search()['id'] == null) {
                        $scope.entity.goodsDesc.customAttributeItems = JSON.parse(response.customAttributeItems);
                    }

                }
            );
            typeTemplateService.findSpecListById(newValue).success(function (response) {
                $scope.specList = response;
            })
        } else {
            $scope.entity.goodsDesc.customAttributeItems = null;
            $scope.specList = null;
        }

    });
    $scope.updateSpecAttribute = function (name, value, $event) {

        var Object = $scope.searchObjectByKey($scope.entity.goodsDesc.specificationItems, 'attributeName', name);

        if (Object != null) {
            if ($event.target.checked) {
                if (Object.attributeValue.indexOf(value) < 0) {
                    Object.attributeValue.push(value);
                }

            } else if (!$event.target.checked) {
                if (Object.attributeValue.length > 1) {
                    Object.attributeValue.splice(Object.attributeValue.indexOf(value), 1);
                } else {
                    $scope.entity.goodsDesc.specificationItems.splice($scope.entity.goodsDesc.specificationItems.indexOf(name), 1);
                }
            }
        }
        else {
            $scope.entity.goodsDesc.specificationItems.push({'attributeName': name, 'attributeValue': [value]});
        }

    };
//            打豆豆

    $scope.decareItems = function (specificationItems) {
        $scope.entity.itemList = [{spec: {}, price: 0, num: 9999, status: '0', isDefault: '0'}];
        if (specificationItems != null) {
            for (var i = 0; i < specificationItems.length; i++) {
                $scope.entity.itemList = itemclone($scope.entity.itemList, specificationItems[i].attributeName, specificationItems[i].attributeValue);
            }
        }
    };
    itemclone = function (list, name, values) {
        var newList = [];
        for (var i = 0; i < list.length; i++) {
            for (var j = 0; j < values.length; j++) {
                var newRow = JSON.parse(JSON.stringify(list[i]));
                newRow['spec'][name] = values[j];
                newList.push(newRow);
            }
        }
        return newList;

    }
    $scope.$watch('entity.goods.isEnableSpec', function (newValue) {
        if (newValue == 0) {
            $scope.entity.goodsDesc.specificationItems = [];
            $scope.entity.itemList = [];
        }

    });
    $scope.status = ['未审核', '审核通过', '审核未通过', '已关闭'];
    $scope.itemCatList = [];
    $scope.findItemList = function () {
        itemCatService.findAll().success(function (response) {
            for (var i = 0; i < response.length; i++) {
                $scope.itemCatList[response[i].id] = response[i].name;
            }
        });
    }
    $scope.checkAttributeValue=function(specName,optionName){
        var items= $scope.entity.goodsDesc.specificationItems;
        var object= $scope.searchObjectByKey(items,'attributeName',specName);
        if(object==null){
            return false;
        }else{
            if(object.attributeValue.indexOf(optionName)>=0){
                return true;
            }else{
                return false;
            }
        }
    }

});
