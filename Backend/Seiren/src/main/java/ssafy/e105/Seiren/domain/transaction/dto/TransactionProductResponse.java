package ssafy.e105.Seiren.domain.transaction.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ssafy.e105.Seiren.domain.transaction.entity.Transaction;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionProductResponse {

    // 상품 사진, 상품 타이틀, 카테고리
    // 남은 글자수 / 구매 글자수
    private Long productId;
    private String productImageUrl;
    private String productTitle;
    private List<String> productCategories;
    private int remainLetter;
    private int totalCount;

    /**
     * transaction entity에서 transactionProductResponse 로
     */
    public static TransactionProductResponse toDto(Transaction transaction) {
        return TransactionProductResponse.builder()
                .productId(transaction.getProduct().getProductId())
                .productImageUrl(transaction.getProduct().getProductImageUrl())
                .productTitle(transaction.getProduct().getProductTitle())
                .build();
    }
}
