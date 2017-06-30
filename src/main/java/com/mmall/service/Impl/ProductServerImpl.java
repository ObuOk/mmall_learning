package com.mmall.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.ICategoryService;
import com.mmall.service.IProductServer;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojianzhong on 2017/6/27.
 */
@Service(value = "iProductService")
public class ProductServerImpl implements IProductServer {


    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ICategoryService iCategoryService;

    @Override
    public ServerResponse saveOrUpdateProduct(Product product) {

        if (product != null) {
            if (product.getSubImages() != null) {
                String[] subImages = product.getSubImages().split(",");
                if (subImages.length > 0) {
                    product.setMainImage(subImages[0]);
                }
            }
                if (product.getId() != null) {
                    int resultCount = productMapper.updateByPrimaryKeySelective(product);
                    if (resultCount > 0) {
                        return ServerResponse.createBySuccess("更新成功");
                    }
                        return ServerResponse.createBySuccess("更新失败");
                }else {

                    int resultCount = productMapper.insert(product);
                    if (resultCount > 0) {
                        return ServerResponse.createBySuccess("保存成功");
                    }
                        return ServerResponse.createBySuccess("保存失败");
                }
        }else {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
    }

    @Override
    public ServerResponse setSaleStatus(Integer productId, Integer status) {

        if (productId == null || status == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);

        int resultCount = productMapper.updateByPrimaryKeySelective(product);
        if (resultCount > 0) {
            return ServerResponse.createBySuccess("设置成功");
        }
            return ServerResponse.createByErrorMessage("设置失败");
    }

    @Override
    public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId) {

        if (productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            ServerResponse.createByErrorMessage("商品已下架或者删除");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);


        return ServerResponse.createBySuccess(productDetailVo);
    }

    @Override
    public ServerResponse<PageInfo> getProductList(Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum,pageSize);
        List<Product> products = productMapper.selectList();
        ArrayList<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product productsItem: products
             ) {
            ProductListVo productListVo = assembleProductListVo(productsItem);
            productListVoList.add(productListVo);
        }
        PageInfo pageInfo = new PageInfo(products);
        pageInfo.setList(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    @Override
    public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum,pageSize);
        productName = new StringBuilder("%").append(productName).append("%").toString();
        List<Product> products = productMapper.selectProductByNameAndId(productName, productId);
        ArrayList<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product productsItem: products
                ) {
            ProductListVo productListVo = assembleProductListVo(productsItem);
            productListVoList.add(productListVo);
        }
        PageInfo pageInfo = new PageInfo(products);
        pageInfo.setList(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    private ProductListVo assembleProductListVo(Product product) {
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setPrice(product.getPrice());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setStatus(product.getStatus());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
        return productListVo;
    }

    private ProductDetailVo assembleProductDetailVo(Product product) {
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setStock(product.getStock());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setSubtitle(product.getSubtitle());


        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));


        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));


        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());

        if (category == null) {
            productDetailVo.setParentCategoryId(0);
        }else {
            productDetailVo.setParentCategoryId(category.getParentId());
        }
        return productDetailVo;
    }

    @Override
    public ServerResponse<ProductDetailVo> productDetail(Integer productId) {

        if (productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            ServerResponse.createByErrorMessage("商品已下架或者删除");
        }

        if (product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()) {
            ServerResponse.createByErrorMessage("商品已下架或者删除");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }

    @Override
    public ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, Integer pageNum, Integer pageSize, String orderBy) {

        if (StringUtils.isBlank(keyword) && categoryId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        List<Integer> categoryIdList = Lists.newArrayList();

        if (categoryId != null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && StringUtils.isBlank(keyword)) {
                PageHelper.startPage(pageNum, pageSize);
                ArrayList<ProductListVo> productVoList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productVoList);
                return ServerResponse.createBySuccess(pageInfo);
            }
            categoryIdList = iCategoryService.selectCategoryAndChildrenById(category.getId()).getData();
        }



            if (StringUtils.isNotBlank(keyword)) {
                keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
            }

            PageHelper.startPage(pageNum, pageSize);

            if (StringUtils.isNotBlank(orderBy)) {
                if (Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
                    String[] orderByArray = orderBy.split("_");
                    PageHelper.orderBy(orderByArray[0] + " " + orderByArray[1]);
                }
            }
            List<Product> products = productMapper.selectByNameAndCategoryId(StringUtils.isBlank(keyword) ? null : keyword, categoryIdList.size() == 0 ? null : categoryIdList);

            List<ProductListVo> productListVoList = Lists.newArrayList();
            for (Product product :
                    products) {
                ProductListVo productListVo = assembleProductListVo(product);
                productListVoList.add(productListVo);
            }
            PageInfo pageInfo = new PageInfo(products);
            pageInfo.setList(productListVoList);
            return ServerResponse.createBySuccess(pageInfo);
        }

}
