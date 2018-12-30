package com.vince.retailmanager.model.entity;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("marketing")
@NoArgsConstructor
public class MarketingFee extends PercentageFee {

  public static MarketingFee create(IncomeStatement incomeStatement) {
    MarketingFee marketingFee = new MarketingFee();
    marketingFee.setAttributes(incomeStatement);
    marketingFee.setDescription("marketing fee");
    marketingFee.setFeePercent(marketingFee.getFranchisor().getMarketingFeePercent());

    marketingFee.init();
    return marketingFee;
  }

//	@Override
//	public String getDescription() {
//		return "marketing fee";
//	}
//
//	@Override
//	public double getFeePercent() {
//		return getFranchisor().getMarketingFeePercent();
//	}
}