package com.wmy.cosmetic.web;

import com.github.pagehelper.PageInfo;
import com.wmy.cosmetic.Exception.ServiceException;
import com.wmy.cosmetic.entity.Employee;
import com.wmy.cosmetic.entity.Product;
import com.wmy.cosmetic.entity.Result;
import com.wmy.cosmetic.entity.ResultCode;
import com.wmy.cosmetic.service.OperationFile;
import com.wmy.cosmetic.service.ProductService;
import com.wmy.cosmetic.utils.UuidUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;


@Controller
@RequestMapping("product")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    @Value("${project.path.domain}")
    String domain;
    @Value("${project.path.productImagepath}")
    String productImagepath;
    @Value("${project.path.productImagepath2}")
    String productImagepath2;
    @Autowired
    ProductService productService;

    @ResponseBody
    @RequestMapping(value = "getProductList", method = RequestMethod.GET)
    public Result<Product> getProductList(@RequestParam(name = "product_id", required = false) Integer product_id,
                                          @RequestParam(name = "product_name", required = false) String product_name,
                                          @RequestParam(name = "product_type", required = false) Integer product_type,
                                          @RequestParam(name = "page", required = false) int page,
                                          @RequestParam(name = "limit", required = false) int number) {
        Result<Product> result = new Result<>();
        result.setCode(0);//默认交易成功
        try {
            PageInfo<Product> productList = productService.getProductList(product_id, product_name,product_type, page, number);
            result.setMsg("产品列表查询成功");
            result.setCount(productList.getTotal());
            result.setData(productList.getList());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setCode(1);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/addProduct")
    @ResponseBody
    public Result<Product> addProduct(Product product, HttpSession session) {
        Result<Product> result = new Result<>();
        result.setCode(0);//默认交易成功
        try {
            Employee empl = (Employee) session.getAttribute("user");
            String path = (String) session.getAttribute(empl.getUsername());
            String filename = path.substring(path.lastIndexOf("/")+1);
            String frompath=productImagepath+"/"+filename;
            String topath=productImagepath2+"/"+filename;
            OperationFile.copyFile(frompath,topath);
            product.setProduct_img(path);
            productService.addProduct(product);
            result.setMsg("添加成功");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setCode(1);
            result.setMsg(e.getMessage());
        }
        return result;
    }


    @PostMapping(value = "/updateProduct")
    @ResponseBody
    public Result<Product> updateProduct(@RequestParam("product_id") Integer product_id,
                                         @RequestParam("product_name") String product_name,
                                         @RequestParam("product_account") String product_account,
                                         @RequestParam("product_price") Double product_price,
                                         @RequestParam("product_type") Integer product_type,
                                         @RequestParam("product_message") String product_message) {
        Result<Product> result = new Result<>();
        result.setCode(0);//默认交易成功
        try {
            Product product=new Product();
            product.setProduct_id(product_id);
            product.setProduct_name(product_name);
            product.setProduct_account(product_account);
            product.setProduct_price(product_price);
            product.setProduct_type(product_type);
            product.setProduct_message(product_message);
            productService.updateProduct(product);
            result.setMsg("修改成功");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setCode(1);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/findProduct")
    public String findProduct(@RequestParam(name = "product_id", required = false) int product_id, Model model) {
        if (StringUtils.isEmpty(product_id)) {
            throw new ServiceException("商品id为空");
        }
        model.addAttribute("productType", productService.productTypeList());
        model.addAttribute("product", productService.findProductById(product_id));
        return "products/updateProductform";
    }
    @ResponseBody
    @RequestMapping(value = "/deleteProduct")
    public Result<String> deleteProduct(@RequestParam(name = "product_id") int product_id) {
        Result<String> result=new Result<>();
        result.setCode(0);//默认交易成功
        try{
            productService.deleteProduct(product_id);
            result.setMsg("删除成功");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            result.setCode(1);
            result.setMsg("删除失败");
        }
        return result;
    }

    @PostMapping("uploadProductImg")
    @ResponseBody
    public String uploadHeader(@RequestParam(value = "file") MultipartFile headerImage, HttpSession session) throws IOException {
        if (headerImage == null) {
            throw new ServiceException("图片为空");
        }
        String filename = headerImage.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        if (suffix.isEmpty()) {
            throw new ServiceException("文件格式不正确");
        }
        //生成随机文件名
        filename = UuidUtils.getUUID() + suffix;
        //确定文件存放路径
        File dest = new File(productImagepath + "/" + filename);
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败" + e.getMessage());
            throw new RuntimeException("上传文件失败，服务器发生异常" + e);
        }
        String productImgUrl = domain + "/product/getImg/" + filename;
        Employee empl = (Employee) session.getAttribute("user");
        session.setAttribute(empl.getUsername(), productImgUrl);
        return Result.success();
    }

    /**
     * 读取商品图片
     *
     * @param fileName
     * @param response
     */
    @GetMapping("getImg/{fileName}")
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        //服务器存放路径
        fileName = productImagepath2 + "/" + fileName;
        //文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        //响应图片
        response.setContentType("image/" + suffix);
        try {
            OutputStream os = response.getOutputStream();
            FileInputStream file = new FileInputStream(fileName);
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = file.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取失败" + e.getMessage());
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/updateProductImg")
    @ResponseBody
    public Result<Product> updateProductImg(@RequestParam(value = "product_id") Integer product_id,@RequestParam(value = "file") MultipartFile productImgUrl) {
        Result<Product> result = new Result<>();
        result.setCode(0);//默认交易成功
        try {
            if (productImgUrl == null) {
                throw new ServiceException("图片为空");
            }
            String filename = productImgUrl.getOriginalFilename();
            String suffix = filename.substring(filename.lastIndexOf("."));
            if (suffix.isEmpty()) {
                throw new ServiceException("文件格式不正确");
            }
            //生成随机文件名
            filename = UuidUtils.getUUID() + suffix;
            //确定文件存放路径
            File dest = new File(productImagepath2 + "/" + filename);
            productImgUrl.transferTo(dest);
            String proImgUrl = domain + "/product/getImg/" + filename;
            productService.updateProductImg(product_id,proImgUrl);
            result.setMsg("修改成功");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setCode(1);
            result.setMsg(e.getMessage());
        }
        return result;
    }
}
