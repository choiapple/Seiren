package ssafy.e105.Seiren.domain.product.service;

import static ssafy.e105.Seiren.domain.product.exception.ProductErrorCode.FAIL_SEARCH_PRODUCT;
import static ssafy.e105.Seiren.domain.product.exception.ProductErrorCode.FAIL_SEARCH_PRODUCT2;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssafy.e105.Seiren.domain.product.dto.ProductCategoryDto;
import ssafy.e105.Seiren.domain.product.dto.ProductDto;
import ssafy.e105.Seiren.domain.product.dto.ProductSearchResponse;
import ssafy.e105.Seiren.domain.product.entity.Product;
import ssafy.e105.Seiren.domain.product.entity.ProductCategory;
import ssafy.e105.Seiren.domain.product.entity.Wish;
import ssafy.e105.Seiren.domain.product.repository.ProductRepository;
import ssafy.e105.Seiren.domain.product.repository.WishRepository;
import ssafy.e105.Seiren.domain.user.entity.User;
import ssafy.e105.Seiren.domain.user.repository.UserRepository;
import ssafy.e105.Seiren.global.error.type.BaseException;
import ssafy.e105.Seiren.global.jwt.JwtTokenProvider;
import ssafy.e105.Seiren.global.utils.ApiError;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchService {

    private final ProductRepository productRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final WishRepository wishRepository;

    @Transactional
    public ProductSearchResponse searchProduct(String nickname, Long gender, Long age, Long mood,
            String sortType, HttpServletRequest request, int page, int size) {
        User user = getUser(request);
        List<Long> categoryIdList = new ArrayList<>();
        if (gender != null) {
            categoryIdList.add(gender);
        }
        if (age != null) {
            categoryIdList.add(age);
        }
        if (mood != null) {
            categoryIdList.add(mood);
        }
        try {
            // 닉네임 x 목록
            if (nickname.isEmpty()) {
                // 최신순 정렬
                if (sortType.equals("Latest")
                        || sortType.isEmpty()) {
                    Pageable pageable = PageRequest.of(page - 1, size);
                    // 필터 선택 x
                    if (categoryIdList.size() == 0) {
                        Page<Product> productPage = productRepository.findAllProductsOrderByCreateAtDesc(
                                pageable);
                        return new ProductSearchResponse(getProductDtoList(productPage, user), productPage.getTotalPages());
                    }
                    // 필터 선택 o
                    Page<Product> productPage = productRepository.findProductsByCategoryIdsOrderByCreateAtDesc(
                            categoryIdList, categoryIdList.size(), pageable);
                    return new ProductSearchResponse(getProductDtoList(productPage, user), productPage.getTotalPages());
                }
                // 판매순 정렬
                else if (sortType.equals("Sales")) {
                    Pageable pageable = PageRequest.of(page - 1, size);
                    // 필터 선택 x
                    if (categoryIdList.size() == 0) {
                        Page<Product> productPage = productRepository.findAllOrderByTransactionCountDesc(
                                pageable);

                        System.out.println("productPage 이후");
                        ProductSearchResponse response = new ProductSearchResponse(
                                getProductDtoList(productPage, user), productPage.getTotalPages());
                        System.out.println("response 받아옴");
                        return response;
                    }
                    // 필터 선택 o
                    Page<Product> productPage = productRepository.findAllByCategoryOrderByTransactionCountDesc(
                            categoryIdList, categoryIdList.size(), pageable);
                    return new ProductSearchResponse(getProductDtoList(productPage, user), productPage.getTotalPages());
                }
            }
            // 닉네임 o 목록
            else if (nickname != null) {
                // 최신순 정렬
                if (sortType.equals("Latest") || sortType.isEmpty()) {
                    Pageable pageable = PageRequest.of(page - 1, size);
                    // 필터 선택 x
                    if (categoryIdList.size() == 0) {
                        Page<Product> productPage = productRepository.findAllProductByUserNickname(
                                nickname, pageable);
                        return new ProductSearchResponse(getProductDtoList(productPage, user), productPage.getTotalPages());
                    }
                    // 필터 선택 o
                    Page<Product> productPage = productRepository.findProductsByCategoryIdsAndNicknameOrderByCreateAtDesc(
                            categoryIdList, categoryIdList.size(), nickname, pageable);
                    return new ProductSearchResponse(getProductDtoList(productPage, user), productPage.getTotalPages());
                }
                // 판매순 정렬
                else if (sortType.equals("Sales")) {
                    Pageable pageable = PageRequest.of(page - 1, size);
                    // 필터 선택 x
                    if (categoryIdList.size() == 0) {
                        Page<Product> productPage = productRepository.findAllByNicknameOrderByTransactionCountDesc(
                                nickname, pageable);
                        return new ProductSearchResponse(getProductDtoList(productPage, user), productPage.getTotalPages());
                    }
                    // 필터 선택 o
                    Page<Product> productPage = productRepository.findAllByCategoryIdsAndNicknameOrderByTransactionCountDesc(
                            categoryIdList, categoryIdList.size(), nickname, pageable);
                    return new ProductSearchResponse(getProductDtoList(productPage, user), productPage.getTotalPages());
                }
            }
            throw new BaseException(
                    new ApiError(FAIL_SEARCH_PRODUCT2.getMessage(),
                            FAIL_SEARCH_PRODUCT2.getCode()));
        } catch (Exception e) {
            throw new BaseException(
                    new ApiError(FAIL_SEARCH_PRODUCT.getMessage(), FAIL_SEARCH_PRODUCT.getCode()));
        }
    }

    @Transactional
    public List<ProductDto> getProductDtoList(Page<Product> productPage, User user) {
        List<ProductDto> productDtoList = new ArrayList<>();
        List<Product> productList = productPage.getContent();
        for (Product product : productList) {
            List<ProductCategoryDto> productCategoryDtoList = new ArrayList<>();
            for (ProductCategory productCategory : product.getProductCategories()) {
                ProductCategoryDto productCategoryDto = new ProductCategoryDto(
                        productCategory);
                productCategoryDtoList.add(productCategoryDto);
            }
            if (user != null) {
                Wish wish = getWish(product.getProductId(), user.getId());
                if (wish != null) {
                    ProductDto productDto = new ProductDto(product, productCategoryDtoList, true);
                    productDtoList.add(productDto);
                    continue;
                }
                ProductDto productDto = new ProductDto(product, productCategoryDtoList, false);
                productDtoList.add(productDto);
                continue;
            }
            ProductDto productDto = new ProductDto(product, productCategoryDtoList, false);
            productDtoList.add(productDto);
        }
        return productDtoList;
    }

    public User getUser(HttpServletRequest request) {
        if (jwtTokenProvider.resolveToken(request) != null) {
            String userEmail = jwtTokenProvider.getUserEmail(
                    jwtTokenProvider.resolveToken(request));
            return userRepository.findByEmail(userEmail).get();
        }
        return null;
    }

    @Transactional
    public Wish getWish(Long productId, Long userId) {
        Optional<Wish> wishOptional = wishRepository.findByUser_IdAndProduct_ProductId(userId,
                productId);
        // Wish를 찾았을 경우에 대한 처리
        if (wishOptional.isPresent()) {
            return wishOptional.get();
        }
        return null;
    }
}
