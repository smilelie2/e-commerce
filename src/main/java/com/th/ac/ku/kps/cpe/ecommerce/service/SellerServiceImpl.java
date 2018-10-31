package com.th.ac.ku.kps.cpe.ecommerce.service;

import com.th.ac.ku.kps.cpe.ecommerce.model.*;
import com.th.ac.ku.kps.cpe.ecommerce.model.UserEntity;
import com.th.ac.ku.kps.cpe.ecommerce.model.buyer.order.read.OrderReadResponse;
import com.th.ac.ku.kps.cpe.ecommerce.model.buyer.order.update.OrderUpdateRequest;
import com.th.ac.ku.kps.cpe.ecommerce.model.buyer.order.update.OrderUpdateResponse;
import com.th.ac.ku.kps.cpe.ecommerce.model.seller.product.ProductEntity;
import com.th.ac.ku.kps.cpe.ecommerce.model.seller.product.ProductPicEntity;
import com.th.ac.ku.kps.cpe.ecommerce.model.seller.product.delete.ProductDeleteRequest;
import com.th.ac.ku.kps.cpe.ecommerce.model.seller.product.delete.ProductDeleteResponse;
import com.th.ac.ku.kps.cpe.ecommerce.model.seller.product.read.*;
import com.th.ac.ku.kps.cpe.ecommerce.model.seller.product.update.ProductUpdateRequest;
import com.th.ac.ku.kps.cpe.ecommerce.model.seller.product.update.ProductUpdateResponse;
import com.th.ac.ku.kps.cpe.ecommerce.model.seller.shipofshop.read.ShipOfShopReadBodyResponse;
import com.th.ac.ku.kps.cpe.ecommerce.model.seller.shipofshop.read.ShipOfShopReadDataBodyResponse;
import com.th.ac.ku.kps.cpe.ecommerce.model.seller.shipofshop.read.ShipOfShopReadResponse;
import com.th.ac.ku.kps.cpe.ecommerce.model.seller.shipofshop.create.ShipOfShopCreateRequest;
import com.th.ac.ku.kps.cpe.ecommerce.model.seller.shipofshop.create.ShipOfShopCreateResponse;
import com.th.ac.ku.kps.cpe.ecommerce.model.seller.shipofshop.delete.ShipOfShopDeleteRequest;
import com.th.ac.ku.kps.cpe.ecommerce.model.seller.shipofshop.delete.ShipOfShopDeleteResponse;
import com.th.ac.ku.kps.cpe.ecommerce.model.seller.shipofshop.update.ShipOfShopUpdateRequest;
import com.th.ac.ku.kps.cpe.ecommerce.model.seller.shipofshop.update.ShipOfShopUpdateResponse;
import com.th.ac.ku.kps.cpe.ecommerce.repository.*;
import com.th.ac.ku.kps.cpe.ecommerce.model.seller.product.ProductVariationEntity;
import com.th.ac.ku.kps.cpe.ecommerce.model.seller.product.create.ProductCreateRequest;
import com.th.ac.ku.kps.cpe.ecommerce.model.seller.product.create.ProductCreateResponse;
import com.th.ac.ku.kps.cpe.ecommerce.model.seller.shop.*;
import com.th.ac.ku.kps.cpe.ecommerce.unity.Common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.Timestamp;
import java.util.*;

@Service
@Configurable
public class SellerServiceImpl implements SellerService{
    private final ShopRepository shopRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ShopHasProductRepository shopHasProductRepository;
    private final CatagoryRepository catagoryRepository;
    private final ProductVariationRepository productVariationRepository;
    private final ProductPicRepository productPicRepository;
    private final ProductHasPromoRepository productHasPromoRepository;
    private final ShipOfShopRepository shipofshopRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public SellerServiceImpl(ShopRepository shopRepository,
                             ProductRepository productRepository,
                             UserRepository userRepository,
                             ShopHasProductRepository shopHasProductRepository,
                             CatagoryRepository catagoryRepository, ProductVariationRepository productVariationRepository, ProductPicRepository productPicRepository, ProductHasPromoRepository productHasPromoRepository, ShipOfShopRepository shipofshopRepository, OrderRepository orderRepository) {
        this.shopRepository = shopRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.shopHasProductRepository = shopHasProductRepository;
        this.catagoryRepository = catagoryRepository;
        this.productVariationRepository = productVariationRepository;
        this.productPicRepository = productPicRepository;
        this.productHasPromoRepository = productHasPromoRepository;
        this.shipofshopRepository = shipofshopRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public ShopCreateResponse shopCreateResponse(ShopCreateRequest restRequest) {

        ShopCreateResponse response = new ShopCreateResponse();
        ShopCreateResponseBody responseBody = new ShopCreateResponseBody();
        ShopEntity shop = new ShopEntity();

        shop.setIdUser(restRequest.getHeader().getId_user());
        shop.setNameShop(restRequest.getRequest().getName_shop());
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        shop.setCreatedAt(timestamp);

        try {
            shopRepository.save(shop);
            responseBody.setResult_code(201);
            responseBody.setResult_description("สร้างร้านค้าสำเร็จ");
            response.setResult(responseBody);
        }
        catch (Exception ex) {
            responseBody.setResult_code(406);
            responseBody.setResult_description("ไม่สามารถสร้างได้ เนื่องจากเกิดข้อผิดพลาดบางอย่าง");
            response.setResult(responseBody);
        }
        return response;
    }

    @Override
    public ShopUpdateResponse shopUpdateResponse(ShopUpdateRequest restRequest) {
        Optional<ShopEntity> shopEntity = shopRepository.findById(restRequest.getHeader().getId_user());
        Common.LoggerInfo(shopEntity);
        return null;
    }

    @Override
    public ProductCreateResponse productCreateResponse(String token,ProductCreateRequest restRequest) {
        List<UserEntity> user = (List<UserEntity>) userRepository.findAllByToken(Collections.singleton(token));
        List<ShopEntity> shop = (List<ShopEntity>) shopRepository.findAllByIdUser(Collections.singleton(user.get(0).getIdUser()));
        Common.LoggerInfo(shop);
        ShopHasProductEntity shopHasProductEntity = new ShopHasProductEntity();

        ProductCreateResponse response = new ProductCreateResponse();
        ProductEntity productEntity;
        Date date = new Date();
        Timestamp timeNow = new Timestamp(date.getTime());
        if(restRequest.getBody().getProduct_variation() != null) {
            try {

                productRepository.save(restRequest.getBody().getCatagory(),
                        restRequest.getBody().getName_product(),
                        restRequest.getBody().getDescription(),
                        restRequest.getBody().getCondition(),
                        timeNow);
                productEntity = productRepository.getLastId();
                shopHasProductEntity.setIdProduct(productEntity.getIdProduct());
                shopHasProductEntity.setIdShop(shop.get(0).getIdShop());
                shopHasProductRepository.save(shopHasProductEntity);

            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(406);
                response.setMsg("Can't save product");
                return response;
            }

            for (int i = 0; i < restRequest.getBody().getProduct_variation().size(); i++) {
                ProductVariationEntity productVariationEntity = new ProductVariationEntity();
                productVariationEntity.setProductEntityOfVariationSet(productEntity);
                productVariationEntity.setName(restRequest.getBody().getProduct_variation().get(i).getName());
                productVariationEntity.setPrice(restRequest.getBody().getProduct_variation().get(i).getPrice());
                productVariationEntity.setStock(restRequest.getBody().getProduct_variation().get(i).getStock());
                try {
                    productVariationRepository.save(productVariationEntity);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    response.setStatus(406);
                    response.setMsg("Can't save variation");
                    return response;
                }
            }
            response.setStatus(201);
            response.setMsg("Created Product And Product variation");
        }
        else  {
            response.setStatus(400);
            response.setMsg("Product variation has required");
        }
        return response;
    }

    @Override
    public ProductReadResponse productReadAllResponse(String token) {
        List<UserEntity> user = (List<UserEntity>) userRepository.findAllByToken(Collections.singleton(token));
        List<ShopEntity> shop = (List<ShopEntity>) shopRepository.findAllByIdUser(Collections.singleton(user.get(0).getIdUser()));
        List<ShopHasProductEntity> shopHasProduct = (List<ShopHasProductEntity>) shopHasProductRepository.findAllByIdShop(Collections.singleton(shop.get(0).getIdShop()));
        Common.LoggerInfo(shopHasProduct);
        ProductReadResponse response = new ProductReadResponse();
        ProductReadBodyResponse bodyResponse = new ProductReadBodyResponse();
        List<ProductReadProductBodyResponse> productBodyResponseList = new ArrayList<>();
        for (int i = 0; i < shopHasProduct.size(); i++) {
            List<ProductEntity> product = (List<ProductEntity>) productRepository.findAllById(Collections.singleton(shopHasProduct.get(i).getIdProduct()));
            List<CatagoryEntity> catagory = (List<CatagoryEntity>) catagoryRepository.findAllByIdCatagory(Collections.singleton(product.get(0).getCatagorySet().getIdCatagory()));

            ProductReadProductBodyResponse productBodyResponse = new ProductReadProductBodyResponse();
            productBodyResponse.setId_product(product.get(0).getIdProduct());
            productBodyResponse.setName_product(product.get(0).getNameProduct());
            productBodyResponse.setDescription(product.get(0).getDescription());
            ProductReadCatagoryProductBodyResponse catagoryProductBodyResponse = new ProductReadCatagoryProductBodyResponse();
            catagoryProductBodyResponse.setId_category(catagory.get(0).getIdCatagory());
            catagoryProductBodyResponse.setName_category(catagory.get(0).getNameCatagory());
            productBodyResponse.setCatagory(catagoryProductBodyResponse);
            productBodyResponse.setCondition(product.get(0).getCondition());
            productBodyResponse.setCreated_at(product.get(0).getCreatedAt());

            List<ProductPicEntity> productPicEntityList = new ArrayList<>(product.get(0).getProductPicEntitySet());
            List<ProductReadProductpicProductBodyResponse> productpicProductBodyResponseList = new ArrayList<>();
            for (int j = 0; j < product.get(0).getProductPicEntitySet().size(); j++) {
                ProductReadProductpicProductBodyResponse productpicProductBodyResponse = new ProductReadProductpicProductBodyResponse();
                productpicProductBodyResponse.setPic_product(productPicEntityList.get(j).getPicProduct());
                productpicProductBodyResponseList.add(productpicProductBodyResponse);
            }
            productBodyResponse.setProduct_pic(productpicProductBodyResponseList);

            List<ProductVariationEntity> productVariationEntityList = new ArrayList<>(product.get(0).getProductVariationEntitySet());
            List<ProductReadVariationProductBodyResponse> variationProductBodyResponseList = new ArrayList<>();
            for (int j = 0; j < product.get(0).getProductVariationEntitySet().size(); j++) {
                ProductReadVariationProductBodyResponse variationProductBodyResponse = new ProductReadVariationProductBodyResponse();
                variationProductBodyResponse.setId_variation(productVariationEntityList.get(j).getIdVariation());
                variationProductBodyResponse.setName(productVariationEntityList.get(j).getName());
                variationProductBodyResponse.setPrice(productVariationEntityList.get(j).getPrice());
                variationProductBodyResponse.setStock(productVariationEntityList.get(j).getStock());

                if (productVariationEntityList.get(j).getProductHasPromoEntitySet() != null) {
                    ProductReadPromotionVariationProductBodyResponse promotionVariationProductBodyResponse = new ProductReadPromotionVariationProductBodyResponse();
                    promotionVariationProductBodyResponse.setId_promo_type(productVariationEntityList.get(j).getProductHasPromoEntitySet().getIdPromoType());
                    promotionVariationProductBodyResponse.setNew_price(productVariationEntityList.get(j).getProductHasPromoEntitySet().getNewPrice());
                    promotionVariationProductBodyResponse.setTime_start(productVariationEntityList.get(j).getProductHasPromoEntitySet().getTimeStart());
                    promotionVariationProductBodyResponse.setTime_end(productVariationEntityList.get(j).getProductHasPromoEntitySet().getTimeEnd());
                    variationProductBodyResponse.setPromotion(promotionVariationProductBodyResponse);
                }
                variationProductBodyResponseList.add(variationProductBodyResponse);
            }
            productBodyResponse.setProduct_variation(variationProductBodyResponseList);
            productBodyResponseList.add(productBodyResponse);
        }
        bodyResponse.setProduct(productBodyResponseList);
        response.setBody(bodyResponse);
        response.setStatus(200);
        response.setMsg("successful");
        return response;
    }

    @Override
    public ProductReadResponse productReadResponse(String token, int id_product) {
        List<UserEntity> user = (List<UserEntity>) userRepository.findAllByToken(Collections.singleton(token));
        List<ShopEntity> shop = (List<ShopEntity>) shopRepository.findAllByIdUser(Collections.singleton(user.get(0).getIdUser()));
        List<ShopHasProductEntity> shopHasProduct = (List<ShopHasProductEntity>) shopHasProductRepository.findAllByIdShop(Collections.singleton(shop.get(0).getIdShop()));
        Common.LoggerInfo(shopHasProduct);
        for(int i = 0; i < shopHasProduct.size(); i++) {
            if (shopHasProduct.get(i).getIdProduct() == id_product) {
                List<ProductEntity> product = (List<ProductEntity>) productRepository.findAllById(Collections.singleton(shopHasProduct.get(i).getIdProduct()));
                Common.LoggerInfo(product);
                ProductReadResponse response = new ProductReadResponse();
                ProductReadBodyResponse bodyResponse = new ProductReadBodyResponse();

                List<ProductReadProductBodyResponse> productBodyResponseList = new ArrayList<>();
                List<CatagoryEntity> catagory = (List<CatagoryEntity>) catagoryRepository.findAllByIdCatagory(Collections.singleton(product.get(0).getCatagorySet().getIdCatagory()));
                ProductReadProductBodyResponse productBodyResponse = new ProductReadProductBodyResponse();
                productBodyResponse.setId_product(product.get(0).getIdProduct());
                productBodyResponse.setName_product(product.get(0).getNameProduct());
                productBodyResponse.setDescription(product.get(0).getDescription());
                ProductReadCatagoryProductBodyResponse catagoryProductBodyResponse = new ProductReadCatagoryProductBodyResponse();
                catagoryProductBodyResponse.setId_category(catagory.get(0).getIdCatagory());
                catagoryProductBodyResponse.setName_category(catagory.get(0).getNameCatagory());
                productBodyResponse.setCatagory(catagoryProductBodyResponse);
                productBodyResponse.setCondition(product.get(0).getCondition());
                productBodyResponse.setCreated_at(product.get(0).getCreatedAt());

                List<ProductPicEntity> productPicEntityList = new ArrayList<>(product.get(0).getProductPicEntitySet());
                List<ProductReadProductpicProductBodyResponse> productpicProductBodyResponseList = new ArrayList<>();
                for (int j = 0; j < product.get(0).getProductPicEntitySet().size(); j++) {
                    ProductReadProductpicProductBodyResponse productpicProductBodyResponse = new ProductReadProductpicProductBodyResponse();
                    productpicProductBodyResponse.setPic_product(productPicEntityList.get(j).getPicProduct());
                    productpicProductBodyResponseList.add(productpicProductBodyResponse);
                }
                productBodyResponse.setProduct_pic(productpicProductBodyResponseList);

                List<ProductVariationEntity> productVariationEntityList = new ArrayList<>(product.get(0).getProductVariationEntitySet());
                List<ProductReadVariationProductBodyResponse> variationProductBodyResponseList = new ArrayList<>();
                for (int j = 0; j < product.get(0).getProductVariationEntitySet().size(); j++) {
                    ProductReadVariationProductBodyResponse variationProductBodyResponse = new ProductReadVariationProductBodyResponse();
                    variationProductBodyResponse.setId_variation(productVariationEntityList.get(j).getIdVariation());
                    variationProductBodyResponse.setName(productVariationEntityList.get(j).getName());
                    variationProductBodyResponse.setPrice(productVariationEntityList.get(j).getPrice());
                    variationProductBodyResponse.setStock(productVariationEntityList.get(j).getStock());

                    if (productVariationEntityList.get(j).getProductHasPromoEntitySet() != null) {
                        ProductReadPromotionVariationProductBodyResponse promotionVariationProductBodyResponse = new ProductReadPromotionVariationProductBodyResponse();
                        promotionVariationProductBodyResponse.setId_promo_type(productVariationEntityList.get(j).getProductHasPromoEntitySet().getIdPromoType());
                        promotionVariationProductBodyResponse.setNew_price(productVariationEntityList.get(j).getProductHasPromoEntitySet().getNewPrice());
                        promotionVariationProductBodyResponse.setTime_start(productVariationEntityList.get(j).getProductHasPromoEntitySet().getTimeStart());
                        promotionVariationProductBodyResponse.setTime_end(productVariationEntityList.get(j).getProductHasPromoEntitySet().getTimeEnd());
                        variationProductBodyResponse.setPromotion(promotionVariationProductBodyResponse);
                    }
                    variationProductBodyResponseList.add(variationProductBodyResponse);
                }
                productBodyResponse.setProduct_variation(variationProductBodyResponseList);
                productBodyResponseList.add(productBodyResponse);
                bodyResponse.setProduct(productBodyResponseList);
                response.setBody(bodyResponse);
                response.setStatus(200);
                response.setMsg("successful");
                return response;
            }
        }
        return null;
    }

    @Override
    public ProductUpdateResponse productUpdateResponse(String token, ProductUpdateRequest restRequest) {
        List<UserEntity> user = (List<UserEntity>) userRepository.findAllByToken(Collections.singleton(token));
        List<ShopEntity> shop = (List<ShopEntity>) shopRepository.findAllByIdUser(Collections.singleton(user.get(0).getIdUser()));
        List<ProductEntity> productEntity = (List<ProductEntity>) productRepository.findAllById(Collections.singleton(restRequest.getBody().getId_product()));
        Common.LoggerInfo(productEntity);
        if (restRequest.getBody().getCatagory() != null)
            productEntity.get(0).setCatagory(restRequest.getBody().getCatagory());
        if (restRequest.getBody().getName_product() != null)
            productEntity.get(0).setNameProduct(restRequest.getBody().getName_product());
        if (restRequest.getBody().getDescription() != null)
            productEntity.get(0).setDescription(restRequest.getBody().getDescription());
        if (restRequest.getBody().getCondition() != null)
            productEntity.get(0).setCondition(restRequest.getBody().getCondition());

        ProductUpdateResponse response = new ProductUpdateResponse();
        try {
            productRepository.save(productEntity.get(0));
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(406);
            response.setMsg("Can't save product");
            return response;
        }

        for (int i = 0; i < restRequest.getBody().getProduct_variation().size(); i++) {
            ProductVariationEntity productVariationEntity = new ProductVariationEntity();
            productVariationEntity.setIdVariation(restRequest.getBody().getProduct_variation().get(i).getId_variation());
            productVariationEntity.setProductEntityOfVariationSet(productEntity.get(0));
            productVariationEntity.setName(restRequest.getBody().getProduct_variation().get(i).getName());
            productVariationEntity.setPrice(restRequest.getBody().getProduct_variation().get(i).getPrice());
            productVariationEntity.setStock(restRequest.getBody().getProduct_variation().get(i).getStock());
            try {
                productVariationRepository.save(productVariationEntity);
            }
            catch (Exception e) {
                e.printStackTrace();
                response.setStatus(406);
                response.setMsg("Can't save variation");
                return response;
            }
        }
        response.setStatus(201);
        response.setMsg("Created Product And Product variation");

        return response;
    }

    @Override
    public ProductDeleteResponse productDeleteResponse(String token, ProductDeleteRequest restRequest) {
        ProductDeleteResponse response = new ProductDeleteResponse();
        List<UserEntity> user = (List<UserEntity>) userRepository.findAllByToken(Collections.singleton(token));
        List<ShopEntity> shop = (List<ShopEntity>) shopRepository.findAllByIdUser(Collections.singleton(user.get(0).getIdUser()));
        List<ShopHasProductEntity> shopHasProductList = (List<ShopHasProductEntity>) shopHasProductRepository.findAllByIdShop(Collections.singleton(shop.get(0).getIdShop()));

        boolean foundProduct = false;
        if (restRequest.getBody().getId_product() != null) {
            for (int i = 0; i < shopHasProductList.size(); i++) {
                for (int j = 0; j < restRequest.getBody().getId_product().length; j++) {
                    if (shopHasProductList.get(i).getIdProduct() == restRequest.getBody().getId_product()[j]) {
                        foundProduct = true;
                        for (int ll = 0; ll < restRequest.getBody().getId_product().length; ll++) {
                            List<ProductEntity> product = (List<ProductEntity>) productRepository.findAllById(Collections.singleton(restRequest.getBody().getId_product()[ll]));
                            if (product.size() == 0) {
                                response.setStatus(404);
                                response.setMsg("Product not found");
                                return response;
                            }
                            List<ProductVariationEntity> productVariationEntity = new ArrayList<>(product.get(0).getProductVariationEntitySet());
                            List<ProductPicEntity> productPic = new ArrayList<>(product.get(0).getProductPicEntitySet());

                            for (int k = 0; k < productPic.size(); k++) { // Delete File in Drive
                                File file = new File(productPic.get(k).getPicProduct());
                                if (file.exists()) {
                                    if (file.delete()) {
                                        Common.LoggerInfo("File deleted successfully");
                                    } else {
                                        Common.LoggerInfo("Fail to delete file");
                                    }
                                }
                                productPicRepository.delete(productPic.get(k));
                            }
                            for (int k = 0; k < productVariationEntity.size(); k++) {
                                if (productVariationEntity.get(k).getProductHasPromoEntitySet() != null) {
                                    ProductHasPromoEntity productHasPromo = productVariationEntity.get(k).getProductHasPromoEntitySet();
                                    productHasPromoRepository.delete(productHasPromo);
                                }
                                productVariationRepository.delete(productVariationEntity.get(k));
                            }
                            ShopHasProductEntity shopHasProduct = new ShopHasProductEntity();
                            shopHasProduct.setIdShop(shop.get(0).getIdShop());
                            shopHasProduct.setIdProduct(restRequest.getBody().getId_product()[ll]);
                            shopHasProductRepository.delete(shopHasProduct);

                            productRepository.delete(product.get(0));
                        }
                    }

                }
            }
            if (!foundProduct) {
                response.setStatus(404);
                response.setMsg("Product not found");
                return response;
            }

        }
        if (restRequest.getBody().getId_variation() != null) {
            for (int i = 0; i < restRequest.getBody().getId_variation().length; i++) {
                try {
                    productVariationRepository.deleteById(restRequest.getBody().getId_variation()[i]);
                    Common.LoggerInfo(restRequest.getBody().getId_variation());
                } catch (Exception e) {
                    response.setStatus(404);
                    response.setMsg("variation not found");
                    return response;
                }
            }
        }
        response.setStatus(200);
        response.setMsg("Delete Successfully");
        return response;
    }

    @Override
    public ShipOfShopReadResponse shipofshopReadResponse(String token) {
        List<UserEntity> user = (List<UserEntity>) userRepository.findAllByToken(Collections.singleton(token));
        List<ShopEntity> shop = (List<ShopEntity>) shopRepository.findAllByIdUser(Collections.singleton(user.get(0).getIdUser()));
        List<ShipOfShopEntity> shipofshop = shipofshopRepository.findByIdShop(shop.get(0).getIdShop());
        Common.LoggerInfo(shipofshop);
        ShipOfShopReadResponse response = new ShipOfShopReadResponse();
        ShipOfShopReadBodyResponse bodyResponse = new ShipOfShopReadBodyResponse();
        List<ShipOfShopReadDataBodyResponse> dataList = new ArrayList<>();
        for (int i = 0; i < shipofshop.size(); i++) {
            ShipOfShopReadDataBodyResponse data = new ShipOfShopReadDataBodyResponse();
            data.setId_ship(shipofshop.get(i).getIdShip());
            data.setId_shop(shipofshop.get(i).getIdShop());
            data.setId_product(shipofshop.get(i).getIdProduct());
            data.setId_type(shipofshop.get(i).getIdType());
            data.setPrice(shipofshop.get(i).getPrice());
            dataList.add(data);
        }
        bodyResponse.setShip_of_shop(dataList);
        response.setBody(bodyResponse);

        response.setStatus(200);
        response.setMsg("successful");
        return response;
    }

    @Override
    public ShipOfShopCreateResponse shipofshopCreateResponse(String token, ShipOfShopCreateRequest restRequest){
        List<UserEntity> user = (List<UserEntity>) userRepository.findAllByToken(Collections.singleton(token));
        List<ShopEntity> shop = (List<ShopEntity>) shopRepository.findAllByIdUser(Collections.singleton(user.get(0).getIdUser()));
        List<ShipOfShopEntity> shipofshop = shipofshopRepository.findByIdShop(shop.get(0).getIdShop());
        ShipOfShopCreateResponse response = new ShipOfShopCreateResponse();
        ShipOfShopEntity shipOfShopEntity = new ShipOfShopEntity();
        shipOfShopEntity.setIdShop(restRequest.getBody().getId_shop());
        shipOfShopEntity.setIdProduct(restRequest.getBody().getId_product());
        shipOfShopEntity.setIdType(restRequest.getBody().getId_type());
        shipOfShopEntity.setPrice(restRequest.getBody().getPrice());
        try {
            shipofshopRepository.save(shipOfShopEntity);
            response.setStatus(201);
            response.setMsg("Created");
        }catch (Exception e) {
            response.setStatus(406);
            response.setMsg("Have some error");
        }
        return response;
    }

    @Override
    public ShipOfShopUpdateResponse shipofshopUpdateResponse(String token, ShipOfShopUpdateRequest restRequest) {
        List<UserEntity> user = (List<UserEntity>) userRepository.findAllByToken(Collections.singleton(token));
        List<ShopEntity> shop = (List<ShopEntity>) shopRepository.findAllByIdUser(Collections.singleton(user.get(0).getIdUser()));
        List<ShipOfShopEntity> shipofshop = shipofshopRepository.findByIdShop(shop.get(0).getIdShop());
        ShipOfShopUpdateResponse response = new ShipOfShopUpdateResponse();
        ShipOfShopEntity shipOfShopEntity = new ShipOfShopEntity();
        shipOfShopEntity.setIdShip(restRequest.getBody().getId_ship());
        if(restRequest.getBody().getId_shop() != 0)
            shipOfShopEntity.setIdShop(restRequest.getBody().getId_shop());
        if(restRequest.getBody().getId_product() != 0)
            shipOfShopEntity.setIdProduct(restRequest.getBody().getId_product());
        if(restRequest.getBody().getId_type() != 0)
            shipOfShopEntity.setIdType(restRequest.getBody().getId_type());
        if(restRequest.getBody().getPrice() != 0)
            shipOfShopEntity.setPrice(restRequest.getBody().getPrice());
        Common.LoggerInfo(shipOfShopEntity);
        shipofshopRepository.save(shipOfShopEntity);

        try {
            shipofshopRepository.save(shipOfShopEntity);
            response.setStatus(200);
            response.setMsg("Update Success");
        }catch (Exception e) {
            response.setStatus(406);
            response.setMsg("Have some error");
        }

        return response;
    }

    @Override
    public ShipOfShopDeleteResponse shipofshopDeleteResponse(String token, ShipOfShopDeleteRequest restRequest) {
        List<UserEntity> user = (List<UserEntity>) userRepository.findAllByToken(Collections.singleton(token));
        List<ShopEntity> shop = (List<ShopEntity>) shopRepository.findAllByIdUser(Collections.singleton(user.get(0).getIdUser()));
        ShipOfShopDeleteResponse response = new ShipOfShopDeleteResponse();
        ShipOfShopEntity shipOfShopEntity = new ShipOfShopEntity();
        shipOfShopEntity.setIdShip(restRequest.getBody().getId_ship());

        try {
            shipofshopRepository.delete(shipOfShopEntity);
            response.setStatus(200);
            response.setMsg("Delete Success");
        }catch (Exception e) {
            response.setStatus(204);
            response.setMsg("No Content");
        }


        return response;
    }

    @Override
    public OrderReadResponse readAllOrderForSellerResponse(String token) {
        return null;
    }

    @Override
    public OrderReadResponse readOrderForSellerResponse(String token, Integer id) {
        return null;
    }

    @Override
    public OrderUpdateResponse updateOrderForSellerResponse(String token, OrderUpdateRequest restRequest) {
        return null;
    }


}
