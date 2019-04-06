package com.zhihao.sell.controller;

import com.zhihao.sell.dataobject.ProductCategory;
import com.zhihao.sell.dataobject.ProductInfo;
import com.zhihao.sell.exception.SellException;
import com.zhihao.sell.form.ProductForm;
import com.zhihao.sell.service.CategoryService;
import com.zhihao.sell.service.ProductService;
import com.zhihao.sell.utils.KeyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/seller/product")
public class SellerProductController {

  @Autowired
  private ProductService productService;

  @Autowired
  private CategoryService categoryService;


  /**
   * List all the product for the seller.
   *
   * @param page - which page to return
   * @param size - how many items in one page
   * @param map - map used to transfer data to template
   * @return all the product for the seller
   */
  @GetMapping("/list")
  public ModelAndView list(@RequestParam(value = "page", defaultValue = "1") Integer page,
      @RequestParam(value = "size", defaultValue = "10") Integer size,
      Map<String, Object> map) {
    PageRequest request = new PageRequest(page - 1, size);
    Page<ProductInfo> productInfoPage = productService.findAll(request);
    map.put("productInfoPage", productInfoPage);
    map.put("currentPage", page);
    map.put("size", size);
    return new ModelAndView("product/list", map);
  }

  /**
   * Controller for on sale.
   *
   * @param productId - the id of the product
   * @param map - used to transfer data to template
   * @return an ModelAndView object
   */
  @RequestMapping("/on_sale")
  public ModelAndView onSale(@RequestParam("productId") String productId,
      Map<String, Object> map) {
    try {
      productService.onSale(productId);
    } catch (SellException e) {
      map.put("msg", e.getMessage());
      map.put("url", "/sell/seller/product/list");
      return new ModelAndView("common/error", map);
    }

    map.put("url", "/sell/seller/product/list");
    return new ModelAndView("common/success", map);
  }


  /**
   * Controller for off sale.
   *
   * @param productId - the id of the product
   * @param map - used to transfer data to template
   * @return an ModelAndView object
   */
  @RequestMapping("/off_sale")
  public ModelAndView offSale(@RequestParam("productId") String productId,
      Map<String, Object> map) {
    try {
      productService.offSale(productId);
    } catch (SellException e) {
      map.put("msg", e.getMessage());
      map.put("url", "/sell/seller/product/list");
      return new ModelAndView("common/error", map);
    }

    map.put("url", "/sell/seller/product/list");
    return new ModelAndView("common/success", map);
  }


  /**
   * Get a particular product.
   * @param productId - the id of the product
   * @param map - the map used to transfer data to front end.
   * @return a particular product
   */
  @GetMapping("/index")
  public ModelAndView index(@RequestParam(value = "productId", required = false) String productId,
      Map<String, Object> map) {
    if (!StringUtils.isEmpty(productId)) {
      ProductInfo productInfo = productService.findOne(productId);
      map.put("productInfo", productInfo);
    }

    // check all categories
    List<ProductCategory> categoryList = categoryService.findAll();
    map.put("categoryList", categoryList);

    return new ModelAndView("product/index", map);
  }

  // save and update

  @PostMapping("/save")
  public ModelAndView save(@Valid ProductForm form,
      BindingResult bindingResult,
      Map<String, Object> map) {
    if (bindingResult.hasErrors()) {
      map.put("msg", bindingResult.getFieldError().getDefaultMessage());
      map.put("url", "/sell/seller/product/index");
      return new ModelAndView("common/error", map);
    }

    ProductInfo productInfo = new ProductInfo();
    try {
      if (!StringUtils.isEmpty(form.getProductId())) {
        productInfo = productService.findOne(form.getProductId());
      } else {
        form.setProductId(KeyUtil.genUniqueKey());
      }
      BeanUtils.copyProperties(form, productInfo);
      productService.save(productInfo);
    } catch (SellException e) {
      map.put("msg", e.getMessage());
      map.put("url", "/sell/seller/product/index");
      return new ModelAndView("common/error", map);
    }

    map.put("url", "/sell/seller/product/list");
    return new ModelAndView("common/success", map);
  }
}
